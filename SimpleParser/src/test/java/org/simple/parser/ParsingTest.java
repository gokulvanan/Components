package org.simple.parser;

import java.util.List;

import org.junit.Test;
import org.simple.parser.core.ErrorBean;
import org.simple.parser.core.ErrorBean.ColErrors;

/**
 * Unit test for simple App.
 */
public class ParsingTest {
   
    @Test
    public void parseExcelFileTest() throws Exception
    {
    	System.out.println("Starting test ");
    	SampleModel model = new SampleModel();
    	List<SampleModel> objs =model.read();
    	System.out.println(model.isSucessfull());
    	if(model.isSucessfull())
    	{
    		System.out.println(objs.size());
    		for(SampleModel obj : objs){
//    			System.out.println(obj.appName);
//    			System.out.println(obj.cid);
//    			System.out.println(obj.expectedResult);
//    			System.out.println(obj.ipaddress);
//    			System.out.println(obj.status);
//    			System.out.println(obj.useragent);
//    			obj.status="Updated";
    			obj.age=500;
    		}
    		model.update();
    		System.out.println(model.isSucessfull());
    		
    	}else{
    		List<ErrorBean> errors=model.getErrors();
    		for(ErrorBean err : errors){
    			System.out.println(err.getRow());
    			for(ColErrors colerr : err.getColErrors())
    			{
    				System.out.println(colerr.getCol());
    				System.out.println(colerr.getMsg());
    			}
    		}
    	}
    	
    	System.out.println("End of test");
    }
}
