package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ebox.R;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.OsInitHelper;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.Tip;

/**
 * 系统初始化
 */
public class FragmentInit extends Fragment implements View.OnClickListener{

    private View view;
    private Button btVerify;  //配置校验
    private Button btBox;   //箱门初始化
    private Button btAgain;   //重测
    private DialogUtil dialog;
    private Tip tip;
    private OsInitHelper helper;

    private OsInitHelper.OsInitListener listener=new OsInitHelper.OsInitListener() {
        @Override
        public void updateSuccess() {
            dialog.closeProgressDialog();
            tip = new Tip(getActivity(), "本地箱门数据初始化成功！", null);
            tip.show(0);
            btBox.setText("已完成服务端箱门数据同步");

            BoxInfoHelper.syncLocalOrderToBox();
        }

        @Override
        public void checkSuccess() {
            dialog.closeProgressDialog();
            tip = new Tip(getActivity(), "本地配置校验成功！", null);
            tip.show(0);
            btVerify.setText("本地配置已校验");
        }

        @Override
        public void updateFailed(String msg) {
            dialog.closeProgressDialog();
            tip = new Tip(getActivity(), msg, null);
            tip.show(0);
        }

        @Override
        public void checkFailed(String msg) {
            dialog.closeProgressDialog();
            tip = new Tip(getActivity(), msg, null);
            tip.show(0);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_init, container, false);
        initViews();
        dialog=new DialogUtil();
        dialog.createProgressDialog(getActivity());
        helper=new OsInitHelper(listener);
        return view;
    }

    private void initViews() {
        btVerify= (Button) view.findViewById(R.id.mgt_bt_fi_verify);
        btVerify.setOnClickListener(this);
        btBox= (Button) view.findViewById(R.id.mgt_bt_fi_box);
        btBox.setOnClickListener(this);
        btAgain= (Button) view.findViewById(R.id.mgt_bt_again);
        btAgain.setOnClickListener(this);
        updateView();
    }

    private void updateView() {
        boolean config= SharePreferenceHelper.getCheckConfigState();
        if (config)
        {
            btVerify.setText("本地配置已校验");
        }else {
            btVerify.setText("本地配置未校验");
        }
        config= SharePreferenceHelper.getInitBoxState();
        if (config)
        {
            btBox.setText("已完成服务端箱门数据同步");
        }else{
            btBox.setText("本地箱门数据未同步");
        }

        if (config) {
            btAgain.setVisibility(View.VISIBLE);
        }else
            btAgain.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mgt_bt_fi_verify:
                configVerify();
                break;
            case R.id.mgt_bt_fi_box:
                boxInit();
                break;
            case R.id.mgt_bt_again:
                SharePreferenceHelper.saveInitBoxState(false);
                SharePreferenceHelper.saveCheckConifgState(false);
                updateView();
                break;
        }
    }

    private void boxInit() {

        boolean checkConfigState = SharePreferenceHelper.getCheckConfigState();
        if (!checkConfigState) {
            tip=new Tip(getActivity(),"请先校验本地配置文件",null);
            tip.show(0);
        }else {
            if (SharePreferenceHelper.getInitBoxState()) {

                tip=new Tip(getActivity(),"服务端箱门数据已同步完成",null);
                tip.show(0);

            }else {

                dialog.showProgressDialog();
                helper.updateBoxState();
            }
        }

    }

    private void configVerify() {
        if (SharePreferenceHelper.getCheckConfigState())
        {
            tip=new Tip(getActivity(),"本地配置文件已校验完成",null);
            tip.show(0);
        }else {

            dialog.showProgressDialog();
            helper.checkLocalConfig();
        }
    }
}
