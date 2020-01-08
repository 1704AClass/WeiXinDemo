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
	 * �����΢�ŶԽ�ʹ�õ� �������ύ��Ϣ��΢�ŷ�����������GET������д�ķ�������ַURL�ϣ�GET����Я���������±���ʾ��
	 * ΢�����ְ�ȫ���ӿڵĶ��� ����������Լ��
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����
		// signature ΢�ż���ǩ����signature����˿�������д��token�����������е�timestamp������nonce������
		// timestamp ʱ���
		// nonce �����
		// echostr ����ַ���

		String token = "wojuedeok";
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");

		// ʹ�������ṩ����Ϣ���б���У�飬У��ɹ����� ��΢�ŷ��͵���Ϣ
		// ��ȷ�ϴ˴�GET��������΢�ŷ���������ԭ������echostr��������
		// ��ôȥ��֤��
		// 1����token��timestamp��nonce�������������ֵ������� 2�������������ַ���ƴ�ӳ�һ���ַ�������sha1����
		// 3�������߻�ü��ܺ���ַ�������signature�Աȣ���ʶ��������Դ��΢��
		List<String> list = new ArrayList<>();
		list.add(token);
		list.add(timestamp);
		list.add(nonce);
		// �����ֵ����� sortĬ��ʹ���ֵ�����
		Collections.sort(list);
		// ƴ���ַ���
		String newStr = "";
		for (String string : list) {
			newStr += string;
		}
		// ����sha1����
		String code = DigestUtils.sha1Hex(newStr);
		if (signature.equals(code))
			resp.getWriter().print(echostr);
		else
			System.out.println("����ʧ��");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		//���΢�Żظ�����
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		try {
			// ����΢�ŵ���Ϣ
			Map<String, String> messagerMap = MessagerUtil.xmlToMap(request);
			//��װ���õ���Ϣ
			TextMessager textMessager = new TextMessager();
			textMessager.setFromUserName(messagerMap.get("ToUserName"));
			textMessager.setToUserName(messagerMap.get("FromUserName"));
			//System.currentTimeMillis()����ֵ ��1970.1.1 00
			textMessager.setCreateTime(System.currentTimeMillis());
			textMessager.setMsgType(MessagerType.MESSAGE_TEXT);
			
			if(messagerMap.get("MsgType").equals(MessagerType.MESSAGE_TEXT)){
				String mess = messagerMap.get("Content");
				if(mess.startsWith("��") && mess.endsWith("?")){
					textMessager.setContent("��������ڣ���ʲô����ô��");
				}else if(mess.equals("����")){
					textMessager.setContent("���������Ĳ���");
				}else if(mess.equals("Ϊʲô��")){
					textMessager.setContent("�벻Ҫ��Ϊʲô<a href=\""
						+ "https://www.baidu.com"
						+ "\">�����ٶȣ�</a>");
				}else if(mess.equals("��")){
					textMessager.setContent("��û�а󶨣�����<a href=\""
							+ "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5ef29b29af35ac93&redirect_uri=http://140.143.232.206/SQServlet&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
							+ "\">�˴�</a>");
					
				}else{
					textMessager.setContent("��ʲô���⣺<a href=\""
							+ "https://www.baidu.com"
							+ "\">�����ٶȣ�</a>");
				}
			}else if(messagerMap.get("MsgType").equals(MessagerType.MESSAGE_IMAGE)){
				//��һ����
				System.out.println("���յ���һ��ͼƬ��image��");
				System.out.println(messagerMap.get("PicUrl"));
			}else{
				//��һ����
				System.out.println("�һ���̫��");
			}
			//�ɹ�
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