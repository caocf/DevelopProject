package com.ebox.mgt.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.R;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.fragment.FragmentAudioTest;
import com.ebox.mgt.ui.fragment.FragmentBasicSet;
import com.ebox.mgt.ui.fragment.FragmentBoxTest;
import com.ebox.mgt.ui.fragment.FragmentCameraTest;
import com.ebox.mgt.ui.fragment.FragmentInit;
import com.ebox.mgt.ui.fragment.FragmentScanTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentNetTest;
import com.ebox.mgt.ui.fragment.pollingfg.model.BoardModel;
import com.ebox.mgt.ui.fragment.pollingfg.model.BoardRequest;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingFileOp;
import com.ebox.mgt.ui.fragment.pollingfg.model.ProductModel;
import com.ebox.mgt.ui.fragment.pollingfg.model.ReqBoard;
import com.ebox.mgt.ui.utils.HardAnimUtils;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 首次安装
 */
public class FirstInstallActivity extends CommonActivity implements View.OnClickListener, View.OnHoverListener {
    //界面属性
    private Button btUpdateSet;
    private Button btBoxTest;
    private Button btAudioTest;
    private Button btCameraTest;
    private Button btScanTest;
    private Button btNetTest;
    private Button btInit;

    private TextView tvFtitle;
    private TextView tvFdes;

    private TextView test1TV, test2TV, test3TV, test4TV, test5TV, test6TV,test7TV;   //测试精灵

    private TextView notestTV;
    private Button overBT;
    private Button singleBT;

    //其他属性
    private FragmentManager fm;
    private FragmentTransaction ts;
    private String TAG_BASICSET = "fragmentBasicSet";
    private String TAG_BOXTEST = "box_test";
    private String TAG_AUDIOTEST = "audio_test";
    private String TAG_CAMERATEST = "camera_test";
    private String TAG_SCANTEST = "scan_test";
    private String TAG_NETTEST = "net_test";
    private String TAG_INIT="FragmentInit";

//    private FragmentUpdateSet fragmentUpdateSet;
    private FragmentBasicSet fragmentBasicSet;
    private FragmentBoxTest fragmentBoxTest;
    private FragmentAudioTest fragmentAudioTest;
    private FragmentCameraTest fragmentCameraTest;
    private FragmentScanTest fragmentScanTest;
    private FragmentNetTest fragmentNetTest;
    private FragmentInit fragmentInit;

    private int nowTestId = R.id.mgt_bt_afi_updateset;
    private boolean isOver; //判断是否完成首次安装

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:

