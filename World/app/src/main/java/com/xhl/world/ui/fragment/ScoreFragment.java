package com.xhl.world.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/7.
 */
@ContentView(R.layout.fragment_score)
public class ScoreFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_other;

    @ViewInject(R.id.tv_score)
    private TextView tv_score;


    @ViewInject(R.id.tv_score_explain)
    private TextView tv_score_explain;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(R.id.title_other)
    private void onScoreDetailClick(View view) {
        getSumContext().pushFragmentToBackStack(ScoreDetailsFragment.class, null);
    }

    @Override
    protected void initParams() {
        title_name.setText(R.string.my_jf);
        title_other.setText("明细");
        getTotal();
    }

    private void getTotal() {
        showLoadingDialog();
        ApiControl.getApi().totalIntegral(new Callback.CommonCallback<ResponseModel<Integer>>() {
            @Override
            public void onSuccess(ResponseModel<Integer> result) {
                if (result.isSuccess()) {
                    Integer resultObj = result.getResultObj();
                    if (resultObj != null) {
                        tv_score.setText(String.valueOf(resultObj));
                    }
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
                hideLoadingDialog();
            }
        });
    }


}
