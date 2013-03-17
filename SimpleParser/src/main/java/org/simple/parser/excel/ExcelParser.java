package org.simple.parser.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.simple.parser.core.ErrorBean;
import org.simple.parser.core.ErrorBean.ColErrors;
import org.simple.parser.core.annotations.ColumnDef;
import org.simple.parser.core.annotations.ParserDef;
import org.simple.parser.core.formatters.CellFormatter;
import org.simple.parser.core.interfaces.IFileBean;
import org.simple.parser.core.interfaces.IFileParser;
import org.simple.parser.core.validators.CellValidator;
import org.simple.parser.exceptions.SimpleParserException;




//TODO Create custom exception object
public class ExcelParser<T extends IFileBean> implements IFileParser<T>{

	//mandatory 
	private int noOfColumns=-1;

	//optional
	private int noOfRows=-1;
	private int sheetNo=-1;
	private int startRow=-1;
	private int startCol=-1;
	private int maxNoOfRows=-1;

	private List<T> fileObjList=null;
	private List<ErrorBean> errorList=null;
	
	/**
	 * Initialise parser configurations from {@link ParserDef} annotation file
	 */
	public void initialize(ParserDef props) throws SimpleParserException  {
		try{
			this.noOfColumns= props.noOfColumns();
			this.noOfRows=props.noOfRows();
			this.sheetNo=props.sheetNo();
			this.sheetNo--;
			this.startRow=props.startRow();
			this.startRow--;
			this.startCol=props.startCol();
			this.startCol--;
			this.maxNoOfRows=props.maxNoOfRows();
		}catch (Exception e) {
			throw new SimpleParserException("Error in configuration msg"+e.getMessage());
		}
	}

	/**
	 * Initialise parser configurations from property file
	 */
	public void initialize(Properties props) throws SimpleParserException  {
		try{
			this.noOfColumns= Integer.parseInt(props.getProperty("NO_OF_COLUMNS", "-1"));
			this.noOfRows=Integer.parseInt(props.getProperty("NO_OF_ROWS", "-1"));
			this.sheetNo=Integer.parseInt(props.getProperty("SHEET_NO", "1"));
			this.sheetNo--;
			this.startRow=Integer.parseInt(props.getProperty("START_ROW", "1"));
			this.startRow--;
			this.startCol=Integer.parseInt(props.getProperty("START_COL", "1"));
			this.startCol--;
			this.maxNoOfRows=Integer.parseInt(props.getProperty("MAX_ROWS", "-1"));
		}catch (Exception e) {
			throw new SimpleParserException("Error in configuration msg"+e.getMessage());
		}
	}


