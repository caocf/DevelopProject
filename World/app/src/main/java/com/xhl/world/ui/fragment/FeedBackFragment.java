package com.xhl.world.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.MDDialogHelper;
import com.xhl.world.ui.utils.MDTintHelper;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/18.
 */
@ContentView(R.layout.fragment_feed_back)
public class FeedBackFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_fb_goods)
    private TextView tv_fb_goods;

    @ViewInject(R.id.tv_fb_order)
    private TextView tv_fb_order;

    @ViewInject(R.id.tv_fb_wl)
    private TextView tv_fb_wl;

    @ViewInject(R.id.tv_fb_service)
    private TextView tv_fb_service;

    @ViewInject(R.id.tv_fb_other)
    private TextView tv_fb_other;


    @ViewInject(R.id.iv_fb_goods)
    private ImageView iv_fb_goods;

    @ViewInject(R.id.iv_fb_order)
    private ImageView iv_fb_order;

    @ViewInject(R.id.iv_fb_wl)
    private ImageView iv_fb_wl;

    @ViewInject(R.id.iv_fb_service)
    private ImageView iv_fb_service;

    @ViewInject(R.id.iv_fb_other)
    private ImageView iv_fb_other;

    @Event(value = {R.id.ll_fd_1, R.id.ll_fd_2, R.id.ll_fd_3, R.id.ll_fd_4, R.id.ll_fd_5})
    private void onTypeClick(View view) {

        switch (view.getId()) {

            case R.id.ll_fd_1:
                changePage(0);
                break;
            case R.id.ll_fd_2:
                changePage(1);
                break;
            case R.id.ll_fd_3:
                changePage(2);
                break;
            case R.id.ll_fd_4:
                changePage(3);
                break;
            case R.id.ll_fd_5:
                changePage(4);
                break;

        }
    }

    private void changePage(int type) {
        mType = type;
        switch (type) {
            case 0:
                iv_fb_goods.setEnabled(true);
                iv_fb_order.setEnabled(false);
                iv_fb_wl.setEnabled(false);
                iv_fb_service.setEnabled(false);
                iv_fb_other.setEnabled(false);
                //字体
                tv_fb_goods.setSelected(true);
                tv_fb_order.setSelected(false);
                tv_fb_wl.setSelected(false);
                tv_fb_service.setSelected(false);
                tv_fb_other.setSelected(false);

                break;
            case 1:
                iv_fb_goods.setEnabled(false);
                iv_fb_order.setEnabled(true);
                iv_fb_wl.setEnabled(false);
                iv_fb_service.setEnabled(false);
                iv_fb_other.setEnabled(false);
                //字体
                tv_fb_goods.setSelected(false);
                tv_fb_order.setSelected(true);
                tv_fb_wl.setSelected(false);
                tv_fb_service.setSelected(false);
                tv_fb_other.setSelected(false);

                break;
            case 2:
                iv_fb_goods.setEnabled(false);
                iv_fb_order.setEnabled(false);
                iv_fb_wl.setEnabled(true);
                iv_fb_service.setEnabled(false);
                iv_fb_other.setEnabled(false);
                //字体
                tv_fb_goods.setSelected(false);
                tv_fb_order.setSelected(false);
                tv_fb_wl.setSelected(true);
                tv_fb_service.setSelected(false);
                tv_fb_other.setSelected(false);

                break;
            case 3:
                iv_fb_goods.setEnabled(false);
                iv_fb_order.setEnabled(false);
                iv_fb_wl.setEnabled(false);
                iv_fb_service.setEnabled(true);
                iv_fb_other.setEnabled(false);
                //字体
                tv_fb_goods.setSelected(false);
                tv_fb_order.setSelected(false);
                tv_fb_wl.setSelected(false);
                tv_fb_service.setSelected(true);
                tv_fb_other.setSelected(false);

                break;
            case 4:
                iv_fb_goods.setEnabled(false);
                iv_fb_order.setEnabled(false);
                iv_fb_wl.setEnabled(false);
                iv_fb_service.setEnabled(false);
                iv_fb_other.setEnabled(true);
                //字体
                tv_fb_goods.setSelected(false);
                tv_fb_order.setSelected(false);
                tv_fb_wl.setSelected(false);
                tv_fb_service.setSelected(false);
                tv_fb_other.setSelected(true);

                break;
        }
    }


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().finish();
    }

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.ed_fb_content)
    private EditText ed_fb_content;

    @ViewInject(R.id.ed_fb_phone)
    private EditText ed_fb_phone;


    @Event(value = R.id.ripple_fb_confirm, type = RippleView.OnRippleCompleteListener.class)
    private void onConfirmClick(View view) {
        confirmFeedBack();
    }

    private int mType;

    @Override
    protected void initParams() {

        //主题色
        int color = MDDialogHelper.resolveColor(getContext(), R.attr.main_theme_accent_color);
        MDTintHelper.setTint(iv_fb_goods, color, R.drawable.icon_fb_goods);
        MDTintHelper.setTint(iv_fb_order, color, R.drawable.icon_fb_order);
        MDTintHelper.setTint(iv_fb_wl, color, R.drawable.icon_fb_wl);
        MDTintHelper.setTint(iv_fb_service, color, R.drawable.icon_fb_service);
        MDTintHelper.setTint(iv_fb_other, color, R.drawable.icon_fb_other);

        title_name.setText("意见反馈");
        changePage(4);
    }

    private void confirmFeedBack() {
        String phone = ed_fb_phone.getText().toString();
        String content = ed_fb_content.getText().toString().trim();
        if (content.length() < 6) {
            SnackMaker.shortShow(title_name,"亲，请您多提点意见吧");
            return;
        }

        if (!TextUtils.isEmpty(phone) && !LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name,R.string.phone_error);
            return;
        }

        showLoadingDialog();
        ApiControl.getApi().feedBack(content, phone, mType + "", new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    SnackMaker.shortShow(title_name,result.getResultObj());
                    ed_fb_content.setText("");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }
}
