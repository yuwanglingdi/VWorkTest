package org.fiberhome.util.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.fiberhome.util.ConfigUtil;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 消息工具类
 * @author lzf
 * @email zfli5694@fiberhome.com
 */

public class MessageUtil {

	public static final int MESSAGE_TYPE_HTML = 1;
	public static final int MESSAGE_TYPE_TEXT = 2;
	public static final int MESSAGE_TYPE_VOICE = 3;
	public static final int MESSAGE_TYPE_POSITION = 4;
	public static final int MESSAGE_TYPE_IMAGE = 5;
	
	public static final String CHAT_SCENE_USER = "user";
	public static final String CHAT_SCENE_GROUP = "group";
	
	private static Logger logger = Logger.getLogger(MessageUtil.class);
	
	public static void sendTextMessage(String senderID, String receiverID, String message) throws JSONException{
		String tokenStr = HttpClientUtil.getAccessTokenString();

		JSONObject msgJson = new JSONObject();
		msgJson.put("sendUserID", senderID);
		msgJson.put("receTargetID", receiverID);
		msgJson.put("sendTime", System.currentTimeMillis());
		msgJson.put("messageType", MESSAGE_TYPE_TEXT);
		msgJson.put("messageID", "");
		msgJson.put("message", message);
		msgJson.put("chatScene", CHAT_SCENE_USER);
		
		String msgUrl = ConfigUtil.configMap.get("msgUrl");
		Map<String, String> headerMap = new HashMap<>();
		
		headerMap.put("access-token", tokenStr);
		headerMap.put("Content-Type", "application/json");
		
		Map<String, Object> resultMap = HttpClientUtil.postJson(msgUrl, headerMap, msgJson);
		String respStr = resultMap.get(HttpClientUtil.CONTENT).toString();
		logger.info(respStr);
	}
	
	public static void main(String[] args) {
		String senderID = ConfigUtil.configMap.get("appID");
		String receiverID = "8120272559";
		String message = "Hello World!";
		try {
			MessageUtil.sendTextMessage(senderID, receiverID, message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
