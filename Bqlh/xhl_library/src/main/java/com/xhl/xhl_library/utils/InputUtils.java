package com.xhl.xhl_library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sum on 15/11/23.
 */
public class InputUtils {

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


    public static boolean isMobilePhoneNumb(String number)
    {
        Pattern pMobile = Pattern.compile("[1][0-9]{10}$");
        Matcher matcher = pMobile.matcher(number);
        return matcher.matches();
    }



}
