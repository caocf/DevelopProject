package com.xhl.world.mvp.views;

import com.xhl.world.model.serviceOrder.Order;

import java.util.List;

/**
 * Created by Sum on 16/1/5.
 */
public interface OrderManagerView extends BaseView {

    void showNullView();

    void hideNullView();

    int viewTag();//表示当前显示的界面

    void showTint(String msg);//错误信息展示

    void setLoadData(List<Order> data);//加载数据

    void addLoadData(List<Order> data);//加载更多数据

    boolean needLoadData();//表示当前界面是否需要加载数据

}
