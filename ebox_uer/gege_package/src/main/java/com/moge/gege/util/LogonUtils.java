package com.moge.gege.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogonUtils
{

    public static boolean matcherLogonId(String logonId)
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

    public static boolean matcherLogonIdCard(String logonId)
    {
        Pattern pMobile = Pattern.compile("[0-9]{12}$");
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

    public static boolean matcherMail(String mail)
    {
        Pattern pMobile = Pattern
                .compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        Matcher matcher = pMobile.matcher(mail);
        if (!matcher.matches())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean matcherPassword(String password)
    {
        if (password.length() < 6 || password.length() > 20)
        {
            return false;
        }
        int num = 0;
        num = Pattern.compile("\\d").matcher(password).find() ? num + 1 : num;
        num = Pattern.compile("[a-zA-Z]").matcher(password).find() ? num + 1
                : num;
        num = Pattern.compile("[-.!@#$%^&*()+?]").matcher(password).find() ? num + 1
                : num;
        if (num >= 2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean matcherLogonPassword(String password)
    {
        if (password.length() < 6 || password.length() > 20)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public static boolean matcherCheckCode(String checkCode)
    {
        if (checkCode.length() < 4 || checkCode.length() > 10)
        {
            return false;
        }
        int num = 0;
        num = Pattern.compile("\\d").matcher(checkCode).find() ? num + 1 : num;
        num = Pattern.compile("[a-zA-Z]").matcher(checkCode).find() ? num + 1
                : num;
        if (num >= 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
