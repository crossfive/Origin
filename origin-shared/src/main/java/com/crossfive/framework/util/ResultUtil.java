package com.crossfive.framework.util;

import com.crossfive.framework.common.dto.Result;

public class ResultUtil {
	
	public final static int SUCCESS = 1;
	
	public final static int FAIL = 0;
	
	public final static int PUSH = 2;
	
	public static Result buildResultSucc() {
		return buildResultSucc(null);
	}

	public static Result buildResultSucc(Object result) {
		Result r = new Result();
		r.setCode(SUCCESS);
		r.setData(result);
		return r;
	}
	
	public static Result buildResultFail(String cause) {
		Result r = new Result();
		r.setCode(FAIL);
		r.setMsg(cause);
		return r;
	}
	
	public static Result buildResultPush(Object obj) {
		Result r = new Result();
		r.setCode(PUSH);
		r.setData(obj);
		return r;
	}
	
	public static Result buildResult(int code, String cause) {
		Result r = new Result();
		r.setCode(code);
		r.setMsg(cause);
		return r;
	}
	
	public static Result buildResult(int code, String cause, Object obj) {
		Result r = new Result();
		r.setCode(code);
		r.setMsg(cause);
		r.setData(obj);
		return r;
	}
}
