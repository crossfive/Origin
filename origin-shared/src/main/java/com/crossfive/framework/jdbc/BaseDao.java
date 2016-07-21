package com.crossfive.framework.jdbc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.crossfive.framework.handler.RowSetHandler;
import com.crossfive.framework.handler.SingleRowSetHandler;

/**
 * 基础Dao
 * @author kira
 *
 * @param <T>
 */
public class BaseDao<T extends Serializable> implements IBaseDao<T> {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private Class<T> clazz;

//	private RowSetHandler<T> handler;

	public BaseDao() {
		try {
			clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		} catch(Exception e) {
			clazz = (Class<T>) this.getClass().getGenericSuperclass();
		}
	}

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public T read(Serializable id) {
		return (T)getSession().get(clazz, id);
	}

	@Override
	public void save(T entity){  
		getSession().save(entity);  
	}  

	/* (non-Javadoc) 
	 * @see com.liang.ssh2.base.BaseDao#update(T) 
	 */  
	@Override
	public void update(T entity){  
		getSession().update(entity);  
	}  
	/* (non-Javadoc) 
	 * @see com.liang.ssh2.base.BaseDao#delete(java.lang.Long) 
	 */  
	@Override
	public void delete(Serializable id){  
		if(id!=null){  
			Object entity=read(id);  
			if(entity!=null){  
				getSession().delete(entity);  
			}  
		}  
	}

	@Override
	public T readById(Serializable id) {
		return (T)getSession().load(clazz, id);
	}

	@Override
	public List<T> querySql(String sql, Object[] args) {
		List<T> list = new ArrayList<T>();
		jdbcTemplate.query(sql, new RowSetHandler<T>(list,clazz), args);
		return list ;
	}
	
	@Override
	public T queryOne(String sql, Object[] args) {
		List<T> result = jdbcTemplate.query(sql, args, new SingleRowSetHandler<T>(clazz));
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
 	}
	
}
