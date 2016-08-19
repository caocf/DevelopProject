package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.Model.Type.StoreProductType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/5/31.
 */
public class StoreApplyUpdateDataHolder extends RecyclerDataHolder<ApplyModel> {

    public StoreApplyUpdateDataHolder(ApplyModel data) {
        super(data);
    }

    @Override
    public int getType() {
        return 12;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store_apply_update, null);
        return new OrderProductItemRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ApplyModel data) {
        OrderProductItemRecyclerView h = (OrderProductItemRecyclerView) vHolder;
        h.onBindData(data);
    }

    class OrderProductItemRecyclerView extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.iv_product_pic)
        private LifeCycleImageView iv_product_pic;

        @ViewInject(R.id.tv_product_name)
        private TextView tv_product_name;

        @ViewInject(R.id.tv_product_price)
        private TextView tv_product_price;

        @ViewInject(R.id.tv_product_max)
        private TextView tv_product_max;

        @ViewInject(R.id.tv_product_num)
        private TextView tv_product_num;

        @ViewInject(R.id.tv_product_order_num)
        private TextView tv_product_order_num;

        @ViewInject(R.id.btn_reduce)
        private View btn_reduce;

        @ViewInject(R.id.btn_add)
        private View btn_add;

        private ApplyModel mItemData;

        public OrderProductItemRecyclerView(View view) {
            super(view);
            x.view().inject(this, view);
            btn_reduce.setOnClickListener(this);
            btn_add.setOnClickListener(this);
        }

        public void onBindData(ApplyModel data) {
            if (data != null) {
                mItemData = data;
                updateOperator(data);

                int productType = data.getProductType();
                //订单数量不为0的时候显示为包含n订单商品
                if (data.pMinNum != 0) {
                    ViewHelper.setViewVisible(tv_product_order_num, true);
                    //订单商品数目
                    tv_product_order_num.setText(mContext.getString(R.string.product_order_num, data.pMinNum));
                }
                //新增装车单商品
                else if (productType == StoreProductType.TYPE_UPDATE_PRODUCT) {
                    ViewHelper.setViewVisible(tv_product_order_num, true);
                    tv_product_order_num.setText(mContext.getString(R.string.product_order_new));
                }
                //新增装车单赠品
                else if (productType == StoreProductType.TYPE_UPDATE_PRODUCT_GIFT) {
                    ViewHelper.setViewVisible(tv_product_order_num, true);
                    tv_product_order_num.setText(mContext.getString(R.string.product_order_new_gift));
                } else {
                    ViewHelper.setViewVisible(tv_product_order_num, false);
                }
                //商品名称
                if (productType == StoreProductType.TYPE_UPDATE_PRODUCT_GIFT || productType == ProductType.PRODUCT_GIFT) {
                    tv_product_name.setText(ProductType.GetGiftTitle(data.getProductName()));
                    tv_product_price.setVisibility(View.INVISIBLE);
                } else {
                    //批发价
                    tv_product_name.setText(data.getProductName());
                    tv_product_price.setVisibility(View.VISIBLE);
                    tv_product_price.setText(mContext.getResources().getString(data.getProductPriceHint(), data.getUnitPrice(), data.getSkuResult().getUnit()));
                }
                //库存
                tv_product_max.setText(mContext.getResources().getString(R.string.product_max_num, data.getStock()));

                //图片
                String productPic = data.getProductPic();
                if (!URLUtil.isNetworkUrl(productPic)) {//新增的商品数据转换对象时候图片已有域名
                    productPic = NetWorkConfig.imageHost + productPic;
                }
                iv_product_pic.bindImageUrl(productPic);
            }
        }

        //更新操作显示(最小数量仅做订单商品显示，不做加减数据操作和比较，加减操作仅操作车销商品类型的数据（StoreProductType 2，3状态）)
        private void updateOperator(ApplyModel model) {
            if (model.getApplyNum() > 0) {
                ViewHelper.setViewGone(tv_product_num, false);
                ViewHelper.setViewGone(btn_reduce, false);
            } else {
                if (model.pMinNum == 0) {
                    ViewHelper.setViewGone(tv_product_num, true);
                }
                ViewHelper.setViewGone(btn_reduce, true);
            }
            //商品数量(包含最小订单商品数量)
            tv_product_num.setText(String.valueOf(model.getApplyNum() + model.pMinNum));
        }

        @Override
        public void onClick(View v) {
            if (mItemData == null) {
                return;
            }
            if (v.getId() == R.id.btn_add) {
                //添加和库存比较
                if (mItemData.getApplyNum() < mItemData.getStock()) {
                    int applyNum = mItemData.getApplyNum();
                    mItemData.setApplyNum(++applyNum);
                    update();
                }
            } else if (v.getId() == R.id.btn_reduce) {
                if (mItemData.getApplyNum() > 0) {
                    int applyNum = mItemData.getApplyNum();
                    mItemData.setApplyNum(--applyNum);
                    update();
                }
            }
        }

        private void update() {
            //刷新操作显示
            updateOperator(mItemData);
        }

    }
}
