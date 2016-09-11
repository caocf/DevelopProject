package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebox.R;
import com.ebox.mgt.ui.BoxManageActivity;
import com.ebox.pub.ui.customview.view.MaterialButton;

/**
 * 箱体管理
 */
public class FragmentBoxMan extends Fragment {

    private View view;
    private MaterialButton boxmanBT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_box_man, null);

        intViews();
        return view;
    }

    private void intViews() {
        boxmanBT = (MaterialButton) view.findViewById(R.id.mgt_bt_fbm_boxman);
        boxmanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), BoxManageActivity.class);
                startActivity(intent1);
            }
        });
    }

}
