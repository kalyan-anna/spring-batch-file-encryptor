package com.crypt.steps;

import org.springframework.batch.item.ItemProcessor;

public class LineEncryptor implements ItemProcessor<String, String> {
	private static final int SHIFT = 4;
	
	/*
	 *  CITE : https://stackoverflow.com/questions/19108737/java-how-to-implement-a-shift-cipher-caesar-cipher
	 */
	@Override
	public String process(String line) throws Exception {
		String s = "";
	    int len = line.length();
	    for(int x = 0; x < len; x++){
	        char c = (char)(line.charAt(x) + SHIFT);
	        if (c > 'z')
	            s += (char)(line.charAt(x) - (26-SHIFT));
	        else
	            s += (char)(line.charAt(x) + SHIFT);
	    }
	    return s;
	}
}
