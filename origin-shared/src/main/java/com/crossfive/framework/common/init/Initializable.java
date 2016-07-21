package com.crossfive.framework.common.init;

import org.springframework.context.support.AbstractApplicationContext;

public interface Initializable {

	public void init() throws Exception;
	
	public void setContext(AbstractApplicationContext context);
}
