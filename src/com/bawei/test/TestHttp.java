package com.bawei.test;

import com.bawei.toweixin.MenuUtil;
import com.bawei.util.AccessTokenUtil;
import com.bawei.util.HttpRequestUtil;

public class TestHttp {
	public static void main(String[] args) {
		/*String string = HttpRequestUtil.createDefault().doGet("http://www.baidu.com");
		System.out.println(string);*/
		
		//token��Ҫ��ȡ
		String accessToken =  AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		//��ȡ�˵��ַ���
		String menu = MenuUtil.menu();
		//��������
		String string = HttpRequestUtil.createDefault().doPost(url, menu);
		System.out.println(string);
		
	}
}