	public void parse(File fileObj, Class<T> ouptutDTOClass) throws SimpleParserException {
		
		InputStream in;
		try
		{
			in = new FileInputStream(fileObj);
		} catch (FileNotFoundException e1)
		{
			throw new SimpleParserException("Invalid File path");
		}
		if(noOfColumns == -1)	throw new SimpleParserException("No of Columns is manadatory for Excel parsing");
		
		Workbook w;
		String fileName=fileObj.getName();
		String[] ext= fileName.split("\\.");
		String type = ext[ext.length-1];
		
		try
		{
			if(type.equalsIgnoreCase("xls"))	w = new HSSFWorkbook(in);
			else								w = new XSSFWorkbook(in);	
		}catch(IOException i)
		{
			throw new SimpleParserException("Error in parsing file using appache.poi lib.. File not a valid excel");
		}
		
		
		fileObjList= new ArrayList<T>();
		errorList=new ArrayList<ErrorBean>();

		Map<Integer,Field> flds= new HashMap<Integer,Field>();
		Map<Integer,Class<? extends CellValidator>[]> validators = new HashMap<Integer,Class<? extends CellValidator>[]>();
        Map<Integer,CellFormatter> formatters = new HashMap<Integer, CellFormatter>();
        Map<Integer,Boolean> unique = new HashMap<Integer, Boolean>();
		int maxIndex=0;
		try
		{
			Field[] allFlds= ouptutDTOClass.getDeclaredFields();
			for(Field fld : allFlds){
				fld.setAccessible(true);
				ColumnDef colDef =fld.getAnnotation(ColumnDef.class);
				if(colDef == null) continue;
				int index = colDef.index();
				flds.put(index, fld);
				validators.put(index,colDef.validators());
                formatters.put(index, colDef.formatter().newInstance());
                unique.put(index, colDef.unique());
				maxIndex = (maxIndex < index) ? index : maxIndex;
			}
			
		}catch (Exception e) {
//			e.printStackTrace();
			throw new SimpleParserException("Error in parsing annotations.. Error msg : "+e.getMessage());
		}

		if(maxIndex > noOfColumns) throw new SimpleParserException("Error in annoation configuration. Col index exceed noOf Columns declared");

		Sheet sheet = w.getSheetAt(sheetNo);
		noOfRows =(noOfRows == -1) ? sheet.getLastRowNum()+1 : noOfRows;
	
		int colWidth=this.noOfColumns-this.startCol;
		
		if(colWidth <= 0) throw new SimpleParserException("Error startCol value exceeds noOfColumns, Check ParserDef/Property file configuration ");
			
		Map<Integer,Map<String,Integer>> colMap = new HashMap<Integer, Map<String,Integer>>();
		int actualRowCount=0;
		L2: for (int i = startRow; i < this.noOfRows; i++)
		{
			ErrorBean err = new ErrorBean(i+1);
			T obj;
			try
			{
				obj = ouptutDTOClass.newInstance();
			}
			catch(Exception er)
			{
				throw new SimpleParserException("Error in creating class instace from input class Object using reflection.. Check JVM security settings");
			}
			
			Row row = sheet.getRow(i);
			if(row == null) 
				continue L2; // ignore blank rows
			int j=0;
			int emptyCount=0;
			try{
				actualRowCount++;
				L1:for (j = this.startCol; j < this.noOfColumns; j++) 
				{
					int col=j+1;
					Cell cell = row.getCell(j);
					Field fld = flds.get(col);
					boolean uniq = unique.get(col);
					if(fld == null) continue L1;// ignore columns not mapped to DTO objects
					String data=(cell == null) ? "" : getCellValAsString(cell);// added to prevent null pointer exception for unused columns

					if(data.trim().isEmpty())
					{
						emptyCount++;
					}
					else{
						if(uniq)// unique constraint check
						{
							Map<String,Integer> m = colMap.get(col);
							if(m== null)
							{	
								m=new HashMap<String, Integer>();
								m.put(data, 1);
							}
							else
							{
								if(m.containsKey(data))
								{
									err.addColError(new ColErrors(j+1, "Unique contraint violated"));// col error
									continue L1;
								}
								else
									m.put(data,1);
							}
							colMap.put(col, m);
						}
					}
					Class<? extends CellValidator>[] validatorclses = validators.get(col);
					for(Class<? extends CellValidator> validatorCls : validatorclses){
						CellValidator validator = validatorCls.newInstance();
						String errorMsg=validator.valid(data) ;
						if(errorMsg  != null){// invalid case
							err.addColError(new ColErrors(j+1, errorMsg));// col error
							continue L1;
						}
					}
					CellFormatter formatter = formatters.get(col);
					data =  formatter.format(data);
					fld.setAccessible(true);
					fld.set(obj, typeConversion(fld.getType(),data));
				}
			}catch (Exception e) { // Added to coninute processing other rows
				err.addColError(new ColErrors(j+1,e.getMessage()));
				j=0;// make sure this obj is not added to fileObjList
			}
			
			if(!err.hasErrors() )// completed full loop without error caseobject
				this.fileObjList.add(obj);
			else
			{
				if(emptyCount != colWidth){ // All col empty is not an error discard this row and process other rows
					this.errorList.add(err);
				}
				else{
					actualRowCount--;// empty row case
				}
				
			}
		}
		
		if(maxNoOfRows != -1 && maxNoOfRows < actualRowCount)
			throw new SimpleParserException("Exceed maximun number("+maxNoOfRows+") of permitted rows ");

	}

	private String getCellValAsString(Cell cell) throws SimpleParserException {
		switch(cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC: 	return cell.getNumericCellValue()+"";
			case Cell.CELL_TYPE_BLANK: 		return "";
			case Cell.CELL_TYPE_BOOLEAN: 	return cell.getBooleanCellValue()+"";
			case Cell.CELL_TYPE_ERROR: 		throw new SimpleParserException("Invalid Cell type");
			case Cell.CELL_TYPE_FORMULA: 	return cell.getCellFormula();
			default:						return cell.getStringCellValue();
		}
	}

	public List<T> getParsedObjects() {
		return this.fileObjList;
	}


	public List<ErrorBean> getErrorObjects() {
		return this.errorList;
	}

	public boolean isSucessfull() {
		return (this.errorList.size() == 0);
	}


	private Object typeConversion(Class<?> clazz,String val) throws ParseException
	{

		if(val == null) 						return null;
		String name = clazz.getSimpleName();
		if(name.equalsIgnoreCase("Short") || name.equalsIgnoreCase("short"))	return (short)Double.parseDouble(val);
		if(name.equalsIgnoreCase("Integer") || name.equalsIgnoreCase("int"))	return (int) Double.parseDouble(val);
		if(name.equalsIgnoreCase("Long"))										return (long) Double.parseDouble(val);
		if(name.equalsIgnoreCase("Float"))										return (float) Double.parseDouble(val);
		if(name.equalsIgnoreCase("Double"))										return Double.parseDouble(val);
		if(name.equalsIgnoreCase("Date")){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // MOVE THIS TO A PROPERTY FILE
			return dateFormat.parse(val);
		}

		return val;
	}


}
