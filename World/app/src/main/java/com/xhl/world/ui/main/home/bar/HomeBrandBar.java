package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */
public class HomeBrandBar extends BaseHomeBar {

    @ViewInject(R.id.tv_ad_name)
    private TextView tv_ad_name;

    @ViewInject(R.id.iv_brand_first)
    private LifeCycleImageView iv_brand_first;
    @ViewInject(R.id.iv_brand_sec)
    private LifeCycleImageView iv_brand_sec;
    @ViewInject(R.id.iv_brand_thr)
    private LifeCycleImageView iv_brand_thr;
    @ViewInject(R.id.iv_brand_small_1)
    private LifeCycleImageView iv_brand_small_1;
    @ViewInject(R.id.iv_brand_small_2)
    private LifeCycleImageView iv_brand_small_2;
    @ViewInject(R.id.iv_brand_small_3)
    private LifeCycleImageView iv_brand_small_3;
    @ViewInject(R.id.iv_brand_small_4)
    private LifeCycleImageView iv_brand_small_4;
    @ViewInject(R.id.iv_brand_small_5)
    private LifeCycleImageView iv_brand_small_5;

    @Event(value = {R.id.iv_brand_first, R.id.iv_brand_sec, R.id.iv_brand_thr,
            R.id.iv_brand_small_1, R.id.iv_brand_small_2, R.id.iv_brand_small_3, R.id.iv_brand_small_4, R.id.iv_brand_small_5})
    private void onBrandClick(View view) {
       /* switch (view.getId()) {
            case R.id.iv_brand_first:
                break;
            case R.id.iv_brand_sec:
                break;
            case R.id.iv_brand_thr:
                break;
            case R.id.iv_brand_small_1:
                break;
            case R.id.iv_brand_small_2:
                break;
            case R.id.iv_brand_small_3:
                break;
            case R.id.iv_brand_small_4:
                break;
            case R.id.iv_brand_small_5:
                break;
        }*/
        Object tag = view.getTag();
        if (tag != null && tag instanceof AdvModel) {
            EventBusHelper.postAdv((AdvModel) tag);
        }
    }

    private List<AdvModel> mBrandData;

    public HomeBrandBar(Context context) {
        super(context);
    }

    public HomeBrandBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    public void setName(String name){
        if (!TextUtils.isEmpty(name)) {
            tv_ad_name.setText(name);
        }
    }

    public void setBrandData(List<AdvModel> data) {
        if (mBrandData == data) {
            return;
        }
        mBrandData = data;
        setShowData(data);
    }

    private void setShowData(List<AdvModel> data) {
        for (int i = 0; i < data.size(); i++) {
            AdvModel model = data.get(i);
            String adLocation = model.getAdLocation();
            if (TextUtils.isEmpty(adLocation)) {
                continue;
            }
            String url = model.getImageUrl();

            Character charAt = adLocation.charAt(adLocation.length() - 1);

            int pos = Integer.parseInt(charAt.toString()) - 1;

            switch (pos) {
                case 0:
                    bindImage(iv_brand_first, url);
                    iv_brand_first.setTag(model);
                    break;
                case 1:
                    bindImage(iv_brand_sec, url);
                    iv_brand_sec.setTag(model);
                    break;
                case 2:
                    bindImage(iv_brand_thr, url);
                    iv_brand_thr.setTag(model);
                    break;
                case 3:
                    bindImage(iv_brand_small_1, url);
                    iv_brand_small_1.setTag(model);
                    break;
                case 4:
                    bindImage(iv_brand_small_2, url);
                    iv_brand_small_2.setTag(model);
                    break;
                case 5:
                    bindImage(iv_brand_small_3, url);
                    iv_brand_small_3.setTag(model);
                    break;
                case 6:
                    bindImage(iv_brand_small_4, url);
                    iv_brand_small_4.setTag(model);
                    break;
                case 7:
                    bindImage(iv_brand_small_5, url);
                    iv_brand_small_5.setTag(model);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_l_brand;
    }
}
