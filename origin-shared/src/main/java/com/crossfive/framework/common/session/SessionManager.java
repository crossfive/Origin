package com.crossfive.framework.common.session;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.crossfive.framework.common.session.listener.SessionAttributeListener;
import com.crossfive.framework.common.session.listener.SessionListener;
/**
 * Session管理器
 * @author kira
 *
 */
public class SessionManager {

	private static final SessionManager instance = new SessionManager();
	
	// 重复sessionid个数
	protected int duplicates = 0;
	
	protected Random random;

	protected final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	protected final List<SessionAttributeListener> sessionAttributeListeners = new ArrayList<SessionAttributeListener>();
	
	protected final List<SessionListener> sessionListeners = new ArrayList<SessionListener>();
	
	private ScheduledThreadPoolExecutor sessionExecutor;
	
	volatile static int MAX_HISTORY_MSG_LEN = 100;
	
	private boolean checkThreadStarted = false;
	
	protected volatile MessageDigest digest;
	
	private String entropy;
	
	private SessionManager() {}
	
	public static SessionManager getInstance() {
		return instance;
	}
	
	public void startSessionCheckThread() {
		if (checkThreadStarted) {
			return;
		}
		
		synchronized (this) {
			if (!checkThreadStarted) {
				checkThreadStarted = true;
			}
			
			sessionExecutor = new ScheduledThreadPoolExecutor(1);
			sessionExecutor.scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					try{
						Collection<Session> allSession = sessions.values();
						for (Session session : allSession) {
							try{
								if (!session.isValid()) {
									System.err.println(session.id + " 过期");
									session.invalidate();
									continue;
								}
								
								Push push = session.getPush();
								if (push != null && push.isPushable()) {
									// 可用 （是否非null就可以推送呢）
									push.heartbeat();
								}
								
							}catch(Exception e) {
								session.invalidate();
								e.printStackTrace();
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			}, 20, 20, TimeUnit.SECONDS);
		}
		
	}
	
	public void access(String sessionId) {
		if (null == sessionId) {
			return;
		}
		Session session = getSession(sessionId);
		if (session != null) {
			session.access();
		}
	}
	
	public Session getSession(String sessionId) {
		return getSession(sessionId, false);
	}
	
	public Session getSession(String sessionId, boolean allowCreate) {
		if (allowCreate) {
			Session session = null == sessionId ? null : _getSession(sessionId);
			if (null == session) {
				session = new Session(generateSessionId(), sessionAttributeListeners, sessionListeners);
				sessions.put(session.getId(), session);
				sessionId = session.getId();
				return session;
			}
		}
		return null == sessionId ? null : _getSession(sessionId);
	}
	
	private Session _getSession(String sessionId) {
		Session session = sessions.get(sessionId);
		return session;
	}
	
	public void addSessionListener(SessionListener sessionListener) {
		sessionListeners.add(sessionListener);
	}
	
	public void addSessionAttributeListener(SessionAttributeListener sessionAttributeListener) {
		sessionAttributeListeners.add(sessionAttributeListener);
	}
	
	protected synchronized String generateSessionId() {
		byte[] random = new byte[16];
		String jvmRoute = System.getProperty("jvmRoute");
		String result = null;
		
		StringBuilder builder = new StringBuilder();
		do {
			int resultLenBytes = 0;
			if (result != null) {
				builder = new StringBuilder();
				duplicates++;
			}
			
			while(resultLenBytes < 16) {
				getRandomBytes(random);
				random = getMessageDigest().digest(random);
				for (int i = 0; i < random.length && resultLenBytes < 16; i++) {
					byte b1 = (byte) ((random[i] & 0xf0) >> 4);
					byte b2 = (byte) (random[i] & 0x0f);
					if (b1 < 10) {
						builder.append((char)('0' + b1));
					}else {
						builder.append((char)('A' + (b1 - 10)));
					}
					
					if (b2 < 10) {
						builder.append((char)('0' + b2));
					}else {
						builder.append((char)('A' + (b2 - 10)));
					}
					resultLenBytes++;
				}
			}
			if (jvmRoute != null) {
				builder.append('.').append(jvmRoute);
			}
			result = builder.toString();
			
			// 如果sessions中包含result，则一直循环随机生成key
		} while (sessions.containsKey(result));
		
		return result;
	}
	
	
	protected void getRandomBytes(byte[] b) {
		if (this.random == null) {
			long seed = System.currentTimeMillis();
			char[] entropy = getEntropy().toCharArray();
			for (int i = 0; i <  entropy.length; i++) {
				// ???
				long update = ((byte) entropy[i] << (i % 8)* 8);
				seed ^= update;
			}
			this.random = new Random();
			this.random.setSeed(seed);
		}
		this.random.nextBytes(b);
	}
	
	private MessageDigest getMessageDigest() {
		if (this.digest == null) {
			try {
				this.digest = MessageDigest.getInstance("MD5");
			} catch (Exception e) {
				
			}
		}
		return digest;
	}
	
	private String getEntropy() {
		if (entropy == null) {
			entropy = this.toString();
		}
		return entropy;
	}
}
