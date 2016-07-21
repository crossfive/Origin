package com.crossfive.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;


public class RequestUtil {

	public static void parseParam(String string, Map<String, Object> paramMap)  throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(string)) {
			return;
		}
		String str = string.trim();
		String[] strs = str.split("&");
		for (String value : strs) {
			String[] values = value.split("=");
			if (values.length == 0) {
				continue;
			}
			String k = Utils.decode(values[0], "utf-8");
			if (values.length == 1) {
				paramMap.put(k, null);
			} else {
				String v = Utils.decode(values[1], "utf-8");
				for (int i = 2; i < values.length; i++) {
					v = v + "=" + Utils.decode(values[i], "utf-8");
				}
				if (paramMap.containsKey(k)) {
					paramMap.put(k, RequestUtil.getValue(paramMap.get(k), v));
				} else {
					paramMap.put(k, new String[] {v});
				}
				
			}
			
		}
		
	}

	private static Object getValue(Object src, String v) {
		String[] strings = (String[]) src;
		if (strings == null || strings.length <= 0) {
			return new String[] {v};
		}
		
		String[] result = new String[strings.length + 1];
		System.arraycopy(strings, 0, result, 0, strings.length);
		result[strings.length] = v;
		return result;
	}

	
	public static void parseParamByJson(byte[] content, Map<String, Object> paramMap)  throws UnsupportedEncodingException {
		JSONObject jo = (JSONObject)JSONObject.parse(content);
		for (Entry<String, Object> entry : jo.entrySet()) {
			paramMap.put(entry.getKey(), entry.getValue());
		}
	}
	
}
