package com.crossfive.framework.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * 行集合处理器
 * @author kira
 *
 * @param <T> 泛型参数
 */
public class RowSetHandler<T> implements RowCallbackHandler {

	private Class<T> clazz;
	
	private List<T> result;
	
	public RowSetHandler(List<T> list, Class<T> clazz) {
		this.result = list;
		this.clazz = clazz;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		try {
				T t = clazz.newInstance();
				for (Field field : clazz.getDeclaredFields()) {
					if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					field.setAccessible(true);
					field.set(t, rs.getObject(field.getName()));
				}
				result.add(t);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
