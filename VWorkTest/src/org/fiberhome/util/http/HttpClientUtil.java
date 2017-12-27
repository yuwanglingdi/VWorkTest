package org.fiberhome.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.fiberhome.util.ConfigUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http工具类
 * @author lzf
 * @date 2017-12-22
 * @email zfli5694@fiberhome.com
 */
public class HttpClientUtil {
	
	public static final String HEADER = "HEADER";
	public static final String CONTENT = "CONTENT";
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * @author lzf
	 * @param url
	 * @param headerMap
	 * @param paramMap
	 * @return map HEADER 为响应头(Header)数组,CONTENT为响应体(Content)的HttpEntity对象
	 */
	public static Map<String, Object> get(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		if(paramMap != null){
			url += HttpClientUtil.convertParamterMapToString(paramMap);
		}
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		if(headerMap != null){
			Iterator<String> it = headerMap.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				String value = headerMap.get(name);
				httpGet.addHeader(name, value);
			}
		}

		try(CloseableHttpResponse resp = client.execute(httpGet);) {
			Header[] headerArr = resp.getAllHeaders();
			resultMap.put(HEADER, headerArr);
			for(Header header : headerArr){
				String name = header.getName();
				String value = header.getValue();
				logger.info("Header name {} value {}", name, value);
			}
			HttpEntity entity = resp.getEntity();
			String entityStr = EntityUtils.toString(entity, "UTF-8");
			resultMap.put(CONTENT, entityStr);
			logger.info(resp.getStatusLine().toString());
			if (entity != null) {
				logger.info("Response content length = {}", entity.getContentLength());
				logger.info("Response content is:\n {}", entityStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * @author lzf
	 * @param url
	 * @param headerMap
	 * @param paramMap
	 * @return map HEADER 为响应头(Header)数组,CONTENT为响应体(Content)的HttpEntity对象
	 */
	public static Map<String, Object> post(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		if(headerMap != null){
			Iterator<String> it = headerMap.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				String value = headerMap.get(name);
				httpPost.addHeader(name, value);
			}
		}

		List<NameValuePair> nvPairList = new ArrayList<NameValuePair>();
		if(paramMap != null){
			Iterator<String> it = paramMap.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				String value = paramMap.get(name);
				nvPairList.add(new BasicNameValuePair(name, value));
			}
		}

		if(nvPairList.size() > 0){
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvPairList, "UTF-8");
				httpPost.setEntity(entity);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		try(CloseableHttpResponse resp = client.execute(httpPost);){
			Header[] headerArr = resp.getAllHeaders();
			resultMap.put(HEADER, headerArr);
			for(Header header : headerArr){
				String name = header.getName();
				String value = header.getValue();
				logger.info("Header name {} value {}", name, value);
			}
			HttpEntity entity = resp.getEntity();
			String entityStr = EntityUtils.toString(entity, "UTF-8");
			resultMap.put(CONTENT, entityStr);

			if (null != entity) {
				logger.info(resp.getStatusLine().toString());
				logger.info("Response content is : \n{}", entityStr);
			}
			resp.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	/**
	 * 发送JSON数据
	 * @author lzf
	 * @param url
	 * @param headerMap
	 * @param json 
	 * @return map HEADER 为响应头(Header)数组,CONTENT为响应体(Content)的HttpEntity对象
	 */
	public static Map<String, Object> postJson(String url, Map<String, String> headerMap, JSONObject json) {
		Map<String, Object> resultMap = new HashMap<>();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		if(headerMap != null){
			Iterator<String> it = headerMap.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				String value = headerMap.get(name);
				httpPost.addHeader(name, value);
			}
		}
		
		try {
			StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
			httpPost.setEntity(se);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		try(CloseableHttpResponse resp = client.execute(httpPost);){
			Header[] headerArr = resp.getAllHeaders();
			resultMap.put(HEADER, headerArr);
			for(Header header : headerArr){
				String name = header.getName();
				String value = header.getValue();
				logger.info("Header name {} value {}", name, value);
			}
			HttpEntity entity = resp.getEntity();
			String entityStr = EntityUtils.toString(entity, "UTF-8");
			resultMap.put(CONTENT, entityStr);
			
			if (null != entity) {
				logger.info(resp.getStatusLine().toString());
				logger.info("Response content is : \n{}", entityStr);
			}
			resp.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	/**
     * map键值对 ——>> key=value&key=value
     * @param parameterMap
     * @return
     */
	private static String convertParamterMapToString(Map<String, String> parameterMap) {
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        return parameterBuffer.toString();
    }

	public static String getAccessTokenString(String url, String ... args) {
		String accessTokenString = "";
		if(args.length < 3){
			logger.info("参数错误");
			return "";
		}
		String accessType = args[0];
		String appID = args[1];
		String appSecret = args[2];
		StringBuffer sb = new StringBuffer(url);
		sb.append("?accessType=").append(accessType).append("&appID=").append(appID).append("&appSecret=").append(appSecret);
		url = sb.toString();
		Map<String, Object> map = HttpClientUtil.get(url, null, null);
		if(map.get(HttpClientUtil.CONTENT) != null){
			String jsonStr = (String) map.get(HttpClientUtil.CONTENT);
			try {
				JSONObject tokenJson = new JSONObject(jsonStr);
				accessTokenString = tokenJson.getJSONObject("result").getString("access_token");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return accessTokenString;
	}
	
	public static String getAccessTokenString() {
		String accessTokenString = HttpClientUtil.getAccessTokenString(ConfigUtil.configMap.get("tokenUrl"), ConfigUtil.configMap.get("accessType"), ConfigUtil.configMap.get("appID"), ConfigUtil.configMap.get("appSecret"));
		return accessTokenString;
	}
	
	@SuppressWarnings("unused")
	private static Map<String, String> convertHeaderArrToMap(Header[] headerArr){
		Map<String, String> map = new HashMap<>();
		for(Header header : headerArr){
			String name = header.getName();
			String value = header.getValue();
			map.put(name, value);
		}
		return map;
	}
	
}