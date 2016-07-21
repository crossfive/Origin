package com.crossfive.framework.util;

/**
 * 二元组
 * @author kira
 *
 * @param <S1>
 * @param <S2>
 */
public class Tuple<S1,S2> {

	public S1 left;
	
	public S2 right;
	
	public Tuple(S1 leftvalue, S2 rightValue) {
		this.left = leftvalue;
		this.right = rightValue;
	}

	public Tuple() {}
	
}
