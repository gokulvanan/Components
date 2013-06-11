package org.my.collections;

/**
 * Every operation takes guaranteed constant time
 * Uses extra time and space to deal with links
 * @author Gokulvanan
 *
 */
public class LinkedStack<Item> implements Stack<Item>{

	private class Node<T>{
		T val = null;
		Node<T> next = null;
		
		Node(T val,Node<T> next){
			this.val=val;
			this.next=next;
		}
	}
	
	private Node<Item> first = null;
	private int size = 0;
	
	public void push(Item obj) {
		first = new Node(obj,first);
		size++;
	}
	
	public Item pop() {
		Item output = first.val;
		first = first.next;
		size--;
		return output;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}
	
}
