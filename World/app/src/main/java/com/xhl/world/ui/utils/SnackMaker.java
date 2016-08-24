package com.xhl.world.ui.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Sum on 16/2/4.
 */
public class SnackMaker {

    public static void shortShow(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void shortShow(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static void longShow(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void longShow(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

}
