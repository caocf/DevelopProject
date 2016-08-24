package com.xhl.world.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.config.Constant;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.SmsModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 15/12/7.
 */
@ContentView(R.layout.fragment_register_next)
public class RegisterNextFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel> {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;

    @ViewInject(R.id.ll_tip)
    private LinearLayout ll_tip;

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;

    @ViewInject(R.id.ed_input_pwd_again)
    private EditText ed_input_pwd_again;


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(R.id.tv_service)
    private void onServiceClick(View view) {

        Intent intent = new Intent(mContext, WebPageActivity.class);
        intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_protocol);
        intent.putExtra(WebPageActivity.TAG_TITLE, mContext.getString(R.string.register_protocol));
        getContext().startActivity(intent);

    }


    @Event(value = R.id.ripple_submit, type = RippleView.OnRippleCompleteListener.class)
    private void onSubmitClick(View view) {
        submit();
    }

    private String mPhone;
    private String mCode;
    private int mType;

    @Override
    protected void initParams() {
        if (mType == RegisterFragment.Type_register) {
            title_name.setText("手机注册");
        } else if (mType == RegisterFragment.Type_forget) {
            title_name.setText("忘记密码");
        } else {
            title_name.setText("修改密码");
        }
        //非注册操作
        if (mType != RegisterFragment.Type_register) {
            tv_submit.setText("提交");
            ll_tip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            HashMap<String, String> map = (HashMap<String, String>) data;
            mPhone = map.get("phone");
            mCode = map.get("code");
            mType = Integer.parseInt(map.get("type"));
        }
    }

    private void submit() {

        ViewUtils.hideKeyBoard(ed_input_pwd_again);

        String pwd1 = ed_input_pwd.getText().toString().trim();
        String pwd2 = ed_input_pwd_again.getText().toString().trim();

        if (TextUtils.isEmpty(pwd1)) {
            SnackMaker.shortShow(title_name,R.string.empty_pwd_hint);
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            SnackMaker.shortShow(title_name,R.string.empty_pwd_hint);
            return;
        }

        if (!pwd1.equals(pwd2)) {
            SnackMaker.shortShow(title_name,"两次密码输入不一致");
            return;
        }
        showLoadingDialog();
        if (mType == RegisterFragment.Type_register) {
            ApiControl.getApi().userRegister(mPhone, pwd1, mCode, this);
        } else {
            ApiControl.getApi().changePwd(mCode, mPhone, pwd1, new CommonCallback<ResponseModel<SmsModel>>() {
                @Override
                public void onSuccess(ResponseModel<SmsModel> result) {
                    if (result.isSuccess()) {
                        SmsModel resultObj = result.getResultObj();
                        if (resultObj.result == 200) {
                            getActivity().finish();
                            EventBusHelper.postReLogin();
                        } else {
                            SnackMaker.shortShow(title_name,"code:" + resultObj.result);
                        }
                    } else {
                        SnackMaker.shortShow(title_name,result.getMessage());
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


    @Override
    public void onSuccess(ResponseModel result) {
        if (result.isSuccess()) {

            getActivity().finish();

            EventBusHelper.postReLogin();
        } else {
            SnackMaker.shortShow(title_name,result.getMessage());
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
}
