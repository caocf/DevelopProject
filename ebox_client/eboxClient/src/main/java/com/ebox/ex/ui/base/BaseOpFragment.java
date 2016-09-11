package com.ebox.ex.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ebox.ex.ui.OperatorHomeActivity;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.service.global.Constants;

/**
 * Created by Android on 2015/10/22.
 */
public abstract class BaseOpFragment extends BaseFragment {

    protected Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (Constants.DEBUG)
         Log.i(TAG,String.format("onCreate %s", ACTIVITY_NAME));
    }

    public String operatorPhone() {
        if (context instanceof OperatorHomeActivity)
        {
            return ((OperatorHomeActivity)context).operatorId;
        }
        return OperatorHelper.mPhone;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Constants.DEBUG)
             Log.i(TAG, String.format("onDetach %s", ACTIVITY_NAME));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
