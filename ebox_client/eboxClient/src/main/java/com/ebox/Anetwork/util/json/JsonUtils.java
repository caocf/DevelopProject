package com.ebox.Anetwork.util.json;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	
	
	public static JSONArray getJSONArray (JSONObject json) throws JSONException{
		JSONArray jsonArray = json.getJSONArray("result");
		return jsonArray;
	}
	
	public static JSONObject getJSONObject(JSONArray jsonArray,int pos) throws JSONException{
		
		if (jsonArray.length()>0) {
			return jsonArray.getJSONObject(pos);
		}
		return null;
	}
	
	public static JSONObject getJSONOFirstbject(JSONArray jsonArray) throws JSONException{
		
		if (jsonArray.length()>0) {
			return jsonArray.getJSONObject(0);
		}
		return null;
	}
	
	public static String getString(JSONObject json,String key) throws JSONException{
		
		if(!json.isNull(key)){
			return json.getString(key);
		}
		return null;
	}
	
}