                    BoardRequest boardRequest = new BoardRequest((ReqBoard) msg.obj, new ResponseEventHandler<BaseRsp>() {
                        @Override
                        public void onResponseSuccess(BaseRsp result) {
                            LogUtil.i("FirstInstallActivity:" + JsonSerializeUtil.bean2Json(result));
                        }

                        @Override
                        public void onResponseError(VolleyError error) {
                            LogUtil.i("FirstInstallActivity:" + error.getMessage());
                        }
                    });
                    executeRequest(boardRequest);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_install);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();

        uploadBorad();

    }

    private void uploadBorad() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BoardModel boardModel = PollingFileOp.getBoardFromJson();
                ProductModel productModel = PollingFileOp.getProductFromJson();

                ReqBoard req = new ReqBoard();
                req.setBoard(boardModel);
                req.setProduct(productModel);
                Message msg = Message.obtain();
                msg.what = 100;
                msg.obj = req;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /*界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_first_install);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initViews() {
        initTitle();
        btUpdateSet = (Button) this.findViewById(R.id.mgt_bt_afi_updateset);
        btBoxTest = (Button) this.findViewById(R.id.mgt_bt_afi_boxtest);
        btAudioTest = (Button) this.findViewById(R.id.mgt_bt_afi_audiotest);
        btCameraTest = (Button) this.findViewById(R.id.mgt_bt_afi_camera);
        btScanTest = (Button) this.findViewById(R.id.mgt_bt_afi_scan);
        btNetTest = (Button) this.findViewById(R.id.mgt_bt_afi_net);
        btInit= (Button) this.findViewById(R.id.mgt_bt_afi_init);
        overBT = (Button) this.findViewById(R.id.mgt_bt_afi_over);
        notestTV = (TextView) this.findViewById(R.id.mgt_tv_afi_ok);
        singleBT = (Button) this.findViewById(R.id.mgt_bt_afi_ok);

        test1TV = (TextView) this.findViewById(R.id.mgt_tv_afi_1);
        test2TV = (TextView) this.findViewById(R.id.mgt_tv_afi_2);
        test3TV = (TextView) this.findViewById(R.id.mgt_tv_afi_3);
        test4TV = (TextView) this.findViewById(R.id.mgt_tv_afi_4);
        test5TV = (TextView) this.findViewById(R.id.mgt_tv_afi_5);
        test6TV = (TextView) this.findViewById(R.id.mgt_tv_afi_6);
        test7TV = (TextView) this.findViewById(R.id.mgt_tv_afi_7);

        test1TV.setOnClickListener(this);
        test2TV.setOnClickListener(this);
        test3TV.setOnClickListener(this);
        test4TV.setOnClickListener(this);
        test5TV.setOnClickListener(this);
        test6TV.setOnClickListener(this);
        test7TV.setOnClickListener(this);


        singleBT.setOnClickListener(this);
        overBT.setOnClickListener(this);

        btUpdateSet.setOnClickListener(this);
        btBoxTest.setOnClickListener(this);
        btAudioTest.setOnClickListener(this);
        btCameraTest.setOnClickListener(this);
        btScanTest.setOnClickListener(this);
        btNetTest.setOnClickListener(this);
        btInit.setOnClickListener(this);


        btUpdateSet.setOnHoverListener(this);
        btBoxTest.setOnHoverListener(this);
        btAudioTest.setOnHoverListener(this);
        btCameraTest.setOnHoverListener(this);
        btScanTest.setOnHoverListener(this);
        btNetTest.setOnHoverListener(this);
        btInit.setOnHoverListener(this);

        tvFtitle = (TextView) this.findViewById(R.id.tv_fh_title);
        tvFdes = (TextView) this.findViewById(R.id.tv_fh_prompt);

        fm = getFragmentManager();

        fragmentBasicSet = new FragmentBasicSet();
        showFragment(fragmentBasicSet, TAG_BASICSET);
        tvFtitle.setText(R.string.mgt_fi_updateset);
        tvFdes.setText(R.string.mgt_fi_updateset_des);

        HardAnimUtils.resultTVAnim(test1TV, test2TV, test3TV, test4TV, test5TV, test6TV,test7TV);

    }

    private void showFragment(Fragment fragment, String tag) {
        ts = fm.beginTransaction();
//        ts.addToBackStack(null);
        ts.replace(R.id.mgt_fl_afi_content, fragment, tag).commit();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //完成单次安装
            case R.id.mgt_bt_afi_ok:
                doTestOK();
                break;

            //完成首次安装
            case R.id.mgt_bt_afi_over:
                //判断首次安装配置是否成功
                Intent intent = new Intent(FirstInstallActivity.this, SuperAdminV2Activity.class);
                if (isOver) {
                    intent.putExtra("isover", true);
                }
                setResult(RESULT_OK, intent);
                finish();
                break;


            //配置更新
            case R.id.mgt_bt_afi_updateset:
                nowTestId = R.id.mgt_bt_afi_updateset;
                refreshFGHeader(R.id.mgt_bt_afi_updateset);
                fragmentBasicSet = (FragmentBasicSet) fm.findFragmentByTag(TAG_BASICSET);
                if (fragmentBasicSet == null) {
                    fragmentBasicSet = new FragmentBasicSet();
                }
                showFragment(fragmentBasicSet, TAG_BASICSET);
                break;

            //箱门测试
            case R.id.mgt_bt_afi_boxtest:
                nowTestId = R.id.mgt_bt_afi_boxtest;
                refreshFGHeader(R.id.mgt_bt_afi_boxtest);
                fragmentBoxTest = (FragmentBoxTest) fm.findFragmentByTag(TAG_BOXTEST);
                if (fragmentBoxTest == null) {
                    fragmentBoxTest = new FragmentBoxTest();
                }
                showFragment(fragmentBoxTest, TAG_BOXTEST);
                break;

            //声音测试
            case R.id.mgt_bt_afi_audiotest:
                nowTestId = R.id.mgt_bt_afi_audiotest;
                refreshFGHeader(R.id.mgt_bt_afi_audiotest);
                fragmentAudioTest = (FragmentAudioTest) fm.findFragmentByTag(TAG_AUDIOTEST);
                if (fragmentAudioTest == null) {
                    fragmentAudioTest = new FragmentAudioTest();
                }
                showFragment(fragmentAudioTest, TAG_AUDIOTEST);
                break;

            //摄像头测试
            case R.id.mgt_bt_afi_camera:
                nowTestId = R.id.mgt_bt_afi_camera;
                refreshFGHeader(R.id.mgt_bt_afi_camera);
                fragmentCameraTest = (FragmentCameraTest) fm.findFragmentByTag(TAG_CAMERATEST);
                if (fragmentCameraTest == null) {
                    fragmentCameraTest = new FragmentCameraTest();
                }
                showFragment(fragmentCameraTest, TAG_CAMERATEST);
                break;

            //扫描枪测试
            case R.id.mgt_bt_afi_scan:
                nowTestId = R.id.mgt_bt_afi_scan;
                refreshFGHeader(R.id.mgt_bt_afi_scan);
                fragmentScanTest = (FragmentScanTest) fm.findFragmentByTag(TAG_SCANTEST);
                if (fragmentScanTest == null) {
                    fragmentScanTest = new FragmentScanTest();
                }
                showFragment(fragmentScanTest, TAG_SCANTEST);
                break;

            //网络测试
            case R.id.mgt_bt_afi_net:
                nowTestId = R.id.mgt_bt_afi_net;
                refreshFGHeader(R.id.mgt_bt_afi_net);
                fragmentNetTest = (FragmentNetTest) fm.findFragmentByTag(TAG_NETTEST);
                if (fragmentNetTest == null) {
                    fragmentNetTest = new FragmentNetTest();
                }
                showFragment(fragmentNetTest, TAG_NETTEST);
                break;

            //系统初始化
            case R.id.mgt_bt_afi_init:
                nowTestId = R.id.mgt_bt_afi_init;
                refreshFGHeader(R.id.mgt_bt_afi_init);
                fragmentInit = (FragmentInit) fm.findFragmentByTag(TAG_INIT);
                if (fragmentInit == null) {
                    fragmentInit = new FragmentInit();
                }
                showFragment(fragmentInit, TAG_INIT);
                break;


            case R.id.mgt_tv_afi_1:
                nowTestId = R.id.mgt_bt_afi_updateset;
                refreshFGHeader(R.id.mgt_bt_afi_updateset);
                fragmentBasicSet = (FragmentBasicSet) fm.findFragmentByTag(TAG_BASICSET);
                if (fragmentBasicSet == null) {
                    fragmentBasicSet = new FragmentBasicSet();
                }
                showFragment(fragmentBasicSet, TAG_BASICSET);
                break;
            case R.id.mgt_tv_afi_2:
                nowTestId = R.id.mgt_bt_afi_boxtest;
                refreshFGHeader(R.id.mgt_bt_afi_boxtest);
                fragmentBoxTest = (FragmentBoxTest) fm.findFragmentByTag(TAG_BOXTEST);
                if (fragmentBoxTest == null) {
                    fragmentBoxTest = new FragmentBoxTest();
                }
                showFragment(fragmentBoxTest, TAG_BOXTEST);
                break;
            case R.id.mgt_tv_afi_3:
                nowTestId = R.id.mgt_bt_afi_audiotest;
                refreshFGHeader(R.id.mgt_bt_afi_audiotest);
                fragmentAudioTest = (FragmentAudioTest) fm.findFragmentByTag(TAG_AUDIOTEST);
                if (fragmentAudioTest == null) {
                    fragmentAudioTest = new FragmentAudioTest();
                }
                showFragment(fragmentAudioTest, TAG_AUDIOTEST);
                break;
            case R.id.mgt_tv_afi_4:
                nowTestId = R.id.mgt_bt_afi_camera;
                refreshFGHeader(R.id.mgt_bt_afi_camera);
                fragmentCameraTest = (FragmentCameraTest) fm.findFragmentByTag(TAG_CAMERATEST);
                if (fragmentCameraTest == null) {
                    fragmentCameraTest = new FragmentCameraTest();
                }
                showFragment(fragmentCameraTest, TAG_CAMERATEST);
                break;
            case R.id.mgt_tv_afi_5:
                nowTestId = R.id.mgt_bt_afi_scan;
                refreshFGHeader(R.id.mgt_bt_afi_scan);
                fragmentScanTest = (FragmentScanTest) fm.findFragmentByTag(TAG_SCANTEST);
                if (fragmentScanTest == null) {
                    fragmentScanTest = new FragmentScanTest();
                }
                showFragment(fragmentScanTest, TAG_SCANTEST);
                break;
            case R.id.mgt_tv_afi_6:
                nowTestId = R.id.mgt_bt_afi_net;
                refreshFGHeader(R.id.mgt_bt_afi_net);
                fragmentNetTest = (FragmentNetTest) fm.findFragmentByTag(TAG_NETTEST);
                if (fragmentNetTest == null) {
                    fragmentNetTest = new FragmentNetTest();
                }
                showFragment(fragmentNetTest, TAG_NETTEST);
                break;

            case R.id.mgt_tv_afi_7:
                nowTestId = R.id.mgt_bt_afi_init;
                refreshFGHeader(R.id.mgt_bt_afi_init);
                fragmentInit = (FragmentInit) fm.findFragmentByTag(TAG_INIT);
                if (fragmentInit == null) {
                    fragmentInit = new FragmentInit();
                }
                showFragment(fragmentInit, TAG_INIT);
                break;

        }

    }

    /**
     * 刷新头部
     */
    private void refreshFGHeader(int id) {
        switch (id) {
            //配置更新
            case R.id.mgt_bt_afi_updateset:
                tvFtitle.setText(R.string.mgt_fi_updateset);
                tvFdes.setText(R.string.mgt_fi_updateset_des);
                break;

            //箱门测试
            case R.id.mgt_bt_afi_boxtest:
                tvFtitle.setText(R.string.mgt_fi_boxtest);
                tvFdes.setText(R.string.mgt_fi_boxtest_des);
                break;

            //声音测试
            case R.id.mgt_bt_afi_audiotest:
                tvFtitle.setText(R.string.mgt_fi_audiotest);
                tvFdes.setText(R.string.mgt_fi_audiotest_des);
                break;

            //摄像头测试
            case R.id.mgt_bt_afi_camera:
                tvFtitle.setText(R.string.mgt_fi_camera);
                tvFdes.setText(R.string.mgt_fi_camera_des);
                break;

            //扫描枪测试
            case R.id.mgt_bt_afi_scan:
                tvFtitle.setText(R.string.mgt_fi_scan);
                tvFdes.setText(R.string.mgt_fi_scan_des);
                break;

            //网络测试
            case R.id.mgt_bt_afi_net:
                tvFtitle.setText(R.string.mgt_fi_net);
                tvFdes.setText(R.string.mgt_fi_net_des);
                break;
            //系统初始化
            case R.id.mgt_bt_afi_init:
                tvFtitle.setText(R.string.mgt_fi_init);
                tvFdes.setText(R.string.mgt_fi_init_des);
                break;
        }
        HardAnimUtils.headerAnim(tvFtitle, tvFdes);
    }


    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        isOver = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
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

    /*业务层*/

    private boolean update_state;
    private boolean boxtest_state;
    private boolean audiotest_state;
    private boolean cameratest_state;
    private boolean scan_state;
    private boolean net_state;
    private boolean init_state;

    /**
     * 处理测试成功
     */
    private void doTestOK() {
        switch (nowTestId) {
            //完成首次配置
            case R.id.mgt_bt_afi_updateset:
                Log.i("tag", "完成首次安装");
                fragmentBasicSet.saveConfig();
                test1TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                update_state = true;
                break;

            //箱门测试
            case R.id.mgt_bt_afi_boxtest:
                Toast.makeText(this, "检测箱门是否打开", Toast.LENGTH_SHORT).show();
                test2TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                boxtest_state = true;
                break;
            case R.id.mgt_bt_afi_audiotest:
                test3TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                audiotest_state = true;
                break;
            case R.id.mgt_bt_afi_camera:
                test4TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                cameratest_state = true;
                break;
            case R.id.mgt_bt_afi_scan:
                test5TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                scan_state = true;
                break;
            case R.id.mgt_bt_afi_net:
                test6TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                net_state = true;
                break;
            case R.id.mgt_bt_afi_init:
                test7TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                init_state = true;
                break;
        }

        //判断是否完成测试
        isOver();
    }

    public void isOver() {
        int update_num = 0;
        int box_num = 0;
        int audio_num = 0;
        int camera_num = 0;
        int scan_num = 0;
        int net_num = 0;
        int init_num=0;

        boolean flag = false;
        if (update_state && boxtest_state && audiotest_state && cameratest_state && scan_state && net_state&&init_state) {
            flag = true;
        } else {
            flag = false;
            overBT.setVisibility(View.INVISIBLE);
        }

        if (update_state) {
            update_num = 1;
        }
        if (boxtest_state) {
            box_num = 1;
        }
        if (audiotest_state) {
            audio_num = 1;
        }
        if (cameratest_state) {
            camera_num = 1;
        }
        if (scan_state) {
            scan_num = 1;
        }
        if (net_state) {
            net_num = 1;
        }
        if (init_state){
            init_num=1;
        }

        notestTV.setText("" + (update_num + box_num + audio_num + camera_num + scan_num + net_num+init_num));

        //全部完成
        if (flag) {
            isOver = true;
            HardAnimUtils.shootOverBT(overBT);
        }
    }

    /**
     //     * 处理测试失败
     //     */
//    private void doTestError() {
//        switch (nowTestId) {
//            case R.id.mgt_bt_afi_updateset:
//                test1TV.setBackgroundColor(getResources().getColor(R.color.pub_red));
//                update_state = false;
//                break;
//            case R.id.mgt_bt_afi_boxtest:
//                test2TV.setBackgroundColor(getResources().getColor(R.color.pub_red));
//                boxtest_state = false;
//                break;
//            case R.id.mgt_bt_afi_audiotest:
//                test3TV.setBackgroundColor(getResources().getColor(R.color.pub_red));
//                audiotest_state = false;
//                break;
//            case R.id.mgt_bt_afi_camera:
//                test4TV.setBackgroundColor(getResources().getColor(R.color.pub_red));
//                cameratest_state = false;
//                break;
//            case R.id.mgt_bt_afi_scan:
//                test4TV.setBackgroundColor(getResources().getColor(R.color.red));
//                scan_state = false;
//                break;
//            case R.id.mgt_bt_afi_net:
//                test4TV.setBackgroundColor(getResources().getColor(R.color.red));
//                net_state = false;
//                break;
//        }
//
//        //判断是否完成测试
//        isOver();
//    }

}
