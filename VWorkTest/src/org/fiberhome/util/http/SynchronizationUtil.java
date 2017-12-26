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
				if(userJson.has("changeVersion")){
					logger.info("changeVersion " + userJson.getInt("changeVersion"));
				}
				if(userJson.has("name")){
					logger.info("name " + userJson.getString("name"));
				}
				if(userJson.has("realname")){
					logger.info("realname " + userJson.getString("realname"));
				}
				if(userJson.has("status")){
					logger.info("status " + userJson.getInt("status"));
				}
				if(userJson.has("userId")){
					logger.info("userId " + userJson.getString("userId"));
				}
				if(userJson.has("entExtend")){
					JSONArray tempJsonArr = new JSONArray(userJson.get("entExtend").toString());
					JSONObject entExtendJson = tempJsonArr.getJSONObject(0);
					if(userJson.has("duty")){
						logger.info("duty {}", entExtendJson.getString("duty"));
					}
					if(userJson.has("用户名")){
						logger.info("用户名 {}", entExtendJson.getString("用户名"));
					}
					if(userJson.has("isFirstLogin")){
						logger.info("isFirstLogin {}", entExtendJson.getString("isFirstLogin"));
					}
					if(userJson.has("orgCode")){
						logger.info("orgCode {}", entExtendJson.getString("orgCode"));
					}
					if(userJson.has("bizStatus")){
						logger.info("bizStatus {}", entExtendJson.getInt("bizStatus"));
					}
					if(userJson.has("orderNum")){
						logger.info("orderNum {}", entExtendJson.getInt("orderNum"));
					}
					if(userJson.has("orgID")){
						logger.info("orgID {}", entExtendJson.getString("orgID"));
					}
				}
				if(userJson.has("account")){
					logger.info("account " + userJson.getString("account"));
				}
				System.out.println();
				
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
				if(unitJson.has("changeVersion")){
					logger.info("changeVersion " + unitJson.getInt("changeVersion"));
				}
				if(unitJson.has("enterpriseID")){
					logger.info("enterpriseID " + unitJson.getInt("enterpriseID"));
				}
				if(unitJson.has("leaf")){
					logger.info("leaf " + unitJson.getInt("leaf"));
				}
				if(unitJson.has("orgCode")){
					logger.info("orgCode " + unitJson.getString("orgCode"));
				}
				if(unitJson.has("orgId")){
					logger.info("orgId " + unitJson.getString("orgId"));
				}
				if(unitJson.has("orgName")){
					logger.info("orgName " + unitJson.getString("orgName"));
				}
				if(unitJson.has("orgStatus")){
					logger.info("orgStatus " + unitJson.getInt("orgStatus"));
				}
				if(unitJson.has("parentOrgId")){
					logger.info("parentOrgId " + unitJson.getString("parentOrgId"));
				}
				if(unitJson.has("remark")){
					logger.info("remark " + unitJson.getString("remark"));
				}
				System.out.println();
				
				changeVersion = Math.max(changeVersion, unitJson.getInt("changeVersion"));
			}
		}
	}
	
	
	
}
