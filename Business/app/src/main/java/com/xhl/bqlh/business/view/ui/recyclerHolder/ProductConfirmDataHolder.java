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

import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/4/9.
 */
public class ProductConfirmDataHolder extends RecyclerDataHolder<ShopCarModel> {

    private RecycleViewCallBack mCallBack;

    public ProductConfirmDataHolder(ShopCarModel data, RecycleViewCallBack callBack) {
        super(data);
        mCallBack = callBack;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_commit, null);
        return new ProductItemRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopCarModel data) {
        ProductItemRecyclerView holder = (ProductItemRecyclerView) vHolder;
        holder.bindData(data);
    }

    class ProductItemRecyclerView extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.iv_product_pic)
        private LifeCycleImageView iv_product_pic;

        @ViewInject(R.id.tv_product_name)
        private TextView tv_product_name;

        @ViewInject(R.id.tv_product_price)
        private TextView tv_product_price;

        @ViewInject(R.id.tv_product_max)
        private TextView tv_product_max;

        @ViewInject(R.id.tv_product_numb)
        private TextView tv_product_numb;

        @ViewInject(R.id.btn_reduce)
        private View btn_reduce;

        @ViewInject(R.id.btn_add)
        private View btn_add;

        private ShopCarModel mItemData;
        private Context mContext;

        public ProductItemRecyclerView(View view) {
            super(view);
            x.view().inject(this, view);
            mContext = view.getContext();
            btn_reduce.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            tv_product_numb.setOnClickListener(this);
        }

        public void bindData(ShopCarModel model) {
            if (model != null) {
                mItemData = model;
                updateOperator(model);
                //商品名称
                tv_product_name.setText(model.productName);
                //库存
                tv_product_max.setText(mContext.getResources().getString(R.string.product_max_num, model.maxNum));
                //批发价
                tv_product_price.setText(mContext.getResources().getString(R.string.product_price, model.productPrice,model.unitMin));
                //图片
                iv_product_pic.bindImageUrl(model.productPic);

            }
        }

        private void updateOperator(ShopCarModel model) {
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
            if (mItemData == null) {
                return;
            }
            if (v.getId() == R.id.tv_product_numb) {
                input();
                return;
            }

            if (v.getId() == R.id.btn_add) {
                //当前数量小于库存
                if (mItemData.curNum < mItemData.maxNum) {
                    mItemData.curNum++;
                    update();
                }
            } else if (v.getId() == R.id.btn_reduce) {
                if (mItemData.curNum > 0) {
                    mItemData.curNum--;
                    update();
                }
            }

        }

        private void update() {
            //刷新操作显示
            updateOperator(mItemData);
            //变向通知刷新总金额
            if (mCallBack != null) {
                mCallBack.onItemClick(getAdapterPosition());
            }
        }

        private void input() {
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle("修改数量");
            View content = View.inflate(mContext, R.layout.dialog_input_num, null);

            final EditText input = (EditText) content.findViewById(R.id.ed_num);
            final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

            tv_num.setText(String.valueOf(mItemData.maxNum));
            String num = String.valueOf(mItemData.curNum);
            input.setText(num);
            input.setSelection(num.length());

            dialog.setView(content);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String num = input.getText().toString();
                    if (!TextUtils.isEmpty(num)) {
                        Integer integer = Integer.valueOf(num);
                        if (integer <= mItemData.maxNum) {
                            mItemData.curNum = integer;
                            update();
                        } else {
                            ToastUtil.showToastLong("超出库存数量");
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
