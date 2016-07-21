package com.crossfive.framework.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 行集合处理器
 * @author kira
 *
 * @param <T> 泛型参数
 */
public class SingleRowSetHandler<T> implements RowMapper<T> {

	private Class<T> clazz;

	public SingleRowSetHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T t = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				field.set(t, rs.getObject(field.getName()));
			}
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}