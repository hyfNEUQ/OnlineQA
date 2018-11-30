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
import org.springframework.stereotype.Component;

import com.Factory.SearcherFactory;
import com.Interface.Searcher;

@Component("zhihusearcher")
public class ZhiHuSearcher extends SearcherFactory implements Searcher {
	static ZhiHuSearcher zhihusearcher = null;
	List<ZhiHuThread> ZhiHuThreadlist = new ArrayList<>();

	public static ZhiHuSearcher GetInstance() {
		if (zhihusearcher == null)
			zhihusearcher = new ZhiHuSearcher();
		return zhihusearcher;
	}

	@Test
	public void test() throws IOException {
		ZhiHuSearcher BDKS = ZhiHuSearcher.GetInstance();
		BDKS.InputQuestion("科比的最高得分");
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
		System.out.println("知乎为您服务");
		// 先清空缓存
		DetailLinkList.clear();
		this.AnswerMap.clear();
		this.ZhiHuThreadlist.clear();
		this.question = question;

		String zhihuurl = "https://www.zhihu.com/search?type=content&q=" + question;
		Document doczhihu = Jsoup.connect(zhihuurl).get();
		Elements elements = doczhihu.select("div.ContentItem.AnswerItem").select("h2.ContentItem-title");
		for (Element e : elements) {

			// System.out.println(e.select("span.Highlight").text());//输出对应的近似问题的标题
			String x = e.select("meta[itemprop=url]").toString();
			String[] a = x.split("content=\"");
			String[] b = a[1].split("\"");
			this.DetailLinkList.add(b[0]);
		}
	}

	@Override
	public Map<String, String> GenerateAnswer() throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		for (String l : this.DetailLinkList) {
			ZhiHuThread zht = new ZhiHuThread();
			zht.detaillink = l;
			zht.start();
			ZhiHuThreadlist.add(zht);
		}
		do {

		} while (Judge() >= 1);
		return this.AnswerMap;
	}

	@Override
	public void ParseDetaliAnswerLink(String detaillink) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub

		Document DetailDoc = Jsoup.connect(detaillink).get();
		// span class="RichText ztext CopyrightRichText-richText"
		Elements elements = DetailDoc.select("div.List-item");
		String answer = null;
		for (Element e : elements) {
			answer = e.select("span.RichText.ztext.CopyrightRichText-richText").text();
		}
		this.AnswerMap.put(DetailDoc.select("h1.QuestionHeader-title").text(), answer);
	}

	@Override
	public int Judge() {
		// TODO Auto-generated method stub
		int sum = 0;
		for (ZhiHuThread l : ZhiHuThreadlist) {
			if (l.isAlive())
				sum++;
		}
		return sum;
	}
	public SearcherFactory GetSearcher() {
		return GetInstance();
	}
}

class ZhiHuThread extends Thread {
	public String detaillink;

	public void run() {
		ZhiHuSearcher zhs = ZhiHuSearcher.GetInstance();
		try {
			zhs.ParseDetaliAnswerLink(detaillink);
		} catch (Exception e) {
			System.out.println("解析detailink异常");
		}
	}
}
