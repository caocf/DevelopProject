package com.xhl.world.ui.main.my;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.BrowseModel;
import com.xhl.world.ui.activity.BaseContainerActivity;
import com.xhl.world.ui.activity.UserInfoActivity;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.RoundedImageView;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/12/3.
 */
public class myTopLoginBar extends BaseBar {

    @ViewInject(R.id.iv_my_pic)
    private RoundedImageView mImage;

    @ViewInject(R.id.tv_care_goods_count)
    private TextView tv_care_goods_count;

    @ViewInject(R.id.tv_care_store_count)
    private TextView tv_care_store_count;

    @ViewInject(R.id.tv_care_history_count)
    private TextView tv_care_history_count;

    private boolean hasLoadBrowse = false;

    @Event(R.id.iv_my_pic)
    private void infoClick(View view) {
        if (AppApplication.appContext.isLogin(getContext())) {
            startActivity(UserInfoActivity.class);
            return;
        }
    }

    @Event(value = {R.id.ripple_care_goods, R.id.ripple_care_store, R.id.ripple_care_history}, type = RippleView.OnRippleCompleteListener.class)
    private void myCareClick(View view) {
        if (!AppApplication.appContext.isLogin(getContext())) {
            return;
        }
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.ripple_care_goods:
                bundle = new Bundle();
                bundle.putInt("tag", BaseContainerActivity.Tag_care_good);
                startActivity(BaseContainerActivity.class, bundle);
                break;
            case R.id.ripple_care_store:
                bundle = new Bundle();
                bundle.putInt("tag", BaseContainerActivity.Tag_care_shop);
                startActivity(BaseContainerActivity.class, bundle);
                break;

            case R.id.ripple_care_history:
                bundle = new Bundle();
                bundle.putInt("tag", BaseContainerActivity.Tag_history_record);
                startActivity(BaseContainerActivity.class, bundle);
                break;

        }
    }

    private boolean faceLoadSuccess = false;

    public myTopLoginBar(Context context) {
        super(context);
    }

    public myTopLoginBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        showUserInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_my_top;
    }

    public void reloadUserInfo() {
        faceLoadSuccess = false;
        showUserInfo();
    }

    public void showUserInfo() {
        if (faceLoadSuccess) {
            return;
        }
        if (AppApplication.appContext.isLogin()) {
            mImage.setImageResource(R.drawable.icon_user_no_head);
           /* if (!hasLoadBrowse) {
            }*/
            showCollectionNum();
        } else {
            mImage.setImageResource(R.drawable.icon_my_login);
            tv_care_store_count.setVisibility(GONE);
            tv_care_history_count.setVisibility(GONE);
            tv_care_goods_count.setVisibility(GONE);
            return;
        }
        String image = AppApplication.appContext.getUserFaceImage();
        if (TextUtils.isEmpty(image)) {
            return;
        }

        mImage.LoadDrawable(image);
        mImage.setListener(new LifeCycleImageView.ImageLoadListener() {
            @Override
            public void onSuccess() {
                faceLoadSuccess = true;
            }

            @Override
            public void onFailed() {

            }
        });
    }

    private void showCollectionNum() {

        ApiControl.getApi().browseAll(new Callback.CommonCallback<ResponseModel<Response<BrowseModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<BrowseModel>> result) {
                if (result.isSuccess()) {
                    BrowseModel myCollect = result.getResultObj().getMyCollect();
                    if (!TextUtils.isEmpty(myCollect.getProductCount())) {
                        tv_care_goods_count.setVisibility(VISIBLE);
                        tv_care_goods_count.setText(myCollect.getProductCount());
                    }
                    if (!TextUtils.isEmpty(myCollect.getShopCount())) {
                        tv_care_store_count.setVisibility(VISIBLE);
                        tv_care_store_count.setText(myCollect.getShopCount());
                    }
                    BrowseModel myFoot = result.getResultObj().getMyFoot();
                    if (!TextUtils.isEmpty(myFoot.getBrowseCount())) {
                        tv_care_history_count.setVisibility(VISIBLE);
                        tv_care_history_count.setText(myFoot.getBrowseCount());
                    }
                    hasLoadBrowse = true;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

}
