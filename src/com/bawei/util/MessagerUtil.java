package com.bawei.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bawei.po.messager.TextMessager;
import com.thoughtworks.xstream.XStream;

public class MessagerUtil {
	
	/**
	 * ����Ϣ�Ի�����װ����xml��ʽ���ַ���
	 * @param entity
	 * @return
	 */
	public static String objectToXml(TextMessager entity){
		
		XStream xstream = new XStream();
		// ������ڵ�  xml
		xstream.alias("xml", entity.getClass());
		return xstream.toXML(entity);
	}

	/**
	 * ��xml��ʽ����ת����map����
	 * @param request
	 * @return
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) {
		Map<String, String> messagerMap = new HashMap<String, String>();
		InputStream ins = null;
		try {
			System.out.println("-----װ����ʼ----");
			// ����xml�ļ��ַ�ʽ 1.dom 2.sax 3.jdom 4.dom4j
			ins = request.getInputStream();
			// ��ʼ����
			SAXReader reader = new SAXReader();
			// ���ö�ȡ���� �����ĵ�����
			Document document = reader.read(ins);
			// ��ø��ڵ�
			Element element = document.getRootElement();
			List<Element> cList = element.elements();
			
			for (Element item : cList) {
				messagerMap.put(item.getName(), item.getText());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// �ر���
				if(ins!=null)
					ins.close();
			System.out.println("-----ת������-----");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messagerMap;

	}
}