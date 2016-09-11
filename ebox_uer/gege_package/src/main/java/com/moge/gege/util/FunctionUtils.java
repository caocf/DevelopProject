package com.moge.gege.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.moge.gege.R;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FunctionUtils
{
    private static final String TAG = "FunctionUtils";

    private static Context mContext;

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void init(Context context)
    {
        mContext = context;
    }

    public static void showToastLong(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String toDBC(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == 12288)
            {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
            {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    public static void showToastLong(Context context, int msgId)
    {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, String msg)
    {
        Toast.makeText(context, TextUtils.isEmpty(msg) ? "" : toDBC(msg),
                Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(Context context, int msgId)
    {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(String msg)
    {
        Toast.makeText(mContext, TextUtils.isEmpty(msg) ? "" : toDBC(msg),
                Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(String msg)
    {
        Toast.makeText(mContext, TextUtils.isEmpty(msg) ? "" : toDBC(msg),
                Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(int msgId)
    {
        Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String formatPriceString(final String price,
            final int partitionNumbers, final int floatNumbers,
            final char partitionSymbol)
    {
        if (price == null || price.length() < 1 || partitionNumbers <= 0
                || floatNumbers < 0 || partitionSymbol <= 0)
        {
            throw new RuntimeException("Format Price String Error!");
        }

        boolean hasDot = false;

        StringBuffer sb = new StringBuffer();

        int dotIndex = price.indexOf('.');

        if (dotIndex == -1)
        {
            dotIndex = price.length();
        }
        else
        {
            hasDot = true;
        }

        int firstCommaIndex = dotIndex - partitionNumbers;
        if (firstCommaIndex <= 0)
        {
            if (hasDot)
            {
                int retLen = dotIndex + floatNumbers;
                int priceLen = price.length() - 1;
                if (floatNumbers == 0)
                {
                    sb.append(price.substring(0, dotIndex));
                }
                else if (retLen < priceLen)
                {
                    String num = price.substring(dotIndex + 3, dotIndex + 4);
                    if (Integer.parseInt(num) > 4)
                    {
                        BigDecimal x = new BigDecimal(price.substring(0,
                                retLen + 1));
                        BigDecimal y = new BigDecimal("0.01");
                        sb.append(x.add(y).toString());
                    }
                    else
                    {
                        sb.append(price.substring(0, retLen + 1));
                    }

                }
                else if (retLen >= priceLen)
                {
                    sb.append(price);
                    for (int i = 0; i < retLen - priceLen; i++)
                    {
                        sb.append('0');
                    }
                }
            }
            else if (floatNumbers > 0)
            {
                sb.append(price);
                sb.append('.');
                for (int i = 0; i < floatNumbers; i++)
                {
                    sb.append('0');
                }
            }
            else
            {
                sb.append(price);
            }

            return sb.toString();
        }

        while (firstCommaIndex > 0)
        {
            firstCommaIndex -= partitionNumbers;
        }

        int startPos = 0;

        while ((firstCommaIndex += partitionNumbers) < dotIndex)
        {
            sb.append(price.substring(startPos, firstCommaIndex));
            sb.append(partitionSymbol);
            startPos = firstCommaIndex;
        }

        sb.append(price.substring(startPos, dotIndex));

        if (hasDot)
        {
            int retLen = dotIndex + floatNumbers;
            int priceLen = price.length() - 1;
            if (floatNumbers == 0)
            {
                // just fall down.
            }
            else if (retLen <= priceLen)
            {
                sb.append(price.substring(dotIndex, retLen + 1));
            }
            else if (retLen > priceLen)
            {
                sb.append(price.substring(dotIndex, priceLen + 1));
                int len = retLen - priceLen;

                for (int i = 0; i < len; i++)
                {
                    sb.append('0');
                }
            }
        }
        else if (floatNumbers > 0)
        {
            sb.append('.');
            for (int i = 0; i < floatNumbers; i++)
            {
                sb.append('0');
            }
        }

        return sb.toString();
    }

    public static String formatePrice(String price)
    {
        if (price == null || price.length() < 1 || price.equals("null"))
        {
            LogUtil.jw(TAG, new Throwable("Format Price String Error!"));
            return "0";
        }
        String temp = FunctionUtils.formatPriceString(price, 3, 2, ',');
        if (temp.startsWith("-,"))
        {
            temp = temp.replace("-,", "-");
        }
        return temp;
    }

    public static String formatTime(String date)
    {
        if (date == null)
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(date)).toString();
    }

    public static int parseIntByString(String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0;
        }
    }

    public static float parseFloatByString(String string)
    {
        try
        {
            return Float.parseFloat(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0.00f;
        }
    }

    public static double parseDoubleByString(String string)
    {
        try
        {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0.00;
        }
        catch (Exception e)
        {
            LogUtil.logException(e);
            return 0.00;
        }
    }

    public static String parseNull(String string)
    {
        return null == string ? "" : string;
    }

    public static String getDouble(double data)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(data);
    }

    public static String getDouble(String data)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(parseDoubleByString(data));
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static byte[] formatByteArray(Bitmap bitmap)
    {
        byte[] data;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        return data;
    }

    public static String getMD5Str(String str)
    {
        MessageDigest messageDigest = null;

        try
        {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e)
        {
            System.exit(-1);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
            {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            }
            else
            {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static byte[] bitmapToByte(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String inputStream2String(InputStream in) throws IOException
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try
        {
            String outputStream = null;
            byte[] data = new byte[1024];
            int count = -1;
            while ((count = in.read(data, 0, 1024)) != -1)
                outStream.write(data, 0, count);
            data = null;
            outputStream = new String(outStream.toByteArray(), HTTP.UTF_8);
            return outputStream;
        }
        finally
        {
            if (outStream != null)
            {
                outStream.close();
            }
            if (in != null)
            {
                in.close();
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Object getValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException
    {
        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field fe : fields)
        {
            LogUtil.d(TAG, fe.getName());
        }
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static void installAPP(Context context, String path)
    {
        File mainFile = new File(path);
        Uri data = Uri.fromFile(mainFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * get Telphone system Version
     */
    public static String getTelphoneSysVersion(Context context)
    {
        String release = android.os.Build.VERSION.RELEASE;
        return release;
    }

    /**
     * get Telphone models
     */
    public static String getTelphoneModels(Context context)
    {
        String model = android.os.Build.MODEL;
        return model;
    }

    /**
     * get Telphone imei
     */
    public static String getTelphoneIMEI(Context context)
    {
        String imei = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

        if (imei == null || imei.equals(""))
        {
            SharedPreferences mSharedPreferences = context
                    .getSharedPreferences("imei_data", Activity.MODE_PRIVATE);
            imei = mSharedPreferences.getString("imei", "");
            if (imei.equals(""))
            {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")
                        .format(new Date());
                imei = getMD5Str(now);
                editor.putString("imei", imei);
                editor.commit();
            }
        }
        return imei;
    }

    /**
     * get system time
     */
    public static String getSystemTime(Context context)
    {
        return getMD5Str(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * get user ip
     */
    public static String getUserIp(Context context)
    {
        // ��ȡwifi����
        String ip;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
        {
            // wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }
        else
        {
            ip = getLocalIpAddress();
        }
        return ip;
    }

    public static String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private static String intToIp(int i)
    {
        return (i & 0xFF) + "." +

        ((i >> 8) & 0xFF) + "." +

        ((i >> 16) & 0xFF) + "." +

        (i >> 24 & 0xFF);
    }

    public static void showIMFPanel(final Context context, Timer timer,
            int delay)
    {
        if (delay <= 0)
        {
            showIMFPanel(context);
            return;
        }
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                showIMFPanel(context);
            }
        }, delay);
    }

    public static void showIMFPanel(final Context context)
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideIMFPanel(final Context context, View text)
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

    public static String encodeParameters(List<NameValuePair> params)
            throws HttpException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < params.size(); i++)
        {
            if (i != 0)
            {
                buf.append("&");
            }
            try
            {
                buf.append(URLEncoder.encode(params.get(i).getName(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(params.get(i).getValue(),
                                "UTF-8"));
            }
            catch (java.io.UnsupportedEncodingException neverHappen)
            {
                throw new HttpException(neverHappen.getMessage(), neverHappen);
            }
        }
        return buf.toString();
    }

    public static String getRequestJson(List<NameValuePair> params)
    {
        JSONStringer js = new JSONStringer();
        try
        {
            js.object();
            for (int i = 0; i < params.size(); i++)
            {
                js.key(params.get(i).getName()).value(params.get(i).getValue());
            }
            js.endObject();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        return js.toString();
    }

    public static void redirectActivity(Context context, Class<?> dstClass)
    {
        final Intent i = new Intent();
        i.setClass(context, dstClass);
        context.startActivity(i);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        }
        else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        }
        else if (minSideLength == -1)
        {
            return lowerBound;
        }
        else
        {
            return upperBound;
        }
    }

    public static LayoutAnimationController getListAnimationController()
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(60);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(120);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float scale)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String getCurrentCityName(Context context)
    {
        /*
         * geocoder = new Geocoder(SuningEBuyApplication
         * .getInstance().getApplicationContext()); //���ڻ�ȡLocation�����Լ�����
         * LocationManager locationManager; String serviceName =
         * Context.LOCATION_SERVICE; //ʵ����һ��LocationManager����
         * locationManager = (LocationManager)SuningEBuyApplication
         * .getInstance().getApplicationContext().getSystemService(serviceName);
         * //provider������ String provider = LocationManager.NETWORK_PROVIDER;
         * // Criteria criteria = new Criteria(); //
         * criteria.setAccuracy(Criteria.ACCURACY_FINE); //�߾��� //
         * criteria.setAltitudeRequired(false); //��Ҫ�󺣰� //
         * criteria.setBearingRequired(false); //��Ҫ��λ //
         * criteria.setCostAllowed(false); //�������л��� //
         * criteria.setPowerRequirement(Criteria.POWER_LOW); //�͹���
         * //ͨ�����һ�εĵ���λ�������Location���� Location location = null;
         * location = locationManager.getLastKnownLocation(provider); String
         * queryed_name = updateWithNewLocation(location); if((queryed_name !=
         * null) && (0 != queryed_name.length())){ cityName = queryed_name; }
         * �ڶ���������ʾ���µ����ڣ���λΪ���룻�����������ĺ����ʾ��С����������λ����
         * �趨ÿ30�����һ���Զ���λ //
         * locationManager.requestLocationUpdates(provider, 3000, 0,
         * locationListener); // //�Ƴ�����������ֻ��һ��widget��ʱ������������õ� //
         * locationManager.removeUpdates(locationListener); return cityName;
         */
        String city = "";
        TelephonyManager telManager = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        GsmCellLocation glc = (GsmCellLocation) telManager.getCellLocation();
        if (glc != null)
        {
            int cid = glc.getCid();
            int lac = glc.getLac();
            String strOperator = telManager.getNetworkOperator();
            int mcc = Integer.valueOf(strOperator.substring(0, 3));
            int mnc = Integer.valueOf(strOperator.substring(3, 5));
            String getNumber = "";
            getNumber += ("cid:" + cid + "\n");
            getNumber += ("cid:" + lac + "\n");
            getNumber += ("cid:" + mcc + "\n");
            getNumber += ("cid:" + mnc + "\n");
            DefaultHttpClient client = new DefaultHttpClient();
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, 20000);
            HttpPost post = new HttpPost("http://www.google.com/loc/json");
            BufferedReader br = null;
            try
            {
                JSONObject jObject = new JSONObject();
                jObject.put("version", "1.1.0");
                jObject.put("host", "maps.google.com");
                jObject.put("request_address", true);
                if (mcc == 460)
                {
                    jObject.put("address_language", "zh_CN");
                }
                else
                {
                    jObject.put("address_language", "en_US");
                }

                JSONArray jArray = new JSONArray();
                JSONObject jData = new JSONObject();
                jData.put("cell_id", cid);
                jData.put("location_area_code", lac);
                jData.put("mobile_country_code", mcc);
                jData.put("mobile_network_code", mnc);
                jArray.put(jData);
                jObject.put("cell_towers", jArray);
                StringEntity se = new StringEntity(jObject.toString());
                post.setEntity(se);

                HttpResponse resp = client.execute(post);
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    br = new BufferedReader(new InputStreamReader(resp
                            .getEntity().getContent()));
                    StringBuffer sb = new StringBuffer();

                    String result = br.readLine();
                    while (result != null)
                    {
                        sb.append(getNumber);
                        sb.append(result);
                        result = br.readLine();
                    }
                    String s = sb.toString();
                    s = s.substring(s.indexOf("{"));
                    // btn.setText(s);
                    JSONObject jo = new JSONObject(s);
                    JSONObject arr = jo.getJSONObject("location");
                    JSONObject address = arr.getJSONObject("address");
                    city = /*
                            * address.getString("region") +
                            */address.getString("city");

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                post.abort();
                client = null;
                if (br != null)
                {
                    try
                    {
                        br.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return city;
    }

    public static NetworkInfo getActiveNetwork(Context context)
    {
        if (context == null)
        {
            return null;
        }
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
        {
            return null;
        }
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
        return aActiveInfo;
    }

    public static boolean isNetworkConnect(String host)
    {
        try
        {
            InetAddress ad = InetAddress.getByName(host);
            // �����Ƿ���Դﵽ�õ�ַ
            boolean state = ad.isReachable(5 * 1000);
            if (state)
            {
                LogUtil.d("Network Info", "�������ӳɹ�" + ad.getHostAddress());
                return true;
            }
            else
            {
                LogUtil.d("Network Info", "��������ʧ��");
                return false;
            }
        }
        catch (UnknownHostException e)
        {
            LogUtil.d("Network Info", "��������ʧ��");
            return false;
        }
        catch (IOException e)
        {
            LogUtil.d("Network Info", "��������ʧ��");
            return false;
        }
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivity)
        {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (null == info)
        {
            return false;
        }
        if (!info.isConnected())
        {
            return false;
        }
        return true;
    }

    public static boolean testNetworkIp(String host)
    {
        try
        {
            String ip = InetAddress.getByName(host).getHostAddress();
            Process p = Runtime.getRuntime().exec("cmd /c ping -n 1 " + ip);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String temp = null;
            StringBuffer strBuffer = new StringBuffer();
            while ((temp = (in.readLine())) != null)
            {
                strBuffer.append(temp);
            }
            if (strBuffer.toString().matches(".*\\(\\d?\\d% loss\\).*"))
            {
                System.out.println("�������ӳɹ�:" + strBuffer.toString());
                return true;
            }
            else
            {
                System.out.println("��������ʧ��");
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void extractMention2Link(TextView v)
    {
        v.setAutoLinkMask(0);
        v.setText(v.getText().toString(), BufferType.SPANNABLE);
        Pattern mymentionsPattern = Pattern.compile("<M (.*?)>(.+?)</M>");

        String mentionsScheme = String.format("%s/?%s=",
                "devdiv://sina_profile", "uid");

        Linkify.addLinks(v, mymentionsPattern, mentionsScheme, null,
                new TransformFilter()
                {
                    public String transformUrl(Matcher match, String url)
                    {
                        return match.group(1);
                    }
                });

        Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
        String trendsScheme = String.format("%s/?%s=",
                "devdiv://sina_profile2", "uid");
        Linkify.addLinks(v, trendsPattern, trendsScheme, null,
                new TransformFilter()
                {
                    @Override
                    public String transformUrl(Matcher match, String url)
                    {
                        return match.group(1);
                    }
                });
        v.setText(htmlfrom(v.getText()));

    }

    public static CharSequence htmlfrom(CharSequence text)
    {
        Pattern htmlflag1 = Pattern.compile("<(.*?)>");
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = htmlflag1.matcher(text);
        while (matcher.find())
        {
            builder.delete(matcher.start(), matcher.end());
            text = builder;
            matcher = htmlflag1.matcher(text);
        }
        Pattern htmlflag2 = Pattern.compile("&(.*?);");
        matcher = htmlflag2.matcher(text);
        while (matcher.find())
        {
            builder.delete(matcher.start(), matcher.end());
            text = builder;
            matcher = htmlflag2.matcher(text);
        }
        return builder;

    }

    public static void recycle(Bitmap[] bitmaps)
    {
        for (int i = 0; i < bitmaps.length; i++)
        {
            if (bitmaps[i] != null && !bitmaps[i].isRecycled())
            {
                bitmaps[i].recycle();
                bitmaps[i] = null;
            }
        }
    }

    public static Bitmap createThumbnailBitmap(Context context, String path,
            int size)
    {
        InputStream input = null;

        try
        {
            Uri uri = Uri.fromFile(new File(path));
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            input.close();

            // Compute the scale.
            int scale = 1;
            while ((options.outWidth / scale > size)
                    || (options.outHeight / scale > size))
            {
                scale *= 2;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            input = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input, null, options);
        }
        catch (IOException e)
        {
            return null;
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    public static boolean checkNameChese(String name)
    {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++)
        {
            if (!isChinese(cTemp[i]))
            {
                res = false;
                break;
            }
        }
        return res;
    }

    public static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
        {
            return true;
        }
        return false;
    }

    public static void saveMyBitmap(String bitName, Bitmap mBitmap,
            boolean isCheckSdcard, Bitmap.CompressFormat type)
            throws IOException
    {
        if (mBitmap == null)
        {
            return;
        }
        if (isCheckSdcard)
        {
            if (!isExistSdCard())
            {
                return;
            }
        }
        File f = new File(bitName);
        if (f.exists())
        {
            f.delete();
        }
        if (!f.getParentFile().exists())
        {
            f.getParentFile().mkdirs();
        }
        f.createNewFile();
        FileOutputStream fOut = null;
        try
        {
            fOut = new FileOutputStream(f);
        }
        catch (FileNotFoundException e)
        {
            LogUtil.d("FileNotFoundException", e.getMessage());
        }
        mBitmap.compress(type, 75, fOut);
        if (fOut != null)
        {
            try
            {
                fOut.flush();
                fOut.close();
            }
            catch (IOException e)
            {
                LogUtil.d("IOException", e.getMessage());
            }
        }
    }

    public static boolean isExistSdCard()
    {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static Bitmap returnBitmap(String url)
    {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try
        {
            myFileUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        try
        {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static synchronized Bitmap loadBitmap(String url)
    {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try
        {
            myFileUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        try
        {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromSd(String imgFilePath)
    {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[12 * 1024];
        bfOptions.inInputShareable = true;
        // bfOptions.inJustDecodeBounds = true;
        File file = new File(imgFilePath);
        FileInputStream fs = null;
        Bitmap bmp = null;
        try
        {
            fs = new FileInputStream(file);
            // bmp = BitmapFactory.decodeStream(fs, null, bfOptions);
            bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
                    bfOptions);
        }
        catch (Exception e)
        {
            LogUtil.e("Exception", e.getMessage());
        }
        finally
        {
            if (fs != null)
            {
                try
                {
                    fs.close();
                }
                catch (IOException e)
                {
                    LogUtil.e("IOException", e.getMessage());
                }
            }
        }
        return bmp;
    }

    public static boolean isExists(String filePath)
    {
        File f = new File(filePath);
        if (f.exists())
        {
            if (f.length() == 0)
            {
                f.delete();
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean hasNotEmptyParam(String... params)
    {
        int size = params.length;
        for (int i = 0; i < size; i++)
        {
            if (TextUtils.isEmpty(params[i]))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean hideInputMethod(Context context)
    {
        View v = ((Activity) context).getCurrentFocus();
        if (v == null)
        {
            return false;
        }

        IBinder binder = v.getWindowToken();
        if (binder == null)
        {
            return false;
        }

        InputMethodManager manager = ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
        {
            return manager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        return false;
    }

    public static void showInputMethod(final Context context)
    {
        InputMethodManager m = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static String getNetType(Context context)
    {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null)
        {
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null)
            {
                String type = info.getTypeName().toLowerCase(
                        Locale.getDefault());// MOBILE��GPRS��;WIFI
                if (type.equals("wifi"))
                {
                    return type;
                }
                else if (type.equals("mobile"))
                {
                    String apn = info.getExtraInfo().toLowerCase(
                            Locale.getDefault());
                    if (apn != null
                            && (apn.equals("cmwap") || apn.equals("3gwap")
                                    || apn.equals("uniwap") || apn
                                        .equals("ctwap")))
                    {
                        return "wap";
                    }
                    else
                    {
                        return apn;
                    }
                }
            }
        }
        return null;
    }

    public static Bitmap Create2DCode(String str) throws WriterException
    {
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 240, 240);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (matrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap Create2DCode(String str, int imageWidth,
            int imageHeight) throws WriterException
    {
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, imageWidth, imageHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (matrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 2000)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setImageSelector(Context context,
            final ImageView imageView, int drawableNormal, int drawableFocused)
    {
        InputStream streamNormal = context.getResources().openRawResource(
                drawableNormal);
        final Bitmap bitmapNormal = BitmapFactory.decodeStream(streamNormal);

        InputStream streamFocused = context.getResources().openRawResource(
                drawableFocused);
        final Bitmap bitmapFocused = BitmapFactory.decodeStream(streamFocused);

        imageView.setImageBitmap(bitmapNormal);

        imageView.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    imageView.setImageBitmap(bitmapFocused);
                }
                else
                {
                    imageView.setImageBitmap(bitmapNormal);
                }
            }
        });

    }

    public static void setImageBackground(Context context, ImageView imageView,
            int drawableNormal)
    {
        InputStream streamNormal = context.getResources().openRawResource(
                drawableNormal);
        Bitmap bitmapNormal = BitmapFactory.decodeStream(streamNormal);
        imageView.setImageBitmap(bitmapNormal);
    }

    public static int getVersionCode()
    {
        int versionCode = -1;
        try
        {
            versionCode = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
        }
        catch (NameNotFoundException e)
        {
            LogUtil.e("NameNotFoundException", e.getMessage());
        }
        return versionCode;
    }

    public static String getVersionName()
    {
        String versionName = "0.00";
        try
        {
            versionName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            LogUtil.e("NameNotFoundException", e.getMessage());
        }
        return versionName;
    }

    public static String getPackageName()
    {
        String packageName = null;
        try
        {
            packageName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).packageName;
        }
        catch (NameNotFoundException e)
        {
            LogUtil.e("NameNotFoundException", e.getMessage());
        }
        return packageName;
    }

    public static String getAPKMd5()
    {
        FileInputStream fileInputStream = null;
        try
        {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(),
                            PackageManager.GET_SIGNATURES);

            String filePath = packageInfo.applicationInfo.sourceDir;

            File apkFile = new File(filePath);
            fileInputStream = new FileInputStream(apkFile);
            return MD5Util.md5Digest(fileInputStream);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                    fileInputStream = null;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getString(int resId)
    {
        return mContext.getResources().getString(resId);
    }

    public static String getUserAgent()
    {
        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0)
        {
            buffer.append(version);
        }
        else
        {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null)
        {
            buffer.append(language.toLowerCase(locale));
            final String country = locale.getCountry();
            if (country != null)
            {
                buffer.append("-");
                buffer.append(country.toLowerCase(locale));
            }
        }
        else
        {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME))
        {
            final String model = Build.MODEL;
            if (model.length() > 0)
            {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0)
        {
            buffer.append(" Build/");
            buffer.append(id);
        }
        final String base = mContext.getResources()
                .getText(R.string.webuseragent).toString();
        return String
                .format(base, PackageUtil.getVersionName(mContext), buffer);
    }

}
