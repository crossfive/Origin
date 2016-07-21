package com.crossfive.framework.jdbc.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.crossfive.framework.jdbc.BaseDao;
import com.crossfive.framework.jdbc.domain.ApiMeta;

@Component
public class ApiMetaDaoImpl extends BaseDao<ApiMeta> implements ApiMetaDao {

	@Override
	public List<ApiMeta> getAllApiMetas() {
		return this.querySql("select * from api_meta", null);
	}

	@Override
	public List<ApiMeta> getApiMetasByType(String type) {
		Object[] params = new Object[] {type};
		return this.querySql("select * from api_meta where apiType = ?", params);
	}

	@Override
	public ApiMeta getApiMeta(String name) {
		Object[] params = new Object[] {name};
		return this.queryOne("select * from api_meta where apiName = ?", params);
	}

	
}
