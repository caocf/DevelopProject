package com.xhl.bqlh;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sum on 16/6/29.
 */
public class RegexTest {


    public static boolean matcherLogonPhone(String logonPhone) {
        Pattern pMobile = Pattern.compile("[1][0-9]{10}$");
        Matcher matcher = pMobile.matcher(logonPhone);
        return matcher.matches();
    }

    public static boolean matcherLogonIdCard(String logonId) {
        Pattern pMobile = Pattern.compile("[0-9]{12}$");
        Matcher matcher = pMobile.matcher(logonId);
        return matcher.matches();
    }

    public static boolean matcherMail(String mail) {
        Pattern pMobile = Pattern
                .compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        Matcher matcher = pMobile.matcher(mail);
        return matcher.matches();
    }


    @Test
    public void startImageFilter() throws Exception {

//        String phone = "<img src=\"/v1/tfs/T1VyZTBCCT1RCvBVdK\" alt=\"\" /><img src=\"/v1/tfs/T1XthTB7hT1RCvBVdK\" alt=\"\" />";
        String phone = "&nbsp; &nbsp;";


//        Pattern pMobile = Pattern.compile("<.*?>");
        Pattern pMobile = Pattern.compile("src=\".+?\"");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
            int start = text.indexOf("\"");
            String substring = text.substring(start+1, text.length() - 2);
            print("url->" + substring);
        }
    }

    @Test
    public void startTime() throws Exception {

        String phone = "does";

        Pattern pMobile = Pattern.compile("do(es)?");
        Matcher matcher = pMobile.matcher(phone);
        String group = "";
        print("" + matcher.matches() + " group:" + group);
    }

    @Test
    public void testN() throws Exception {

        String phone = "ooo";

        Pattern pMobile = Pattern.compile("o?");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }

    @Test
    public void testM() throws Exception {

        String phone = "Is is the cost of of gasoline going up up?";

        Pattern pMobile = Pattern.compile("\\b([a-z]+) \1\\b");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }

    @Test
    public void testP() throws Exception {

        String phone = "Window 200";

        Pattern pMobile = Pattern.compile("Window (?:200|98)");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }

    @Test
    public void testQ() throws Exception {

        String phone = "Food";

        Pattern pMobile = Pattern.compile("[^aod]");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }
    @Test
    public void testPhone() throws Exception {

        String phone = "115656263709";

        Pattern pMobile = Pattern.compile("[1][1][0-9]{10}$");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }
    @Test
    public void testC() throws Exception {

        String phone = "aaaaa";
//        String phone = "a";

        Pattern pMobile = Pattern.compile("^a{2,}?");

        Matcher matcher = pMobile.matcher(phone);

        while (matcher.find()) {
            String text = phone.substring(matcher.start(), matcher.end());
            print("text:" + text);
        }

        print("mached:" + matcher.matches());
    }

    public static void print(String msg) {
        System.out.println(msg);
    }
}
