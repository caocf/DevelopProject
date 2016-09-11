package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.utils.MGViewUtil;

public class BoxRackNumSetFragment extends Fragment implements View.OnClickListener {

    private EditText ed_01, ed_02, ed_03, ed_04, ed_05, ed_06, ed_07, ed_08, ed_09, ed_10, ed_11, ed_12, ed_13, ed_14, ed_15, ed_16;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView(inflater);

        MGViewUtil.scaleContentView((ViewGroup) view);

        return view;
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.mgt_fragment_set_rack_num, null);

        view.findViewById(R.id.bt_01).setOnClickListener(this);
        view.findViewById(R.id.bt_02).setOnClickListener(this);
        view.findViewById(R.id.bt_03).setOnClickListener(this);
        view.findViewById(R.id.bt_04).setOnClickListener(this);
        view.findViewById(R.id.bt_05).setOnClickListener(this);
        view.findViewById(R.id.bt_06).setOnClickListener(this);
        view.findViewById(R.id.bt_07).setOnClickListener(this);
        view.findViewById(R.id.bt_08).setOnClickListener(this);
        view.findViewById(R.id.bt_09).setOnClickListener(this);
        view.findViewById(R.id.bt_10).setOnClickListener(this);
        view.findViewById(R.id.bt_11).setOnClickListener(this);
        view.findViewById(R.id.bt_12).setOnClickListener(this);
        view.findViewById(R.id.bt_13).setOnClickListener(this);
        view.findViewById(R.id.bt_14).setOnClickListener(this);
        view.findViewById(R.id.bt_15).setOnClickListener(this);
        view.findViewById(R.id.bt_16).setOnClickListener(this);
        ed_01 = (EditText) view.findViewById(R.id.et_01);
        ed_02 = (EditText) view.findViewById(R.id.et_02);
        ed_03 = (EditText) view.findViewById(R.id.et_03);
        ed_04 = (EditText) view.findViewById(R.id.et_04);
        ed_05 = (EditText) view.findViewById(R.id.et_05);
        ed_06 = (EditText) view.findViewById(R.id.et_06);
        ed_07 = (EditText) view.findViewById(R.id.et_07);
        ed_08 = (EditText) view.findViewById(R.id.et_08);
        ed_09 = (EditText) view.findViewById(R.id.et_09);
        ed_10 = (EditText) view.findViewById(R.id.et_10);
        ed_11 = (EditText) view.findViewById(R.id.et_11);
        ed_12 = (EditText) view.findViewById(R.id.et_12);
        ed_13 = (EditText) view.findViewById(R.id.et_13);
        ed_14 = (EditText) view.findViewById(R.id.et_14);
        ed_15 = (EditText) view.findViewById(R.id.et_15);
        ed_16 = (EditText) view.findViewById(R.id.et_16);
        readSave();
        return view;
    }

    private void readSave() {
        setSave(ed_01, 1);
        setSave(ed_02, 2);
        setSave(ed_03, 3);
        setSave(ed_04, 4);
        setSave(ed_05, 5);
        setSave(ed_06, 6);
        setSave(ed_07, 7);
        setSave(ed_08, 8);
        setSave(ed_09, 9);
        setSave(ed_10, 10);
        setSave(ed_11, 11);
        setSave(ed_12, 12);
        setSave(ed_13, 13);
        setSave(ed_14, 14);
        setSave(ed_15, 15);
        setSave(ed_16, 16);
    }

    private void setSave(EditText et, int rack) {
        int num = SharePreferenceHelper.getRackNum(rack);
        et.setText(num + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_01:
                save(1, ed_01);
                break;
            case R.id.bt_02:
                save(2, ed_02);
                break;
            case R.id.bt_03:
                save(3, ed_03);
                break;
            case R.id.bt_04:
                save(4, ed_04);
                break;
            case R.id.bt_05:
                save(5, ed_05);
                break;
            case R.id.bt_06:
                save(6, ed_06);
                break;
            case R.id.bt_07:
                save(7, ed_07);
                break;
            case R.id.bt_08:
                save(8, ed_08);
                break;
            case R.id.bt_09:
                save(9, ed_09);
                break;
            case R.id.bt_10:
                save(10, ed_10);
                break;
            case R.id.bt_11:
                save(11, ed_11);
                break;
            case R.id.bt_12:
                save(12, ed_12);
                break;
            case R.id.bt_13:
                save(13, ed_13);
                break;
            case R.id.bt_14:
                save(14, ed_14);
                break;
            case R.id.bt_15:
                save(15, ed_15);
                break;
            case R.id.bt_16:
                save(16, ed_16);
                break;


        }
    }

    private void save(int rack, EditText index) {

        String num = index.getText().toString();

        SharePreferenceHelper.saveRackNum(rack, Integer.parseInt(num));

        Toast.makeText(getActivity(), rack + "组 保存完成", Toast.LENGTH_LONG).show();
    }
}
