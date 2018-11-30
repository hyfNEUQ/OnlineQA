package com.Interface;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.Factory.SearcherFactory;

public interface Searcher {
public void InputQuestion(String question) throws IOException;
public Map<String,String> GenerateAnswer() throws IllegalArgumentException, IOException;
public  void ParseDetaliAnswerLink(String detaillink) throws IllegalArgumentException, IOException;
public int Judge();
public  SearcherFactory GetSearcher();
}
