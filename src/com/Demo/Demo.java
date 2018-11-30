package com.Demo;

import java.io.IOException;

import com.Factory.SearcherFactory;
import com.Interface.Searcher;
import com.Searcher.BaiDuJingYanSearcher;
import com.Searcher.BaiDuKnowsSearcher;
import com.Searcher.SearcherOf360;
import com.Searcher.ZhiHuSearcher;

public class Demo {
public static void main(String []args) throws IOException
{
	//测试百度问答
	Searcher searcherfactory=new BaiDuKnowsSearcher();
	BaiDuKnowsSearcher BDKS=(BaiDuKnowsSearcher) searcherfactory.GetSearcher();
	BDKS.InputQuestion("科比是多少顺位被选中的");
	BDKS.GenerateAnswer();
	BDKS.ShowResult();
	//测试百度经验
	searcherfactory=new BaiDuJingYanSearcher();
	BaiDuJingYanSearcher BDJY=(BaiDuJingYanSearcher) searcherfactory.GetSearcher();
	BDJY.InputQuestion("生蚝怎么做好吃");
	BDJY.GenerateAnswer();
	BDJY.ShowResult();
	//测试360问答
	searcherfactory=new SearcherOf360();
	SearcherOf360 s360=(SearcherOf360) searcherfactory.GetSearcher();
	s360.InputQuestion("科比签约了哪个品牌的球鞋");
	s360.GenerateAnswer();
	s360.ShowResult();
	//测试知乎
	searcherfactory=new ZhiHuSearcher();
	ZhiHuSearcher ZHS=(ZhiHuSearcher) searcherfactory.GetSearcher();
	ZHS.InputQuestion("怎么评价科比的职业生涯");
	ZHS.GenerateAnswer();
	ZHS.ShowResult();
}
}
