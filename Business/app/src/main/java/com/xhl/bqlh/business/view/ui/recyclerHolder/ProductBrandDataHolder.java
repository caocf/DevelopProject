package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductBrandModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/14.
 */
public class ProductBrandDataHolder extends RecyclerDataHolder<ProductBrandModel> {


    private RecycleViewCallBack mCallBack;

    private int mBaseColor;
    private int mGreenColor;


    public ProductBrandDataHolder(ProductBrandModel data,RecycleViewCallBack callBack) {
        super(data);
        mCallBack = callBack;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        mGreenColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mBaseColor = ContextCompat.getColor(context, R.color.base_light_text_color);

        return new BrandViewHolder(View.inflate(context, R.layout.item_product_brand, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductBrandModel data) {
        BrandViewHolder holder = (BrandViewHolder) vHolder;
        holder.mTvBrand.setText(data.getBrandName());

        if (getHolderState() == HOLDER_SELECT) {
            holder.mTvBrand.setTextColor(mGreenColor);
        } else {
            holder.mTvBrand.setTextColor(mBaseColor);
        }
    }

    class BrandViewHolder extends RecyclerViewHolder implements View.OnClickListener{

        TextView mTvBrand;

        public BrandViewHolder(View view) {
            super(view);
            mTvBrand = (TextView) view.findViewById(R.id.tv_brand);
            mTvBrand.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCallBack != null) {
                mCallBack.onItemClick(getAdapterPosition());
            }
        }
    }
}
