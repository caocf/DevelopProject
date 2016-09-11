package com.ebox.ex.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ebox.pub.service.global.Constants;
import com.ebox.pub.utils.MGViewUtil;

/**
 * Created by Android on 2015/10/27.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity context;
    public static final String TAG = "BaseOpFragment";
    private boolean isCache;
    private View mView;
    protected final String ACTIVITY_NAME = getClass().getSimpleName();

    //view id
    protected abstract int getViewId();

    //初始化view
    protected abstract void initView(View view);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (Constants.DEBUG)
            Log.i(TAG, String.format("onCreate %s", ACTIVITY_NAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Constants.DEBUG)
            Log.i(TAG, String.format("onCreateView %s", ACTIVITY_NAME));
        if (mView == null) {
            View view = inflater.inflate(getViewId(), null);
            mView = view;
            isCache = false;
            MGViewUtil.scaleContentView((ViewGroup) mView);
        } else {
            ViewParent parent = mView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeAllViews();
            }
            isCache = true;
        }
        if (!isCache) {
            initView(mView);
        }
        return mView;
    }

}
