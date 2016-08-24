package com.xhl.world.mvp.views;

/**
 * Created by Sum on 15/12/12.
 */
public interface BaseView extends MvpView {

    void showLoadingView();

    void hideLoadingView();

    void showReLoadView();

    boolean isFirstLoading();

}
