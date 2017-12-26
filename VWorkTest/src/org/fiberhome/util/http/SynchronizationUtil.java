package org.fiberhome.util.http;

import java.util.HashMap;
import java.util.Map;

import org.fiberhome.util.ConfigUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizationUtil {
	
	public static final int EVENT_VALUE_USER = 1;
	public static final int EVENT_VALUE_UNIT = 2;
	
	private static final Logger logger = LoggerFactory.getLogger(SynchronizationUtil.class);

	public static void synchronize(JSONObject json) throws JSONException {
		int eventType = json.getInt("eventValue");
		if(eventType == EVENT_VALUE_USER){
			synchroinzeUser(json);
		}else if(eventType == EVENT_VALUE_UNIT){
			synchroinzeUnit(json);
		}
	}
	
	public static void synchroinzeUser(JSONObject json) throws JSONException{
		String tokenStr = HttpClientUtil.getAccessTokenString();
		String userUrl = ConfigUtil.configMap.get("userUrl");
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("access-token", tokenStr);
		Map<String, Object> resultMap = null;
		
		int changeVersion = 0;
		boolean continueSync = true;
		while(continueSync) {
			resultMap = HttpClientUtil.get(userUrl + changeVersion, headerMap, null);
			String respContent = resultMap.get(HttpClientUtil.CONTENT).toString();
			JSONObject synUserJson = new JSONObject(respContent);
			continueSync = synUserJson.getBoolean("continueSync");
			JSONArray userArr = synUserJson.getJSONArray("resultList");
			for(int i=0; i<userArr.length(); i++){
				JSONObject userJson = userArr.getJSONObject(i);
				logger.info("changeVersion " + userJson.getInt("changeVersion"));
				logger.info("name " + userJson.getString("name"));
				logger.info("realname " + userJson.getString("realname"));
				logger.info("status " + userJson.getInt("status"));
				logger.info("userId " + userJson.getString("userId"));
				if(userJson.has("entExtend")){
					logger.info("entExtend " + userJson.get("entExtend"));
				}
				logger.info("account " + userJson.getString("account"));
				logger.info("\n");
				
				changeVersion = Math.max(changeVersion, userJson.getInt("changeVersion"));
			}
		}
		
	}
	
	public static void synchroinzeUnit(JSONObject json) throws JSONException{
		String tokenStr = HttpClientUtil.getAccessTokenString();
		String unitUrl = ConfigUtil.configMap.get("unitUrl");
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("access-token", tokenStr);
		Map<String, Object> resultMap = null;
		
		int changeVersion = 0;
		boolean continueSync = true;
		while(continueSync) {
			changeVersion++;
			resultMap = HttpClientUtil.get(unitUrl + changeVersion, headerMap, null);
			String respContent = resultMap.get(HttpClientUtil.CONTENT).toString();
			JSONObject synUnitJson = new JSONObject(respContent);
			continueSync = synUnitJson.getBoolean("continueSync");
			JSONArray unitArr = synUnitJson.getJSONArray("resultList");
			for(int i=0; i<unitArr.length(); i++){
				JSONObject unitJson = unitArr.getJSONObject(i);
				logger.info("changeVersion " + unitJson.getInt("changeVersion"));
				logger.info("enterpriseID " + unitJson.getInt("enterpriseID"));
				logger.info("leaf " + unitJson.getInt("leaf"));
				logger.info("orgCode " + unitJson.getString("orgCode"));
				logger.info("orgId " + unitJson.getString("orgId"));
				logger.info("orgName " + unitJson.getString("orgName"));
				logger.info("orgStatus " + unitJson.getInt("orgStatus"));
				logger.info("parentOrgId " + unitJson.getString("parentOrgId"));
				logger.info("remark " + unitJson.getString("remark"));
				logger.info("\n");
				
				changeVersion = Math.max(changeVersion, unitJson.getInt("changeVersion"));
			}
		}
	}
	
	
	
}
