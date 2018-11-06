package com.tzq.login.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tzq.login.service.WechatService;



@Component
public class ThirdSessionAuthFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(WechatService.class);
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Autowired
//    private ConsumerMapper consumerMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //获取请求头部分的Authorization
        String authHeader = request.getHeader(this.tokenHeader);
        //如果请求路径为微信通知后台支付结果则不需要token（之后会在具体的controller中，对双方签名进行验证防钓鱼）
        String url = request.getRequestURI().substring(request.getContextPath().length());

        if (url.equals("/auth") || url.equals("/test")) {
            chain.doFilter(request, response);
            return;
        }

        if (null == authHeader || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("非法访问用户");
        }
        // The part after "Bearer "
        final String thirdSessionId = authHeader.substring(tokenHead.length());
        logger.info("认证开始！！！token: " + thirdSessionId);
        String wxSessionObj = stringRedisTemplate.opsForValue().get(thirdSessionId);
        logger.info("认证！！！取得token: " + wxSessionObj);
        String prefix = "*";//这个*一定要加，否则无法模糊查询
        Set<String> keys = stringRedisTemplate.keys(prefix);
        for(String key : keys){
        	logger.info("认证！！！取得keys: " + key);
        }
        if (StringUtils.isEmpty(wxSessionObj)) {
            throw new RuntimeException("用户身份已过期");
        }
        logger.info("获得令牌");

        // 设置当前登录用户
        try (AppContext appContext = new AppContext(wxSessionObj.substring(wxSessionObj.indexOf("#") + 1))) {
            chain.doFilter(request, response);
        }
    }

}
