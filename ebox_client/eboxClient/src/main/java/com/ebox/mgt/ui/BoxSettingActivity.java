package com.ebox.mgt.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

public class BoxSettingActivity extends CommonActivity {
    private EditText et_box_seq;
    private EditText et_box_cnt;
    private EditText et_led_ip;
    private EditText et_led_port;
    private EditText et_service_ctrl;
    private EditText et_dot;
    private EditText et_time1;
    private EditText et_time2;
    private EditText et_backlight1;
    private EditText et_backlight2;
    private EditText et_camera;
    private EditText et_main_board;
    private EditText et_scantimer;
    private EditText et_theme;
    private EditText et_screen;
    private EditText et_njusm_ctrl;
    private EditText et_web_ctrl;
    private EditText et_community_id;


    private Tip tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgt_activity_box_setting);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
    }

    private void initView() {
        et_web_ctrl= (EditText) findViewById(R.id.et_web_ctrl);
        et_box_seq = (EditText) findViewById(R.id.et_box_seq);
        et_box_cnt = (EditText) findViewById(R.id.et_box_cnt);
        et_screen= (EditText) findViewById(R.id.et_screen);
        et_led_ip = (EditText) findViewById(R.id.et_led_ip);
        et_led_port = (EditText) findViewById(R.id.et_led_port);
        et_service_ctrl = (EditText) findViewById(R.id.et_service_ctrl);
        et_dot = (EditText) findViewById(R.id.et_dot);
        et_time1 = (EditText) findViewById(R.id.et_time1);
        et_time2 = (EditText) findViewById(R.id.et_time2);
        et_backlight1 = (EditText) findViewById(R.id.et_backlight1);
        et_backlight2 = (EditText) findViewById(R.id.et_backlight2);
        et_camera = (EditText) findViewById(R.id.et_camera);
        et_main_board = (EditText) findViewById(R.id.et_main_board);
        et_scantimer = (EditText) findViewById(R.id.et_scantimer);
        et_theme = (EditText) findViewById(R.id.et_theme);
        et_njusm_ctrl = (EditText) findViewById(R.id.et_njusm_ctrl);

        et_community_id = (EditText) findViewById(R.id.et_community_id);


        et_box_seq.setText(AppApplication.getInstance().getTerminal_code());
        et_box_cnt.setText(GlobalField.config.getCount().toString());
        et_led_ip.setText(GlobalField.config.getLed_ip());
        et_led_port.setText(GlobalField.config.getLed_port().toString());
        et_service_ctrl.setText(GlobalField.config.getServiceCtrl().toString());
        et_dot.setText(GlobalField.config.getDot() + "");
        et_time1.setText(GlobalField.config.getTime1() + "");
        et_time2.setText(GlobalField.config.getTime2() + "");
        et_backlight1.setText(GlobalField.config.getBacklight1() + "");
        et_backlight2.setText(GlobalField.config.getBacklight2() + "");
        et_camera.setText(GlobalField.config.getCameraCtrl() + "");
        et_main_board.setText(GlobalField.config.getMaim_board() + "");
        et_scantimer.setText(GlobalField.config.getScanTimer() + "");
        et_theme.setText(GlobalField.config.getTheme() + "");
        et_njusm_ctrl.setText(GlobalField.config.getNjUms() + "");
        et_community_id.setText(GlobalField.config.getCommunityId());
        et_screen.setText(GlobalField.config.getScreen()+"");
        et_web_ctrl.setText(GlobalField.config.getWeb_nj().toString());
        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 2;
        data.backStr = "保存并返回";
        data.tvContent = getResources().getString(R.string.mgt_box_setting);
        data.tvVisibility = true;
        title.setData(data, this);
        title.setZCTitleListner(new Title.ZCTitleListener() {
            @Override
            public void onOperateBtnClick(int index) {
                if (index == 1) {
                    saveConfig();
                }
                BoxSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        // 开启摄像头
       // AppApplication.getInstance().getCc().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    public void finish() {
        super.finish();
        if (tip != null) {
            tip.closeTip();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private boolean checkParam() {
        String time1 = et_time1.getText().toString();
        if (Integer.valueOf(time1) < 0 && Integer.valueOf(time1) > 23) {
            tip = new Tip(BoxSettingActivity.this,
                    getResources().getString(R.string.mgt_backlight_time_error),
                    null);
            tip.show(0);
            return false;
        }
        String backlight1 = et_backlight1.getText().toString();
        if (Integer.valueOf(backlight1) < 0 && Integer.valueOf(backlight1) > 255) {
            tip = new Tip(BoxSettingActivity.this,
                    getResources().getString(R.string.mgt_backlight_error),
                    null);
            tip.show(0);
            return false;
        }

        String time2 = et_time2.getText().toString();
        if (Integer.valueOf(time2) < 0 && Integer.valueOf(time2) > 23) {
            tip = new Tip(BoxSettingActivity.this,
                    getResources().getString(R.string.mgt_backlight_time_error),
                    null);
            tip.show(0);
            return false;
        }
        String backlight2 = et_backlight2.getText().toString();
        if (Integer.valueOf(backlight2) < 0 && Integer.valueOf(backlight2) > 255) {
            tip = new Tip(BoxSettingActivity.this,
                    getResources().getString(R.string.mgt_backlight_error),
                    null);
            tip.show(0);
            return false;
        }

        return true;
    }


    private void saveConfig() {
        String content = et_box_seq.getText().toString();

        if (content == null) {
            content = "";
        }
        GlobalField.config.setTerminal_code(content);

        content = et_box_cnt.getText().toString();

        GlobalField.config.setCount(Integer.valueOf(content));

        content = et_dot.getText().toString();

        GlobalField.config.setDot(Integer.valueOf(content));

        content = et_led_ip.getText().toString();

        GlobalField.config.setLed_ip(content);

        content = et_led_port.getText().toString();

        GlobalField.config.setLed_port(Integer.valueOf(content));

        content = et_service_ctrl.getText().toString();

        GlobalField.config.setServiceCtrl(Integer.valueOf(content));

        String time1 = et_time1.getText().toString();

        String backlight1 = et_backlight1.getText().toString();

        GlobalField.config.setTime1(Integer.valueOf(time1));
        GlobalField.config.setBacklight1(Integer.valueOf(backlight1));


        time1 = et_time2.getText().toString();

        backlight1 = et_backlight2.getText().toString();

        GlobalField.config.setTime2(Integer.valueOf(time1));
        GlobalField.config.setBacklight2(Integer.valueOf(backlight1));

        content = et_camera.getText().toString();

        GlobalField.config.setCameraCtrl(Integer.valueOf(content));

        content = et_scantimer.getText().toString();

        GlobalField.config.setScanTimer(Integer.valueOf(content));

        content = et_theme.getText().toString();

        GlobalField.config.setTheme(Integer.valueOf(content));


        content = et_main_board.getText().toString();

        GlobalField.config.setMaim_board(Integer.valueOf(content));

        content = et_njusm_ctrl.getText().toString();

        GlobalField.config.setNjUms(Integer.valueOf(content));

        content = et_community_id.getText().toString();

        GlobalField.config.setCommunityId(content);

        content= et_screen.getText().toString();
        GlobalField.config.setScreen(Integer.valueOf(content));

        content= et_web_ctrl.getText().toString();
        GlobalField.config.setWeb_nj(Integer.valueOf(content));

        FileOp.saveTemp(GlobalField.config);
/*        tip = new Tip(BoxSettingActivity.this,
                getResources().getString(R.string.mgt_save_ok),
                null);
        tip.show(0);*/

        Toast.makeText(BoxSettingActivity.this,getResources().getString(R.string.mgt_save_ok),Toast.LENGTH_LONG).show();

    }

}
