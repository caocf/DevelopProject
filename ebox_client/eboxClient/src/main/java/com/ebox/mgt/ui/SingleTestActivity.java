package com.ebox.mgt.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.fragment.FragmentAudioTest;
import com.ebox.mgt.ui.fragment.FragmentBoxMan;
import com.ebox.mgt.ui.fragment.FragmentCameraTest;
import com.ebox.mgt.ui.fragment.FragmentHighSet;
import com.ebox.mgt.ui.fragment.FragmentScanTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentNetTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentScreenTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentTouchTest;
import com.ebox.mgt.ui.utils.HardAnimUtils;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 单项检测
 */
public class SingleTestActivity extends CommonActivity implements View.OnClickListener, View.OnHoverListener {

    //界面属性
    private Button btCamera;
    private Button btAudio;
    private Button btTouch;
    private Button btScreen;
    private Button btScan;
    private Button btNet;
    private Button btBoxManager;
    private Button btSet;

    private TextView tvFtitle;
    private TextView tvFdes;
    private Button overBT;
    private TextView notestTV;
    private Button singleBT;


    private TextView test1TV, test2TV, test3TV, test4TV, test5TV, test6TV, test7TV, test8TV;   //测试精灵

    private FragmentCameraTest fragmentCameraTest;
    private FragmentAudioTest fragmentAudioTest;
    private FragmentTouchTest fragmentTouchTest;
    private FragmentScreenTest fragmentScreenTest;
    private FragmentScanTest fragmentScanTest;
    private FragmentNetTest fragmentNetTest;
    private FragmentHighSet fragmentHighSet;
    private FragmentBoxMan fragmentBoxMan;

    private String TAG_CAMERATEST = "camera_test";
    private String TAG_AUDIOTEST = "audio_test";
    private String TAG_TOUCH = "touch_test";
    private String TAG_SCREEN = "screen_test";
    private String TAG_SCANTEST = "scan_test";
    private String TAG_NETTEST = "net_test";
    private String TAG_BOX_MAN = "box_manager";
    private String TAG_SET = "set";

