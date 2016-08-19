package com.xhl.bqlh.business.view.ui.bar;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseBar;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/7/25.
 */
public class StoreCarReturnBar extends BaseBar implements View.OnClickListener {

    public StoreCarReturnBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.cb_state)
    private CheckBox cb_state;

    @ViewInject(R.id.iv_product_pic)
    private LifeCycleImageView iv_product_pic;

    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;

    @ViewInject(R.id.tv_product_max)
    private TextView tv_product_max;

    @ViewInject(R.id.tv_product_numb)
    private TextView tv_product_numb;

    @Event(R.id.btn_reduce)
    private void onReduceClick(View view) {
        if (mData.curNum > 0) {
            mData.curNum--;
            update();
        }
    }

    @Event(R.id.btn_add)
    private void onAddClick(View view) {
        if (mData.curNum < mData.getStock()) {
            mData.curNum++;
            update();
        }
    }

    private void update() {
        if (mData.curNum == 0) {
            mData.checked = false;
        } else {
            mData.checked = true;
        }
        tv_product_numb.setText(String.valueOf(mData.curNum));
        cb_state.setChecked(mData.checked);
    }

    private ProductModel mData;

    @Override
    protected void initParams() {
        setOnClickListener(this);
    }

    public void onBindData(ProductModel product) {
        mData = product;
        iv_product_pic.bindImageUrl(product.getProductPic());
        tv_product_name.setText(product.getProductName());
        tv_product_max.setText("最大退仓数量:" + product.getStock()+product.getSkuResult().getUnit());
        tv_product_numb.setText(String.valueOf(product.curNum));
        cb_state.setChecked(product.checked);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_store_car_return;
    }

    @Override
    public void onClick(View v) {
        boolean dataChecked = mData.checked;
        cb_state.setChecked(!dataChecked);
        mData.checked = !dataChecked;
    }
}
