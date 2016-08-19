package com.xhl.bqlh.view.custom;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/1/22.
 */
public class ProductOperatorBroad {

    private Activity mContext;

    private ProductModel mProduct;
    private OperatorListener listener;
    private int mProductNum = 1;
    private int mMaxNum;
    private BottomSheetDialog mSheetDialog;

    public ProductOperatorBroad(Activity context, ProductModel product) {
        mContext = context;
        mProduct = product;
        mSheetDialog = new BottomSheetDialog(context);
        initView();
    }

    private TextView mNumText;

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_product_op, null);

        AutoUtils.auto(rootView);

        LifeCycleImageView pic = _findViewById(rootView, R.id.iv_product_pic);
        pic.bindImageUrl(mProduct.getProductPic());

        TextView name = _findViewById(rootView, R.id.tv_product_name);//名称
        name.setText(mProduct.getProductName());

        TextView price = _findViewById(rootView, R.id.tv_product_price);//价格
        price.setText(mContext.getString(R.string.price_b, mProduct.getBussinessPrice()));

        TextView stock = _findViewById(rootView, R.id.tv_product_stock);//库存
        stock.setText(mContext.getString(R.string.product_stock, mProduct.getStock()));

        TextView min = _findViewById(rootView, R.id.tv_min_num);//最小起订量
        min.setText(mContext.getString(R.string.product_min_mun, mProduct.getOrderMinNum()));

        rootView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddClick(mProductNum);
                    mSheetDialog.dismiss();
                }
            }
        });

        mMaxNum = Integer.valueOf(mProduct.getStock());

        mNumText = _findViewById(rootView, R.id.op_content);
        if (mProduct.mCurNum != 0) {
            mNumText.setText(String.valueOf(mProduct.mCurNum));
        } else {
            mNumText.setText(String.valueOf(mProduct.getOrderMinNum()));
        }

        mProductNum = mProduct.mCurNum;

        TextView add = _findViewById(rootView, R.id.op_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMaxNum <= mProductNum) {
                    return;
                }
                mProductNum++;
                mNumText.setText(String.valueOf(mProductNum));
            }
        });

        TextView sub = _findViewById(rootView, R.id.op_sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProductNum == mProduct.getOrderMinNum()) {
                    return;
                }
                mProductNum--;
                mNumText.setText(String.valueOf(mProductNum));
            }
        });

        mNumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNum();
            }
        });

        mSheetDialog.setContentView(rootView);
        mSheetDialog.setCanceledOnTouchOutside(true);
        mSheetDialog.setCancelable(true);
    }

    public <T> T _findViewById(View parent, int id) {

        return (T) parent.findViewById(id);
    }

    public void show() {
        if (mSheetDialog != null) {
            mSheetDialog.show();
        }
    }

    public void setListener(OperatorListener listener) {
        this.listener = listener;
    }

    private void inputNum() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("输入数量");
        View content = View.inflate(mContext, R.layout.dialog_input_num, null);

        final EditText input = (EditText) content.findViewById(R.id.ed_num);
        final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

        tv_num.setText(String.valueOf(mProduct.getStock()));
        String num = String.valueOf(mProduct.mCurNum);
        input.setText(num);
        input.setSelection(num.length());

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    Integer valueOf = Integer.valueOf(num);
                    if (valueOf > mProduct.getStock()) {
                        ToastUtil.showToastLong("超出库存数量");
                    } else if (valueOf < mProduct.getOrderMinNum()) {
                        ToastUtil.showToastLong("不能小于最小起定量");
                    } else {
                        mProduct.mCurNum = valueOf;
                        mProductNum = valueOf;
                        mNumText.setText(num);
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    public interface OperatorListener {
        void onAddClick(int num);
    }

}
