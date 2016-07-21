package com.crossfive.framework.common.session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.crossfive.framework.common.session.event.SessionAttributeEvent;
import com.crossfive.framework.common.session.event.SessionEvent;
import com.crossfive.framework.common.session.listener.SessionAttributeListener;
import com.crossfive.framework.common.session.listener.SessionListener;
import com.crossfive.framework.util.Tuple;

/**
 * Session格式
 * @author kira
 *
 */
public class Session {
	
	private static enum Type {
		CREATE, DESTORY, ADD, REPLACE, REMOVE
	}
	
	public ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
	
	public String id;
	
	public long createTime;
	
	public long lastAccessTime;
	
	public boolean isValid = true;
	
	public boolean expire = false;
	
	public List<Tuple<String, byte[]>> msgList = null;
	
	/** 推送通道 */
	public Push push = null;
	
	private final Object lock = new Object();
	
	private boolean discard = false;
	
	public long sessionTimeoutSecond;
	
	public List<SessionListener> sessionListeners;
	
	public List<SessionAttributeListener> sessionAttributeListeners;
	
	public Session(String id, List<SessionAttributeListener> sessionAttributeListeners, List<SessionListener> sessionListeners) {
		this.id = id;
		this.createTime = System.currentTimeMillis();
		this.lastAccessTime = System.currentTimeMillis();
		this.sessionListeners = sessionListeners;
		this.sessionAttributeListeners = sessionAttributeListeners;
//		this.servletConfig = servletConfig;
		this.sessionTimeoutSecond = 5 * 60 * 1000;
		
		notifyListener(Type.CREATE);
	}
	
	public String getId() {
		return id;
	}
	
	
	public Object getAttribute(String key) {
		return map.get(key);
	}
	
	public void setAttribute(String key, Object value) {
		Object obj = map.put(key, value);
		if (obj == null) {
			notifyListener(new SessionAttributeEvent(key, value, this), Type.ADD);
		} else {
			notifyListener(new SessionAttributeEvent(key, value, this), Type.REPLACE);
		}
	}
	
	public boolean removeAttribute(String key) {
		Object obj = map.remove(key);
		// 通知listener
		notifyListener(new SessionAttributeEvent(key, obj, this), Type.REMOVE);
		
		return null != obj;
	}
	
	
	public void invalidate() {
		expire();
	}
	
	public void markDiscard() {
		this.discard = true;
	}
	
	public void access() {
		this.lastAccessTime = System.currentTimeMillis();
	}
	
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public boolean isValid() {
		if (expire) {
			return false;
		}
		
		if (discard) {
			return false;
		}
		
		if ((System.currentTimeMillis() - lastAccessTime)  >  sessionTimeoutSecond) {
			isValid = false;
			expire = true;
		}
		
		return isValid;
	}
	
	public void expire() {
		if (discard) {
			discard();
			return;
		}
		// 通知sessionListener,销毁session
		try {
			notifyListener(Type.DESTORY);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (msgList != null) {
			msgList.clear();
		}
		
		if (push != null) {
			try {
				push.clear();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		// 清除session内容
		map.clear();
		map = null;
//		GroupManager
		SessionManager.getInstance().sessions.remove(id);
	}
	
	private void discard() {
		// 通知sessionListener,销毁session
		try {
			notifyListener(Type.DESTORY);
		} catch(Exception e) {
			e.printStackTrace();
		}

		if (msgList != null) {
			msgList.clear();
		}

		if (push != null) {
			try {
				push.discard();
			}catch(Exception e) {
				// 
				e.printStackTrace();
			}
		}
		// 清除session内容
		map.clear();
		map = null;
		//				GroupManager
		SessionManager.getInstance().sessions.remove(id);		
	}

	public void setPush(Push push) {
		this.push = push;
	}
	
	public Push getPush() {
		return push;
	}
	
	public void saveHistoryMsg(String command, byte[] body) {
		Tuple<String, byte[]> obj = new Tuple<String, byte[]>(command, body);
		synchronized (lock) {
			if (msgList == null) {
				msgList = new ArrayList<Tuple<String, byte[]>>();
			}
			if (msgList.size() < SessionManager.MAX_HISTORY_MSG_LEN) {
				msgList.add(obj);
			}
		}
	}
	
	private void doHistoryMsg() {
		if (msgList != null && msgList.size() > 0) {
			synchronized (lock) {
				for (Tuple<String, byte[]> obj : msgList) {
					// 积攒的内容写回通道
					push.push(this, obj.left, obj.right);
				}
				msgList.clear();
			}
		}
	}
	
	public void push(String command, byte[] body) {
		if (push != null && push.isPushable()) {
			doHistoryMsg();
			push.push(this, command, body);
		}else {
			saveHistoryMsg(command, body);
		}
	}
	
	private void notifyListener(SessionEvent event, Type type) {
		for (SessionAttributeListener listener : sessionAttributeListeners) {
			switch (type) {
			case ADD:
				listener.attributeAdd((SessionAttributeEvent) event);
				break;
			case REMOVE:
				listener.attributeRemoved((SessionAttributeEvent) event);
				break;
			case REPLACE:
				listener.attributeReplaced((SessionAttributeEvent) event);
				break;
			default:
				break;
			}
		}
	}
	
	private void notifyListener(Type type) {
		for (SessionListener listener : sessionListeners) {
			switch (type) {
			case CREATE:
				listener.sessionCreated(new SessionEvent(this));
				break;
			case DESTORY:
				listener.sessionDestoryed(new SessionEvent(this));
				break;
			default:
				break;
			}
		}
	}
}
