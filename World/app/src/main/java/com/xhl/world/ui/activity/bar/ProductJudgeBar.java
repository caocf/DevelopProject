package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.model.EvaluateModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.RoundedImageView;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.xhl_library.utils.LogonUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/25.
 */
public class ProductJudgeBar extends BaseBar {

    @ViewInject(R.id.iv_judge_use_pic)//用户头像
    private RoundedImageView iv_judge_use_pic;

    @ViewInject(R.id.tv_judge_user_name)
    private TextView tv_judge_user_name;

    @ViewInject(R.id.tv_judge_time)
    private TextView tv_judge_time;

    @ViewInject(R.id.ll_judge_start)
    private LinearLayout ll_judge_start;//评价的星星数

    @ViewInject(R.id.tv_judge_content)
    private TextView tv_judge_content;//评价的内容

    @ViewInject(R.id.ll_judge_image)
    private LinearLayout ll_judge_image;//评价的图片


    public ProductJudgeBar(Context context) {
        super(context);
    }

    public ProductJudgeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {

    }

    //用户名
    public void subUserName(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        StringBuilder userName = new StringBuilder();

        if (LogonUtils.matcherLogonPhone(name)) {
            userName.append(name, 0, 3).append("****").append(name, 8, name.length());
        }
        tv_judge_user_name.setText(userName.toString());
    }

    public void onBindJudgeData(EvaluateModel evaluate) {

        subUserName(evaluate.getUserName());

        iv_judge_use_pic.bindImageUrl(evaluate.getVipPic());

        tv_judge_time.setText(evaluate.getCreateTime());

        tv_judge_content.setText(evaluate.getRateContent());

        //星星数目
        //1:5;2:5;3:5;
        Integer num = 0;
        try {
            String rate = evaluate.getRate();
            String[] rateNum = rate.split(";");
            String productRate = rateNum[0].split(":")[1];
            num = Integer.valueOf(productRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ll_judge_start.removeAllViews();
        for (int i = 0; i < num; i++) {
            ImageView view = new ImageView(mContext);
            view.setImageResource(R.drawable.icon_start);
            ll_judge_start.addView(view);
        }

        // /v1/tfs/T1nthTByYT1RCvBVdK,/v1/tfs/T1VRhTByDT1RCvBVdK
        loadPic(evaluate.getRateImg());
    }

    //评价图片
    private void loadPic(String rateImg) {
        if (TextUtils.isEmpty(rateImg)) {
            return;
        }
        ll_judge_image.removeAllViews();

        if (!rateImg.contains(",")) {
            ll_judge_image.addView(createEvaluateImage(rateImg));
        } else {
            String[] imageUrl = rateImg.split(",");
            for (String url : imageUrl) {
                ImageView view = createEvaluateImage(url);
                ll_judge_image.addView(view);
            }
        }
    }

    private ImageView createEvaluateImage(String img) {
        LifeCycleImageView imageView = new LifeCycleImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110, 110);
        params.leftMargin = 26;
        imageView.setLayoutParams(params);

        final String url = NetWorkConfig.imageHost + img;

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDetails(url);
            }
        });
        imageView.bindImageUrl(url);

        AutoUtils.autoSize(imageView);
        return imageView;
    }

    private void imageDetails(String url) {
        EventBusHelper.postImageDetailsEvent(url);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_product_judge;
    }
}
