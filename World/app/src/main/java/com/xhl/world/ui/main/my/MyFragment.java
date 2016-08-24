package com.xhl.world.ui.main.my;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.config.Constant;
import com.xhl.world.ui.activity.BaseContainerActivity;
import com.xhl.world.ui.activity.OrderManagerActivity;
import com.xhl.world.ui.activity.OrderReturnManagerActivity;
import com.xhl.world.ui.activity.SettingActivity;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.GlobalEvent;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.view.LazyScrollView;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.DisplayUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/11/25.
 */
@ContentView(R.layout.fragment_my)
public class MyFragment extends BaseAppFragment {

    private static final int MAX_ALPHA = 255;
    private int move_distance;
    private Drawable mTopBgAlpha;
    @ViewInject(R.id.bar_my_top_login)
    private myTopLoginBar mLoginBar;

    @ViewInject(R.id.ll_my_top)
    private LinearLayout mMyTopBar;

    @ViewInject(R.id.lazy_scrollview)
    private LazyScrollView mScrollView;

    @ViewInject(R.id.my_top_line)
    private View line;

    @Event(value = {R.id.ripple_all_my_order, R.id.ripple_bar_my_msg, R.id.rl_order_wait_for_sender, R.id.rl_order_wait_for_pay, R.id.rl_order_wait_for_take, R.id.rl_order_wait_for_judge, R.id.rl_order_return_goods}, type = RippleView.OnRippleCompleteListener.class)
    private void onOrderClick(View view) {
        if (!AppApplication.appContext.isLogin(getContext())) {
            return;
        }
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ripple_bar_my_msg:
//                getActivity().startActivity(new Intent(mContext, MessageActivity.class));
                break;

            case R.id.ripple_all_my_order:
                intent = new Intent(mContext, OrderManagerActivity.class);
                intent.putExtra(OrderManagerActivity.Tag_key, OrderManagerActivity.Tag_key_all);
                getActivity().startActivity(intent);
                break;

            case R.id.rl_order_wait_for_pay:
                intent = new Intent(mContext, OrderManagerActivity.class);
                intent.putExtra(OrderManagerActivity.Tag_key, OrderManagerActivity.Tag_key_wait_for_pay);
                getActivity().startActivity(intent);
                break;

            case R.id.rl_order_wait_for_sender:
                intent = new Intent(mContext, OrderManagerActivity.class);
                intent.putExtra(OrderManagerActivity.Tag_key, OrderManagerActivity.Tag_key_wait_for_sender);
                getActivity().startActivity(intent);
                break;


            case R.id.rl_order_wait_for_take:
                intent = new Intent(mContext, OrderManagerActivity.class);
                intent.putExtra(OrderManagerActivity.Tag_key, OrderManagerActivity.Tag_key_wait_for_take);
                getActivity().startActivity(intent);
                break;

            case R.id.rl_order_wait_for_judge:
                intent = new Intent(mContext, OrderManagerActivity.class);
                intent.putExtra(OrderManagerActivity.Tag_key, OrderManagerActivity.Tag_key_wait_for_judge);
                getActivity().startActivity(intent);
                break;

            case R.id.rl_order_return_goods:
                getActivity().startActivity(new Intent(mContext, OrderReturnManagerActivity.class));
                break;

        }
    }


    @Event(value = {R.id.ripple_my_service, R.id.ripple_my_func_qd,R.id.ripple_my_func_jf, R.id.ripple_my_func_sc}, type = RippleView.OnRippleCompleteListener.class)
    private void onByLoginFuncClick(View view) {
        if (!AppApplication.appContext.isLogin(getContext())) {
            return;
        }
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ripple_my_func_qd:
                intent = new Intent(mContext, BaseContainerActivity.class);
                intent.putExtra(BaseContainerActivity.Tag_fragment, BaseContainerActivity.Tag_coupon_new);
                getActivity().startActivity(intent);
                break;

            case R.id.ripple_my_func_jf://我的积分
                intent = new Intent(mContext, BaseContainerActivity.class);
                intent.putExtra(BaseContainerActivity.Tag_fragment, BaseContainerActivity.Tag_my_score);
                getActivity().startActivity(intent);
                break;

            case R.id.ripple_my_func_sc://我的收藏
                intent = new Intent(mContext, BaseContainerActivity.class);
                intent.putExtra(BaseContainerActivity.Tag_fragment, BaseContainerActivity.Tag_care_good);
                getActivity().startActivity(intent);
                break;
            case R.id.ripple_my_service://空港客服
                SnackMaker.shortShow(view,R.string.fun_building);
                break;
        }

    }

    @Event(value = { R.id.ripple_bar_my_setting, R.id.ripple_my_feedback, R.id.ripple_my_about}, type = RippleView.OnRippleCompleteListener.class)
    private void onByNoLoginClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ripple_bar_my_setting:
                getActivity().startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.ripple_my_feedback:
                intent = new Intent(mContext, BaseContainerActivity.class);
                intent.putExtra(BaseContainerActivity.Tag_fragment, BaseContainerActivity.Tag_feed_back);
                getActivity().startActivity(intent);
                break;
            case R.id.ripple_my_about:
                intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_about);
                intent.putExtra(WebPageActivity.TAG_TITLE, getString(R.string.my_about));
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    protected void initParams() {
        mTopBgAlpha = ContextCompat.getDrawable(getContext(), R.drawable.while_bg);

        move_distance = DisplayUtil.px2dip(getContext(), 220);

        mTopBgAlpha.setAlpha(0);

      /*  mScrollView.setmListener(new LazyScrollView.ScrollListener() {
            @Override
            public void onScrollBottom() {
            }

            @Override
            public void onScrollY(int nowY, int oldY) {
                // updateBgAlpha(nowY);
            }
        });*/

    }

    private void updateBgAlpha(int distance) {
        int alpha = getAlpha(distance, move_distance);
        if (alpha >= 200) {
            ViewUtils.changeViewVisible(line, true);
        } else {
            ViewUtils.changeViewVisible(line, false);
        }
        mTopBgAlpha.setAlpha(alpha > 255 ? 255 : alpha);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mMyTopBar.setBackground(mTopBgAlpha);
        }
    }

    private int getAlpha(float distance, float max) {

        return (int) (distance * 1.0f / max * MAX_ALPHA);
    }

    public void onEvent(GlobalEvent event) {
        int type = event.getEventType();
        //重新加载用户数据
        if (type == EventType.Event_Reload_Info || type == EventType.Event_Exit_Login) {
            mLoginBar.reloadUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLoginBar.reloadUserInfo();
    }
}
