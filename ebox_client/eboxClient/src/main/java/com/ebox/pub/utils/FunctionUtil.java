package com.ebox.pub.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.enums.ItemStatusType;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionUtil
{
    public static final String HTTP_WAP = "wap";

    private static long lastClickTime;

    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void shakeAnimation(Context context, View v)
    {
        // Animation shake = AnimationUtils.loadAnimation(context,
        // R.anim.shake);
        // v.startAnimation(shake);
    }

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick(int millisecond)
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < millisecond)
        {
            return true;
        }
        lastClickTime = time;
        return false;
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
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(data);
    }

    public static String getDouble(String data)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(parseDoubleByString(data));
    }

    public static String fixNumber(int number)
    {
        if (number < 10)
        {
            return "0" + number;
        }
        else
        {
            return String.valueOf(number);
        }
    }

    // 12345 -> 12,345
    public static String formatPoolNum(String pools)
    {
        int length = pools.length();
        StringBuilder sb = new StringBuilder();
        int start;
        int num;

        if (length <= 3)
        {
            return pools;
        }

        start = length % 3;
        if (start == 0)
        {
            num = length / 3;
            start = 3;
        }
        else
        {
            num = length / 3 + 1;
        }

        sb.append(pools.substring(0, start));
        start = sb.length();

        for (int i = 1; i < num; i++)
        {
            sb.append(",");
            sb.append(pools.substring(start, start + 3));
            start = start + 3;
        }

        return sb.toString();
    }

    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static SpannableStringBuilder highLight(String text,
            String delimiters, int colorRed, int colorBlue)
    {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);// ���ڿɱ��ַ���
        CharacterStyle spanRed = null;
        CharacterStyle spanBlue = null;
        int end = text.indexOf(delimiters);
        if (end == -1)
        {
            spanRed = new ForegroundColorSpan(colorRed);
            spannable.setSpan(spanRed, 0, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else
        {
            spanRed = new ForegroundColorSpan(colorRed);
            spanBlue = new ForegroundColorSpan(colorBlue);
            spannable.setSpan(spanRed, 0, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(spanBlue, end + 1, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.replace(end, end + 1, " ");
        }
        return spannable;
    }

    public static boolean matcherTel(String logonId)
    {
        Pattern pMobile = Pattern.compile("[1][0-9]{10}$");
        Matcher matcher = pMobile.matcher(logonId);
        if (!matcher.matches())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }

    public static String hexByes2String(byte[] bytes,String charsetName){
        if("".equals(charsetName)) charsetName="UTF-16BE";
        try {
            return new String(bytes,charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static byte[] hexBytesH2L(byte[] bytes){
        byte tmp = 0;
        for(int i=0;i<bytes.length;i++){

            if(i%2==0) {
                tmp=bytes[i];
                bytes[i]=bytes[i+1];

            }
            else bytes[i]=tmp;

        }
        return bytes;
    }

    public static String getSexString(String r){
        String rt="";
        int rx = Integer.parseInt(r);
        switch(rx) {
            case 0:
                rt="未知";
                break;
            case 1:
                rt="男";
                break;
            case 2:
                rt="女";
                break;
            case 9:
                rt="未说明";
                break;
        }
        return rt;
    }

    public static String getNationalString(String code) {
        String rtn= "";
        if(code.equals("01")){
            rtn= "汉";
        } else if(code.equals("02")){
            rtn= "蒙古";
        } else if(code.equals("03")){
            rtn= "回";
        } else if(code.equals("04")){
            rtn= "藏";
        } else if(code.equals("05")){
            rtn= "维吾尔";
        } else if(code.equals("06")){
            rtn= "苗";
        } else if(code.equals("07")){
            rtn= "彝";
        } else if(code.equals("08")){
            rtn= "壮";
        } else if(code.equals("09")){
            rtn= "布依";
        } else if(code.equals("10")){
            rtn= "朝鲜";
        } else if(code.equals("11")){
            rtn= "满";
        } else if(code.equals("12")){
            rtn= "侗";
        } else if(code.equals("13")){
            rtn= "瑶";
        } else if(code.equals("14")){
            rtn= "白";
        } else if(code.equals("15")){
            rtn= "土家";
        } else if(code.equals("16")){
            rtn= "哈尼";
        } else if(code.equals("17")){
            rtn= "哈萨克";
        } else if(code.equals("18")){
            rtn= "傣";
        } else if(code.equals("19")){
            rtn= "黎";
        } else if(code.equals("20")){
            rtn= "傈僳";
        } else if(code.equals("21")){
            rtn= "佤";
        } else if(code.equals("22")){
            rtn= "畲";
        } else if(code.equals("23")){
            rtn= "高山";
        } else if(code.equals("24")){
            rtn= "拉祜";
        } else if(code.equals("25")){
            rtn= "水";
        } else if(code.equals("26")){
            rtn= "东乡";
        } else if(code.equals("27")){
            rtn= "纳西";
        } else if(code.equals("28")){
            rtn= "景颇";
        } else if(code.equals("29")){
            rtn= "柯尔克孜";
        } else if(code.equals("30")){
            rtn= "土";
        } else if(code.equals("31")){
            rtn= "达斡尔";
        } else if(code.equals("32")){
            rtn= "仫佬";
        } else if(code.equals("33")){
            rtn= "羌";
        } else if(code.equals("34")){
            rtn= "布朗";
        } else if(code.equals("35")){
            rtn= "撒拉";
        } else if(code.equals("36")){
            rtn= "毛南";
        } else if(code.equals("37")){
            rtn= "仡佬";
        } else if(code.equals("38")){
            rtn= "锡伯";
        } else if(code.equals("39")){
            rtn= "阿昌";
        } else if(code.equals("40")){
            rtn= "普米";
        } else if(code.equals("41")){
            rtn= "塔吉克";
        } else if(code.equals("42")){
            rtn= "怒";
        } else if(code.equals("43")){
            rtn= "乌孜别克";
        } else if(code.equals("44")){
            rtn= "俄罗斯";
        } else if(code.equals("45")){
            rtn= "鄂温克";
        } else if(code.equals("46")){
            rtn= "德昂";
        } else if(code.equals("47")){
            rtn= "保安";
        } else if(code.equals("48")){
            rtn= "裕固";
        } else if(code.equals("49")){
            rtn= "京";
        } else if(code.equals("50")){
            rtn= "塔塔尔";
        } else if(code.equals("51")){
            rtn= "独龙";
        } else if(code.equals("52")){
            rtn= "鄂伦春";
        } else if(code.equals("53")){
            rtn= "赫哲";
        } else if(code.equals("54")){
            rtn= "汉";
        } else if(code.equals("55")){
            rtn= "门巴";
        } else if(code.equals("56")){
            rtn= "珞巴";
        } else if(code.equals("97")){
            rtn= "其他";
        } else if(code.equals("98")){
            rtn= "外国血统中国籍人士";
        }


        return rtn;
    }

    public static int byteToInt(byte[] b) {

        int mask=0xff;
        int temp=0;
        int n=0;
        if(b.length>=2){
            for(int i=0;i<2;i++){
                n<<=8;
                temp=b[i]&mask;
                n|=temp;
            }
        }

        return n;
    }
    /**
     * 将Byte数组转换成十六进制字符串
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static String byteToHex(byte[] b) {
        String str = "";
        for(int i = 0; i < b.length; i++)
        {
            str += ",0x"+Integer.toHexString(b[i] & 0xFF);
        }
        return str;
    }
    /**
     * 将一个单字节的Byte转换成十六进制的数
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return "0x"+Integer.toHexString(i);
    }

    /**
     * 判断传入的参数号码为哪家运营商
     * @param mobile
     * @return 运营商名称
     */
    public static String validateMobile(String mobile){
        String returnString="";
        if(mobile==null || mobile.trim().length()!=11){
            return "-1";        //mobile参数为空或者手机号码长度不为11，错误！
        }
        if(mobile.trim().substring(0,3).equals("134") ||  mobile.trim().substring(0,3).equals("135") ||
                mobile.trim().substring(0,3).equals("136") || mobile.trim().substring(0,3).equals("137")
                || mobile.trim().substring(0,3).equals("138")  || mobile.trim().substring(0,3).equals("139") ||  mobile.trim().substring(0,3).equals("150") ||
                mobile.trim().substring(0,3).equals("151") || mobile.trim().substring(0,3).equals("152")
                || mobile.trim().substring(0,3).equals("157") || mobile.trim().substring(0,3).equals("158") || mobile.trim().substring(0,3).equals("159")
                || mobile.trim().substring(0,3).equals("187") || mobile.trim().substring(0,3).equals("188")){
            returnString="01";   //中国移动
        }
        if(mobile.trim().substring(0,3).equals("130") ||  mobile.trim().substring(0,3).equals("131") ||
                mobile.trim().substring(0,3).equals("132") || mobile.trim().substring(0,3).equals("156")
                || mobile.trim().substring(0,3).equals("185")  || mobile.trim().substring(0,3).equals("186")){
            returnString="02";   //中国联通
        }
        if(mobile.trim().substring(0,3).equals("133") ||  mobile.trim().substring(0,3).equals("153") ||
                mobile.trim().substring(0,3).equals("180") || mobile.trim().substring(0,3).equals("189")){
            returnString="03";   //中国电信
        }
        if(returnString.trim().equals("")){
            returnString="00";   //未知运营商
        }
        return returnString;
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;)
        {
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public final static boolean validMobilePhone(String phone)
    {
        if(phone.length() == 11 &&
                (phone.startsWith("13")
                        || phone.startsWith("14")
                        || phone.startsWith("15")
                        || phone.startsWith("17")
                        || phone.startsWith("18")
                )
                &&isNumeric(phone))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getMoneyStr(long money)
    {
        String re ="";
        if(money < 0) {
            money = Math.abs(money);
            re +="￥ -";
            re +=money/100+"."+String.format("%02d",money%100);
        } else {
            re = "￥"+money/100+"."+String.format("%02d",money%100);
        }
        return re;
    }

    public static String getItemStatusDesc(Integer status)
    {
        if(status.equals(ItemStatusType.create))
        {
            return "未取走";
        }
        else if(status.equals(ItemStatusType.sms_fail))
        {
            return "投递成功";
        }
        else if(status.equals(ItemStatusType.sms_success))
        {
            return "投递成功";
        }
        else if(status.equals(ItemStatusType.item_timeout))
        {
            return "快件超时";
        }
        else if(status.equals(ItemStatusType.operator_get))
        {
            return "快递员取出";
        }
        else if(status.equals(ItemStatusType.customer_get))
        {
            return "已取走";
        }
        else if(status.equals(ItemStatusType.admin_get))
        {
            return "管理员取出";
        }
        return "未知";
    }

    public static String getAllAppVersion() {
        String curVersion = "";

        PackageManager pManager = AppApplication.globalContext.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0
                    && pak.packageName.startsWith("com.ebox")) {
                curVersion = curVersion+pak.packageName+":"+pak.versionName+"    ";
            }
        }

        return curVersion;
    }

    public static String getCurVersion() {
        String curVersion = "";

        PackageManager pManager = AppApplication.globalContext.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0
                    && pak.packageName.equals("com.ebox")) {
                curVersion = pak.versionName;
                break;
            }
        }

        return curVersion;
    }

    public static void startApp(Context context, String pkg, String cls)
    {
        ComponentName componet = new ComponentName(pkg, cls);

        Intent i = new Intent();
        i.setComponent(componet);
        context.startActivity(i);
    }

    public static boolean checkApp(Context context, String pkg)
    {
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = context.getPackageManager().queryIntentActivities(mainIntent, 0);

        for(int i = 0; i < mApps.size(); i++)
        {
            ResolveInfo info = mApps.get(i);

            //该应用的包名
            String pkgThis = info.activityInfo.packageName;

            if(pkgThis.equals(pkg))
            {
                return true;
            }
        }

        return false;
    }

    /*
    **超过长度的字符转为。。。
     */
    public static String getCutString(String str,int count)
    {
        StringBuilder ret = new StringBuilder();
        if (parseNull(str).length()>count)
        {
            ret.append(str.substring(0,count));
            ret.append("...");
        }
        else
        {
            ret.append(parseNull(str));
        }
        return ret.toString();
    }
}
