package com.bawei.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.bawei.po.MessagerType;
import com.bawei.po.messager.TextMessager;
import com.bawei.util.MessagerUtil;
import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;

public class InstallServlet extends HttpServlet {

	/**
	 * 负责和微信对接使用的 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带参数如下表所示：
	 * 微信体现安全：接口的定义 体现能力和约定
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 参数 描述
		// signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		// timestamp 时间戳
		// nonce 随机数
		// echostr 随机字符串

		String token = "wojuedeok";
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");

		// 使用威信提供的信息进行本地校验，校验成功代表 是微信发送的信息
		// 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容
		// 怎么去验证？
		// 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密
		// 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		List<String> list = new ArrayList<>();
		list.add(token);
		list.add(timestamp);
		list.add(nonce);
		// 进行字典排序 sort默认使用字典排序
		Collections.sort(list);
		// 拼接字符串
		String newStr = "";
		for (String string : list) {
			newStr += string;
		}
		// 进行sha1加密
		String code = DigestUtils.sha1Hex(newStr);
		if (signature.equals(code))
			resp.getWriter().print(echostr);
		else
			System.out.println("接入失败");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		//解决微信回复乱码
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		try {
			// 接受微信的消息
			Map<String, String> messagerMap = MessagerUtil.xmlToMap(request);
			//封装公用的信息
			TextMessager textMessager = new TextMessager();
			textMessager.setFromUserName(messagerMap.get("ToUserName"));
			textMessager.setToUserName(messagerMap.get("FromUserName"));
			//System.currentTimeMillis()毫秒值 从1970.1.1 00
			textMessager.setCreateTime(System.currentTimeMillis());
			textMessager.setMsgType(MessagerType.MESSAGE_TEXT);
			
			if(messagerMap.get("MsgType").equals(MessagerType.MESSAGE_TEXT)){
				String mess = messagerMap.get("Content");
				if(mess.startsWith("你") && mess.endsWith("?")){
					textMessager.setContent("恩恩额，我在，有什么事情么？");
				}else if(mess.equals("测试")){
					textMessager.setContent("我再配合你的测试");
				}else if(mess.equals("为什么？")){
					textMessager.setContent("请不要问为什么<a href=\""
						+ "https://www.baidu.com"
						+ "\">请您百度！</a>");
				}else if(mess.equals("绑定")){
					textMessager.setContent("还没有绑定？请点击<a href=\""
							+ "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5ef29b29af35ac93&redirect_uri=http://140.143.232.206/SQServlet&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
							+ "\">此处</a>");
					
				}else{
					textMessager.setContent("有什么问题：<a href=\""
							+ "https://www.baidu.com"
							+ "\">请您百度！</a>");
				}
			}else if(messagerMap.get("MsgType").equals(MessagerType.MESSAGE_IMAGE)){
				//做一件事
				System.out.println("我收到了一个图片（image）");
				System.out.println(messagerMap.get("PicUrl"));
			}else{
				//做一件事
				System.out.println("我还不太懂");
			}
			//成功
			String xmlStr = MessagerUtil.objectToXml(textMessager);
			out.print(xmlStr);
			out.flush();
			
		} catch (Exception e) {

		}finally {
			if(out!=null){
				out.close();
			}
		}
	}

}
