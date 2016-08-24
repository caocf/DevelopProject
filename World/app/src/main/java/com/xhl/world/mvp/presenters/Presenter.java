package com.xhl.world.mvp.presenters;

/**
 * Created by Sum on 15/12/12.
 */
public abstract class Presenter {

    /**
     * 创建代理
     */
    public abstract void onStart();

    /**
     * 代理UI交互
     */
    public void onResume() {}

    /**
     * 代理UI停止
     */
    public abstract void onStop();

}
