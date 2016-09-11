package com.ebox.ex.ui.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.ebox.R;
import com.ebox.ex.ui.bar.RechargeBar;
import com.ebox.ex.ui.bar.RechargeNoteBar;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.ex.utils.OperatorHelper;

/**
 * Created by Android on 2015/10/22.
 * 充值Fragment
 */
public class OpRechargeFragment extends BaseOpFragment implements CompoundButton.OnCheckedChangeListener {

    private RadioButton rb_recharge, rb_note;
    private FrameLayout fl_content;
    private String operator_id;


    private int mCurPage = 0;

    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpRechargeFragment();
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_recharge;
    }

    @Override
    protected void initView(View view) {
        operator_id = OperatorHelper.mPhone;

        rb_recharge = (RadioButton) view.findViewById(R.id.rb_recharge);
        rb_note = (RadioButton) view.findViewById(R.id.rb_recharge_note);

        rb_recharge.setOnCheckedChangeListener(this);
        rb_note.setOnCheckedChangeListener(this);

        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);

        changePage(0);

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.rb_recharge:
                if (isChecked) {
                    changePage(0);
                }
                break;

            case R.id.rb_recharge_note:
                if (isChecked) {
                    changePage(1);
                }
                break;
        }
    }

    //0  充值    1充值记录
    private void changePage(int page) {
        mCurPage = page;


        rb_recharge.setTextColor(getResources().getColor(R.color.ex_black));
        rb_note.setTextColor(getResources().getColor(R.color.ex_black));
        rb_recharge.setChecked(false);
        rb_note.setChecked(false);
        fl_content.removeAllViews();

        switch (mCurPage) {
            case 0:
                rb_recharge.setTextColor(getResources().getColor(R.color.ex_box_select));
                rb_recharge.setChecked(true);
                fl_content.addView(new RechargeBar(context, operator_id));
                break;
            case 1:
                rb_note.setTextColor(getResources().getColor(R.color.ex_box_select));
                rb_note.setChecked(true);
                fl_content.addView(new RechargeNoteBar(context));
                break;
        }
    }


}
