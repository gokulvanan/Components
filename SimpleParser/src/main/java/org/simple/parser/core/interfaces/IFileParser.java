package org.simple.parser.core.interfaces;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.simple.parser.core.ErrorBean;
import org.simple.parser.core.annotations.ParserDef;
import org.simple.parser.exceptions.SimpleParserException;


/**
 * Main interface used by classes to access parser functionalities
 * @author gokulvanan
 *
 */
public interface IFileParser<T extends IFileBean> {

	
	public void initialize(Properties props) throws SimpleParserException;
	
	public void initialize(ParserDef props) throws SimpleParserException;
	
	public void parse(File file, Class<T> ouptutDTOClass ) throws SimpleParserException;
	
	public List<T> getParsedObjects();
	
	public List<ErrorBean> getErrorObjects();
	
	public boolean isSucessfull();
	
	
}
