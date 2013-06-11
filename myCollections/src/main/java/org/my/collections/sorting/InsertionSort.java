package org.my.collections.sorting;

import java.util.Comparator;

/**
 * Ideal to use for short arrays 
 * Can be used to check arrays that are already sorted.. (Aprox N)
 * Worst for reverse sorted arrays O(N^2)
 * @author Gokulvanan
 *
 * @param <T>
 */
public class InsertionSort<T extends Comparable<T>> extends Sort<T>{

	@Override
	public T[] sort(T[] data) {
		for(int i=1,len=data.length; i<len; i++){
			for(int j=i-1; j>=0 && lesser(data[j+1],data[j]); j--)	
				swap(data, j+1, j);
			
		}
		return data;
	}

	@Override
	public void sort(Object[] data, Comparator c) {
		for(int i=1,len=data.length; i<len; i++){
			for(int j=i-1; j>=0 && lesser(data[j+1],data[j],c); j--)	
				swap(data,j+1,j);
		}
	}

	@Override
	public Integer[] indexSort(T[] data) {
		int len=data.length;
		Integer[] index = new Integer[len];
		for(int i=0; i<len; i++) index[i]=i;
		for(int i=1; i<len; i++){
			for(int j=i-1; j>=0 && lesser(data[index[j+1]], data[index[j]]); j--)
				swap(index, j+1, j);
		}
		return index;
	}
}
