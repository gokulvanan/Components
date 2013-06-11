package org.my.collections.sorting;

import java.util.Comparator;

/**
 * 
 * @author Gokulvanan
 *
 * @param <T>
 */
public class MergeSort<T extends Comparable<T>> extends Sort<T>{
	@SuppressWarnings("unchecked")
	@Override
	public T[] sort(T[] data) {
		int len = data.length;
		T[] buff = (T[]) new Comparable[len];
		recursiveSort(data,buff,0,len);
		return data;
	}

	
	@Override
	public void sort(Object[] data, Comparator c) {
		int len = data.length;
		Object[] buff =  new Object[len];
		recursiveSort(data,buff,0,len,c);
	}

	@Override
	public Integer[] indexSort(T[] data) {
		
		return null;
	}

	
	private void recursiveSort(T[] data, T[] buff, int i, int len) {
		if(i<len) return;
		int mid = (i + len) / 2;
		recursiveSort(data, buff, i, mid);
		recursiveSort(data, buff, mid, len);
		merge(data,buff,i,len,mid);
	}

	private void merge(T[] data, T[] buff, int low, int high, int mid) {
		for(int i=0; i<data.length; i++) buff[i] = data[i];
		for(int i=low; low < high; i++){
			if(low > mid)  							data[i] = buff[high--];
			else if(high < mid) 					data[i] = buff[low++];
			else if(lesser(buff[low],buff[high])) 	data[i] = buff[low++];
			else 							 		data[i] = buff[high--];
		}
	}
	
	private void recursiveSort(Object[] data, Object[] buff, int i, int len, Comparator<T> c) {
		if(i<len) return;
		int mid = (i + len) / 2;
		recursiveSort(data, buff, i, mid,c);
		recursiveSort(data, buff, mid, len,c);
		merge(data,buff,i,len,mid,c);
	}

	private void merge(Object[] data, Object[] buff, int low, int high, int mid,Comparator<T> c) {
		for(int i=0; i<data.length; i++) buff[i] = data[i];
		for(int i=low; low < high; i++){
			if(low > mid)  							data[i] = buff[high--];
			else if(high < mid) 					data[i] = buff[low++];
			else if(lesser(buff[low],buff[high],c)) 	data[i] = buff[low++];
			else 							 		data[i] = buff[high--];
		}
	}

	
}
