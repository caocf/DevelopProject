package com.xhl.world.ui.view.pub.callback;

/**
 * Created by Sum on 15/12/12.
 */
public interface RecyclerViewScrollStateListener extends RecyclerViewScrollBottomListener{

    //向上滚动动作
    void onScrollingUp();

    //向下滚动动作
    void onScrollingDown();
}
