package com.bawei.test;

import com.bawei.toweixin.MenuUtil;
import com.bawei.util.AccessTokenUtil;
import com.bawei.util.HttpRequestUtil;

public class TestHttp {
	public static void main(String[] args) {
		/*String string = HttpRequestUtil.createDefault().doGet("http://www.baidu.com");
		System.out.println(string);*/
		
		//token需要获取
		String accessToken =  AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		//获取菜单字符串
		String menu = MenuUtil.menu();
		//发送请求
		String string = HttpRequestUtil.createDefault().doPost(url, menu);
		System.out.println(string);
		
	}
}
