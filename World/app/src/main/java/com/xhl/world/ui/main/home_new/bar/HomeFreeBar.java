package com.xhl.world.ui.main.home_new.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xhl.world.R;
import com.xhl.world.model.AdvHTest;
import com.xhl.world.ui.view.pub.BaseBar;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/11/26.
 */
public class HomeFreeBar extends BaseBar {

    @ViewInject(R.id.iv_1)
    private ImageView iv_1;

    @ViewInject(R.id.iv_2)
    private ImageView iv_2;

    public HomeFreeBar(Context context) {
        super(context);
    }

    public HomeFreeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    public void onBindData(Object adv) {
        //
        AdvHTest test = (AdvHTest) adv;
        iv_1.setImageResource(test.getList().get(0).getResId());
        iv_2.setImageResource(test.getList().get(1).getResId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_h_free;
    }
}
