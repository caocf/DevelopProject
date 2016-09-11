package com.moge.gege.network.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.data.PersistentData;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;

public class BaseRequest<T> extends Request<T>
{
    private static final String TAG = "BaseRequest";
    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private String mBody;
    private ResponseEventHandler<T> mListener = null;
    private boolean mEnableCookie = true;
    private Map<String, String> mHeaders = new HashMap<String, String>();
    private final VolleyError mNetworkError = new VolleyError(
            FunctionUtils.getString(R.string.no_network));

    public BaseRequest(int method, String url, String body, Class<T> clazz,
            ResponseEventHandler<T> listener)
    {
        super(method, url, null);
        this.mBody = body;
        this.mClazz = clazz;
        this.mListener = listener;

        // cancel retry request while network not fine except get method
        if (method != Method.GET)
        {
            setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        LogUtil.d(TAG, url);
    }

    public void setResponseListener(
            ResponseEventHandler<T> responseEventListener)
    {
        this.mListener = responseEventListener;
    }

    public void setEnableCookie(boolean enable)
    {
        this.mEnableCookie = enable;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        mHeaders.put("Charset", "UTF-8");
        // headers.put("Content-Type", "application/json");
        mHeaders.put("Accept-Encoding", "gzip,deflate");
        mHeaders.put("User-Agent", FunctionUtils.getUserAgent());

        // request with cookie
        if (mEnableCookie)
        {
            String cookie = PersistentData.instance().getCookie();
            if (cookie != null)
            {
                mHeaders.put("Cookie", cookie);
            }
        }
        return mHeaders;
    }

    @Override
    public byte[] getBody()
    {
        try
        {
            return mBody == null ? null : mBody.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException uee)
        {
            LogUtil.e(
                    TAG,
                    String.format(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            mBody, "utf-8"));
            return null;
        }
    }

    @Override
    protected void deliverResponse(T response)
    {
        if (mListener != null)
        {
            mListener.onResponseSucess(this, response);
        }
    }

    @Override
    public void deliverError(VolleyError error)
    {
        if (mListener != null)
        {
            if (DeviceInfoUtil.isHaveInternet(AppApplication.instance()
                    .getGlobalContext()))
            {
                mListener.onResponseError(error);
            }
            else
            {
                mListener.onResponseError(mNetworkError);
            }

        }
    }

    private String getRealyCookie(String cookie)
    {
        if (cookie == null || false == cookie.contains(";"))
        {
            return null;
        }

        String[] cookieStoreArray = cookie.split(";");
        int lengthCookieStore = cookieStoreArray.length;
        for (int i = 0; i < lengthCookieStore; i++)
        {
            String tempCookie = cookieStoreArray[i];
            if (!TextUtils.isEmpty(tempCookie) && tempCookie.contains("sid="))
            {
                return tempCookie;
            }
        }

        return null;
    }

    private int getShort(byte[] data)
    {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    private String getRealString(byte[] data)
    {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t)
            {
                in = new GZIPInputStream(bis);
            }
            else
            {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in),
                    1000);
            for (String line = r.readLine(); line != null; line = r.readLine())
            {
                sb.append(line);
            }
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            // support gzip
            // String json = new String(response.data,
            // HttpHeaderParser.parseCharset(response.headers));
            String json = getRealString(response.data);
            LogUtil.d(TAG, json);

            // 保存cookie
            String tag = this.getTag().toString();
            if (tag.equalsIgnoreCase(RequestManager.CookieTag))
            {
                // String cookie = getRealyCookie(response.headers
                // .get("Set-Cookie"));
                String cookie = response.headers.get("Set-Cookie");
                if (cookie != null)
                {
                    PersistentData.instance().setCookie(cookie);
                }
            }

            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        // catch (UnsupportedEncodingException e)
        // {
        // return Response.error(new ParseError(e));
        // }
        catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
