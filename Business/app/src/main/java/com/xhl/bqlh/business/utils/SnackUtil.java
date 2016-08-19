package com.xhl.bqlh.business.utils;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by Sum on 16/2/4.
 */
public class SnackUtil {

    public static void shortShow(View view, String msg) {
        if (view != null) {
            if (TextUtils.isEmpty(msg)) {
                msg = "网络异常";
            }
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void shortShow(View view, int resId) {
        if (view != null) {
            Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void longShow(View view, String msg) {
        if (view != null) {
            if (TextUtils.isEmpty(msg)) {
                msg = "网络异常";
            }
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void longShow(View view, int resId) {
        if (view != null)
            Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

}
