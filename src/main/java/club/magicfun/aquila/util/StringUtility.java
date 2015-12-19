package club.magicfun.aquila.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

	public static String extractFirstFewDigits(String inputString) {
		Pattern p = Pattern.compile("[^0-9]*([0-9]*)[^0-9]*");
		Matcher m = p.matcher(inputString);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}
	
	public static boolean containsAny(String targetString, String searchChars){
		return targetString.contains(searchChars);
	}
	
	public static void main(String[] args){
		
		String str1 = "(51人气)";
		String str2 = "（637人气）";
		String str3 = "TEST 51 IKIK";
		String str4 = "X637X";
		
		System.out.println(StringUtility.extractFirstFewDigits(str1));
		System.out.println(StringUtility.extractFirstFewDigits(str2));
		System.out.println(StringUtility.extractFirstFewDigits(str3));
		System.out.println(StringUtility.extractFirstFewDigits(str4));
		
	}
}
