package com.xhl.bqlh.view.helper;

import android.content.Context;
import android.content.Intent;

import com.xhl.bqlh.view.ui.activity.ContainerActivity;
import com.xhl.bqlh.view.ui.activity.SearchActivity;
import com.xhl.bqlh.view.ui.fragment.AreaSelectFragment;
import com.xhl.bqlh.view.ui.fragment.UserInfoFragment;
import com.xhl.bqlh.view.ui.fragment.UserLoginFragment;
import com.xhl.bqlh.view.ui.fragment.UserSettingFragment;

/**
 * Created by Sum on 16/7/1.
 */
public class FragmentContainerHelper {

    public static final String F_TAG = "fragment_tag";

    public static final String F_COLOR = "fragment_color";

    public static final int fragment_login = 10;
    public static final int fragment_search = 11;
    public static final int fragment_setting = 12;
    public static final int fragment_user = 13;
    public static final int fragment_area = 14;

    public static Class getFragmentByTag(int tag) {
        Class cz = null;
        switch (tag) {
            case fragment_login:
                cz = UserLoginFragment.class;
                break;
            case fragment_setting:
                cz = UserSettingFragment.class;
                break;
            case fragment_user:
                cz = UserInfoFragment.class;
                break;
            case fragment_area:
                cz = AreaSelectFragment.class;
                break;
        }
        return cz;
    }

    public static void startFragment(Context context, int tag) {
        if (tag == fragment_search) {
            Intent intent = new Intent(context, SearchActivity.class);
            context.startActivity(intent);
        } else {
            startFragment(context, tag, -1);
        }
    }

    public static void startFragment(Context context, int tag, int colorResId) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(F_TAG, tag);
        intent.putExtra(F_COLOR, colorResId);
        context.startActivity(intent);
    }

}
