package org.simple.parser.core.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.UrlValidator;
/**
 * Created with IntelliJ IDEA.
 * User: valarmathyvelan
 * Date: 1/13/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BasicValidators {

    public static class SpecialCharValidator implements CellValidator{

        private static Pattern patt = Pattern.compile(".*[~!@#$%^&*()<>;']+.*");
        public String valid(String data) {
            Matcher match = patt.matcher(data);
            if(match.matches())
                return new StringBuilder().append(" Cell contains invalid characters ").toString();
            else
                return null;
        }
    }

    public static class URLValidator implements CellValidator{

        public static UrlValidator validator = new UrlValidator();
        public String valid(String data){
            if(validator.isValid(data))
                return null;
            else
                return new StringBuilder("Cell is not a valid url").toString();
        }
    }

    public static class MandatoryValidator implements CellValidator {


        public String valid(String data) {
            if(data != null && !data.isEmpty())
                return null;
            else
                return "Mandatory Column can not be Empty";
        }
    }

    public static class IntegerValidator implements CellValidator{

        public String valid(String data){
            try{
                Integer.parseInt(data);
                return null;
            }catch (Exception e){
                return new StringBuilder("Cell content is not a valid Integer").toString();
            }
        }
    }
    
    public static class LongValidator implements CellValidator{

        public String valid(String data){
            try{
                Long.parseLong(data);
                return null;
            }catch (Exception e){
                return new StringBuilder("Cell content is not a valid Long").toString();
            }
        }
    }
    

    public static class WholeNumberValidator implements CellValidator{

        public String valid(String data){
            try{
                long val =Long.parseLong(data);
                if(val < 0) return "Cell content has a neagative numebr.. only whole numbers are allowed";
                return null;
            }catch (Exception e){
                return "Cell content is not a valid whole number";
            }
        }
    }
   
    public static class PositiveDoubleValidator implements CellValidator{

        public String valid(String data){
            try{
                double val = Double.parseDouble(data);
                if(val < 0) return "Cell content has a negative number.. only positive numbers allowed";
                return null;
            }catch (Exception e){
                return "Cell content is not a valid Double";
            }
        }
    }

    public static class DoubleValidator implements CellValidator{

        public String valid(String data){
            try{
                double val = Double.parseDouble(data);
                return null;
            }catch (Exception e){
                return new StringBuilder("Cell content is not a valid Double").toString();
            }
        }
    }
    
    /**
     * Sample Date validator that check for dates with DD-MM-YYYY format
     * @author gokulvanan
     *
     */
    public static class DateValidator implements CellValidator{
    	public static String dateFormat = "DD-MM-YYYY";
        public String valid(String data) {
            try{
            	new SimpleDateFormat().parse(data);
            	return null;
            }catch (ParseException e) {
            	return "Invalid Date format.. Date format should be : "+dateFormat;
			}
        }
    }
}
