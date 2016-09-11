package com.ebox.pub.websocket.helper;

import com.ebox.ex.network.model.enums.ErrorCode;

import org.json.JSONException;
import org.json.JSONObject;

public class AppException extends Exception implements ErrorCode{

    private static final long serialVersionUID = -7414024298295830080L;

    private int code;
    private String message;

    public AppException()
    {
        super();
    }

    public AppException(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public interface AppExceptionCode{
        int NO_TEL=20013;
        int ERROR_VERIFY_CODE=20017;
    }

	public Object fromJSON(JSONObject jo) throws JSONException
    {
    	if(jo == null)
    		return null;
        this.code = jo.getInt("code");
        this.message = jo.getString("message");
        return this;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject jo = new JSONObject();
        jo.put("code", this.code);
        jo.put("message", this.message);
        return jo;
    }

    @Override
    public String toString()
    {
        try
        {
            JSONObject jo = toJSON();
            return jo.toString();
        }
        catch (JSONException e)
        {
            return super.toString();
        }
    }
}