    //其他属性
    private FragmentManager fm;
    private FragmentTransaction ts;
    private int nowTestId = R.id.mgt_bt_ast_camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_test);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }


    /* 界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_single_test);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initViews() {
        initTitle();

        btCamera = (Button) this.findViewById(R.id.mgt_bt_ast_camera);
        btAudio = (Button) this.findViewById(R.id.mgt_bt_ast_audio);
        btTouch = (Button) this.findViewById(R.id.mgt_bt_ast_touch);
        btScreen = (Button) this.findViewById(R.id.mgt_bt_ast_screen);
        btScan = (Button) this.findViewById(R.id.mgt_bt_ast_scan);
        btNet = (Button) this.findViewById(R.id.mgt_bt_ast_net);
        btBoxManager = (Button) this.findViewById(R.id.mgt_bt_ast_box);
        btSet = (Button) this.findViewById(R.id.mgt_bt_ast_set);

        overBT = (Button) this.findViewById(R.id.mgt_bt_ast_over);
        overBT.setOnClickListener(this);
        notestTV = (TextView) this.findViewById(R.id.mgt_tv_ast_ok);
        singleBT = (Button) this.findViewById(R.id.mgt_bt_ast_ok);
        singleBT.setOnClickListener(this);

        test1TV = (TextView) this.findViewById(R.id.mgt_tv_ast_1);
        test2TV = (TextView) this.findViewById(R.id.mgt_tv_ast_2);
        test3TV = (TextView) this.findViewById(R.id.mgt_tv_ast_3);
        test4TV = (TextView) this.findViewById(R.id.mgt_tv_ast_4);
        test5TV = (TextView) this.findViewById(R.id.mgt_tv_ast_5);
        test6TV = (TextView) this.findViewById(R.id.mgt_tv_ast_6);
        test7TV = (TextView) this.findViewById(R.id.mgt_tv_ast_7);
        test8TV = (TextView) this.findViewById(R.id.mgt_tv_ast_8);

        test1TV.setOnClickListener(this);
        test2TV.setOnClickListener(this);
        test3TV.setOnClickListener(this);
        test4TV.setOnClickListener(this);
        test5TV.setOnClickListener(this);
        test6TV.setOnClickListener(this);
        test7TV.setOnClickListener(this);
        test8TV.setOnClickListener(this);

        tvFtitle = (TextView) this.findViewById(R.id.tv_fh_title);
        tvFdes = (TextView) this.findViewById(R.id.tv_fh_prompt);


        btCamera.setOnClickListener(this);
        btAudio.setOnClickListener(this);
        btTouch.setOnClickListener(this);
        btScreen.setOnClickListener(this);
        btScan.setOnClickListener(this);
        btNet.setOnClickListener(this);
        btBoxManager.setOnClickListener(this);
        btSet.setOnClickListener(this);

        btCamera.setOnHoverListener(this);
        btAudio.setOnHoverListener(this);
        btTouch.setOnHoverListener(this);
        btScreen.setOnHoverListener(this);
        btScan.setOnHoverListener(this);
        btNet.setOnHoverListener(this);
        btBoxManager.setOnHoverListener(this);
        btSet.setOnHoverListener(this);


        fm = getFragmentManager();
        fragmentCameraTest = new FragmentCameraTest();
        showFragment(fragmentCameraTest, TAG_CAMERATEST);
        tvFtitle.setText(R.string.mgt_st_camera_test);
        tvFdes.setText(R.string.mgt_st_camera_test);

        HardAnimUtils.resultTVAnim(test1TV, test2TV, test3TV, test4TV, test5TV, test6TV, test7TV, test8TV);


    }

    private void showFragment(Fragment fragment, String tag) {
        ts = fm.beginTransaction();
//        ts.addToBackStack(null);
        ts.replace(R.id.mgt_fl_ast_content, fragment, tag).commit();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //完成单次安装
            case R.id.mgt_bt_ast_ok:
                doTestOK();
                break;

            //完成配置更新
            case R.id.mgt_bt_ast_over:
                finish();
                break;


            //摄像头测试
            case R.id.mgt_bt_ast_camera:
                nowTestId = R.id.mgt_bt_ast_camera;
                refreshFGHeader(R.id.mgt_bt_ast_camera);
                fragmentCameraTest = (FragmentCameraTest) fm.findFragmentByTag(TAG_CAMERATEST);
                if (fragmentCameraTest == null) {
                    fragmentCameraTest = new FragmentCameraTest();
                }
                showFragment(fragmentCameraTest, TAG_CAMERATEST);
                break;

            //声音测试
            case R.id.mgt_bt_ast_audio:
                nowTestId = R.id.mgt_bt_ast_audio;
                refreshFGHeader(R.id.mgt_bt_ast_audio);
                fragmentAudioTest = (FragmentAudioTest) fm.findFragmentByTag(TAG_AUDIOTEST);
                if (fragmentAudioTest == null) {
                    fragmentAudioTest = new FragmentAudioTest();
                }
                showFragment(fragmentAudioTest, TAG_AUDIOTEST);
                break;

            //触摸屏测试
            case R.id.mgt_bt_ast_touch:
                nowTestId = R.id.mgt_bt_ast_touch;
                refreshFGHeader(R.id.mgt_bt_ast_touch);
                fragmentTouchTest = (FragmentTouchTest) fm.findFragmentByTag(TAG_TOUCH);
                if (fragmentTouchTest == null) {
                    fragmentTouchTest = new FragmentTouchTest();
                }
                showFragment(fragmentTouchTest, TAG_TOUCH);
                break;

            //显示屏测试
            case R.id.mgt_bt_ast_screen:
                nowTestId = R.id.mgt_bt_ast_screen;
                refreshFGHeader(R.id.mgt_bt_ast_screen);
                fragmentScreenTest = (FragmentScreenTest) fm.findFragmentByTag(TAG_SCREEN);
                if (fragmentScreenTest == null) {
                    fragmentScreenTest = new FragmentScreenTest();
                }
                showFragment(fragmentScreenTest, TAG_SCREEN);
                break;

            //扫描枪测试
            case R.id.mgt_bt_ast_scan:
                nowTestId = R.id.mgt_bt_ast_scan;
                refreshFGHeader(R.id.mgt_bt_ast_scan);
                fragmentScanTest = (FragmentScanTest) fm.findFragmentByTag(TAG_SCANTEST);
                if (fragmentScanTest == null) {
                    fragmentScanTest = new FragmentScanTest();
                }
                showFragment(fragmentScanTest, TAG_SCANTEST);
                break;

            //网络测试
            case R.id.mgt_bt_ast_net:
                nowTestId = R.id.mgt_bt_ast_net;
                refreshFGHeader(R.id.mgt_bt_ast_net);
                fragmentNetTest = (FragmentNetTest) fm.findFragmentByTag(TAG_NETTEST);
                if (fragmentNetTest == null) {
                    fragmentNetTest = new FragmentNetTest();
                }
                showFragment(fragmentNetTest, TAG_NETTEST);
                break;

            //箱体管理
            case R.id.mgt_bt_ast_box:
                nowTestId = R.id.mgt_bt_ast_box;
                refreshFGHeader(R.id.mgt_bt_ast_box);
                fragmentBoxMan = (FragmentBoxMan) fm.findFragmentByTag(TAG_BOX_MAN);
                if (fragmentBoxMan == null) {
                    fragmentBoxMan = new FragmentBoxMan();
                }
                showFragment(fragmentBoxMan, TAG_BOX_MAN);
                break;

            //高级配置
            case R.id.mgt_bt_ast_set:
                nowTestId = R.id.mgt_bt_ast_set;
                refreshFGHeader(R.id.mgt_bt_ast_set);
                fragmentHighSet = (FragmentHighSet) fm.findFragmentByTag(TAG_SET);
                if (fragmentHighSet == null) {
                    fragmentHighSet = new FragmentHighSet();
                }
                showFragment(fragmentHighSet, TAG_SET);
                break;








            //摄像头测试
            case R.id.mgt_tv_ast_1:
                nowTestId = R.id.mgt_bt_ast_camera;
                refreshFGHeader(R.id.mgt_bt_ast_camera);
                fragmentCameraTest = (FragmentCameraTest) fm.findFragmentByTag(TAG_CAMERATEST);
                if (fragmentCameraTest == null) {
                    fragmentCameraTest = new FragmentCameraTest();
                }
                showFragment(fragmentCameraTest, TAG_CAMERATEST);
                break;

            //声音测试
            case R.id.mgt_tv_ast_2:
                nowTestId = R.id.mgt_bt_ast_audio;
                refreshFGHeader(R.id.mgt_bt_ast_audio);
                fragmentAudioTest = (FragmentAudioTest) fm.findFragmentByTag(TAG_AUDIOTEST);
                if (fragmentAudioTest == null) {
                    fragmentAudioTest = new FragmentAudioTest();
                }
                showFragment(fragmentAudioTest, TAG_AUDIOTEST);
                break;

            //触摸屏测试
            case R.id.mgt_tv_ast_3:
                nowTestId = R.id.mgt_bt_ast_touch;
                refreshFGHeader(R.id.mgt_bt_ast_touch);
                fragmentTouchTest = (FragmentTouchTest) fm.findFragmentByTag(TAG_TOUCH);
                if (fragmentTouchTest == null) {
                    fragmentTouchTest = new FragmentTouchTest();
                }
                showFragment(fragmentTouchTest, TAG_TOUCH);
                break;

            //显示屏测试
            case R.id.mgt_tv_ast_4:
                nowTestId = R.id.mgt_bt_ast_screen;
                refreshFGHeader(R.id.mgt_bt_ast_screen);
                fragmentScreenTest = (FragmentScreenTest) fm.findFragmentByTag(TAG_SCREEN);
                if (fragmentScreenTest == null) {
                    fragmentScreenTest = new FragmentScreenTest();
                }
                showFragment(fragmentScreenTest, TAG_SCREEN);
                break;

            //扫描枪测试
            case R.id.mgt_tv_ast_5:
                nowTestId = R.id.mgt_bt_ast_scan;
                refreshFGHeader(R.id.mgt_bt_ast_scan);
                fragmentScanTest = (FragmentScanTest) fm.findFragmentByTag(TAG_SCANTEST);
                if (fragmentScanTest == null) {
                    fragmentScanTest = new FragmentScanTest();
                }
                showFragment(fragmentScanTest, TAG_SCANTEST);
                break;

            //网络测试
            case R.id.mgt_tv_ast_6:
                nowTestId = R.id.mgt_bt_ast_net;
                refreshFGHeader(R.id.mgt_bt_ast_net);
                fragmentNetTest = (FragmentNetTest) fm.findFragmentByTag(TAG_NETTEST);
                if (fragmentNetTest == null) {
                    fragmentNetTest = new FragmentNetTest();
                }
                showFragment(fragmentNetTest, TAG_NETTEST);
                break;

            //箱体管理
            case R.id.mgt_tv_ast_7:
                nowTestId = R.id.mgt_bt_ast_box;
                refreshFGHeader(R.id.mgt_bt_ast_box);
                fragmentBoxMan = (FragmentBoxMan) fm.findFragmentByTag(TAG_BOX_MAN);
                if (fragmentBoxMan == null) {
                    fragmentBoxMan = new FragmentBoxMan();
                }
                showFragment(fragmentBoxMan, TAG_BOX_MAN);
                break;

            //高级配置
            case R.id.mgt_tv_ast_8:
                nowTestId = R.id.mgt_bt_ast_set;
                refreshFGHeader(R.id.mgt_bt_ast_set);
                fragmentHighSet = (FragmentHighSet) fm.findFragmentByTag(TAG_SET);
                if (fragmentHighSet == null) {
                    fragmentHighSet = new FragmentHighSet();
                }
                showFragment(fragmentHighSet, TAG_SET);
                break;

        }
    }


    /**
     * 刷新头部
     */
    private void refreshFGHeader(int id) {
        switch (id) {
            //1摄像头测试
            case R.id.mgt_bt_ast_camera:
                tvFtitle.setText(R.string.mgt_st_camera_test);
                tvFdes.setText(R.string.mgt_st_camera_test_des);
                break;
            //2声音测试
            case R.id.mgt_bt_ast_audio:
                tvFtitle.setText(R.string.mgt_st_audio_test);
                tvFdes.setText(R.string.mgt_st_audio_test_des);
                break;

            //3，触摸屏测试
            case R.id.mgt_bt_ast_touch:
                tvFtitle.setText(R.string.mgt_st_touch_test);
                tvFdes.setText(R.string.mgt_st_touch_des);
                break;

            //4显示屏测试
            case R.id.mgt_bt_ast_screen:
                tvFtitle.setText(R.string.mgt_st_screen_test);
                tvFdes.setText(R.string.mgt_st_screen_des);
                break;


            //5扫描枪测试
            case R.id.mgt_bt_ast_scan:
                tvFtitle.setText(R.string.mgt_st_scan_test);
                tvFdes.setText(R.string.mgt_st_scan_test_des);
                break;

            //6网络测试
            case R.id.mgt_bt_ast_net:
                tvFtitle.setText(R.string.mgt_st_net_test);
                tvFdes.setText(R.string.mgt_st_net_test_des);
                break;
            //7箱体管理
            case R.id.mgt_bt_ast_box:
                tvFtitle.setText(R.string.mgt_st_box_manager);
                tvFdes.setText(R.string.mgt_st_box_manager_des);
                break;

            //6网络测试
            case R.id.mgt_bt_ast_set:
                tvFtitle.setText(R.string.mgt_st_set);
                tvFdes.setText(R.string.mgt_st_set_des);
                break;
        }
        HardAnimUtils.headerAnim(tvFtitle, tvFdes);
    }

    /**
     * 鼠标滑动监听
     */
    @Override
    public boolean onHover(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_HOVER_ENTER:
                HardAnimUtils.menuEnter(v);
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_HOVER_EXIT:
                HardAnimUtils.menuExit(v);
                break;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }


    /*逻辑层*/
    private boolean camera_state;
    private boolean audio_state;
    private boolean touch_state;
    private boolean screen_state;
    private boolean scan_state;
    private boolean net_state;
    private boolean box_state;
    private boolean set_state;


    /**
     * 处理测试成功
     */
    private void doTestOK() {
        switch (nowTestId) {


            //摄像头测试
            case R.id.mgt_bt_ast_camera:
                test1TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                camera_state = true;
                break;

            //声音测试
            case R.id.mgt_bt_ast_audio:
                test2TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                audio_state = true;
                break;

            //触摸屏测试
            case R.id.mgt_bt_ast_touch:
                test3TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                touch_state = true;
                break;

            //显示屏测试
            case R.id.mgt_bt_ast_screen:
                test4TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                screen_state = true;
                break;

            //扫描枪测试
            case R.id.mgt_bt_ast_scan:
                test5TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                scan_state = true;
                break;

            //网络测试
            case R.id.mgt_bt_ast_net:
                test6TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                net_state = true;
                break;

            //箱体管理
            case R.id.mgt_bt_ast_box:
                test7TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                box_state = true;
                break;

            //高级配置
            case R.id.mgt_bt_ast_set:
                test8TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                set_state = true;
                break;

        }

        //判断是否完成测试
        isOver();
    }

    public void isOver() {
        int camera_num = 0;
        int audio_num = 0;
        int touch_num = 0;
        int screen_num = 0;
        int scan_num = 0;
        int net_num = 0;
        int box_num = 0;
        int set_num = 0;

        boolean flag = false;
        if (camera_state && audio_state && touch_state && screen_state && scan_state && net_state && box_state && set_state) {
            flag = true;
        } else {
            flag = false;
            overBT.setVisibility(View.INVISIBLE);
        }

        if (camera_state) {
            camera_num = 1;
        }
        if (audio_state) {
            audio_num = 1;
        }
        if (touch_state) {
            touch_num = 1;
        }
        if (screen_state) {
            screen_num = 1;
        }
        if (scan_state) {
            scan_num = 1;
        }
        if (net_state) {
            net_num = 1;
        }
        if (set_state) {
            set_num = 1;
        }

        notestTV.setText("" + (camera_num + audio_num + touch_num + screen_num + scan_num + net_num + box_num+set_num));

        //全部完成
        if (flag) {
            HardAnimUtils.shootOverBT(overBT);
        }
    }

}
