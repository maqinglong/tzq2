package com.tzq.usermanage.dao;

import com.tzq.usermanage.model.WxchUser;

public interface WxchUserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(WxchUser record);

    int insertSelective(WxchUser record);

    WxchUser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(WxchUser record);

    int updateByPrimaryKey(WxchUser record);
}