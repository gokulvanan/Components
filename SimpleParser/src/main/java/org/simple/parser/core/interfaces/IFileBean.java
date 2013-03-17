package org.simple.parser.core.interfaces;

import java.io.File;
import java.util.List;

import org.simple.parser.core.ErrorBean;
import org.simple.parser.exceptions.SimpleParserException;


public interface IFileBean {
	
//	public void initialize() throws SimpleParserException;
	
	public  List<? extends IFileBean> read() throws SimpleParserException;
	
	public  List<? extends IFileBean> read(File newFile) throws SimpleParserException;
	
	public List<ErrorBean> getErrors();
	
	public boolean isSucessfull();

}
