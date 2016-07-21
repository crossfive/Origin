package com.crossfive.framework.common.session.listener;

import com.crossfive.framework.common.session.event.SessionAttributeEvent;

public interface SessionAttributeListener {

	public void attributeAdd(SessionAttributeEvent event);
	
	public void attributeRemoved(SessionAttributeEvent event);
	
	public void attributeReplaced(SessionAttributeEvent event);
	
}
