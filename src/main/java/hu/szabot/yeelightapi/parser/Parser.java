package hu.szabot.yeelightapi.parser;

public class Parser {

	public static int convertToInt(String value)
	{
		return value!=null?Integer.valueOf(value):0;
	}

	public static String boolToString(boolean bool)
	{
		return bool?"on":"off";
	}
	
	public static boolean stringToBool(String bool)
	{
		return "on".equals(bool);
	}
	
	
}
