package com.crossfive.framework.common.dto;

public class UserDto {

	/** user的唯一键 */
	private String id;
	
	/** user的编号 */
	private String userId;
	
	/** playerId */
	private long playerId;
	
	/** 用户名 */
	private String userName;
	
	/** 运营来源 */
	private String yx;
	
	/** 上一次登陆时间 */
	private long lastLoginTime;
	
	/** 身份证id */
	private String sfId;
	
	/** 防沉迷状态 */
	private int antiAddiction;
	
	/** 在线时间，用户防沉迷统计 */
	private long onlineTime;
	
	/** 是否首次登陆 */
	private boolean isFirstLogin;

	public String getId() {
		if (id == null) {
			synchronized(this) {
				if (id == null) {
					id = new StringBuilder(30).append(userId).append("-").append(yx).toString();
				}
			}
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getYx() {
		return yx;
	}

	public void setYx(String yx) {
		this.yx = yx;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public int getAntiAddiction() {
		return antiAddiction;
	}

	public void setAntiAddiction(int antiAddiction) {
		this.antiAddiction = antiAddiction;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * 创建新的UserDto
	 * @param userId
	 * @param yx
	 * @return
	 */
	public static UserDto getNewUserDto(String userId, long playerId, String yx) {
		UserDto userDto = new UserDto();
		userDto.setUserId(userId);
		userDto.setPlayerId(playerId);
		userDto.setYx(yx);
		userDto.setLastLoginTime(System.currentTimeMillis());
		return userDto;
	}
	
}
