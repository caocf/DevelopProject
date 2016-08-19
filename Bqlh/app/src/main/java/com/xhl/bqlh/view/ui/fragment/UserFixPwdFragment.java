package com.xhl.bqlh.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
 * Created by Summer on 2016/7/20.
 */
@ContentView(R.layout.fragment_user_fix_pwd)
public class UserFixPwdFragment extends BaseAppFragment {

    @ViewInject(R.id.ed_old_pwd)
    private TextView ed_old_pwd;

    @ViewInject(R.id.ed_new_pwd)
    private TextView ed_new_pwd;

    @ViewInject(R.id.ed_new_pwd_again)
    private TextView ed_new_pwd_again;

    @Event(R.id.btn_confirm)
    private void onClick(View view) {

        ViewHelper.KeyBoardHide(view);

        String oldPwd = ed_old_pwd.getText().toString();

        String newPwd1 = ed_new_pwd.getText().toString();

        String newPwd2 = ed_new_pwd_again.getText().toString();

        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtil.showToastShort("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(newPwd1)) {
            ToastUtil.showToastShort("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(newPwd2)) {
            ToastUtil.showToastShort("请再次输入新密码");
            return;
        }
        if (!newPwd1.equals(newPwd2)) {
            ToastUtil.showToastShort("两次输入密码不一致");
            return;
        }
        checkOldPwd(oldPwd);
    }

    private void checkOldPwd(String old) {
        ApiControl.getApi().userFixCheckPwd(old, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                if (result.getObj().equals("success")) {
                    changePwd();
                } else {
                    ToastUtil.showToastShort("旧密码错误");
                }
            }

            @Override
            public void finish() {

            }
        });
    }

    private void changePwd() {

        String newPwd1 = ed_new_pwd.getText().toString();

        ApiControl.getApi().userFixNewPwd(newPwd1, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                if (result.getObj().equals("success")) {
                    ToastUtil.showToastLong("修改成功");
                    getSumContext().popTopFragment(null);
                }
            }

            @Override
            public void finish() {

            }
        });

    }

    @Override
    protected void initParams() {
        super.initBackBar("修改密码", true, false);
    }
}
