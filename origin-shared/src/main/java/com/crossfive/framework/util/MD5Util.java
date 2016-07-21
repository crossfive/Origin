package com.crossfive.framework.util;


import io.netty.util.internal.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * MD5工具类
 * @author kira
 *
 */
public class MD5Util {

	private static final int BUFFER_SIZE = 8 *1024;
	
	private static final String HASH_TYPE = "MD5";
	
	private static final String ZIP_DIST = "dist/script.zip";
	
	private static final String SCRIPT_FILE = "/script";
	
	private static final char[] hexChar = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
	
	public static final String ENCODE_BASE_64 = "base64";
	
	public static final String ENCODE_SIMPLE = "simple";
	
	public static String getMD5Code(String stringToMd5,String algorithm){
		MessageDigest md5;
		byte[] result;
		try {
			md5 = MessageDigest.getInstance(HASH_TYPE);
			byte[] bt = stringToMd5.getBytes();
			md5.update(bt);
			result = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if (algorithm == null) {
			return StringUtil.toHexString(result);
		}else if (algorithm.equals(ENCODE_BASE_64)){
			return Base64.encodeBase64String(result);
		}
		return toHexString(result);
	}
	
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (byte c : b) {
			sb.append(hexChar[(c & 0xf0) >> 4]);
			sb.append(hexChar[c & 0x0f]);
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
			System.out.println(getMD5Code("password", null));
	}
	
}
