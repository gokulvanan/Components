package org.my.collections.utils;

public class SortUtils {

	public static boolean isSorted(Comparable[] c){
		for(int i=1,len=c.length;i<len; i++){
			if(c[i].compareTo(c[i-1])<0)	return false;
		}
		return true;
	}
	
	
}
