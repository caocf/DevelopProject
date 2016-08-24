package com.xhl.world.ui.main.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.FlashSaleMode;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.Base.BaseFragment;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */

@ContentView(R.layout.v_fragment_falsh_sale)
public class FlashSaleFragment extends BaseFragment {

    @ViewInject(R.id.iv_flash_sale_i1)
    private LifeCycleImageView iv_flash_sale_i1;

    @ViewInject(R.id.iv_flash_sale_i2)
    private LifeCycleImageView iv_flash_sale_i2;

    @ViewInject(R.id.iv_flash_sale_i3)
    private LifeCycleImageView iv_flash_sale_i3;

    @ViewInject(R.id.tv_flash_sale__l1_money)
    private TextView tv_flash_sale__l1_money;

    @ViewInject(R.id.tv_flash_sale__l2_money)
    private TextView tv_flash_sale__l2_money;

    @ViewInject(R.id.tv_flash_sale__l3_money)
    private TextView tv_flash_sale__l3_money;

    @ViewInject(R.id.tv_flash_sale__l1_money_per)
    private TextView tv_flash_sale__l1_money_per;

    @ViewInject(R.id.tv_flash_sale__l2_money_per)
    private TextView tv_flash_sale__l2_money_per;

    @ViewInject(R.id.tv_flash_sale__l3_money_per)
    private TextView tv_flash_sale__l3_money_per;
    private boolean isFirstLoad = true;

    private FlashSaleMode mData;

    public static FlashSaleFragment newInstance(FlashSaleMode flashSaleMode) {
        FlashSaleFragment saleFragment = new FlashSaleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", flashSaleMode);
        saleFragment.setArguments(bundle);

        return saleFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mData = (FlashSaleMode) getArguments().getSerializable("data");
        if (mData == null) {
            LogUtil.e("flash sale item data is null");
        }
    }

    @Override
    protected void initParams() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            updateShow();
            isFirstLoad = false;
        }
    }

    private void updateShow() {
        if (mData != null) {
            List<String> data = mData.urls;
            for (int i = 0; i < data.size(); i++) {
                String url = data.get(i);
                if (i == 0) {
                    updateImage(iv_flash_sale_i1, url);
                    updateText(tv_flash_sale__l1_money, "45");
                    updatePerText(tv_flash_sale__l1_money_per, "6.4");
                } else if (i == 1) {
                    updateImage(iv_flash_sale_i2, url);
                    updateText(tv_flash_sale__l2_money, "4");
                    updatePerText(tv_flash_sale__l2_money_per, "1");
                } else if (i == 2) {
                    updateImage(iv_flash_sale_i3, url);
                    updateText(tv_flash_sale__l3_money, "3");
                    updatePerText(tv_flash_sale__l3_money_per, "98");
                } else {
                    break;
                }
            }
        }
    }

    private void updateImage(LifeCycleImageView iv, String url) {
        iv.bindImageUrl(url);
    }

    private void updateText(TextView tv, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(getString(R.string.home_false_sale_money, text));
        }
    }

    private void updatePerText(TextView tv, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(getString(R.string.home_false_sale_money_per, text));
        }
    }
}
