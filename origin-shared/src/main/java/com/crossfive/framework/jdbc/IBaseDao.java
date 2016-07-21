package com.crossfive.framework.jdbc;

import java.io.Serializable;
import java.util.List;

public interface IBaseDao <T> {

	T read(Serializable id);

	void save(T entity);

	void update(T entity);

	void delete(Serializable id);

	T readById(Serializable id);

	T queryOne(String sql, Object[] args);

	List<T> querySql(String sql, Object[] args);

}
