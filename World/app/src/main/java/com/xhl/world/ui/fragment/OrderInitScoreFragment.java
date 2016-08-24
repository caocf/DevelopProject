package com.xhl.world.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 16/3/5.
 * 订单可用的积分
 */
@ContentView(R.layout.fragment_order_score)
public class OrderInitScoreFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_score_all)
    private TextView tv_score_all;

    @ViewInject(R.id.tv_score_hint)
    private TextView tv_score_hint;

    @ViewInject(R.id.tv_score_proportion)
    private TextView tv_score_proportion;

    @ViewInject(R.id.ed_input_score)
    private EditText ed_input_score;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }


    @Event(value = R.id.ripple_commit, type = RippleView.OnRippleCompleteListener.class)
    private void onCommitClick(View view) {
        ViewUtils.hideKeyBoard(view);
        try {
            String inputScore = ed_input_score.getText().toString().trim();
            if (TextUtils.isEmpty(inputScore)) {
                return;
            }
            Integer score = Integer.valueOf(inputScore);

            if (score <= mLimit && score <= mUserScore) {
                if (mListener != null) {
                    getSumContext().popTopFragment(null);
                    mListener.selectScore(score);
                }
            } else {
                ed_input_score.getEditableText().clear();
                SnackMaker.shortShow(view, "输入积分个数有误");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private selectScoreListener mListener;

    private Integer mLimit = 0;
    private Integer mUserScore = 0;
    private String mProportion = "0";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof selectScoreListener) {
            mListener = (selectScoreListener) context;
        }
    }

    @Override
    protected void initParams() {
        title_name.setText("积分");

        tv_score_all.setText("账号积分：" + mUserScore);
        tv_score_hint.setText("当前订单积分使用上限：" + mLimit);

        tv_score_proportion.setText("积分兑换比例："+mProportion);
    }


    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof HashMap) {
            HashMap<String, String> score = (HashMap<String, String>) data;
            try {
                mUserScore = Integer.valueOf(score.get("score"));
                mLimit = Integer.valueOf(score.get("limit"));
                mProportion = score.get("proportion");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public interface selectScoreListener {
        void selectScore(Integer score);
    }
}
