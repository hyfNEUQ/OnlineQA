package com.Searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.Factory.SearcherFactory;
import com.Interface.Searcher;

public class BaiDuJingYanSearcher extends SearcherFactory implements Searcher {
	static BaiDuJingYanSearcher BDJY = null;
	List<JYThread> JYHL = new ArrayList<>();

	public static BaiDuJingYanSearcher GetInstance() {
		if (BDJY == null)
			BDJY = new BaiDuJingYanSearcher();
		return BDJY;
	}
	@Test
    public void test() throws IOException
    {
		BaiDuJingYanSearcher BDKS=BaiDuJingYanSearcher.GetInstance();
    	BDKS.InputQuestion("生蚝怎么做");
    	Map<String, String> map = BDKS.GenerateAnswer();
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
		System.out.println("百度经验为您服务");
		// 先清空缓存
		DetailLinkList.clear();
		this.AnswerMap.clear();
		this.JYHL.clear();
		String url = "https://jingyan.baidu.com/search?word=" + question;
		Document docBaiDuJingYan = Jsoup.connect(url).get();
		Elements elements = docBaiDuJingYan.select("div.search-list").select("dl");
		for (Element e : elements) {
			this.DetailLinkList.add("https://jingyan.baidu.com" + e.select("dt").select("a").attr("href"));
		}
	}

	@Override
	public Map<String, String> GenerateAnswer() throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		for (String dl : this.DetailLinkList) {
			JYThread JYH = new JYThread();
			JYH.detaillink = dl;
			JYH.start();
			JYHL.add(JYH);
		}
		do {

		} while (Judge() >= 1);
		return this.AnswerMap;
	}

	@Override
	public void ParseDetaliAnswerLink(String detaillink) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		Document doc = Jsoup.connect(detaillink).get();
		String title = doc.select("h1.exp-title-h1").text();
		String content = doc.select("div.content-list-text").text();
		this.AnswerMap.put(this.stringutil.StandardDetailAnswer(title), this.stringutil.StandardDetailAnswer(content));
	}

	@Override
	public int Judge() {
		// TODO Auto-generated method stub
		int sum = 0;
		for (JYThread JYH : this.JYHL) {
			if (JYH.isAlive())
				sum++;
		}
		return sum;
	}
	@Override
	public SearcherFactory GetSearcher() {
		// TODO Auto-generated method stub
		return GetInstance();
	}
}

class JYThread extends Thread {
	String detaillink;

	public void run() {
		BaiDuJingYanSearcher BDJY = BaiDuJingYanSearcher.GetInstance();
		try {
			BDJY.ParseDetaliAnswerLink(detaillink);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
