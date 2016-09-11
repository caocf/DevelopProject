package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.mgt.ui.UnionSetActivity;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.service.global.GlobalField;

/**
 * 银联配置
 */
public class FragmentUnionSet extends Fragment {

    private View view;
    private Button unionBT;
    private EditText et_union,et_service;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_union_set, null);
        initViews();
        return view;
    }

    private void initViews() {
        unionBT = (Button) view.findViewById(R.id.mgt_bt_fus_union);
        unionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent unionIntent = new Intent(getActivity(), UnionSetActivity.class);
                startActivity(unionIntent);
            }
        });

        et_union= (EditText) view.findViewById(R.id.et_box_seq);
        et_service= (EditText) view.findViewById(R.id.et_box_service);

        et_union.setText(GlobalField.config.getNjUms().toString());
        et_service.setText(GlobalField.config.getServiceCtrl().toString());

    }


    public void saveConfig() {
        String content = et_union.getText().toString();

        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setNjUms(Integer.valueOf(content));


        content = et_service.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }


        GlobalField.config.setServiceCtrl(Integer.valueOf(content));



        FileOp.saveTemp(GlobalField.config);
/*        tip = new Tip(BoxSettingActivity.this,
                getResources().getString(R.string.mgt_save_ok),
                null);
        tip.show(0);*/

        Toast.makeText(getActivity(), getResources().getString(R.string.mgt_save_ok), Toast.LENGTH_LONG).show();

    }


}
