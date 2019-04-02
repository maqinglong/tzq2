package com.tzq.sys.dao;

import org.springframework.stereotype.Component;

import com.tzq.sys.model.ExeFunction;

/**
 * 执行函数的公共方法
 */
@Component
public interface ExeFunctionDao {
	public String createMaxNo(ExeFunction tExeFunction);
}
