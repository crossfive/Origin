package com.crossfive.framework.jdbc.dao;

import java.util.List;

import com.crossfive.framework.jdbc.IBaseDao;
import com.crossfive.framework.jdbc.domain.ApiMeta;

public interface ApiMetaDao extends IBaseDao<ApiMeta> {

	public List<ApiMeta> getAllApiMetas();
	
	public List<ApiMeta> getApiMetasByType(String type);
	
	public ApiMeta getApiMeta(String name);
	
}
