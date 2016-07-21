package com.crossfive.framework.jdbc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_meta")
public class ApiMeta implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3561630527024146846L;

	@Id
	@GeneratedValue
	private long id;
	
	private String apiName;
	
	private String description;
	
	private String apiType;
	
	private String apiParams;
	
	private String serviceMeta;
	
	private int state;
	
	private int version;
	
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getApiParams() {
		return apiParams;
	}

	public void setApiParams(String apiParams) {
		this.apiParams = apiParams;
	}

	public String getServiceMeta() {
		return serviceMeta;
	}

	public void setServiceMeta(String serviceMeta) {
		this.serviceMeta = serviceMeta;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
