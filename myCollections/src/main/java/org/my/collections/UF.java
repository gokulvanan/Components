package org.my.collections;

/**
 * Simple Implementation of Union Find Algo.. (Weighted Quick Union with Path Compression);
 * Time to Instantiate of Object O(N)
 * Time to perform union of of 2 values O(lgN) 
 * Time to lookup if connected = O(lg*N) ~= 1
 * @author Gokulvanan
 *
 */
public class UF {
	
	int [] data = null;
	int [] size = null;
	int count = 0;
	
	/**
	 * Initialize data structure
	 * @param N
	 */
	public UF(int N){
		count = N;
		data = new int[N];
		size = new int[N];
		for(int i=0 ; i<N; i++){
			data[i]=i;
			size[i]=1;
		}
	}
	
	/**
	 * Establish connection between two elements
	 * @param p
	 * @param q
	 * @throws Exception
	 */
	public void union (int p, int q) throws Exception{
		int rootP = parent(p);
		int rootQ = parent(q);
		if(rootP == rootQ) throw new Exception("The elements are already connected");
		if(size[rootP] > size[rootQ]) 	{ data[rootQ] = data[rootP]; size[rootP] += size[rootQ];}
		else 							{ data[rootP] = data[rootQ]; size[rootQ] += size[rootP];}
		count --;// no of components reduces for every union operation
	}
	
	/**
	 * Return true if connected
	 * @param p
	 * @param q
	 * @return
	 */
	public boolean connected(int p, int q){
		return parent(p) == parent(q);
	}
	
	/**
	 * No of components
	 * @return
	 */
	public int noOfComponents(){
		return count;
	}
	
	/**
	 * return parent of element 
	 * @param i
	 * @return
	 */
	public int parent(int i){
		while(i != data[i]){
			i = data[i];
			data[i]=data[data[i]]; // path compression
		}
		return i;
	}
}
