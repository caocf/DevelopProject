package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.OrderDetail;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.custom.RoundedImageView;
import com.xhl.bqlh.view.ui.activity.ProductDetailsActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/12.
 */
public class OrderItemProductBar extends BaseBar {

    public OrderItemProductBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.iv_product_image)
    private RoundedImageView imageView;

    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;

    @ViewInject(R.id.tv_product_price)
    private TextView tv_product_price;

    @ViewInject(R.id.tv_product_num)
    private TextView tv_product_num;

    @ViewInject(R.id.tv_product_active)
    private TextView tv_product_active;

    @ViewInject(R.id.tv_product_active_give)
    private TextView tv_product_active_give;

    private String mProductId;

    private boolean mHasDetails = true;

    @Event(R.id.iv_product_image)
    private void onDetailsClick(View view) {
        if (!TextUtils.isEmpty(mProductId) && mHasDetails) {
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
            intent.putExtra("id", mProductId);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void initParams() {
    }

    public void bindInfo(OrderDetail pro) {
        if (pro != null) {
            mProductId = pro.getGoodId();

            imageView.LoadDrawable(pro.getProductPic());
            tv_product_name.setText(pro.getProductName());
            tv_product_price.setText(getResources().getString(R.string.price, pro.getUnitPrice()));
            tv_product_num.setText("x" + pro.getNum());
            if (!TextUtils.isEmpty(pro.getRemark())) {
                tv_product_active.setVisibility(VISIBLE);
                tv_product_active_give.setVisibility(VISIBLE);
                tv_product_active.setText(pro.getRemark());
                tv_product_active_give.setText(getResources().getString(R.string.product_give_num, pro.getGiveNum()));
            } else {
                tv_product_active.setVisibility(GONE);
                tv_product_active_give.setVisibility(GONE);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_order_item_produt;
    }

    public void setmHasDetails(boolean mHasDetails) {
        this.mHasDetails = mHasDetails;
    }
}
