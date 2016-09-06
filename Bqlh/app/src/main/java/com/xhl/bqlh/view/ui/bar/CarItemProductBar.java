package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.ui.activity.ProductDetailsActivity;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/9.
 */
public class CarItemProductBar extends BaseBar implements Callback.CommonCallback<ResponseModel<Object>> {

    private CarItemBar mParent;

    public CarItemProductBar(Context context, CarItemBar parent) {
        super(context);
        mParent = parent;
    }

    @ViewInject(R.id.tv_active_hint)
    private TextView tv_active_hint;

    @ViewInject(R.id.tv_child_goods_count)
    private TextView tv_goods_count;

    @ViewInject(R.id.tv_child_goods_title)
    private TextView tv_goods_title;

    @ViewInject(R.id.tv_child_goods_price)
    private TextView tv_goods_price;

    @ViewInject(R.id.tv_child_goods_old_price)
    private TextView tv_goods_old_price;

    @ViewInject(R.id.iv_child_goods_icon)
    private ImageView iv_goods_icon;

    @ViewInject(R.id.rb_child_check_all)
    private CheckBox rb_check_child;

    @Event(R.id.btn_child_reduce)
    private void onReduceGoodsClick(View view) {
        if (mCar.mCurNum > mMinNum) {
            mCurFixNum = -1;
            fixNum();
        } else {
            ToastUtil.showToastShort("该商品最小起订量为" + mMinNum);
        }
    }

    @Event(R.id.btn_child_plus)
    private void onPlusGoodsClick(View view) {
        mCurFixNum = 1;
        fixNum();
    }

    @Event(R.id.tv_child_goods_count)
    private void onInputGoodsClick(View view) {
        inputNum();
    }

    @Event(R.id.rb_child_check_all)
    private void onCheckClick(View view) {
        mCar.isChecked = rb_check_child.isChecked();
        //通知刷新
        mParent.updateSelectState();
    }

    @Event(R.id.iv_child_goods_icon)
    private void onGoodsClick(View view) {
        String id = mCar.getProductId();
        if (!TextUtils.isEmpty(id)) {
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
            intent.putExtra("id", id);
            mContext.startActivity(intent);
        }
    }

    private boolean mIsLoading = false;

    private int mCurFixNum = 0;//当前修改的数量的值

    private CarModel mCar;

    private String mStoreId, mProductId;

    private int mStock, mMinNum;

    @Override
    protected void initParams() {
        //设置中线
        tv_goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    public void onBindData(CarModel data) {
        if (mCar == data) {
            rb_check_child.setChecked(data.isChecked);
            return;
        }
        mCar = data;
        ProductModel product = data.getProduct();
        mStock = product.getStock();
        mMinNum = data.getOrderMinNum();
        mStoreId = product.getStoreId();
        mProductId = data.getProductId();
        //选中状态
        rb_check_child.setChecked(data.isChecked);
        tv_goods_count.setText(String.valueOf(data.mCurNum));
        tv_goods_title.setText(product.getProductName());
//        iv_goods_icon.LoadDrawable(product.getProductPic());
        LifeCycleImageView.LoadImageView(iv_goods_icon, product.getProductPic());
        tv_goods_old_price.setText(getResources().getString(R.string.price, product.getOriginalPrice()));
        tv_goods_price.setText(getResources().getString(R.string.price, data.getProductPrice()));
        if (!TextUtils.isEmpty(data.getPromoteRemark())) {
            tv_active_hint.setText(data.getPromoteRemark());
            tv_active_hint.setVisibility(VISIBLE);
        } else {
            tv_active_hint.setVisibility(GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_car_item_product;
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
        mCar.mCurNum += mCurFixNum;
        tv_goods_count.setText(String.valueOf(mCar.mCurNum));
        //勾选时通知总金额刷新
        if (mCar.isChecked) {
            EventHelper.postCommonEvent(CommonEvent.ET_RELOAD_CAR_MONEY);
            //通知刷新
            mParent.updateSelectState();
        }
    }

    @Override
    public void onSuccess(ResponseModel<Object> result) {
        if (result.isSuccess()) {
            updateToShow();
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

    private void inputNum() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("输入数量");
        View content = View.inflate(mContext, R.layout.dialog_input_num, null);

        final EditText input = (EditText) content.findViewById(R.id.ed_num);
        final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

        tv_num.setText(String.valueOf(mStock));
        String num = String.valueOf(mCar.mCurNum);
        input.setText(num);
        input.setSelection(num.length());

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    Integer valueOf = Integer.valueOf(num);
                    if (valueOf > mStock) {
                        ToastUtil.showToastLong("超出库存数量");
                    } else if (valueOf < mMinNum) {
                        ToastUtil.showToastLong("不能小于最小起定量");
                    } else {
                        //要修改的数量
                        mCurFixNum = valueOf - mCar.mCurNum;
                        fixNum();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }
}
