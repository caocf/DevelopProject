package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ebox.R;
import com.ebox.mgt.ui.CameraTestActivity;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 摄像头测试
 */
public class FragmentCameraTest extends Fragment implements View.OnClickListener {


    private View view;

    private Button cameraBT;
    private FragmentManager fm;

    private RadioGroup gcamera;
    private RadioButton rb_ok_camera, rb_error_camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera_test, null);
        initViews();
        return view;
    }

    private void initViews() {

        rb_ok_camera = (RadioButton) view.findViewById(R.id.rb_ok_camera);
        rb_error_camera = (RadioButton) view.findViewById(R.id.rb_error_camera);


        gcamera = (RadioGroup) view.findViewById(R.id.rg_camera);
        gcamera.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_audio:
                        PollingStore.storePoll("cameraTest", PollType.TEST_OK);
                        break;
                    case R.id.rb_error_audio:
                        PollingStore.storePoll("cameraTest", PollType.TEST_ERROR);
                        break;
                }
            }
        });

        cameraBT = (Button) view.findViewById(R.id.mgt_bt_p_camera);
        cameraBT.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mgt_bt_p_camera:
                Intent intent1 = new Intent(getActivity(), CameraTestActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
