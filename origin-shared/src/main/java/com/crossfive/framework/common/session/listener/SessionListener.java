package com.crossfive.framework.common.session.listener;

import java.util.EventListener;

import com.crossfive.framework.common.session.event.SessionEvent;

public interface SessionListener extends EventListener {
	
	public void sessionCreated(SessionEvent se);
	
	public void sessionDestoryed (SessionEvent se);
	
}
