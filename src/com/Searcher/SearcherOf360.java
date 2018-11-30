package com.Searcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Component;

import com.Factory.SearcherFactory;
import com.Interface.Searcher;

@Component("searcherof360")
public class SearcherOf360 extends SearcherFactory implements Searcher {
	public static SearcherOf360 search360 = null;
	public List<Searcher360Thread> stlist = new ArrayList<>();

	public static SearcherOf360 GetInstance() {
		if (search360 == null)
			search360 = new SearcherOf360();
		return search360;
	}

	@Test
	public void test() throws IOException {
		
		String x = "科比";
		SearcherOf360.GetInstance().InputQuestion(x);
		Map<String, String> map = SearcherOf360.GetInstance().GenerateAnswer();
		Set<java.util.Map.Entry<String, String>> entrySet = map.entrySet();
		System.out.println();
		for (java.util.Map.Entry<String, String> entry : entrySet) {
			System.out.println("Question：" + entry.getKey());
			System.out.println("Answer：" + entry.getValue());
		}
	}

	@Override
	public void InputQuestion(String question) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("360问答为您服务");
		// 先清空缓存
		DetailLinkList.clear();
		this.AnswerMap.clear();
		this.stlist.clear();
		String url = "http://wenda.so.com/search/?q=" + question;
		Document doc = Jsoup.connect(url).get();
		org.jsoup.select.Elements elements = doc.select("ul.qa-list").select("li.item.js-normal-item")
				.select("div.qa-i-hd");
		String urlhead = "https://wenda.so.com";
		for (Element e : elements) {
			String x = e.toString();
			String[] a = x.split("href=");
			String[] b = a[1].split("><b>");
			String detailurl = urlhead + b[0].replaceAll("\"", "");
			this.DetailLinkList.add(detailurl);
		}
	}

	@Override
	public Map<String, String> GenerateAnswer() throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		for (String dl : DetailLinkList) {
			Searcher360Thread st = new Searcher360Thread();
			st.detaillink = dl;
			try
			{st.run();
			}
			catch(Exception e)
			{
				
			}
		}
		do {

		} while (Judge() >= 1);
		return this.AnswerMap;
	}

	@Override
	public void ParseDetaliAnswerLink(String detaillink) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		Document doc=null;
		try{
			 doc= Jsoup.connect(detaillink).get();
		}
		catch(Exception e)
		{
			return ;
		}
		Elements elements = doc.select("div.grid.clearfix");
		String title = elements.select("h2.title.js-ask-title").text();
		String answer = elements.select("div.resolved-cnt").text();
		if(title!=null &&answer!="")this.AnswerMap.put(title, answer);
	}

	@Override
	public int Judge() {
		// TODO Auto-generated method stub
		int sum = 0;
		for (Searcher360Thread st : stlist) {
			if (st.isAlive())
				sum++;
		}
		return sum;
	}
	public SearcherFactory GetSearcher() {
		return GetInstance();
	}
}

class Searcher360Thread extends Thread {
	public String detaillink;
	public void run() {
		try {
			SearcherOf360.GetInstance().ParseDetaliAnswerLink(detaillink);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
