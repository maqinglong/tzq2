package com.tzq.usermanage.dao;

import com.tzq.usermanage.model.ActUser;
import com.tzq.usermanage.model.ActUserKey;

public interface ActUserMapper {
    int deleteByPrimaryKey(ActUserKey key);

    int insert(ActUser record);

    int insertSelective(ActUser record);

    ActUser selectByPrimaryKey(ActUserKey key);

    int updateByPrimaryKeySelective(ActUser record);

    int updateByPrimaryKey(ActUser record);
}