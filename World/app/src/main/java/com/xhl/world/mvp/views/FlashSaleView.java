package com.xhl.world.mvp.views;

import java.util.List;

/**
 * Created by Sum on 15/12/12.
 */
public interface FlashSaleView<T> extends BaseView {

    void setLoadData(List<T> data);

    void appendLoadData(List<T> data);

    void onLoadFinish(String msg);

}
