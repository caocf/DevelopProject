package com.xhl.xhl_library.Base.Sum;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhl.xhl_library.Base.BaseFragment;
import com.xhl.xhl_library.BaseConfig;
import com.xhl.xhl_library.utils.log.Logger;

/**
 * Created by Administrator on 2015/11/15.
 */
public abstract class SumFragment extends BaseFragment implements ISumFragment {

    private static final boolean DEBUG = BaseConfig.isDebug;
    protected Object mDataIn;
    protected boolean mFirstResume = true;
    public Context mContext;

    public SumFragmentActivity getSumContext() {
        if (getActivity() instanceof SumFragmentActivity) {
            return (SumFragmentActivity) getActivity();
        }
        return null;
    }

    @Override
    public void onEnter(Object data) {
        mDataIn = data;

        if (DEBUG) {
            showStatus("onEnter");
        }
    }

    @Override
    public void onLeave() {
        if (DEBUG) {
            showStatus("onLeave");
        }
    }

    @Override
    public void onBack() {
        if (DEBUG) {
            showStatus("onBack");
        }
    }

    @Override
    public void onBackWithData(Object data) {
        if (DEBUG) {
            showStatus("onBackWithData");
        }
    }

    @Override
    public boolean processBackPressed() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (DEBUG) {
            showStatus("onAttach");
        }
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            showStatus("onCreate");
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
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
        if (DEBUG) {
            showStatus("onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG) {
            showStatus("onPause");
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
    public void onDetach() {
        super.onDetach();
        if (DEBUG) {
            showStatus("onDetach");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG) {
            showStatus("onCreateView");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (DEBUG) {
            showStatus("onViewCreated");
        }
    }

    private void showStatus(String status) {
        Logger.d("fragment-lifecycle   " + status + "  " + this.getClass().getSimpleName());
    }
}
