package com.xhl.world.ui.main.shopping;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ShoppingItemChildDetailsModel;
import com.xhl.world.model.ShoppingItemDetailsModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.ShopEvent;
import com.xhl.world.ui.event.ShoppingEvent;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.pub.BaseBar;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/14.
 * 购物车每一项
 */
public class ShopItemBar extends BaseBar {
    @ViewInject(R.id.rb_parent_check_all)
    private CheckBox rb_check_all;

    @ViewInject(R.id.iv_shop_icon)
    private LifeCycleImageView iv_shop_icon;

    @ViewInject(R.id.tv_shop_title)
    private TextView tv_shop_title;

    @ViewInject(R.id.ripple_view_shop)
    private RelativeLayout ripple_view_shop;

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;

    @ViewInject(R.id.tv_goods_count)
    private TextView tv_goods_count;

    @ViewInject(R.id.tv_goods_postal)
    private TextView tv_goods_postal;

    @ViewInject(R.id.tv_goods_revenue)
    private TextView tv_goods_revenue;

    private SparseArray<ShopItemChildBar> mChildesBar;
    private ShoppingItemDetailsModel mShopDetails;
    private int mAdapterPosition = -1;//适配器中的位置

    public ShopItemBar(Context context) {
        super(context);
    }

    public ShopItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        mChildesBar = new SparseArray<>();
        rb_check_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_check_all.isChecked()) {
                    checkAllState();
                    countSelectAllChild();
                } else if (!rb_check_all.isChecked()) {
                    clearAllState();
                    countUnSelectAllChild();
                }
            }
        });

        ripple_view_shop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shopClick();
            }
        });
    }

    //跳转店铺
    private void shopClick() {
        if (TextUtils.isEmpty(mShopDetails.getShop_id())) {
            return;
        }
        ShopEvent event = new ShopEvent();
        event.shopId = mShopDetails.getShop_id();
        event.shopUrl = mShopDetails.getShop_url();
        event.shopIcon = mShopDetails.getShop_icon();
        event.shopTitle = mShopDetails.getShop_title();

        EventBusHelper.post(event);
    }

    private void posEvent(int action) {
        ShoppingEvent event = new ShoppingEvent();
        event.action = action;
        event.adapter_postion = mAdapterPosition;
        event.obj = mShopDetails;
        EventBus.getDefault().post(event);
    }

    /**
     * 更新选中的数据
     */
    public void countSelectAllChild() {
        for (int i = 0; i < mChildesBar.size(); i++) {
            ShopItemChildBar childBar = mChildesBar.get(i);
            childBar.checkChildGoodsState(true);
        }
        posEvent(EventType.Shopping_Event_item_select_all);
    }

    /**
     * 取消选中的数据
     */
    public void countUnSelectAllChild() {
        for (int i = 0; i < mChildesBar.size(); i++) {
            ShopItemChildBar childBar = mChildesBar.get(i);
            childBar.checkChildGoodsState(false);
        }
        posEvent(EventType.Shopping_Event_item_un_select_all);
    }

    /**
     * 只做更新View的全选显示
     */
    public void checkAllState() {
        for (int i = 0; i < mChildesBar.size(); i++) {
            ShopItemChildBar childBar = mChildesBar.get(i);
            childBar.checkChildSelectState(true);
        }
    }

    /**
     * 只做更新View的取消全选显示
     */
    public void clearAllState() {
        for (int i = 0; i < mChildesBar.size(); i++) {
            ShopItemChildBar childBar = mChildesBar.get(i);
            childBar.checkChildSelectState(false);
        }
    }

    /**
     * 更新父View全选状态显示
     *
     * @param checked
     */
    public void updateCheckState(boolean checked) {
        rb_check_all.setChecked(checked);
    }

    public void setShoppingDetails(ShoppingItemDetailsModel details) {

        if (mShopDetails == details && mChildesBar.size() != 0) {
            return;
        }
        mShopDetails = details;
        //添加一个店铺里面的全部商品
        if (details.getShop_details() != null) {
            addChildesView(details.getShop_details());
        }

        //商店名称
        tv_shop_title.setText(details.getShop_title());
        //商店图标
        iv_shop_icon.bindImageUrl(details.getShop_icon());
        //更新选中状态
        if (mShopDetails.goodsAllInCar()) {
            checkAllState();
            rb_check_all.setChecked(true);
        } else {
            rb_check_all.setChecked(false);
        }
        String goods_free = mShopDetails.getShop_total_free();
        String goods_postal = mShopDetails.getShop_total_postal();
        String goods_revenue = mShopDetails.getShop_total_revenue();
        //更新费用
        updateFree(goods_free, goods_postal, goods_revenue);
    }

    private void addChildesView(List<ShoppingItemChildDetailsModel> childDetails) {
        ll_content.removeAllViews();
        mChildesBar.clear();

        //添加子类数据
        for (int i = 0; i < childDetails.size(); i++) {
            ShopItemChildBar childBar = new ShopItemChildBar(mContext);
            ll_content.addView(childBar);
            mChildesBar.append(i, childBar);
            childBar.setChildShowData(childDetails.get(i));
            childBar.setAdapterPosition(mAdapterPosition);
        }
    }

    //每个店铺的相关费用需要根据用户选中的商品动态计算
    public void updateFree(String free, String postal, String revenue) {
        String goods_free = "0";//小计物品费用
        if (!TextUtils.isEmpty(free)) {
            goods_free = free;
        }
//        String goods_postal = "免邮";//邮费
     /*   if (!TextUtils.isEmpty(postal)) {
            goods_postal = postal;
        }*/
        String goods_revenue = "0";//关税
        if (!TextUtils.isEmpty(revenue)) {
            goods_revenue = revenue;
        }
        tv_goods_count.setText(getResources().getString(R.string.shop_goods_count, goods_free));
        tv_goods_postal.setText("运费：免邮");
//        tv_goods_postal.setText(getResources().getString(R.string.shop_goods_postal, goods_postal));
//        tv_goods_revenue.setText(getResources().getString(R.string.shop_goods_revenue, goods_revenue));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_item;
    }

    public void setAdapterPosition(int position) {
        this.mAdapterPosition = position;
    }
}
