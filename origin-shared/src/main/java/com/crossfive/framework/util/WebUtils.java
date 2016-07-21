package com.crossfive.framework.util;

import java.net.InetSocketAddress;

import org.springframework.util.StringUtils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

public class WebUtils {

	public static String getIp(HttpRequest httpRequest, Channel channel) {
		String ip = httpRequest.headers().get("Cdn-Src-Ip");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.headers().get("x-forwarded-for");
		}
		
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.headers().get("Proxy-Client-IP");
		}
		
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.headers().get("WL-Proxy-Client-IP");
		} 
		
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.headers().get("x-real-ip");
		} 
		
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		}
		
		return ip;
	}
}
