package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.callback.SelectProductListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/4/7.
 */
public class ProductChildDataHolder extends RecyclerDataHolder<ProductModel> {

    private SelectProductListener mSelectListener;

    public ProductChildDataHolder(ProductModel data, SelectProductListener listener) {
        super(data);
        mSelectListener = listener;
    }

    @Override
    public int getType() {
        return 11;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_select_child, null);
        return new ProductItemRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductModel data) {
        ProductItemRecyclerView holder = (ProductItemRecyclerView) vHolder;
        holder.bindData(data);
    }

    private class ProductItemRecyclerView extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.iv_product_pic)
        private LifeCycleImageView iv_product_pic;

        @ViewInject(R.id.tv_product_name)
        private TextView tv_product_name;

        @ViewInject(R.id.tv_product_price)
        private TextView tv_product_price;

        @ViewInject(R.id.tv_product_max)
        private TextView tv_product_max;

        @ViewInject(R.id.tv_product_remark)
        private TextView tv_product_remark;

        @ViewInject(R.id.tv_product_numb)
        private TextView tv_product_numb;

        @ViewInject(R.id.ll_operator)
        private View ll_operator;

        @ViewInject(R.id.line)
        private View line;

        @ViewInject(R.id.btn_reduce)
        private View btn_reduce;

        @ViewInject(R.id.btn_add)
        private View btn_add;

        @ViewInject(R.id.rl_content)
        private View rl_content;

        private ProductModel mProduct;
        private Context mContext;

        public ProductItemRecyclerView(View view) {
            super(view);
            x.view().inject(this, view);
            mContext = view.getContext();
            btn_reduce.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            tv_product_numb.setOnClickListener(this);
        }

        public void bindData(ProductModel model) {
            if (model != null) {
                mProduct = model;
                updateOperator(model);
                //商品名称
                if (model.getProductType() == ProductType.PRODUCT_GIFT) {
                    tv_product_name.setText(ProductType.GetGiftTitle(model.getProductName()));
                } else {
                    tv_product_name.setText(model.getProductName());
                }
                tv_product_remark.setText(model.getPromoteRemark());
                //库存
                Integer stock = model.getStock();
                if (stock == 0) {
                    if (model.getProductType() == ProductType.PRODUCT_GIFT) {
                        tv_product_max.setText(mContext.getResources().getString(R.string.product_max1_0));
                    } else {
                        tv_product_max.setText(mContext.getResources().getString(R.string.product_max_0));
                    }
                } else {
                    if (model.getProductType() == ProductType.PRODUCT_GIFT) {
                        tv_product_max.setText(mContext.getResources().getString(R.string.product_max1_num, stock));
                    } else {
                        tv_product_max.setText(mContext.getResources().getString(R.string.product_max_num, stock));
                    }
                }
                if (model.getProductType() == ProductType.PRODUCT_GIFT) {
                    tv_product_price.setVisibility(View.GONE);
                } else {
                    //批发价
                    tv_product_price.setText(mContext.getResources().getString(model.getProductPriceHint(), model.getProductPrice(), model.getSkuResult().getUnit()));
                }
                //图片
                iv_product_pic.bindImageUrl(model.getProductPic());

                if (model.getStock() <= 0) {
                    ViewHelper.setViewGone(ll_operator, true);
                } else {
                    ViewHelper.setViewGone(ll_operator, false);
                }
                ViewHelper.setViewVisible(rl_content, true);
            } else {
                ViewHelper.setViewVisible(rl_content, false);
                ViewHelper.setViewGone(ll_operator, true);
                line.setVisibility(View.GONE);
            }
        }

        private void updateOperator(ProductModel model) {
            if (model.curNum > 0) {
                ViewHelper.setViewGone(tv_product_numb, false);
                ViewHelper.setViewGone(btn_reduce, false);
            } else {
                ViewHelper.setViewGone(tv_product_numb, true);
                ViewHelper.setViewGone(btn_reduce, true);
            }
            //商品数量
            tv_product_numb.setText(String.valueOf(model.curNum));
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_add) {
                //当前数量小于库存
                if (mProduct.curNum < mProduct.getStock()) {
                    mProduct.curNum++;
                    if (mSelectListener != null) {
                        mSelectListener.onAddCar(mProduct, iv_product_pic);
                    }
                    updateOperator(mProduct);
                }

            } else if (id == R.id.btn_reduce) {
                if (mProduct.curNum > 0) {
                    mProduct.curNum--;
                    if (mSelectListener != null) {
                        mSelectListener.onReduceCar(mProduct);
                    }
                    updateOperator(mProduct);
                }
            } else if (id == R.id.tv_product_numb) {
                onNumClick();
            }
        }

        private void onNumClick() {
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle("修改数量");
            View content = View.inflate(mContext, R.layout.dialog_input_num, null);

            final EditText input = (EditText) content.findViewById(R.id.ed_num);
            final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

            tv_num.setText(String.valueOf(mProduct.getStock()));
            String num = String.valueOf(mProduct.curNum);
            input.setText(num);
            input.setSelection(num.length());

            dialog.setView(content);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String num = input.getText().toString();
                    if (!TextUtils.isEmpty(num)) {
                        Integer integer = Integer.valueOf(num);
                        if (integer <= mProduct.getStock()) {
                            mProduct.curNum = integer;
                            updateOperator(mProduct);
                            if (mSelectListener != null) {
                                mSelectListener.onReduceCar(mProduct);
                            }
                        } else {
                            ToastUtil.showToastLong("超出商品总数量");
                        }
                    }
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
