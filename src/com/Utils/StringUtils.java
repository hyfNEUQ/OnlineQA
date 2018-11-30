package com.Utils;

import org.springframework.stereotype.Repository;

@Repository("stringutil")
public class StringUtils {
	public StringUtils()
	{
		
	}
	//答案标准化
	public static String StandardDetailAnswer(String answer)
	  {
		  if(answer==null)return null;
	  	String []regex= {" 听语音","展开全部","本回答由网友推荐","已赞过","已踩过","评论","收起","<","本回答","被",
	  			"网友","采纳","由","谢邀"};
	  	for(int i=0;i<regex.length;i++) {
	  	answer=answer.replaceAll(regex[i], "");
	  	}
	  	return answer;
	  }
}
