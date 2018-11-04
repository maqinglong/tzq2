package com.tzq.login.dao;

import com.tzq.login.model.WxTzqUser;

public interface WxTzqUserMapper {
    int deleteByPrimaryKey(String wechatOpenid);

    int insert(WxTzqUser record);

    int insertSelective(WxTzqUser record);

    WxTzqUser selectByPrimaryKey(String wechatOpenid);

    int updateByPrimaryKeySelective(WxTzqUser record);

    int updateByPrimaryKey(WxTzqUser record);
}