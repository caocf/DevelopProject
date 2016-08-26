package com.xhl.world.ui.main.home_new.bar;

import android.content.Context;
import android.widget.ImageView;

import com.xhl.world.R;
import com.xhl.world.model.AdvHTest;
import com.xhl.world.ui.view.pub.BaseBar;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/8/25.
 */
public class HomeSeasonBar extends BaseBar {

    public HomeSeasonBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.iv1)
    private ImageView iv1;

    @ViewInject(R.id.iv2)
    private ImageView iv2;

    @ViewInject(R.id.iv3)
    private ImageView iv3;

    @ViewInject(R.id.iv4)
    private ImageView iv4;

    @ViewInject(R.id.iv5)
    private ImageView iv5;

    @ViewInject(R.id.iv6)
    private ImageView iv6;

    @Override
    protected void initParams() {

    }

    public void onBindData(Object adv) {
        //
        AdvHTest test = (AdvHTest) adv;
        iv1.setImageResource(test.getList().get(0).getResId());
        iv2.setImageResource(test.getList().get(1).getResId());
        iv3.setImageResource(test.getList().get(2).getResId());
        iv4.setImageResource(test.getList().get(3).getResId());
        iv5.setImageResource(test.getList().get(4).getResId());
        iv6.setImageResource(test.getList().get(5).getResId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_h_season_buy;
    }
}
