package org.simple.parser.core.validators;

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
public class BasicValidators {

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
                int val = Integer.parseInt(data);
                return null;
            }catch (Exception e){
                return new StringBuilder("Cell content is not a valid Integer").toString();
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
}
