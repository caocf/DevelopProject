package com.xhl.world.ui.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sum on 15/12/7.
 */
public class ViewUtils {


    public static void changeViewVisible(View view, boolean booleanShow) {
        if (view == null) {
            return;
        }
        if (booleanShow) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void changeVisible(View view, boolean booleanShow) {
        if (view == null) {
            return;
        }
        if (booleanShow) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void showKeyBoard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyBoard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
