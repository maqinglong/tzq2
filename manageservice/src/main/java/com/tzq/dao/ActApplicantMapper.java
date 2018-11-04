package com.tzq.dao;

import com.tzq.model.ActApplicant;

public interface ActApplicantMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActApplicant record);

    int insertSelective(ActApplicant record);

    ActApplicant selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActApplicant record);

    int updateByPrimaryKey(ActApplicant record);
}