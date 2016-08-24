package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/27.
 */
public class HomeSpecialSellBar extends BaseHomeBar {

    private TextView bar_home_special_sell_title;

    private LinearLayout bar_home_special_sell_content;

    private List<AdvModel> mBrandData;

    private List<LifeCycleImageView> mContentChild = new ArrayList<>();

    private boolean mIsFirstView = false;

    public HomeSpecialSellBar(Context context) {
        super(context);
    }

    public HomeSpecialSellBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        bar_home_special_sell_title = _findViewById(R.id.bar_home_special_sell_title);
        bar_home_special_sell_content = _findViewById(R.id.bar_home_special_sell_content);
    }

    public void setSpecialSellData(List<AdvModel> data) {

        mBrandData = data;

        //清理并创建新的子view　
        bar_home_special_sell_content.removeAllViews();
        mContentChild.clear();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                mIsFirstView = true;
            } else {
                mIsFirstView = false;
            }
            View itemView = createItemView(data.get(i));
            bar_home_special_sell_content.addView(itemView);
        }

        setShowData(mBrandData);
    }

    public void setAdName(String name){

        if (TextUtils.isEmpty(name)) {
            return;
        }

        bar_home_special_sell_title.setText(name);

    }

    public void updateShowData(List<AdvModel> data) {
        setShowData(data);
    }

    private void setShowData(List<AdvModel> data) {
        for (int i = 0; i < data.size(); i++) {
            String url = data.get(i).getImageUrl();
            LifeCycleImageView imageView = getChildImageView(i);
            bindImage(imageView, url);
        }
    }

    private LifeCycleImageView getChildImageView(int index) {
        LifeCycleImageView view = null;
        if (index < mContentChild.size()) {
            view = mContentChild.get(index);
        }
        return view;
    }

    //创建一个特卖view
    private View createItemView(final AdvModel data) {

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_special_sell_imageview, null);
//
//        LifeCycleImageView imageView = (LifeCycleImageView) view.findViewById(R.id.iv_special_sell);
        LifeCycleImageView imageView = new LifeCycleImageView(getContext());

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.postAdv(data);
            }
        });
        int mar = 0;

        if (!mIsFirstView) {
            mar = mContext.getResources().getDimensionPixelOffset(R.dimen.px_dimen_24);
        }
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.special_sale_image_view_height);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, height);

        params.topMargin = mar;

        imageView.setLayoutParams(params);

        AutoUtils.auto(imageView);

        mContentChild.add(imageView);

        return imageView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_l_special_sell;
    }


}
