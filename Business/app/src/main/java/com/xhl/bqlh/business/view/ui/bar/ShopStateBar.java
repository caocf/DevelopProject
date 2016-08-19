package com.xhl.bqlh.business.view.ui.bar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.Member;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseBar;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.EventHelper;

import org.xutils.common.Callback;

/**
 * Created by Sum on 16/6/2.
 */
public class ShopStateBar extends BaseBar implements Callback.CommonCallback<ResponseModel<String>> {

    @Override
    protected boolean autoInject() {
        return false;
    }

    private TextView tv_state;
    private Member member;
    private boolean loading = false;

    public ShopStateBar(Context context) {
        super(context);
    }

    public ShopStateBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        tv_state = _findViewById(R.id.tv_shop_type);
        tv_state.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }

    private void addFriend() {
        String shopId = this.member.getRetailerId();
        if (!loading && !TextUtils.isEmpty(shopId)) {
            //新会员和拒绝状态可以发送添加
            if (member.getState() == 0 || member.getState() == 3) {
                loading = true;
                ApiControl.getApi().registerAddRetailer(shopId, this);
            }
        }
    }

    //加入会员
    private void shopNew() {
        tv_state.setText(R.string.shop_friend_add_new);
        tv_state.setBackgroundResource(R.drawable.code_shop_friend_1);
        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_while));
    }

    //已发邀请
    private void shopStart() {
        tv_state.setText(R.string.shop_friend_add_finish);
        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_orange));
        tv_state.setBackgroundResource(R.drawable.code_shop_friend_0);

    }

    //拒绝
    private void shopRefuse() {
        tv_state.setText(R.string.shop_friend_add_refuse);
        tv_state.setBackgroundResource(R.drawable.code_shop_friend_1);
        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_while));
    }

    //完成
    private void shopFinish() {
        tv_state.setText(R.string.shop_friend_add_finish);
        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_orange));
        tv_state.setBackgroundResource(R.drawable.code_shop_friend_0);
    }

    /**
     * 显示会员数据
     *
     * @param member 会员信息状态
     */
    public void showState(Member member) {
        if (member != null) {
            this.member = member;
            int state = member.getState();
            switch (state) {
                case 0:
                    shopNew();
                    break;
                case 1:
                    shopStart();
                    break;
                case 2:
                    shopFinish();
                    break;
                case 3:
                    shopRefuse();
                    break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_state;
    }


    @Override
    public void onSuccess(ResponseModel<String> result) {
        if (result.isSuccess()) {
            ToastUtil.showToastShort("添加成功");
            //刷新数据
            member.setState(1);
            shopStart();
            //保存到数据库
            DbTaskHelper.getInstance().saveOneMember(member);
            //保存到内存中
            ModelHelper.saveOneMember(member);
            //重新加载任务
            EventHelper.postCommonEvent(CommonEvent.EVENT_RELOAD_TASK);
        } else {
            ToastUtil.showToastLong(result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToastShort(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        loading = false;
    }
}
