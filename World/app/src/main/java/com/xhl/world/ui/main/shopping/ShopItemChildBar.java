package com.xhl.world.ui.main.shopping;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ShoppingItemChildDetailsModel;
import com.xhl.world.model.Type.ShoppingGoodsType;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.ShoppingEvent;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.world.ui.view.LifeCycleImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/15.
 * 购物车单个商品操作类
 */
public class ShopItemChildBar extends BaseBar {

    @ViewInject(R.id.tv_child_goods_count)
    private TextView tv_goods_count;
    @ViewInject(R.id.tv_child_goods_title)
    private TextView tv_goods_title;
    @ViewInject(R.id.tv_child_goods_price)
    private TextView tv_goods_price;
    @ViewInject(R.id.tv_child_goods_old_price)
    private TextView tv_goods_old_price;

    @ViewInject(R.id.iv_child_goods_icon)
    private LifeCycleImageView iv_goods_icon;

    @ViewInject(R.id.rb_child_check_all)
    private CheckBox rb_check_child;

    @Event(R.id.btn_child_reduce)
    private void onReduceGoodsClick(View view) {
        reduceOneGoodFromCar();
    }

    @Event(R.id.btn_child_plus)
    private void onPlusGoodsClick(View view) {
        addOneGoodToCar();
    }

    private int mGoodsCount;
    private int mMaxCount = -1;

    public ShoppingItemChildDetailsModel mChildGoods;
    private int mAdapterPosition = -1;

    public ShopItemChildBar(Context context) {
        super(context);
    }

    public ShopItemChildBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        //设置中线
        tv_goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        rb_check_child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_check_child.isChecked()) {
                    addAllGoodsToCar();
                } else if (!rb_check_child.isChecked()) {
                    clearAllGoodsFromCar();
                }
            }
        });
        iv_goods_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.postProductDetails(mChildGoods.getGoods_id());
            }
        });
    }

    private void addAllGoodsToCar() {
        checkChildGoodsState(true);
        posEvent(EventType.Shopping_Event_item_child_select_all);
    }

    private void clearAllGoodsFromCar() {
        checkChildGoodsState(false);
        posEvent(EventType.Shopping_Event_item_child_un_select_all);
    }

    private void posEvent(int action) {
        ShoppingEvent event = new ShoppingEvent();
        event.action = action;
        event.adapter_postion = mAdapterPosition;
        event.obj = mChildGoods;
        EventBus.getDefault().post(event);
    }

    private void addOneGoodToCar() {
        //最大数目
        if (mMaxCount != -1 && mGoodsCount == mMaxCount) {
            return;
        }
        rb_check_child.setChecked(true);
        mGoodsCount++;
        //更新显示
        updateGoodsCount(mGoodsCount);
        //修改数量
        mChildGoods.setGoods_count(mGoodsCount);
        //选中
        checkChildGoodsState(true);
        //通知修改完成
        posEvent(EventType.Shopping_Event_item_child_add_goods);
    }

    private void reduceOneGoodFromCar() {
        if (mGoodsCount == 1) {
            return;
        }
        rb_check_child.setChecked(true);
        mGoodsCount--;
        updateGoodsCount(mGoodsCount);
        mChildGoods.setGoods_count(mGoodsCount);
        checkChildGoodsState(true);
        posEvent(EventType.Shopping_Event_item_child_reduce_goods);
    }

    public void updateGoodsCount(int count) {
        mGoodsCount = count;
        tv_goods_count.setText(String.valueOf(count));
    }

    public boolean isChecked() {
        return rb_check_child.isChecked();
    }

    public void checkChildSelectState(boolean checked) {
        rb_check_child.setChecked(checked);
    }

    public void checkChildGoodsState(boolean putInCar) {
        if (putInCar) {
            mChildGoods.setGoods_in_car(ShoppingGoodsType.STATE_IN_CAR);
        } else {
            mChildGoods.setGoods_in_car(ShoppingGoodsType.STATE_UN_IN_CAR);
        }
    }

    public void setChildShowData(ShoppingItemChildDetailsModel childShowData) {
        if (childShowData == null) {
            return;
        }
        mChildGoods = childShowData;
        //商品状态
        if (mChildGoods.goodsInCar()) {
            rb_check_child.setChecked(true);
        } else {
            rb_check_child.setChecked(false);
        }
        //商品数目
        mGoodsCount = childShowData.getGoods_count();
        tv_goods_count.setText(String.valueOf(mGoodsCount));
        //标题
        tv_goods_title.setText(childShowData.getGoods_title());
        //原价
        String goods_old_price = childShowData.getGoods_old_price();
        if (TextUtils.isEmpty(goods_old_price)) {
            tv_goods_old_price.setVisibility(INVISIBLE);
        } else
            tv_goods_old_price.setText(getResources().getString(R.string.price, goods_old_price));
        //现价
        String goods_price = childShowData.getGoods_price();
        if (TextUtils.isEmpty(goods_price)) {
            goods_price = "0";
        }
        tv_goods_price.setText(getResources().getString(R.string.price, goods_price));

        iv_goods_icon.bindImageUrl(childShowData.getGoods_icon());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_child_item;
    }

    public void setAdapterPosition(int position) {
        this.mAdapterPosition = position;
    }
}
