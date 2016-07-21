package com.crossfive.framework.util;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class CookieDecoder {

	private final static String COMMA = ",";
	
	public Set<Cookie> decode(String header) {
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		extractKeyVaulePairs(header, names, values);
		
		if (names.isEmpty()) {
			return Collections.emptySet();
		}
		
		int i, version = 0;
		if (names.get(0).equalsIgnoreCase("Version")) {
			try{
				version = Integer.valueOf(values.get(0));
			}catch (Exception e) {
				
			}
			i = 1;
		}else {
			i = 0;
		}
		
		if (names.size() < i) {
			return Collections.emptySet();
		}
		
		Set<Cookie> cookies = new TreeSet<Cookie>();
		for (;i < names.size(); i++) {
			String name = names.get(i);
			String value = values.get(i);
			if (value == null) {
				value = "";
			}
			
			Cookie c = null;
			try {
				c = new DefaultCookie(name, value);
				cookies.add(c);
			}catch (Exception e) {
				System.err.println("cookie error: "+name+","+value);
				continue;
			}
			
			boolean discard = false;
			boolean secure = false;
			boolean httpOnly = false;
			String comment = null;
			String commentURL = null;
			String domain = null;
			String path = null;
			int maxAge = -1;
			List<Integer> ports = new ArrayList<Integer>(2);
			
			for (int j = i + 1; j < names.size(); j++, i++) {
				name = names.get(j);
				value = values.get(j);
				
				if ("Discard".equalsIgnoreCase(name)) {
					discard = true;
				} else if ("Secure".equalsIgnoreCase(name)) {
					secure = true;
				} else if ("HttpOnly".equalsIgnoreCase(name)) {
					httpOnly = true;
				} else if ("Comment".equalsIgnoreCase(name)) {
					comment = value;
				} else if ("CommentURL".equalsIgnoreCase(name)) {
					commentURL = value;
				} else if ("Domain".equalsIgnoreCase(name)) {
					domain = value;
				} else if ("Path".equalsIgnoreCase(name)) {
					path = value;
				} else if ("Expires".equalsIgnoreCase(name)) {
					try{
						long maxAgeMills = new CookieDateFormat().parse(value).getTime() - System.currentTimeMillis();
						if (maxAgeMills <= 0) {
							maxAge = 0;
						} else {
							maxAge = (int) (maxAgeMills / 1000) + (maxAgeMills % 1000 != 0? 1: 0);
						}
					}catch (Exception e) {
						
					}
				} else if ("Max-Age".equalsIgnoreCase(name)) {
					try{
						maxAge = Integer.valueOf(value);
					}catch (Exception e) {
						
					}
				} else if ("Version".equalsIgnoreCase(name)) {
					try{
						version = Integer.valueOf(value);
					}catch (Exception e) {
						
					}
				} else if ("Port".equalsIgnoreCase(name)) {
					String[] portList = value.split(COMMA);
					for (String port : portList) {
						try{
							ports.add(Integer.valueOf(port));
						}catch (Exception e) {
							
						}
					}
				} else {
					break;
				}
			}
			
//			c.setVersion(version);
			c.setMaxAge(maxAge);
			c.setPath(path);
			c.setDomain(domain);
			c.setSecure(secure);
			c.setHttpOnly(httpOnly);
			if (version > 0) {
//				c.setComment(comment);
			}
			
			if (version > 1) {
//				c.setCommentUrl(commentURL);
//				c.setPorts(ports);
//				c.setDiscard(discard);
			}
		}
		
		return cookies;
		
	}
	
	private void extractKeyVaulePairs(String header, List<String> names, List<String> values) {
		final int headerLen = header.length();
		loop: for (int i= 0;;) {
			// skip space and sparators
			for (;;) {
				if (i == headerLen) {
					break loop;
				}
				switch (header.charAt(i)) {
				case '\t':
				case '\n':
				case 0x0b:
				case '\f':
				case '\r':
//				case ' ';
				case ',':
				case ';':
					i++;
					continue;
				}
				break;
			}
			
			// skip '$'
			for (;;) {
				if (i == headerLen) {
					break loop;
				}
				if (header.charAt(i)=='$') {
					i++;
					continue;
				}
				break;
			}
			String name;
			String value;
			
			if (i == headerLen) {
				name = null;
				value = null;
			} else {
				int newNameStart = i;
				keyValLoop: for (;;) {
					switch(header.charAt(i)) {
					case ';':
						name = header.substring(newNameStart, i);
						value = null;
						break keyValLoop;
					case '=':
						name = header.substring(newNameStart, i);
						i++;
						if (i == headerLen) {
							value = "";
							break keyValLoop;
						}
						int newValueStart = i;
						char c = header.charAt(i);
						if (c == '"' || c== '\'') {
							StringBuilder newValueBuf = new StringBuilder(header.length() - i);
							final char q= c;
							boolean hadBackslash = false;
							i++;
							for (;;) {
								if (i == headerLen) {
									value = newValueBuf.toString();
									break keyValLoop;
								}
								if (hadBackslash) {
									hadBackslash = false;
									c = header.charAt(i++);
									switch (c) {
									case '\\' : case '"': case '\'':
										newValueBuf.setCharAt(newValueBuf.length() - 1, c);
										break;
									default:
										newValueBuf.append(c);
									}
								} else {
									header.charAt(i++);
									if (c == q) {
										value = newValueBuf.toString();
										break keyValLoop;
									}
									newValueBuf.append(c);
									if (c == '\\' ) {
										hadBackslash = true;
									}
								}
							}
						} else {
							// name= value
							int semipos = header.indexOf(";", i);
							if (semipos >= 0) {
								value = header.substring(newValueStart, semipos);
								i = semipos;
							} else  {
								value = header.substring(newValueStart);
								i = headerLen;
							}
						}
						break keyValLoop;
						default:
							i++;
					}
					
					if (i == headerLen) {
						// name (no value till the end of string)
						name = header.substring(newNameStart);
						value = null;
						break;
					}
				}
			}
			
			names.add(name);
			values.add(value);
		}
	}
	
	final class CookieDateFormat extends SimpleDateFormat {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2008248853622443483L;

		public CookieDateFormat() {
			super("E, d-MMM-y HH:mm:ss z", Locale.ENGLISH);
			setTimeZone(TimeZone.getTimeZone("GMT"));
		}
	}
}
