package com.xhl.xhl_library.Base.Sum;

/**
 * Created by Administrator on 2015/11/15.
 */
public interface ISumFragment {

    void onEnter(Object data);

    void onLeave();

    void onBack();

    void onBackWithData(Object data);

    boolean processBackPressed();
}
