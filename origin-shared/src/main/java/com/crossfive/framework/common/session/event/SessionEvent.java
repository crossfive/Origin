package com.crossfive.framework.common.session.event;

import com.crossfive.framework.common.session.Session;

/**
 * Session事件
 * @author kira
 *
 */
public class SessionEvent {

	public Session session;

	public SessionEvent(Session session) {
		this.session = session;
	}
	
}
