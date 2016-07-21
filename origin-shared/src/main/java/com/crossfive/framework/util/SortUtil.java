package com.crossfive.framework.util;

public class SortUtil {

	/**
	 * 选择排序(首尾同排)
	 * @param r	 排序对象
	 * @param n 对象长度
	 */
	public void SelectSort(int r[],int n) {  
	    int min ,max, tmp;  
	    for (int i=1 ; i <= n/2; i++) {    
	        // 做不超过n/2趟选择排序   
	        min = i; max = i ; //分别记录最大和最小关键字记录位置  
	        for (int j= i+1; j<= n-i; j++) {  
	            if (r[j] > r[max]) {   
	                max = j ; 
	                continue ;   
	            }    
	            if (r[j]< r[min]) {   
	                min = j ;   
	            }     
	      }    
	      //该交换操作还可分情况讨论以提高效率  
	      tmp = r[i-1]; r[i-1] = r[min]; r[min] = tmp;  
	      tmp = r[n-i]; r[n-i] = r[max]; r[max] = tmp;   
	  
	    }   
	}  
}
