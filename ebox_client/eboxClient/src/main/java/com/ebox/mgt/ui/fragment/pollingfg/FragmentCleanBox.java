package com.ebox.mgt.ui.fragment.pollingfg;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;
import com.ebox.mgt.ui.utils.HardAnimUtils;

/**
 * 清洁柜体
 */
public class FragmentCleanBox extends BasepollFragment {

    private View view;
    private RadioButton okRB, noRB;
    private TextView cleanTV;
    private Button okBT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_box_clean, null);
        initViews();
        return view;
    }

    private void initViews() {
        cleanTV= (TextView) view.findViewById(R.id.mgt_tv_fbc_clean);
        ObjectAnimator anim=ObjectAnimator.ofFloat(cleanTV,HardAnimUtils.TRANSLATIONY,0,200f);
        anim.setDuration(1000);
        anim.setInterpolator(new OvershootInterpolator());
        anim.start();

        okBT= (Button) view.findViewById(R.id.bt_fbc_ok);

        PollingStore.storePoll("isClear","1");

    }


}
