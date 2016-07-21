package com.crossfive.framework.util;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URLDecoder;

public class Utils {

	public static String decode(String str, String encode) throws UnsupportedEncodingException{
		try {
			return URLDecoder.decode(str, encode);
		}catch (UnsupportedEncodingException usee) {
			throw usee;
		}catch (Throwable t) {
			// 解析失败
			return str;
		}
	}
	
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> anClass) {
		Class<?> cc = clazz;
		T annotation = null;
		while (cc != null && cc != Object.class) {
			annotation = cc.getAnnotation(anClass);
			if (annotation != null) {
				return annotation;
			}
			cc = cc.getSuperclass();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> Object  cast(Object src, Class<T> type) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (src == null) {
			throw new RuntimeException("Parse error!! Invalid parameter has been received ");
		}
		if (type == src.getClass() || type.getGenericSuperclass() == src.getClass()) {
			return (T)src; 
		}
		if (!type.isArray() && src instanceof Object[]) {
			src = ((Object[])src)[0];
		} else if (!src.getClass().isPrimitive() && type.isPrimitive() && src.getClass().getField("TYPE").get(null) == type) {
			return (T)src;
		}
		if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
			return Integer.parseInt((String) src);
		}
		return (T)src;
	}

}
