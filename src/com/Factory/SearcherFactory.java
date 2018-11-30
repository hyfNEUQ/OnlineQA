package com.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.Utils.StringUtils;

public class SearcherFactory {
	protected String question;
	protected List<String> DetailLinkList = new ArrayList<>();
	public Map<String, String> AnswerMap=new HashMap<String,String>();
	@Autowired
	public StringUtils stringutil;
	public void ShowResult() {
		Map<String, String> map = AnswerMap;
		Set<java.util.Map.Entry<String, String>> entrySet = map.entrySet();
		System.out.println();
		for (java.util.Map.Entry<String, String> entry : entrySet) {
			System.out.println("Question：" + entry.getKey());
			System.out.println("Answer：" + entry.getValue());
		}
	}
}
