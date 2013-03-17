package org.simple.parser.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.simple.parser.core.annotations.ParserDef;
import org.simple.parser.core.interfaces.IFileParser;
import org.simple.parser.core.interfaces.IFileBean;
import org.simple.parser.exceptions.SimpleParserException;


/**
 * Parent class for any Bean mapped to a file 
 * This class provides methods to readFile and retrive List<Beans>   
 * @author gokulvanan
 *
 */
public class FileBean<T extends IFileBean> implements IFileBean{

	@SuppressWarnings("rawtypes")
	public static IFileParser parser;
	public static File srcFile=null;

	private void initialize() throws SimpleParserException{
		
		@SuppressWarnings("unchecked")
		Class<T> childClass=(Class<T>) this.getClass();
		ParserDef parserAnno = childClass.getAnnotation(ParserDef.class);
		if(parserAnno == null)		throw new SimpleParserException("ParserDef Annotation not maped to model");
		String srcPath=parserAnno.srcFilePath();
		if(!srcPath.equals( "NULL"))
		{
			try
			{
				srcFile = new File(srcPath);
			}catch(Exception e)
			{
				throw new SimpleParserException("Error in reading input file "+e.getMessage());
			}	
		}
		
		try 
		{
			parser = parserAnno.parser().newInstance();
		} catch (Exception e) 
		{
			throw new SimpleParserException("Invalid parser class specified in parserDef");
		}
		parser.initialize(parserAnno);
	}

	@SuppressWarnings("unchecked")
	public  List<T> read() throws SimpleParserException{
		if(parser == null || srcFile == null)	initialize();
		parser.parse(srcFile, this.getClass());
		return parser.getParsedObjects();
	}

	@SuppressWarnings("unchecked")
	public  List<T> read(File newFile) throws SimpleParserException{
		if(parser == null)			initialize();
		parser.parse(newFile, this.getClass());
		return parser.getParsedObjects();
	}

	@SuppressWarnings("unchecked")
	public List<ErrorBean> getErrors(){
		return parser.getErrorObjects();
	}

	public boolean isSucessfull(){
		return parser.isSucessfull();
	}



}
