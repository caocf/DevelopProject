package com.ebox.mgt.ui.fragment.pollingfg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 屏幕测试
 */
public class FragmentScreenTest extends BasepollFragment {

    private View view;
    private RelativeLayout screenRL;
    private TextView screenTV;

    private Button okBT,errorBT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screen_test, null);
        initViews();
        return view;
    }

    private void initViews() {
        screenRL = (RelativeLayout) view.findViewById(R.id.mgt_rl_screen);
        screenTV = (TextView) view.findViewById(R.id.mgt_tv_screen);
        screenTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenRL.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                screenTV.setText("蓝色");
            }
        });

        okBT= (Button) view.findViewById(R.id.bt_ok);
        errorBT= (Button) view.findViewById(R.id.bt_error);

        PollingStore.storePoll("isTestScreen",PollType.TEST_OK);

        okBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollingStore.storePoll("isTestScreen",PollType.TEST_OK);
                Toast.makeText(AppApplication.getInstance(), "显示屏测试成功", Toast.LENGTH_SHORT).show();
            }
        });

        errorBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollingStore.storePoll("isTestScreen", PollType.TEST_ERROR);
                Toast.makeText(AppApplication.getInstance(),"显示屏测试失败",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
