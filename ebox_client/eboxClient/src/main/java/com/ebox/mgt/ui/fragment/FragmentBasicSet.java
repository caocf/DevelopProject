package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.service.global.GlobalField;

/**
 * 基本配置
 */
public class FragmentBasicSet extends Fragment {

    private View view;
    private EditText et_box_seq;    //柜体唯一标识
    private EditText et_box_cnt;    //副柜子个数
    private EditText et_dot;    //版本
    private EditText et_board;  //主板配置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basic_set, null);
        initViews();
        return view;
    }

    private void initViews() {
        et_box_seq = (EditText) view.findViewById(R.id.et_box_seq);
        et_box_cnt = (EditText) view.findViewById(R.id.et_box_cnt);
        et_board = (EditText) view.findViewById(R.id.et_board);
        et_dot = (EditText) view.findViewById(R.id.et_dot);

        et_box_seq.setText(AppApplication.getInstance().getTerminal_code());
        et_box_cnt.setText(GlobalField.config.getCount().toString());
        et_board.setText(GlobalField.config.getMaim_board() + "");
        et_dot.setText(GlobalField.config.getDot() + "");
    }

    public void saveConfig() {
        String content = et_box_seq.getText().toString();

        if (content == null) {
            content = "";
        }
        GlobalField.config.setTerminal_code(content);

        content = et_box_cnt.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setCount(Integer.valueOf(content));

        content = et_board.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setMaim_board(Integer.valueOf(content));

        content = et_dot.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setDot(Integer.valueOf(content));


        FileOp.saveTemp(GlobalField.config);
/*        tip = new Tip(BoxSettingActivity.this,
                getResources().getString(R.string.mgt_save_ok),
                null);
        tip.show(0);*/

        Toast.makeText(getActivity(), getResources().getString(R.string.mgt_save_ok), Toast.LENGTH_LONG).show();

    }


}
