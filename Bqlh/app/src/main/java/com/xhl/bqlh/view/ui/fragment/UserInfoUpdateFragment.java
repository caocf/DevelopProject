package com.xhl.bqlh.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.SnackUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.ViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/7/20.
 */
@ContentView(R.layout.fragment_user_info_update)
public class UserInfoUpdateFragment extends BaseAppFragment {


    @ViewInject(R.id.ed_input_info)
    private EditText ed_input_info;

    @ViewInject(R.id.tv_right)
    private TextView tv_right;

    @Event(R.id.fl_btn_right)
    private void onClick(View view) {
        String input = ed_input_info.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            SnackUtil.longShow(view, "请输入修改信息");
        } else {
            ViewHelper.KeyBoardHide(view);
            updateInfo(input);
        }
    }

    private String mHint;
    private String mOldText;
    private String mTitle;

    private UserInfo mUser;
    private String mTag;

    @Override
    protected void initParams() {
        super.initBackBar(mTitle, true, true);
        tv_right.setText(R.string.dialog_ok);
        ed_input_info.setHint(mHint);
        ed_input_info.setText(mOldText);
    }

    @Override
    public void onResume() {
        super.onResume();
        ed_input_info.requestFocus();
        ViewHelper.KeyBoardShow(ed_input_info);
    }

    @Override
    public void onEnter(Object data) {
        super.onEnter(data);
        if (data != null) {
            Object[] info = (Object[]) data;
            mTitle = (String) info[0];
            mHint = (String) info[1];
            mTag = (String) info[3];
            mUser = (UserInfo) info[2];

            if (mTag.equals("1")) {
                mOldText = mUser.liablePhone;
            } else if (mTag.equals("2")) {
                mOldText = mUser.liableMail;
            } else if (mTag.equals("3")) {
                mOldText = mUser.address;
            }
        }
    }

    private void updateInfo(String content) {
        if (mTag.equals("1")) {
            mOldText = mUser.liablePhone = content;
        } else if (mTag.equals("2")) {
            mOldText = mUser.liableMail = content;
        } else if (mTag.equals("3")) {
            mOldText = mUser.address = content;
        }

        ApiControl.getApi().userUpdateInfo(mUser.address, mUser.liablePhone, mUser.liableMail, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                if (result.getObj().equals("success")) {

                    AppDelegate.appContext.saveLoginInfo(mUser);

                    getSumContext().popTopFragment(mUser);
                }
            }

            @Override
            public void finish() {

            }
        });
    }
}
