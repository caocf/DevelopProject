package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.service.global.GlobalField;

/**
 * 扩展配置
 */
public class FragmentExtendsSet extends Fragment {

    private View view;
    private EditText et_theme;
    private EditText et_led_ip;
    private EditText et_web_ctrl;
    private EditText et_backlight1;
    private EditText et_backlight2;
    private EditText et_time1;
    private EditText et_time2;
    private EditText et_scantimer;
    private EditText et_community_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_extends_set, null);
        initViews();
        return view;
    }

    private void initViews() {
        et_theme = (EditText) view.findViewById(R.id.et_theme);
        et_led_ip = (EditText) view.findViewById(R.id.et_led_ip);
        et_web_ctrl = (EditText) view.findViewById(R.id.et_web_ctrl);
        et_time1 = (EditText) view.findViewById(R.id.et_time1);
        et_time2 = (EditText) view.findViewById(R.id.et_time2);
        et_backlight1 = (EditText) view.findViewById(R.id.et_backlight1);
        et_backlight2 = (EditText) view.findViewById(R.id.et_backlight2);
        et_scantimer = (EditText) view.findViewById(R.id.et_scan);
        et_community_id = (EditText) view.findViewById(R.id.et_community_id);

        et_theme.setText(GlobalField.config.getTheme() + "");
        et_led_ip.setText(GlobalField.config.getLed_ip());
        et_web_ctrl.setText(GlobalField.config.getWeb_nj().toString());
        et_time1.setText(GlobalField.config.getTime1() + "");
        et_time2.setText(GlobalField.config.getTime2() + "");
        et_backlight1.setText(GlobalField.config.getBacklight1() + "");
        et_backlight2.setText(GlobalField.config.getBacklight2() + "");
        et_scantimer.setText(GlobalField.config.getScanTimer() + "");
        et_community_id.setText(GlobalField.config.getCommunityId());


    }

    public void saveConfig() {
        String content = et_theme.getText().toString();

        if (content == null) {
            content = "";
        }
        GlobalField.config.setTheme(Integer.valueOf(content));

        content = et_led_ip.getText().toString();
        GlobalField.config.setLed_ip(content);

        content = et_web_ctrl.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setWeb_nj(Integer.valueOf(content));

        content = et_time1.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setTime1(Integer.valueOf(content));

        content = et_time2.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setTime2(Integer.valueOf(content));

        content = et_backlight1.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setBacklight1(Integer.valueOf(content));

        content = et_backlight2.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setBacklight2(Integer.valueOf(content));

        content = et_scantimer.getText().toString();
        if (content == null||content.equals("")) {
            content = "0";
        }
        GlobalField.config.setScanTimer(Integer.valueOf(content));

        content = et_community_id.getText().toString();
        GlobalField.config.setCommunityId(content);


        FileOp.saveTemp(GlobalField.config);
        Toast.makeText(getActivity(), getResources().getString(R.string.mgt_save_ok), Toast.LENGTH_LONG).show();

    }


}
