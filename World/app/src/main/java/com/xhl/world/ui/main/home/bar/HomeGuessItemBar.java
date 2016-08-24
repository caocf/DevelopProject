package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ProductModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 15/12/1.
 */
public class HomeGuessItemBar extends BaseHomeBar implements View.OnClickListener {

    @ViewInject(R.id.iv_guess_you_like)
    private LifeCycleImageView mIvImage1;

    @ViewInject(R.id.tv_item_name)
    private TextView mTvName1;

    @ViewInject(R.id.tv_item_price)
    private TextView mTvPrice1;

    @ViewInject(R.id.iv_guess_you_like2)
    private LifeCycleImageView mIvImage2;

    @ViewInject(R.id.tv_item_name2)
    private TextView mTvName2;

    @ViewInject(R.id.tv_item_price2)
    private TextView mTvPrice2;

    private ProductModel mProduct1;
    private ProductModel mProduct2;

    public HomeGuessItemBar(Context context) {

        super(context);
    }

    public HomeGuessItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    public void onBindGuessLike(List<ProductModel> data) {
        //左边
        mProduct1 = data.get(0);
        mIvImage1.bindImageUrl(mProduct1.getProductPicUrl());
        mIvImage1.setOnClickListener(this);
        setTextViewText(mTvName1, mProduct1.getProductName());
        setTextViewText(mTvPrice1, getResources().getString(R.string.price, mProduct1.getOriginalPrice()));

        //右边
        mProduct2 = data.get(1);
        mIvImage2.bindImageUrl(mProduct2.getProductPicUrl());
        mIvImage2.setOnClickListener(this);
        setTextViewText(mTvName2, mProduct2.getProductName());
        setTextViewText(mTvPrice2, getResources().getString(R.string.price, mProduct2.getOriginalPrice()));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guess_you_like;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_guess_you_like:
                EventBusHelper.postProductDetails(mProduct1.getProductId());
                break;
            case R.id.iv_guess_you_like2:
                EventBusHelper.postProductDetails(mProduct2.getProductId());
                break;
        }
    }
}
