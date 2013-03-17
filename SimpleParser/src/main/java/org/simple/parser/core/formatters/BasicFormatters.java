package org.simple.parser.core.formatters;

public class BasicFormatters {

	public static class ToUpperCase implements CellFormatter{

		public String format(String input) {
			
			if(input != null && input.trim().length() > 0)
				return input.toUpperCase();
			return input;
		}
		
	}

}
