package com.ebox.ex.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.pub.service.global.GlobalField;

/**
 * Created by Android on 2015/10/22.
 */
public class OpHelpFragment extends BaseOpFragment {

    private TextView tv_tel;    //可配置的客服电话

    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpHelpFragment();
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_help;
    }

    @Override
    protected void initView(View view) {
        tv_tel= (TextView) view.findViewById(R.id.ex_tv_fop_tel);

        if (GlobalField.showConfig == null||GlobalField.showConfig.dot_name.equals("魔格")) {
            return;
        }

        String phone = GlobalField.showConfig.call_center;
        if (!TextUtils.isEmpty(phone)) {
            tv_tel.setText(phone);
        }

    }


}
