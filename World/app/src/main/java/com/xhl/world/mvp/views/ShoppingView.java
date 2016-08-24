package com.xhl.world.mvp.views;

import java.util.List;

/**
 * Created by Sum on 15/12/15.
 */
public interface ShoppingView<T> extends BaseView {

    void setLoadData(List<T> data);//全部购物订单数据

    void addLoadData(List<T> data);//添加购物订单数据

    void showGoodsNullView();//空订单界面

    void hideGoodsNullView();

    void changeCountPanel(boolean idShow);

    void showEditView();//订单界面

    void hideEditView();

    boolean isEditModel();

    void checkItemBoxState(int itemPosition, boolean checkState);//更新每一项头部选中状态

    void msgHint(String error);

    void updateTotalFree(String free, String goodsCount);//更新全部金额和商品数目

    void updateItemFree(int itemPosition, String free, String postal, String revenue);//更新小计金额

    void updateTotalBoxState(boolean checkState);//更新全部选中按钮状态
}
