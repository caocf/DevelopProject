package com.xhl.bqlh.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.ViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/8/9.
 */
@ContentView(R.layout.fragment_user_forget_pwd_edit)
public class UserForgetPwdEditFragment extends BaseAppFragment {

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;

    @ViewInject(R.id.ed_input_pwd_again)
    private EditText ed_input_pwd_again;

    @Event(R.id.btn_confirm)
    private void okClick(View view) {
        submit();
    }

    private String mUserName;

    @Override
    protected void initParams() {
        super.initBackBar("设置新密码", true, false);
    }

    @Override
    public void onEnter(Object data) {
        super.onEnter(data);
        if (data != null) {
            mUserName = (String) data;
        }
    }

    private void submit() {

        ViewHelper.KeyBoardHide(ed_input_pwd_again);

        String pwd1 = ed_input_pwd.getText().toString().trim();
        String pwd2 = ed_input_pwd_again.getText().toString().trim();

        if (TextUtils.isEmpty(pwd1)) {
            ToastUtil.showToastShort(R.string.empty_pwd_hint);
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            ToastUtil.showToastShort(R.string.empty_pwd_hint);
            return;
        }

        if (!pwd1.equals(pwd2)) {
            ToastUtil.showToastShort("两次密码输入不一致");
            return;
        }
        showLoadingDialog();

        ApiControl.getApi().userForgetPwdNew(mUserName, pwd1, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                ToastUtil.showToastShort("修改成功");
                getSumContext().popToRoot(null);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }
}
