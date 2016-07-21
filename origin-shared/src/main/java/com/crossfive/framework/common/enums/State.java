package com.crossfive.framework.common.enums;

public enum State {
	ACTIVE((short)0),
	FOBBIDEN((short)1),
	DEPRECATED((short)2);
	short state;
	State(short state) {
		this.state = state;
	}
	public short state() {
		return state;
	}
}
