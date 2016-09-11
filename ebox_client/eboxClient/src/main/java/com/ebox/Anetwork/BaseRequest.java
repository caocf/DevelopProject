package com.ebox.Anetwork;

import android.content.pm.PackageInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ebox.Anetwork.util.Base64;
import com.ebox.AppApplication;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class BaseRequest<T> extends Request<T> implements Runnable
{
    private static final String TAG = "BaseRequest";
    public  Gson mGson = new Gson();
    private final Class<T> mClazz;
    private String mBody;
    private ResponseEventHandler<T> mListener = null;
    private Map<String, String> mHeaders = new HashMap<String, String>();
    private boolean mEnableCookie = false;
    private boolean mNeedMainThread = true;//默认主线程执行
    private int responseResult;
    private T response;
    private VolleyError error;

    public BaseRequest(int method,
                       String url,
                       String body,
                       Class<T> clazz,
            ResponseEventHandler<T> listener)
    {
        super(method, url, null);
        this.mBody = body;
        this.mClazz = clazz;
        this.mListener = listener;
        // cancel retry request while network not fine except get method

        setRetryPolicy(
                new DefaultRetryPolicy(
                12*1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        LogUtil.d(TAG, url);
    }

    public void setEnableCookie(boolean enableCookie)
    {
        mEnableCookie=enableCookie;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        String terminal_code = AppApplication.getInstance().getTerminal_code();

        mHeaders.put("Charset", "UTF-8");
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Accept-Encoding", "gzip,deflate");
        String s = "Basic " + Base64.encode(terminal_code+":"+terminal_code);

        mHeaders.put("Authorization", s);//终端base64

        try {
            String board=android.os.Build.MODEL;
            String verOs=android.os.Build.VERSION.RELEASE;
            String vercode= getVersionName();

            String userAgent="ebox/"+vercode+" ; "+" (Linux; U ; Android "+verOs+" ; "+board+" ; "+terminal_code+")";

            mHeaders.put("User-Agent", userAgent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mEnableCookie)
       {
            String cookie = OperatorHelper.cacheCookie;
            if (cookie != null)
            {
                mHeaders.put("Cookie", cookie);
            }
        }

        return mHeaders;
    }

    public  String getVersionName()
    {
        try
        {
            PackageInfo info =   AppApplication.getInstance().getPackageManager().getPackageInfo(
                    AppApplication.getInstance().getPackageName(), 0);
            return info.versionName;
        }
        catch (Exception e)
        {
        }
        return "";
    }


    @Override
    public byte[] getBody()
    {
        try
        {
            LogUtil.d(TAG,"prams: "+mBody);
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
    protected void deliverResponse(T response) {
        if (response==null)//控制返回错误
        {
            LogUtil.i("server response null");
            deliverError(new VolleyError());
            return;
        }

        responseResult = 1;
        this.response = response;

        if (mNeedMainThread) {
            if (mListener != null) {
                mListener.onResponseSuccess(response);
            }
        } else {
            AppApplication.getInstance().executorService.execute(this);
        }


    }

    @Override
    public void deliverError(VolleyError error)
    {
        responseResult=2;
        this.error=error;
        if (mNeedMainThread) {
            if (mListener != null)
            {
                mListener.onResponseError(error);
            }
        }else{
            AppApplication.getInstance().executorService.execute(this);
        }
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
            String json = getRealString(response.data);

            LogUtil.d(TAG, json);

            // 保存登陆cookie
            if (mEnableCookie)
            {
                String cookie = response.headers.get("Set-Cookie");
                if (cookie != null)
                {
                   OperatorHelper.cacheCookie = cookie;
                }
            }

            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public void run() {

        if (responseResult==1)
        {
            if (mListener != null)
            {
                mListener.onResponseSuccess(response);
            }
        }
        else if(responseResult==2)
        {
            if (mListener != null)
            {
                mListener.onResponseError(error);
            }

        }
    }

    public void setNeedMainThread(boolean mNeedMainThread) {
        this.mNeedMainThread = mNeedMainThread;
    }
}
