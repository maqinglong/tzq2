package com.tzq.dao;

import com.tzq.model.ecsUsers;
import com.tzq.model.ecsUsersWithBLOBs;

public interface ecsUsersMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(ecsUsersWithBLOBs record);

    int insertSelective(ecsUsersWithBLOBs record);

    ecsUsersWithBLOBs selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(ecsUsersWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ecsUsersWithBLOBs record);

    int updateByPrimaryKey(ecsUsers record);
}