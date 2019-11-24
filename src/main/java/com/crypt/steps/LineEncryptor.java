package com.crypt.steps;

import org.springframework.batch.item.ItemProcessor;

public class LineEncryptor implements ItemProcessor<String, String> {
	@Override
	public String process(String item) throws Exception {
		System.out.println("inside line processor....");
		return item + "aaaaaaaaaaaaaa";
	}
}
