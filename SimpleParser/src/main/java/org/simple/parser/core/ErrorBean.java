package org.simple.parser.core;

import java.util.LinkedList;
import java.util.List;

public class ErrorBean {

	public static class ColErrors{
		
		private int col;
		private String msg;
		public ColErrors(int col, String msg) {
			this.col=col;
			this.msg=msg;
		}
		
		public int getCol(){
			return this.col;
		}
		
		public String getMsg(){
			return this.msg;
		}
	}
	
	private int row=-1;
	private List<ColErrors> colErros=null;
	
	public ErrorBean(int row)
	{
		this.row=row;
		this.colErros=new LinkedList<ErrorBean.ColErrors>();
	}
	
	public void addColError(ColErrors obj){
		this.colErros.add(obj);
	}
	
	public boolean hasErrors(){
		return this.colErros.size() > 0;
	}
	
	public int getRow(){
		return this.row;
	}
	
	public List<ColErrors> getColErrors(){
		return this.colErros;
	}
	
}
