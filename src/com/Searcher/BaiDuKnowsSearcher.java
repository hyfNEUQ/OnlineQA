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
import com.Utils.StringUtils;

public class BaiDuKnowsSearcher extends SearcherFactory implements Searcher {
	private static BaiDuKnowsSearcher BDKS = null;

	List<GetAnswerThread> GetAnswerThreadList = new ArrayList<>();

	// 获取单例
	public static BaiDuKnowsSearcher GetInstance() {
		if (BDKS == null)
			BDKS = new BaiDuKnowsSearcher();
		return BDKS;
	}

	// 工厂模式
	public SearcherFactory GetSearcher() {
		return GetInstance();
	}
    @Test
    public void test() throws IOException
    {
    	BaiDuKnowsSearcher BDKS=BaiDuKnowsSearcher.GetInstance();
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
		System.out.println("百度问答为您服务");
		// 先清空缓存
		DetailLinkList.clear();
		this.AnswerMap.clear();
		GetAnswerThreadList.clear();
		this.question = question;
		String baidusearchurl = "http://zhidao.baidu.com/search?ct=17&pn=0&tn=ikaslist&rn=3&fr=wwwt&word=" + question;
		Document doc = Jsoup.connect(baidusearchurl).get();
		Elements elements = doc.select("dl");
		// 第一遍遍历出答案的列表URL
		for (Element element : elements) {
			Elements Links = element.select("span.mr-8");
			for (Element link : Links) {
				if (link.toString().contains("href") && link.toString().contains("question")) {
					String x = link.toString();
					String[] a = x.split("href=");
					String[] b = a[1].split("target");
					String DetailUrl = b[0].replaceAll("\"", "");
					DetailLinkList.add(DetailUrl);
				}
			}
		}
		// 第二遍遍历出URL里的详细答案列表
		for (String detaillink : DetailLinkList) {
			//System.out.println("解析" + detaillink);
			GetAnswerThread t = new GetAnswerThread();
			t.detaillink = detaillink;
			t.start();
			GetAnswerThreadList.add(t);
		}

	}

	public int Judge() {
		int sum = 0;
		for (GetAnswerThread l : GetAnswerThreadList) {
			if (l.isAlive())
				sum++;
		}
		return sum;
	}

	@Override

	public void ParseDetaliAnswerLink(String detaillink) throws IllegalArgumentException {

		Document DetailDoc = null;
		try {
			DetailDoc = Jsoup.connect(detaillink).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Elements elements = DetailDoc.select("div.answer-text.mb-10.line");
		String DetailOriginAnswer = null;
		for (Element e : elements) {
			DetailOriginAnswer = e.text();
			break;
		}
        String tile=DetailDoc.select("h1.wgt-header-title-text").text();
        if(DetailOriginAnswer!=null)this.AnswerMap.put(tile, stringutil.StandardDetailAnswer(DetailOriginAnswer));
	}
	@Override
	public Map<String, String> GenerateAnswer() throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		for (String l : this.DetailLinkList) {
			GetAnswerThread gt = new GetAnswerThread();
			gt.detaillink = l;
			gt.start();
			GetAnswerThreadList.add(gt);
		}
		do
		{
			
		}while(Judge()>=1);
		return this.AnswerMap;
	}

}

class GetAnswerThread extends Thread {
	public String detaillink;

	public void run() {
		BaiDuKnowsSearcher BDKS = BaiDuKnowsSearcher.GetInstance();
		BDKS.ParseDetaliAnswerLink(detaillink);
	}
}
