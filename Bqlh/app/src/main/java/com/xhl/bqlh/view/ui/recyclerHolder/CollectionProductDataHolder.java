package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Summer on 2016/7/18.
 */
public class CollectionProductDataHolder extends RecyclerDataHolder<ProductModel> {

    private RecycleViewCallBack callBack;

    public CollectionProductDataHolder(ProductModel data, RecycleViewCallBack callBack) {
        super(data);
        this.callBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        View view = View.inflate(context, R.layout.item_collection_product, null);

        view.setLayoutParams(new RecyclerView.LayoutParams(-1, 230));

        AutoUtils.auto(view);

        return new Product(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductModel data) {
        Product holder = (Product) vHolder;
        holder.onBindData(data);
    }

    class Product extends RecyclerViewHolder implements View.OnClickListener {

        private ProductModel mData;

        private ImageView iv_product_image;

        private TextView tv_product_name;

        private TextView tv_product_price;

        private TextView tv_product_b_price;

        public Product(View view) {
            super(view);
            iv_product_image = (ImageView) view.findViewById(R.id.iv_product_image);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) view.findViewById(R.id.tv_product_price);
            tv_product_b_price = (TextView) view.findViewById(R.id.tv_product_b_price);
            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick();
                    return true;
                }
            });
        }

        public void onBindData(ProductModel data) {
            mData = data;
//            iv_product_image.LoadDrawable(data.getProductPic());
            LifeCycleImageView.LoadImageView(iv_product_image, data.getProductPic());
            tv_product_name.setText(data.getProductName());
            tv_product_price.setText(mContext.getString(R.string.price_s, data.getProductPrice()));

            String bussinessPrice = data.getBussinessPrice();
            if (!TextUtils.isEmpty(bussinessPrice)) {
                ViewHelper.setViewVisible(tv_product_b_price, true);
                tv_product_b_price.setText(mContext.getString(data.priceId(), bussinessPrice));
            } else {
                ViewHelper.setViewVisible(tv_product_b_price, false);
            }
        }

        @Override
        public void onClick(View v) {
            if (mData != null) {
                EventHelper.postProduct(mData.getContentId());
            }
        }

        private void longClick() {
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle("删除商品");
            dialog.setMessage("您确定删除收藏的商品吗?");
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDelete();
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }

        private void onDelete() {
            ApiControl.getApi().collectDelete(mData.getId(), new DefaultCallback<ResponseModel<Object>>() {
                @Override
                public void success(ResponseModel<Object> result) {
                    if (callBack != null) {
                        callBack.onItemClick(getAdapterPosition(), null);
                    }
                }

                @Override
                public void finish() {

                }
            });
        }
    }
}
