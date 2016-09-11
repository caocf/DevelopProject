package com.ebox.mgt.ui.fragment.pollingfg;

import android.app.Activity;
import android.app.Fragment;

import com.ebox.mgt.ui.fragment.pollingfg.model.ReqPolling;

/**
 * Created by prin on 2015/9/30.
 */
public class BasepollFragment extends Fragment {

    public ReqPolling reqPolling;
//    public OnReqpollListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof OnReqpollListener) {
//            listener = (OnReqpollListener) activity;
//        } else {
//            throw new RuntimeException("mp is null");
//        }
    }



//    /**
//     * 定义收集监听者的
//     */
//    public void setReqpollListener(ReqPolling req, OnReqpollListener listener) {
//        this.reqPolling=req;
//        this.listener=listener;
//    }
//
//
//    public interface OnReqpollListener {
//
//        void setReqpollParam();
//    }

}
