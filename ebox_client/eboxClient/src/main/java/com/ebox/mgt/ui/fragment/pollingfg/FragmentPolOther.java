package com.ebox.mgt.ui.fragment.pollingfg;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.mgt.ui.CameraTestActivity;
import com.ebox.mgt.ui.ScanActivity;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;
import com.ebox.pub.utils.RingUtil;

/**
 * 巡检的其他界面
 */
public class FragmentPolOther extends BasepollFragment implements View.OnClickListener {

    private View view;

    private Button audioBT, scanBT, cameraBT, activitOKBT;
    private FragmentManager fm;

    private RadioGroup gmedia, gscan, gcamera;
    private EditText et_overtime;
    private RadioButton rb_ok_audio, rb_error_audio, rb_ok_scan, rb_error_scan, rb_ok_camera, rb_error_camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pol_other, null);
        initViews();
        return view;
    }

    private void initViews() {

        rb_ok_audio = (RadioButton) view.findViewById(R.id.rb_ok_audio);
        rb_error_audio = (RadioButton) view.findViewById(R.id.rb_error_audio);
        rb_ok_scan = (RadioButton) view.findViewById(R.id.rb_ok_scan);
        rb_error_scan = (RadioButton) view.findViewById(R.id.rb_error_scan);
        rb_ok_camera = (RadioButton) view.findViewById(R.id.rb_ok_camera);
        rb_error_camera = (RadioButton) view.findViewById(R.id.rb_error_camera);
        et_overtime = (EditText) view.findViewById(R.id.et_overtime);


        String audioTest = PollingStore.getStore("audioTest");
        String scanTest = PollingStore.getStore("scanTest");
        String cameraTest = PollingStore.getStore("cameraTest");
        String overtimeNum = PollingStore.getStore("overtimeNum");

        if (audioTest.equals(PollType.TEST_OK)) {
            rb_ok_audio.setChecked(true);
        } else if (audioTest.equals(PollType.TEST_ERROR)) {
            rb_error_audio.setChecked(true);
        }else {
            PollingStore.storePoll("audioTest",PollType.TEST_OK);
        }

        if (scanTest.equals(PollType.TEST_OK)) {
            rb_ok_scan.setChecked(true);
        } else if (scanTest.equals(PollType.TEST_ERROR)) {
            rb_error_scan.setChecked(true);
        }else{
            PollingStore.storePoll("scanTest",PollType.TEST_OK);
        }

        if (cameraTest.equals(PollType.TEST_OK)) {
            rb_ok_camera.setChecked(true);
        } else if (cameraTest.equals(PollType.TEST_ERROR)) {
            rb_error_camera.setChecked(true);
        }else{
            PollingStore.storePoll("cameraTest",PollType.TEST_OK);
        }

        if (!overtimeNum.equals(PollingStore.POLL_NULL)) {
            et_overtime.setText(overtimeNum);
        }else{
            PollingStore.storePoll("overtimeNum","0");
        }


        et_overtime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                PollingStore.storePoll("overtimeNum", editable.toString());
            }
        });


        gmedia = (RadioGroup) view.findViewById(R.id.rg_media);
        gscan = (RadioGroup) view.findViewById(R.id.rg_scan);
        gcamera = (RadioGroup) view.findViewById(R.id.rg_camera);
        et_overtime = (EditText) view.findViewById(R.id.et_overtime);

        gmedia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_audio:
                        PollingStore.storePoll("audioTest",PollType.TEST_OK);
                        break;
                    case R.id.rb_error_audio:
                        PollingStore.storePoll("audioTest", PollType.TEST_ERROR);
                        break;
                }
            }
        });

        gscan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_scan:
                        PollingStore.storePoll("scanTest", PollType.TEST_OK);
                        break;
                    case R.id.rb_error_scan:
                        PollingStore.storePoll("scanTest", PollType.TEST_ERROR);
                        break;
                }
            }
        });

        gcamera.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_camera:
                        PollingStore.storePoll("cameraTest", PollType.TEST_OK);
                        break;
                    case R.id.rb_error_camera:
                        PollingStore.storePoll("cameraTest", PollType.TEST_ERROR);
                        break;
                }
            }
        });


        audioBT = (Button) view.findViewById(R.id.mgt_bt_p_audio);
        scanBT = (Button) view.findViewById(R.id.mgt_bt_p_scan);
        cameraBT = (Button) view.findViewById(R.id.mgt_bt_p_camera);

        activitOKBT = (Button) getActivity().findViewById(R.id.mgt_bt_ap_ok);


        audioBT.setOnClickListener(this);
        scanBT.setOnClickListener(this);
        cameraBT.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mgt_bt_p_audio:
                Toast.makeText(getActivity(), "声音是否播放了", Toast.LENGTH_SHORT).show();
                RingUtil.playRingtone(RingUtil.deliver_id);
                break;
            case R.id.mgt_bt_p_scan:
                Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
                startActivity(scanIntent);
                break;
            case R.id.mgt_bt_p_camera:
                Intent intent1 = new Intent(getActivity(), CameraTestActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
