package org.fiberhome.util.http;

import java.util.HashMap;
import java.util.Map;

import org.fiberhome.util.ConfigUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SynchronizationUtil {
	
	public static final int EVENT_VALUE_USER = 1;
	public static final int EVENT_VALUE_UNIT = 2;

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
				System.out.println("changeVersion " + userJson.getInt("changeVersion"));
				System.out.println("name " + userJson.getString("name"));
				System.out.println("realname " + userJson.getString("realname"));
				System.out.println("status " + userJson.getInt("status"));
				System.out.println("userId " + userJson.getString("userId"));
				if(userJson.has("entExtend")){
					System.out.println("entExtend " + userJson.get("entExtend"));
				}
				System.out.println("account " + userJson.getString("account"));
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
				System.out.println("changeVersion " + unitJson.getInt("changeVersion"));
				System.out.println("enterpriseID " + unitJson.getInt("enterpriseID"));
				System.out.println("leaf " + unitJson.getInt("leaf"));
				System.out.println("orgCode " + unitJson.getString("orgCode"));
				System.out.println("orgId " + unitJson.getString("orgId"));
				System.out.println("orgName " + unitJson.getString("orgName"));
				System.out.println("orgStatus " + unitJson.getInt("orgStatus"));
				System.out.println("parentOrgId " + unitJson.getString("parentOrgId"));
				System.out.println("remark " + unitJson.getString("remark"));
				System.out.println();
				
				changeVersion = Math.max(changeVersion, unitJson.getInt("changeVersion"));
			}
		}
	}
	
	
	
}
