package com.xhl.world.mvp.domain;

import com.xhl.world.model.Entities.ShoppingOrderEntities;
import com.xhl.world.mvp.presenters.Presenter;

import java.util.ArrayList;

/**
 * Created by Sum on 15/12/15.
 */
public interface ShoppingUserCase extends UserCase {

    void selectAllChildGoods(int position, Object obj);

    void unSelectAllChildGoods(int position, Object obj);

    void selectChildGoods(int position, Object obj);

    void unSelectChildGoods(int position, Object obj);

    void addGoods(int position, Object obj);

    void reduceGoods(int position, Object obj);

    void setPresenter(Presenter presenter);

    void deleteSelectGoods();

    void moveToCollection();

    void refreshTop();

    void refreshBottom();

    ArrayList<ShoppingOrderEntities> getAccountOrders();
}
