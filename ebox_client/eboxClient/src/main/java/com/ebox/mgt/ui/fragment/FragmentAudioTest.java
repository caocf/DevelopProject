package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebox.R;
import com.ebox.pub.ui.customview.view.MaterialButton;
import com.ebox.pub.utils.RingUtil;

/**
 * 声音测试
 */
public class FragmentAudioTest extends Fragment {

    private MaterialButton beginBT;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_audio_test, null);
        initViews();
        return view;
    }

    private void initViews() {
        beginBT = (MaterialButton) view.findViewById(R.id.mgt_bt_fat_begin);
        beginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingUtil.playRingtone(RingUtil.deliver_id);
            }
        });
    }


}
