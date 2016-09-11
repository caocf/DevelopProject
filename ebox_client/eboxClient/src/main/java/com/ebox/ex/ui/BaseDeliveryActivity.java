package com.ebox.ex.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.ui.base.callback.OpenDoorResCallBack;
import com.ebox.ex.ui.fragment.BoxManualSelectionFragment;
import com.ebox.ex.ui.fragment.ResultDeliveryFragment;
import com.ebox.pub.service.AppService;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

public class BaseDeliveryActivity extends CommonActivity implements BoxManualSelectionFragment.OnFragmentSelectionListener,
        ResultDeliveryFragment.deliveryListener,OpenDoorResCallBack{

    private int fragment_tag;

    public static final int title_pickup_order = 0;//用户取件
    public static final int title_store_order = 1;//存件
    public static final int title_withdraw_order = 2;//回收快件
    public static final int title_manual_select= 3;//手工选择箱门

    private Title title;
    private Title.TitleData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);

        fragment_tag = getIntent().getIntExtra("tag",-1);

        MGViewUtil.scaleContentView(this, R.id.rootView);
        initTitle();
    }

    private void initTitle() {

        title=(Title) findViewById(R.id.title);
        data=title.new TitleData();
        data.backVisibility=1;
        data.tvContent=getContent();

        data.tvVisibility=true;
        title.setData(data, this);
    }

    private String getContent() {

        if (fragment_tag == title_manual_select)
        {//手动选择箱门
            return getResources().getString(R.string.ex_choose_yourself);
        }if (fragment_tag ==title_store_order)
        {//打开箱门界面
            return getResources().getString(R.string.ex_op_delivery_finish);
        }

        return "未定义类型";
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        if (fragment_tag == title_manual_select)
        {
            startFragment(BoxManualSelectionFragment.newInstance());
        }
        else  if (fragment_tag == title_store_order)
        {
            //投递快件不可点击返回
            title.back.setVisibility(View.GONE);
            startFragment(ResultDeliveryFragment.newInstance(getIntent().getExtras()));
            AppService.getIntance().setTimeoutLeft(30);
        }
    }

    private void startFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,fragment).commit();
    }


    @Override
    public void onBoxItemSelection(BoxInfoType infoType)
    {
        //手动选择箱门回调
        if (infoType!=null)
        {
            Intent data = new Intent();
            data.putExtra("box", infoType);
            setResult(RESULT_OK, data);
            this.finish();
        }
    }

    @Override
    public void confirmDelivery()
    {
        //继续投递
        Intent data = new Intent();
        data.putExtra("action", 0);
        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    public void cancelDelivery() {
        //取消投递
        Intent data = new Intent();
        data.putExtra("action", 1);
        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    public void exit() {
        //退出登陆
        Intent data = new Intent();
        data.putExtra("action",2);
        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    public void OpenSuccess() {

    }

    @Override
    public void OpenFailed()
    {
        //手动点击返回
        title.back.setVisibility(View.VISIBLE);
        //关闭界面
        this.finish();
    }
}
