package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.activity.ProductDetailsActivity;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/6.
 */
public class SearchItemBar extends BaseBar implements Callback.CommonCallback<ResponseModel<Object>> {

    public SearchItemBar(Context context) {
        super(context);
    }

    public SearchItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewInject(R.id.iv_product_image)
    private ImageView iv_product_image;

    @ViewInject(R.id.tv_product_active)
    private TextView tv_product_active;

    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;

    @ViewInject(R.id.tv_product_b_price)
    private TextView tv_product_b_price;

    @ViewInject(R.id.tv_product_s_price)
    private TextView tv_product_s_price;

    @ViewInject(R.id.tv_product_numb)
    private TextView tv_product_numb;

    @ViewInject(R.id.btn_reduce)
    private View btn_reduce;

    @ViewInject(R.id.btn_add)
    private View btn_add;

    @Event(R.id.tv_product_numb)
    private void onFixNumClick(View view) {
        inputNum();
    }

    @Event(R.id.btn_reduce)
    private void onReduceClick(View view) {
        if (!AppDelegate.appContext.isLogin(mContext)) {
            return;
        }
        if (mItemData.mCurNum == mItemData.getOrderMinNum()) {
            String tip = getResources().getString(R.string.product_min_mun_tip, mItemData.mCurNum);
            ToastUtil.showToastLong(tip);
            return;
        }
        mCurFixNum = -1;
        fixNum();
    }

    @Event(R.id.btn_add)
    private void onAddClick(View view) {
        if (!AppDelegate.appContext.isLogin(mContext)) {
            return;
        }

        boolean containsId = GlobalCarInfo.instance().containsId(mProductId);
        if (containsId) {
            mCurFixNum = 1;
        } else if (mItemData.mCurNum == 0) {
            mCurFixNum = mItemData.getOrderMinNum();
        }
        fixNum();
    }

    @Event(R.id.rl_content)
    private void onDeatilsClick(View view) {
        String id = mItemData.getId();
        if (!TextUtils.isEmpty(id)) {
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
            intent.putExtra("id", id);
            mContext.startActivity(intent);
        }
    }

    private ProductModel mItemData;

    private int mCurFixNum = 0;//当前修改的数量的值

    private String mStoreId, mProductId;

    private boolean mIsLoading = false;

    @Override
    protected void initParams() {

    }

    //修改数量
    private void fixNum() {
        if (!mIsLoading) {
            mIsLoading = true;
            ApiControl.getApi().carAdd(mProductId, mStoreId, mCurFixNum, this);
        }
    }

    //更新显示
    private void updateToShow() {
        mItemData.mCurNum += mCurFixNum;
        //添加到全局中
        GlobalCarInfo.instance().putCarInfo(mItemData);

        updateInfo();
    }

    public void onBindData(ProductModel product) {
        if (product == null) {
            return;
        }
        mItemData = product;
        mStoreId = product.getStoreId();
        mProductId = product.getId();

        //添加已经加入的数量
        boolean containsId = GlobalCarInfo.instance().containsId(product.getId());
        if (containsId) {
            String carNum = GlobalCarInfo.instance().getCarNum(product.getId());
            product.mCurNum = Integer.valueOf(carNum);
        }

        //信息显示
//        iv_product_image.LoadDrawable(product.getProductPic());
        LifeCycleImageView.LoadImageView(iv_product_image, product.getProductPic());

        tv_product_name.setText(product.getProductName());
        //活动
        String productActive = product.getProductActive();
        if (!TextUtils.isEmpty(productActive)) {
            ViewHelper.setViewGone(tv_product_active, false);
            tv_product_active.setText(productActive);
        } else {
            ViewHelper.setViewGone(tv_product_active, true);
        }

        String bussinessPrice = product.getBussinessPrice();
        if (!TextUtils.isEmpty(bussinessPrice)) {
            ViewHelper.setViewVisible(tv_product_b_price, true);
            tv_product_b_price.setText(mContext.getString(mItemData.priceId(), bussinessPrice));
        } else {
            ViewHelper.setViewVisible(tv_product_b_price, false);
        }
        tv_product_s_price.setText(mContext.getString(R.string.price_s, product.getOriginalPrice()));

        updateInfo();
    }

    private void updateInfo() {
        if (mItemData.mCurNum > 0) {
            tv_product_numb.setText(String.valueOf(mItemData.mCurNum));
            ViewHelper.setViewGone(tv_product_numb, false);
            ViewHelper.setViewGone(btn_reduce, false);
        } else {
            ViewHelper.setViewGone(tv_product_numb, true);
            ViewHelper.setViewGone(btn_reduce, true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_search_product;
    }

    private void inputNum() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("输入数量");
        View content = View.inflate(mContext, R.layout.dialog_input_num, null);

        final EditText input = (EditText) content.findViewById(R.id.ed_num);
        final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

        tv_num.setText(String.valueOf(mItemData.getStock()));
        String num = String.valueOf(mItemData.mCurNum);
        input.setText(num);
        input.setSelection(num.length());

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    Integer valueOf = Integer.valueOf(num);
                    if (valueOf > mItemData.getStock()) {
                        ToastUtil.showToastLong("超出库存数量");
                    } else if (valueOf < mItemData.getOrderMinNum()) {
                        ToastUtil.showToastLong("不能小于最小起定量");
                    } else {
                        //要修改的数量
                        mCurFixNum = valueOf - mItemData.mCurNum;
                        fixNum();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onSuccess(ResponseModel<Object> result) {
        if (result.isSuccess()) {
            if (!GlobalCarInfo.instance().containsId(mProductId)) {
                ToastUtil.showToastLong(R.string.product_op_add_success);
            }

            updateToShow();

            EventHelper.postReLoadCarEvent();
        } else {
            ToastUtil.showToastLong(result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Logger.e(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mIsLoading = false;
    }
}
