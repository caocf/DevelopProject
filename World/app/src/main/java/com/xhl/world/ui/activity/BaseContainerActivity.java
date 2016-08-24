package com.xhl.world.ui.activity;

import android.os.Bundle;

import com.xhl.world.R;
import com.xhl.world.ui.fragment.CouponFragment;
import com.xhl.world.ui.fragment.FeedBackFragment;
import com.xhl.world.ui.fragment.MyCareFragment;
import com.xhl.world.ui.fragment.MyHistoryRecordFragment;
import com.xhl.world.ui.fragment.ScoreFragment;

/**
 * Created by Sum on 16/1/3.
 */
public class BaseContainerActivity extends BaseAppActivity {

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {
    }

    public static final String Tag_fragment = "tag";

    public static final int Tag_care_good = 0;//关注商品
    public static final int Tag_care_shop = 1;//关注店铺
    public static final int Tag_history_record = 6;//浏览记录
    public static final int Tag_my_score = 7;//我的积分
    public static final int Tag_feed_back = 8;//意见反馈
    public static final int Tag_coupon_new = 9;//优惠券

    private int mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_op);
        Bundle extras = getIntent().getExtras();
        mTag = extras.getInt(Tag_fragment, 0);

        switch (mTag) {
            case Tag_coupon_new:
                pushFragmentToBackStack(CouponFragment.class, null);
                break;
            case Tag_history_record:
                pushFragmentToBackStack(MyHistoryRecordFragment.class, null);
                break;
            case Tag_my_score:
                pushFragmentToBackStack(ScoreFragment.class, null);
                break;
            case Tag_feed_back:
                pushFragmentToBackStack(FeedBackFragment.class, null);
                break;
            case Tag_care_good:
            case Tag_care_shop:
                pushFragmentToBackStack(MyCareFragment.class, mTag);
                break;
        }
    }


    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }
}
