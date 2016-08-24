package com.xhl.xhl_library.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * Created by Sum on 15/11/21.
 */
public abstract class BaseFragment extends Fragment {

    private static final boolean DEBUG = false;
    private boolean isInject = false;
    protected View mView;//显示的Fragment布局,对象的缓存使用

    protected abstract void initParams();

    /**
     * -keepattributes *Annotation*
     * -keepclassmembers class * {
     * void *(android.view.View);
     * *** *Click(...);
     * *** *Event(...);
     * }
     **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (!isInject) {
            isInject = true;
            mView = x.view().inject(this, inflater, container);
            initParams();
        } else {
            ViewParent parent = mView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mView);
                LogUtil.e("base fragment remove from parent  " + this.getClass().getName());
            }
            if (DEBUG) {
                LogUtil.v("onCreateView show cache view");
            }
        }

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (DEBUG) {
            showStatus("onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            showStatus("onCreate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG) {
            showStatus("onDetach");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) {
            showStatus("onActivityCreated");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) {
            showStatus("onStart");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (DEBUG) {
            showStatus("onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            showStatus("onDestroy");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG) {
            showStatus("onResume");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) {
            showStatus("onStop");
        }
    }

    private void showStatus(String status) {
        LogUtil.d("fragment-lifecycle   " + status + "  " + this.getClass().getSimpleName());
    }

}
