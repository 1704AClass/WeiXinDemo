package com.bawei.toweixin;


import com.alibaba.fastjson.JSONObject;
import com.bawei.button.ClickButton;
import com.bawei.button.ViewButton;
import com.bawei.po.Button;
import com.bawei.po.Menu;

public class MenuUtil {
	public static String menu(){
		//第一个二级菜单
		ClickButton c11 = new ClickButton();
		c11.setType("click");
		c11.setKey("c11");
		c11.setName("ofo优惠");
		ClickButton c12 = new ClickButton();
		c12.setType("click");
		c12.setKey("c12");
		c12.setName("小蓝优惠");
		ClickButton c13 = new ClickButton();
		c13.setType("click");
		c13.setKey("c13");
		c13.setName("摩拜优惠");
		ClickButton c14 = new ClickButton();
		c14.setType("click");
		c14.setKey("c14");
		c14.setName("小绿优惠");
		//第二个二级菜单
		ClickButton c21 = new ClickButton();
		c21.setType("click");
		c21.setKey("c21");
		c21.setName("个人优惠");
		ViewButton v22 = new ViewButton();
		v22.setType("view");
		v22.setUrl("http://www.baidu.com");
		v22.setName("个人中心");
		
		//三个一级菜单
		ViewButton v1 = new ViewButton();
		v1.setType("view");
		v1.setName("首页");
		v1.setUrl("http://www.qq.com");
		ClickButton c1 = new ClickButton();
		c1.setKey("c1");
		c1.setName("优惠中心");
		c1.setType("click");
		c1.setSub_button(new Button[]{c11,c12,c13,c14});
		ClickButton c2 = new ClickButton();
		c2.setKey("c2");
		c2.setName("个人中心");
		c2.setType("click");
		c2.setSub_button(new Button[]{c21,v22});
		
		Menu menu = new Menu();
		menu.setButton(new Button[]{v1,c1,c2});
		
		return JSONObject.toJSONString(menu);
	}
}
