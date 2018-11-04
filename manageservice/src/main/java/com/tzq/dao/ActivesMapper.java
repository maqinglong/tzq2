package com.tzq.dao;

import com.tzq.model.Actives;

public interface ActivesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Actives record);

    int insertSelective(Actives record);

    Actives selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Actives record);

    int updateByPrimaryKey(Actives record);
}