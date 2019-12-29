package com.tzq.usermanage.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tzq.sys.utils.PubFun;
import com.tzq.usermanage.dao.ActUserMapper;
import com.tzq.usermanage.dao.ActUserMapperExt;
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
	@Autowired
	ActUserMapperExt actUserMapperExt;

	/**
	 * 保存用户信息
	 * @param actUser
	 * @return
	 */
	public int saveActUser(ActUser actUser) {
		int ret = 0;
		if ( actUser != null ) {
			try {
				ActUser actUserOld = actUserMapperExt.selectByWechatOpenid(actUser.getWechatOpenid());
				if (actUserOld != null ) {
					actUser.setUserNo(actUserOld.getUserNo());
					actUser.setUpdateTime(new Date());
					actUserMapperExt.updateByUserNoSelective(actUser);
				}else {
					String userNo = PubFun.CreateMaxSerialNo("AU", 5);
					actUser.setUserNo(userNo);
					actUser.setCreateTime(new Date());
					ret = actUserMapper.insert(actUser);
				}
				if (ret >0 ) {
					logger.info("保存用户信息成功!userNo:{},userName{}",actUser.getUserNo(),actUser.getUserName());
				}
			}catch(Exception e) {
				logger.error("插入数据有误{}",e.getMessage());
				e.printStackTrace();				
			}
		}
		return ret;
	}
	
	/**
	 * 取得用户信息
	 * @param wechatOpenid
	 * @return
	 */
	public ActUser getActUserInfo(String wechatOpenid) {
		ActUser ret = actUserMapperExt.selectByWechatOpenid(wechatOpenid);
		return ret;
	}
	
	/**
	 * 取得所有教练的信息
	 * @return
	 */
	public List<ActUser> getAllActUserInfo(){
		List<ActUser> ret = actUserMapperExt.selectAllUsersInfo();		
		return ret;
	}
}
