package com.moge.gege.ui.helper;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

public class URLs implements Serializable
{
    private final static String HOST = "dev.api.aimoge.com";

    public final static String HTTP = "http://";
    public final static String HTTPS = "https://";

    private final static String URL_SPLITTER = "/";
    private final static String URL_UNDERLINE = "_";

    public final static String URL_HOME_TAG = "/index";
    public final static String URL_PERSIONAL_CENTER_TAG = "/user/center";
    public final static String URL_TOPIC_LIST_TAG = "/topic/list";
    public final static String URL_TOPIC_DETAIL_TAG = "/topic/view";
    public final static String URL_TRADING_LIST_TAG = "/trading/list";
    public final static String URL_TRADING_DETAIL_TAG = "/trading/view";
    public final static String URL_ACTIVITY_LIST_TAG = "/activity/list";
    public final static String URL_ACTIVITY_DETAIL_TAG = "/activity/view";
    public final static String URL_SERVICE_LIST_TAG = "/living/service/list";
    public final static String URL_SERVICE_DETAIL_TAG = "/living/service/view";
    public final static String URL_CONVENIENCE_SERVICE_DETAIL_TAG = "/convenience/service/view";
    public final static String URL_TRADING_PROMOTION_TAG = "/trading/promotions/list";

    public final static int URL_HOME = 0x000;
    public final static int URL_PERSIONAL_CENTER = 0x001;
    public final static int URL_TOPIC_LIST = 0x002;
    public final static int URL_TOPIC_DETAIL = 0x003;
    public final static int URL_TRADING_LIST = 0x004;
    public final static int URL_TRADING_DETAIL = 0x005;
    public final static int URL_ACTIVITY_LIST = 0x006;
    public final static int URL_ACTIVITY_DETAIL = 0x007;
    public final static int URL_SERVICE_LIST = 0x008;
    public final static int URL_SERVICE_DETAIL = 0x009;
    public final static int URL_CONVENIENCE_SERVICE_DETAIL = 0x00A;
    public final static int URL_TRADING_PROMOTION_LIST = 0x0A1;
    public final static int URL_WEBVIEW = 0xFFF;

    private int mUrlType;
    private String mUrl;
    private Map<String, String> mParamsMap = new HashMap<String, String>();

    public int getUrlType()
    {
        return mUrlType;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public Map<String, String> getParams()
    {
        return mParamsMap;
    }

    public final static URLs parseURL(String path)
    {
        if (TextUtils.isEmpty(path))
        {
            return null;
        }

        String newpath;
        boolean bInteralProtocol = false;

        if (path.startsWith("http://") || path.startsWith("https://"))
        {
            newpath = path;
            bInteralProtocol = false;
        }
        else if (path.startsWith("moge://"))
        {
            newpath = path.replace("moge://", "http://");
            bInteralProtocol = true;
        }
        else
        {
            return null;
        }

        URLs urls = null;
        String objId = "";
        try
        {
            URL url = new URL(newpath);

            urls = new URLs();

            // 站内链接
            if (bInteralProtocol)
            {
                if (newpath.contains(URL_HOME_TAG))
                {
                    urls.mUrlType = URL_HOME;
                }
                else if (newpath.contains(URL_PERSIONAL_CENTER_TAG))
                {
                    urls.mUrlType = URL_PERSIONAL_CENTER;
                }
                else if (newpath.contains(URL_TOPIC_LIST_TAG))
                {
                    urls.mUrlType = URL_TOPIC_LIST;
                }
                else if (newpath.contains(URL_TOPIC_DETAIL_TAG))
                {
                    urls.mUrlType = URL_TOPIC_DETAIL;
                }
                else if (newpath.contains(URL_TRADING_LIST_TAG))
                {
                    urls.mUrlType = URL_TRADING_LIST;
                }
                else if (newpath.contains(URL_TRADING_DETAIL_TAG))
                {
                    urls.mUrlType = URL_TRADING_DETAIL;
                }
                else if (newpath.contains(URL_ACTIVITY_LIST_TAG))
                {
                    urls.mUrlType = URL_ACTIVITY_LIST;
                }
                else if (newpath.contains(URL_ACTIVITY_DETAIL_TAG))
                {
                    urls.mUrlType = URL_ACTIVITY_DETAIL;
                }
                else if (newpath.contains(URL_SERVICE_LIST_TAG))
                {
                    urls.mUrlType = URL_SERVICE_LIST;
                }
                else if (newpath.contains(URL_SERVICE_DETAIL_TAG))
                {
                    urls.mUrlType = URL_SERVICE_DETAIL;
                }
                else if (newpath.contains(URL_CONVENIENCE_SERVICE_DETAIL_TAG))
                {
                    urls.mUrlType = URL_CONVENIENCE_SERVICE_DETAIL;
                }
                else if (newpath.contains(URL_TRADING_PROMOTION_TAG))
                {
                    urls.mUrlType = URL_TRADING_PROMOTION_LIST;
                }

                int index = newpath.indexOf("?");
                if (index != -1)
                {
                    decodeUrl(urls, newpath.substring(index + 1));
                }
            }
            else
            {
                urls.mUrlType = URL_WEBVIEW;
                urls.mUrl = newpath;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            urls = null;
        }
        return urls;
    }

    /**
     * 解析url获得objId
     * 
     * @param path
     * @param url_type
     * @return
     */
    private final static String parseObjId(String path, String url_type)
    {
        String objId = "";
        int p = 0;
        String str = "";
        String[] tmp = null;
        p = path.indexOf(url_type) + url_type.length();
        str = path.substring(p);
        if (str.contains(URL_SPLITTER))
        {
            tmp = str.split(URL_SPLITTER);
            objId = tmp[0];
        }
        else
        {
            objId = str;
        }
        return objId;
    }

    /**
     * 解析url获得objKey
     * 
     * @param path
     * @param url_type
     * @return
     */
    private final static String parseObjKey(String path, String url_type)
    {
        path = URLDecoder.decode(path);
        String objKey = "";
        int p = 0;
        String str = "";
        String[] tmp = null;
        p = path.indexOf(url_type) + url_type.length();
        str = path.substring(p);
        if (str.contains("?"))
        {
            tmp = str.split("?");
            objKey = tmp[0];
        }
        else
        {
            objKey = str;
        }
        return objKey;
    }

    /**
     * 对URL进行格式处理
     * 
     * @param path
     * @return
     */
    private final static String formatURL(String path)
    {
        if (path.startsWith("http://") || path.startsWith("https://"))
            return path;
        return "http://" + URLEncoder.encode(path);
    }

    private static void decodeUrl(URLs urls, String params)
    {
        if (params != null)
        {
            String array[] = params.split("&");
            for (String parameter : array)
            {
                String v[] = parameter.split("=");
                urls.mParamsMap.put(URLDecoder.decode(v[0]),
                        URLDecoder.decode(v[1]));
            }
        }
    }
}
