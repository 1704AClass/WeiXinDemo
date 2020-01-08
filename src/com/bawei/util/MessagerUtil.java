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
	 * 把消息对话对象装换成xml格式的字符串
	 * @param entity
	 * @return
	 */
	public static String objectToXml(TextMessager entity){
		
		XStream xstream = new XStream();
		// 定义根节点  xml
		xstream.alias("xml", entity.getClass());
		return xstream.toXML(entity);
	}

	/**
	 * 把xml格式的流转换成map集合
	 * @param request
	 * @return
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) {
		Map<String, String> messagerMap = new HashMap<String, String>();
		InputStream ins = null;
		try {
			System.out.println("-----装换开始----");
			// 解析xml的几种方式 1.dom 2.sax 3.jdom 4.dom4j
			ins = request.getInputStream();
			// 开始解析
			SAXReader reader = new SAXReader();
			// 设置读取的流 返回文档对象
			Document document = reader.read(ins);
			// 获得根节点
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
				// 关闭流
				if(ins!=null)
					ins.close();
			System.out.println("-----转换结束-----");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messagerMap;

	}
}
