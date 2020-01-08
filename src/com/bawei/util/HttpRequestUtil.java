package com.bawei.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.caspar.hoe.StringHoe;

/**
 * http请求工具
 * @author yry
 */
public class HttpRequestUtil {

	//记录日志使用
	private static final Logger log = LoggerFactory.getLogger(HttpRequestUtil.class);
	//生成httpClient对象
	private CloseableHttpClient httpClient;
	
	//默认编码格式
	private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
	
	
	//生成头部信息map集合（表单方式）
	private final static Map<String, String> DEFAULT_HEADERS_FORM_UTF8 = new HashMap<String, String>();
	static {
		DEFAULT_HEADERS_FORM_UTF8.put(HttpHeaders.CONTENT_TYPE,
				"application/x-www-form-urlencoded");
		DEFAULT_HEADERS_FORM_UTF8.put(HttpHeaders.CONTENT_ENCODING,
				DEFAULT_ENCODING);
	}
	//生成头部信息map集合（json方式)
	private final static Map<String, String> DEFAULT_HEADERS_JSON_UTF8 = new HashMap<String, String>();
	static {
		DEFAULT_HEADERS_JSON_UTF8.put(HttpHeaders.CONTENT_TYPE,
				"application/json");
		DEFAULT_HEADERS_JSON_UTF8.put(HttpHeaders.CONTENT_ENCODING,
				DEFAULT_ENCODING);
	}

	//创建请求对象
	public static HttpRequestUtil createDefault() {
		return new HttpRequestUtil(5000, 5000, 5000,
				DEFAULT_ENCODING);
	}

	/**
	 * 
	 * @param connectionRequestTimeout 指从连接池获取连接的timeout	
	 * @param connectionTimeout 指客户端和服务器建立连接的timeout�? 就是http请求的三个阶段，一：建立连接；二：数据传输；三，断开连接。超时后会ConnectionTimeOutException
	 * @param socketTimeout 指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 * @param defaultCharset
	 */
	public HttpRequestUtil(int connectionRequestTimeout, int connectionTimeout,
			int socketTimeout, String defaultCharset) {
		super();
		init(connectionRequestTimeout, connectionTimeout, socketTimeout,
				defaultCharset, null);
	}

	public HttpRequestUtil(int connectionRequestTimeout, int connectionTimeout,
			int socketTimeout, String defaultCharset, String proxyServer) {
		super();
		init(connectionRequestTimeout, connectionTimeout, socketTimeout,
				defaultCharset, proxyServer);
	}

	/**
	 * 构建（初始化方法)
	 */
	private void init(int connectionRequestTimeout, int connectionTimeout,
			int socketTimeout, String defaultCharset, String proxyServer) {
		httpClient = HttpClients.createDefault();

		Builder requestBuilder = RequestConfig.custom();
		requestBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
		requestBuilder.setConnectTimeout(connectionTimeout);
		requestBuilder.setSocketTimeout(socketTimeout);
		//isNotEmpty “ ”将空格也作为参数，isNotBlank则排除空格参数“ ”
		if (StringUtils.isNotBlank(proxyServer)) {
			requestBuilder.setProxy(HttpHost.create(proxyServer));
		}
		requestBuilder.build();
	}

	/**
	 * 请求方法
	 * @param request
	 * @return
	 */
	private String execute(HttpUriRequest request) {
		HttpResponse response = null;
		String content = null;
		try {
			response = httpClient.execute(request);
			//如果请求成功进行回调  200 调用第三方接口的时候 一般都会有回执
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				content = EntityUtils.toString(response.getEntity(),DEFAULT_ENCODING);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			HttpClientUtils.closeQuietly(response);
			response = null;
		}
		log.info(content);
		return content;
	}

	/**
	 * 模拟http请求doGet方式
	 * @param url
	 * @return
	 */
	public String doGet(String url) {
		return execute(new HttpGet(url));
	}

	/**
	 * 模拟http请求doGet方式 返回json对象
	 * @param url
	 * @return
	 */
	public JSONObject doGetToJsonObject(String url) {
		String responseStr = doGet(url);
		if(StringHoe.isEmpty(responseStr)){
			return null;
		}
		return JSONObject.parseObject(responseStr);
	}
	

	/**
	 * 表单方式post提交
	 * @param url
	 * @param param
	 * @return
	 */
	public String doPost(String url, Map<String, String> param) {
		HttpPost request = new HttpPost(url);
		if (param != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(),
						entry.getValue());
				list.add(pair);
			}
			try {
				request.setEntity(new UrlEncodedFormEntity(list,
						DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return execute(request);
	}

	/**
	 * json方式post提交
	 * @param url
	 * @param param
	 * @return
	 */
	public String doPost(String url, String param) {
		if(StringHoe.isEmpty(url)){
			return null;
		}
		HttpPost request = new HttpPost(url.trim());
		if (param != null) {
			// 设置发�?�消息的参数
			StringEntity entity = new StringEntity(param, DEFAULT_ENCODING);
			entity.setContentEncoding(DEFAULT_ENCODING);
			entity.setContentType("application/json");
			request.setEntity(entity);
		}
		return execute(request);
	}

}
