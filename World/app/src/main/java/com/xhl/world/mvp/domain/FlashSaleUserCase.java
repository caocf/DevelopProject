package com.xhl.world.mvp.domain;

import com.xhl.world.mvp.domain.back.ActionCallBack;

/**
 * Created by Sum on 15/12/12.
 */
public interface FlashSaleUserCase<T> extends UserCase{


    void refreshTop();

    void refreshBottom();

    void callBack(ActionCallBack<T> callBack);
}
