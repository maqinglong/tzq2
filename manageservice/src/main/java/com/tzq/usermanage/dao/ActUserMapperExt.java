package com.tzq.usermanage.dao;

import org.springframework.data.repository.query.Param;

import com.tzq.usermanage.model.ActUser;

public interface ActUserMapperExt {
    ActUser selectByWechatOpenid(@Param("wechatOpenid")String wechatOpenid);
    int updateByUserNoSelective(ActUser record);
}