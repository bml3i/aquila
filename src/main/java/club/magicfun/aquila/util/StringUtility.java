package club.magicfun.aquila.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

	public static String extractFirstFewDigits(String inputString) {
		Pattern p = Pattern.compile("[.]*([0-9]*)[.]*");
		Matcher m = p.matcher(inputString);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}
}
