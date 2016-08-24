package com.xhl.world.ui.view;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ProductDetailsModel;

/**
 * Created by Sum on 16/1/22.
 */
public class ProductOperatorBroad {

    private Activity mContext;

    private ProductDetailsModel mProduct;
    private OperatorListener listener;
    private int mProductNum = 1;
    private int mMaxNum;
    private BottomSheetDialog mSheetDialog;

    public ProductOperatorBroad(Activity context, ProductDetailsModel product) {
        mContext = context;
        mProduct = product;
        mSheetDialog = new BottomSheetDialog(context);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_product_op, null);

        LifeCycleImageView pic = _findViewById(rootView, R.id.iv_product_pic);
        pic.bindImageUrl(mProduct.getProductPic());

        TextView name = _findViewById(rootView, R.id.tv_product_name);//名称
        name.setText(mProduct.getProductName());

        TextView price = _findViewById(rootView, R.id.tv_product_price);//价格
        price.setText(mContext.getString(R.string.price, mProduct.getRetailPrice()));

        TextView stock = _findViewById(rootView, R.id.tv_product_stock);//库存
        stock.setText(mContext.getString(R.string.product_stock, mProduct.getStock()));

        rootView.findViewById(R.id.ripple_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddClick(mProductNum);
                    mSheetDialog.dismiss();
                }
            }
        });

        mMaxNum = Integer.valueOf(mProduct.getStock());

        final TextView content = _findViewById(rootView, R.id.op_content);
        content.setText("1");

        TextView add = _findViewById(rootView, R.id.op_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMaxNum <= mProductNum) {
                    return;
                }
                mProductNum++;
                content.setText(String.valueOf(mProductNum));
            }
        });

        TextView sub = _findViewById(rootView, R.id.op_sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProductNum == 1) {
                    return;
                }
                mProductNum--;
                content.setText(String.valueOf(mProductNum));
            }
        });

        mSheetDialog.setContentView(rootView);
        mSheetDialog.setCanceledOnTouchOutside(true);
        mSheetDialog.setCancelable(true);
    }

    private void showPopupWindow() {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = 0.7f;

        mContext.getWindow().setAttributes(params);
    }

    private void closePopupWindow() {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = 1f;
        mContext.getWindow().setAttributes(params);
    }

    public <T> T _findViewById(View parent, int id) {

        return (T) parent.findViewById(id);
    }

    public void show() {
//        showAtLocation(mContext.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        if (mSheetDialog != null) {
            mSheetDialog.show();
        }
    }

    public void setListener(OperatorListener listener) {
        this.listener = listener;
    }

    public interface OperatorListener {
        void onAddClick(int num);
    }

}
