package com.xhl.world.ui.main.home_new.bar;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.AdvHTest;
import com.xhl.world.ui.view.pub.BaseBar;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/8/25.
 */
public class ProductABar extends BaseBar {

    public ProductABar(Context context) {
        super(context);
    }

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.iv1)
    private ImageView iv1;

    @ViewInject(R.id.iv2)
    private ImageView iv2;

    @ViewInject(R.id.iv3)
    private ImageView iv3;

    @Override
    protected void initParams() {

    }
    public void onBindData(Object adv){
        //
        AdvHTest test = (AdvHTest) adv;
        tv_title.setText(test.getName());
        iv1.setImageResource(test.getList().get(0).getResId());
        iv2.setImageResource(test.getList().get(1).getResId());
        iv3.setImageResource(test.getList().get(2).getResId());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.bar_h_product_a;
    }
}
