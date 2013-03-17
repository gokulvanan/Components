package org.simple.parser;

import org.simple.parser.core.FileBean;
import org.simple.parser.core.annotations.ColumnDef;
import org.simple.parser.core.annotations.ParserDef;
import org.simple.parser.core.formatters.BasicFormatters;
import org.simple.parser.core.validators.BasicValidators;
import org.simple.parser.excel.ExcelParser;

@ParserDef(parser=ExcelParser.class,
srcFilePath="/home/gokulvanan/Documents/TestFile.xlsx",
noOfColumns=3, // 3 columns
startRow=2 // ignore row 1 header
)
public class SampleModel extends FileBean<SampleModel>{

	@ColumnDef(index=1, 
			validators={BasicValidators.MandatoryValidator.class},
			unique=true)
	public int id;
	@ColumnDef(index=2,
			formatter=BasicFormatters.ToUpperCase.class)
	public String name;
	@ColumnDef(index=3)
	public int age;

}


