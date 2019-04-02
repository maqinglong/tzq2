package com.tzq.usermanage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tzq.usermanage.controller.ActUserController;
import com.tzq.usermanage.dao.ActUserMapper;
import com.tzq.usermanage.model.ActUser;

/**
 * 用户处理服务类
 * @author 马庆龙
 *
 */
@Service
public class ActUserService {
	
	private Logger logger = LoggerFactory.getLogger(ActUserService.class.getName());
	@Autowired
	ActUserMapper actUserMapper ;
	

	/**
	 * 保存用户信息
	 * @param actUser
	 * @return
	 */
	public int saveActUser(ActUser actUser) {
		int ret = 0;
		if ( actUser != null ) {
			try {
				ret = actUserMapper.insert(actUser);
				if (ret >0 ) {
					logger.info("插入用户信息成功!userNo:{},userName{}",actUser.getUserNo(),actUser.getUserName());
				}
			}catch(Exception e) {
				logger.error("插入数据有误{}",e.getMessage());
				e.printStackTrace();				
			}
		}
		return ret;
	}
}
