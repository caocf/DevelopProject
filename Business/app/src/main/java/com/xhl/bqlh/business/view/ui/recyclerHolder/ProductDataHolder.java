package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * Created by Sum on 16/4/15.
 */
public class ProductDataHolder extends RecyclerDataHolder<ProductModel> {

    public ProductDataHolder(ProductModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View bar = View.inflate(context, R.layout.item_product_statistics, null);
        bar.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new ProductViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductModel data) {
        ProductViewHolder holder = (ProductViewHolder) vHolder;
        holder.onBindData(data);
    }

    private class ProductViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.iv_product_pic)
        private LifeCycleImageView iv_product_pic;

        @ViewInject(R.id.tv_product_name)
        private TextView tv_product_name;

        @ViewInject(R.id.tv_product_price)
        private TextView tv_product_price;

        @ViewInject(R.id.tv_product_num)
        private TextView tv_product_num;

        public ProductViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(ProductModel data) {

            if (data == null) {
                return;
            }
            iv_product_pic.bindImageUrl(data.getProductPic());
            if (data.getProductType() == ProductType.PRODUCT_GIFT) {
                tv_product_name.setText(ProductType.GetGiftTitle(data.getProductName()));
                tv_product_price.setVisibility(View.INVISIBLE);
            } else {
                tv_product_name.setText(data.getProductName());
                tv_product_price.setVisibility(View.VISIBLE);
            }
            BigDecimal productPrice = data.getProductPrice();
            tv_product_price.setText(mContext.getString(data.getProductPriceHint(), NumberUtil.getDouble(productPrice.toString()), data.getSkuResult().getUnit()));
            tv_product_num.setText(mContext.getString(R.string.product_num, data.getStock()));
        }
    }
}
