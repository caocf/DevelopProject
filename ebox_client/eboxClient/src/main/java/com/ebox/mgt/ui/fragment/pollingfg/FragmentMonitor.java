package com.ebox.mgt.ui.fragment.pollingfg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 监控
 */
public class FragmentMonitor extends BasepollFragment {

    private View view;
    private RadioGroup groupDisk, groupMonitor;
    private RadioButton rb_ok_disc, rb_no_disc, rb_ok_camera, rb_no_camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pol_monitor, null);
        initViews();
        return view;
    }

    private void initViews() {
        rb_ok_disc = (RadioButton) view.findViewById(R.id.rb_ok_disc);
        rb_no_disc = (RadioButton) view.findViewById(R.id.rb_no_disc);
        rb_ok_camera = (RadioButton) view.findViewById(R.id.rb_ok_camera);
        rb_no_camera = (RadioButton) view.findViewById(R.id.rb_no_camera);


        String discVcrState = PollingStore.getStore("discVcrState");
        String monitorCamState = PollingStore.getStore("monitorCamState");

        if (discVcrState.equals(PollType.OK)) {
            rb_ok_disc.setChecked(true);
        } else if (discVcrState.equals(PollType.ERROR)) {
            rb_no_disc.setChecked(true);
        }else{
            PollingStore.storePoll("discVcrState",PollType.OK);
        }

        if (monitorCamState.equals(PollType.OK)) {
            rb_ok_camera.setChecked(true);
        } else if (discVcrState.equals(PollType.ERROR)) {
            rb_no_camera.setChecked(true);
        }else{
            PollingStore.storePoll("monitorCamState",PollType.OK);
        }

        groupDisk = (RadioGroup) view.findViewById(R.id.rg_state_disk);
        groupDisk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_disc:
                        PollingStore.storePoll("discVcrState",PollType.OK);
                        break;
                    case R.id.rb_no_disc:
                        PollingStore.storePoll("discVcrState", PollType.ERROR);
                        break;
                }
            }
        });


        groupMonitor = (RadioGroup) view.findViewById(R.id.rg_state_monitor);
        groupMonitor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_camera:
                        PollingStore.storePoll("monitorCamState", PollType.OK);
                        break;
                    case R.id.rb_no_camera:
                        PollingStore.storePoll("monitorCamState", PollType.ERROR);
                        break;
                }
            }
        });

    }


}
