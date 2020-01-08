package com.bawei.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONObject;
import com.bawei.util.HttpRequestUtil;


public class SQServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//测试看看是否能够获得code
		String code = request.getParameter("code");
		System.out.println(code);
		//去微信服务器中获取accessToken
		JSONObject joJsonObject = HttpRequestUtil.createDefault().doGetToJsonObject("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx5ef29b29af35ac93&secret=96622bdecdf7236592b5f272f0795a85&code="+code+"&grant_type=authorization_code");
		String openid = joJsonObject.getString("openid").toString();
		String access_token = joJsonObject.get("access_token").toString();
		
		
		
		
		//请求获取用户信息
		JSONObject userinfo = HttpRequestUtil.createDefault().doGetToJsonObject("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN");
		//封装信息到前端
		request.setAttribute("openid", userinfo.get("openid").toString());
		request.setAttribute("sex", userinfo.get("sex").toString().equals("1")?"男":"女");
		request.setAttribute("name", userinfo.get("nickname").toString());
		request.setAttribute("sheng", userinfo.get("province").toString());
		request.setAttribute("shi", userinfo.get("city").toString());
		request.setAttribute("tx", userinfo.get("headimgurl").toString());
	
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}
