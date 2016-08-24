package com.xhl.world.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 16/1/19.
 */
@ContentView(R.layout.fragment_safe_account)
public class AccountSafeFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_verity_state)
    private TextView tv_verity_state;

    private boolean nameHasVerify = true;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }


    @Event(value = R.id.ripple_user_pwd, type = RippleView.OnRippleCompleteListener.class)
    private void onUserPwdClick(View view) {
        getSumContext().pushFragmentToBackStack(RegisterFragment.class, RegisterFragment.Type_forget_by_login);
    }

    @Event(value = R.id.ripple_user_id, type = RippleView.OnRippleCompleteListener.class)
    private void onUserIdClick(View view) {
        if (!nameHasVerify) {
            getSumContext().pushFragmentToBackStack(AccountCertifyFragment.class, null);
        }
    }

    @Override
    protected void initParams() {
        title_name.setText("账户安全");
    }

    @Override
    public void onResume() {
        super.onResume();
        checkInfo();
    }

    private void checkInfo() {

        showLoadingDialog();

        ApiControl.getApi().checkSecurity(new Callback.CommonCallback<ResponseModel<HashMap<String, String>>>() {
            @Override
            public void onSuccess(ResponseModel<HashMap<String, String>> result) {
                if (result.isSuccess()) {
                    //实名认证状态
                    if (result.getResultObj().containsKey("realNameState")) {
                        String state = result.getResultObj().get("realNameState");

                        //0-未认证，1-待审核，2-已审核，3-审核未通过
                        if (state.equals("1")) {
                            nameHasVerify = true;
                            tv_verity_state.setText("待审核");

                        } else if (state.equals("2")) {
                            nameHasVerify = true;
                            tv_verity_state.setText("已审核");

                        } else if (state.equals("3")) {
                            nameHasVerify = true;
                            tv_verity_state.setText("审核未通过");

                        } else {
                            nameHasVerify = false;
                            tv_verity_state.setText("未认证");
                        }
                    }

                } else {
                    SnackMaker.shortShow(title_name,result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,ex.getMessage());
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
