package org.flush.erates.utils;

public class UnicodeConverter {
	
	public String convertUnicodeToString(String unicode) {
		String letters[] = unicode.split("U+005D");
		String convertedStr = "";
		for (String letter: letters) {
			convertedStr += letter;
		}
		return convertedStr;
	}
	
}
