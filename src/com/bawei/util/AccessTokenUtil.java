package com.bawei.util;

import com.alibaba.fastjson.JSONObject;

public class AccessTokenUtil {
	public static String getAccessToken(){
		JSONObject jObject = HttpRequestUtil.createDefault().doGetToJsonObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5ef29b29af35ac93&secret=96622bdecdf7236592b5f272f0795a85");
		return jObject.get("access_token").toString();
	}
}
