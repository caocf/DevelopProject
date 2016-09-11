package com.ebox.ex.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.QRImageUtil;

public class RechargeWeiActivity extends CommonActivity {

    private TextView tv_money, tv_id;
    private ImageView iv_qrcode;

    private String orderURL;
    private String orderMoney;
    private String orderId;
    private final static String TAG = "RechargeWeiActivity    ";
    private Title mTitle;
    private Title.TitleData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wei);
        MGViewUtil.scaleContentView(this, R.id.rootView);

        orderURL = getIntent().getStringExtra("order_url");
        orderMoney = getIntent().getStringExtra("order_money");
        orderId = getIntent().getStringExtra("order_id");

        LogUtil.i(TAG+orderMoney);

        initView();
    }

    private void initView() {
        initTitle();

        iv_qrcode = (ImageView) findViewById(R.id.ex_iv_arw_qrcode);
        tv_money = (TextView) findViewById(R.id.ex_tv_arw_money);
        tv_id = (TextView) findViewById(R.id.ex_tv_arw_id);

        if (orderURL != null) {
            Bitmap bitmap = QRImageUtil.createQRImage(orderURL, BitmapFactory.decodeResource(getResources(), R.drawable.ex_icon_weixin));
            iv_qrcode.setImageBitmap(bitmap);
        }

        if (orderMoney != null) {
            tv_money.setText(orderMoney+"元");
        }

        if (orderId != null) {
            tv_id.setText(orderId);
        }


    }

    private void initTitle() {
        mTitle = (Title) findViewById(R.id.title);
        data = mTitle.new TitleData();
        data.backVisibility = 1;
        data.tvContent = "微信支付";
        data.tvVisibility = true;
        mTitle.setData(data, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTitle.showTimer();
        LogUtil.i(TAG + "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTitle.stopTimer();
        LogUtil.i(TAG + "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //更新快递员信息
        sendBroadcast(new Intent(OperatorHomeActivity.BROADCAST_UPDATE_OPERATOR));
    }
}
