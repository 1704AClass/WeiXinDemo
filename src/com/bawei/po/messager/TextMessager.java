package com.bawei.po.messager;

import com.bawei.po.MessagerEntity;
/**
 * 专门接受文本消息
 * @author yangrenyu
 *
 */
public class TextMessager extends MessagerEntity {
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
}
