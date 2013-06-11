package org.my.collections.sorting;

import java.util.Comparator;

/**
 * Parent Class that encapsulates all sorting classes
 * @author Gokulvanan
 *
 * @param <T>
 */
public abstract class Sort<T extends Comparable<T>> {

	/**
	 * Sorts array of Objects
	 * @param data
	 * @return
	 */
	public abstract T[] sort(T[] data);
	
	/**
	 * Sorts array of objects based on Compartor logic
	 * @param data
	 * @param c
	 */
	public abstract void sort(Object[] data, Comparator c);
	
	/**
	 * Sorts and return index of array in sorted order.. 
	 * Note: Array of objects is untouched
	 * @param data
	 * @return
	 */
	public abstract Integer[] indexSort(T[] data);
	
//	public abstract void sort(List<T> data);
//	
//	public abstract void sort(List data, Comparator c);
	

	// HELPER METHODS
	
	public boolean lesser(T c,T v){
		return c.compareTo(v)<0; // return true if c is less than v
	}
	
	public boolean lesser(Object v,Object w, Comparator c){
		return c.compare(v,w)<0; // return true if c is less than v
	}
	public void swap(Object[] data, int i, int j){
		Object buff = data[i];
		data[i] =data[j];
		data[j] = buff;
	}
}
