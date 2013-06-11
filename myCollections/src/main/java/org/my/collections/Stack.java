package org.my.collections;

public interface Stack<Item> {

	public void push(Item obj);
	
	public Item pop();
	
	public boolean isEmpty();
	
	public int size();
}
