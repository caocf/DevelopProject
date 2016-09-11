package com.ebox.mgt.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.R;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentBoxLock;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentCleanBox;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentElectri;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentMonitor;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentNetTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentPolOther;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentScreenTest;
import com.ebox.mgt.ui.fragment.pollingfg.FragmentTouchTest;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingFileOp;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingRequest;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;
import com.ebox.mgt.ui.fragment.pollingfg.model.ReqPolling;
import com.ebox.mgt.ui.utils.HardAnimUtils;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.google.gson.Gson;

/**
 * 巡检
 * <p>
 * 功能：运维人员进行巡检工作
 * 逻辑：当返回的时候，对数据进行保存，并上传终端号，巡检结果（1为巡检全部完成，0为未完成全部巡检即退出），巡检时间
 */
public class PollingActivity extends CommonActivity implements View.OnClickListener, View.OnHoverListener {

    //界面属性
    private Button btClean;
    private Button btScreen;
    private Button btTouch;
    private Button btElectric;
    private Button btMonitor;
    private Button btNet;
    private Button btBoxLock;
    private Button btOther;
    private Button btResult;

    private TextView test1TV, test2TV, test3TV, test4TV, test5TV, test6TV, test7TV, test8TV, test9TV, test10TV;   //测试精灵

    private TextView tvFtitle;
    private TextView tvFdes;

    private TextView notestTV;
    private Button overBT;
    private Button singleBT;


    //其他属性
    private FragmentManager fm;
    private FragmentTransaction ts;
    private String TAG_CLEAN = "fCleanBox";
    private String TAG_SCREEN = "fScreenTest";
    private String TAG_TOUCH = "fTouchTest";
    private String TAG_ELECTRIC = "fElectri";
    private String TAG_MONITOR = "fMonitor";
    private String TAG_NETTEST = "fNetTest";
    private String TAG_BOXLOCK = "fBoxLock";
    private String TAG_POLOTHER = "fPolOther";

    private FragmentCleanBox fCleanBox;
    private FragmentScreenTest fScreenTest;
    private FragmentTouchTest fTouchTest;
    private FragmentElectri fElectri;
    private FragmentMonitor fMonitor;
    private FragmentNetTest fNetTest;
    private FragmentBoxLock fBoxLock;
    private FragmentPolOther fPolOther;

    private int nowTestId = R.id.mgt_bt_ap_clean;
    public static String POLLING_SP = "polling_sp";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static String ISUNDO = "isundo";

    private static String TAG = "PollingActivity+";
    private ReqPolling req;

    private String superName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();

        sp = getSharedPreferences(POLLING_SP, MODE_PRIVATE);
        editor = sp.edit();

        initSpirit();


        PollingStore.getSP();


    }



    /*界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        title.back.setOnClickListener(this);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_polling);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initViews() {
        initTitle();

        btClean = (Button) this.findViewById(R.id.mgt_bt_ap_clean);
        btScreen = (Button) this.findViewById(R.id.mgt_bt_ap_screen);
        btTouch = (Button) this.findViewById(R.id.mgt_bt_ap_touch);
        btElectric = (Button) this.findViewById(R.id.mgt_bt_ap_electric);
        btMonitor = (Button) this.findViewById(R.id.mgt_bt_ap_monitor);
        btNet = (Button) this.findViewById(R.id.mgt_bt_ap_3g);
        btBoxLock = (Button) this.findViewById(R.id.mgt_bt_ap_boxlock);
        btOther = (Button) this.findViewById(R.id.mgt_bt_ap_other);

        overBT = (Button) this.findViewById(R.id.mgt_bt_ap_over);
        notestTV = (TextView) this.findViewById(R.id.mgt_tv_ap_ok);
        singleBT = (Button) this.findViewById(R.id.mgt_bt_ap_ok);

        test1TV = (TextView) this.findViewById(R.id.mgt_tv_ap_1);
        test2TV = (TextView) this.findViewById(R.id.mgt_tv_ap_2);
        test3TV = (TextView) this.findViewById(R.id.mgt_tv_ap_3);
        test4TV = (TextView) this.findViewById(R.id.mgt_tv_ap_4);
        test5TV = (TextView) this.findViewById(R.id.mgt_tv_ap_5);
        test6TV = (TextView) this.findViewById(R.id.mgt_tv_ap_6);
        test7TV = (TextView) this.findViewById(R.id.mgt_tv_ap_7);
        test8TV = (TextView) this.findViewById(R.id.mgt_tv_ap_8);

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

        singleBT.setOnClickListener(this);
        overBT.setOnClickListener(this);


        btClean.setOnClickListener(this);
        btScreen.setOnClickListener(this);
        btTouch.setOnClickListener(this);
        btElectric.setOnClickListener(this);
        btMonitor.setOnClickListener(this);
        btNet.setOnClickListener(this);
        btBoxLock.setOnClickListener(this);
        btOther.setOnClickListener(this);

        btClean.setOnHoverListener(this);
        btScreen.setOnHoverListener(this);
        btTouch.setOnHoverListener(this);
        btElectric.setOnHoverListener(this);
        btMonitor.setOnHoverListener(this);
        btNet.setOnHoverListener(this);
        btBoxLock.setOnHoverListener(this);
        btOther.setOnHoverListener(this);

        fm = getFragmentManager();


        req = PollingFileOp.getPollingTotalFromJson("polling.json");

        if (req==null){
            req = new ReqPolling();
        }


        if (superName!=null){
            req.setSuper_name(superName);
        }

        fCleanBox = new FragmentCleanBox();
        fCleanBox.reqPolling=req;
        showFragment(fCleanBox, TAG_CLEAN);
        tvFtitle.setText(R.string.mgt_p_clean);
        tvFdes.setText(R.string.mgt_p_clean_des);
        HardAnimUtils.resultTVAnim(test1TV, test2TV, test3TV, test4TV, test5TV, test6TV, test7TV, test8TV);
    }

    private void showFragment(Fragment fragment, String tag) {
        ts = fm.beginTransaction();
//        ts.addToBackStack(null);
        ts.replace(R.id.mgt_fl_ap_content, fragment, tag).commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //退出运维首页按钮的监听
            case R.id.bt_back:
                savePollingJson(req);
                finish();
                break;

            //完成单次安装
            case R.id.mgt_bt_ap_ok:
                doTestOK();
                break;

            //完成巡检
            case R.id.mgt_bt_ap_over:
                //巡检完成
//                doPollingOver(POLLING_OVER);
                finish();
                break;


            //清洁柜体
            case R.id.mgt_bt_ap_clean:
                nowTestId = R.id.mgt_bt_ap_clean;
                refreshFGHeader(R.id.mgt_bt_ap_clean);
                fCleanBox = (FragmentCleanBox) fm.findFragmentByTag(TAG_CLEAN);
                if (fCleanBox == null) {
                    fCleanBox = new FragmentCleanBox();
                    fCleanBox.reqPolling = req;
                }
                showFragment(fCleanBox, TAG_CLEAN);
                break;

            //显示屏
            case R.id.mgt_bt_ap_screen:
                nowTestId = R.id.mgt_bt_ap_screen;
                refreshFGHeader(R.id.mgt_bt_ap_screen);
                fScreenTest = (FragmentScreenTest) fm.findFragmentByTag(TAG_SCREEN);
                if (fScreenTest == null) {
                    fScreenTest = new FragmentScreenTest();
                    fScreenTest.reqPolling = req;
                }
                showFragment(fScreenTest, TAG_SCREEN);
                break;

            //触摸屏
            case R.id.mgt_bt_ap_touch:
                nowTestId = R.id.mgt_bt_ap_touch;
                refreshFGHeader(R.id.mgt_bt_ap_touch);
                fTouchTest = (FragmentTouchTest) fm.findFragmentByTag(TAG_TOUCH);
                if (fTouchTest == null) {
                    fTouchTest = new FragmentTouchTest();
                    fTouchTest.reqPolling = req;
                }
                showFragment(fTouchTest, TAG_TOUCH);
                break;

            //供电系统
            case R.id.mgt_bt_ap_electric:
                nowTestId = R.id.mgt_bt_ap_electric;
                refreshFGHeader(R.id.mgt_bt_ap_electric);
                fElectri = (FragmentElectri) fm.findFragmentByTag(TAG_ELECTRIC);
                if (fElectri == null) {
                    fElectri = new FragmentElectri();
                    fElectri.reqPolling = req;
                }
                showFragment(fElectri, TAG_ELECTRIC);
                break;

            //监控
            case R.id.mgt_bt_ap_monitor:
                nowTestId = R.id.mgt_bt_ap_monitor;
                refreshFGHeader(R.id.mgt_bt_ap_monitor);
                fMonitor = (FragmentMonitor) fm.findFragmentByTag(TAG_MONITOR);
                if (fMonitor == null) {
                    fMonitor = new FragmentMonitor();
                    fMonitor.reqPolling = req;
                }
                showFragment(fMonitor, TAG_MONITOR);
                break;

            //3G
            case R.id.mgt_bt_ap_3g:
                nowTestId = R.id.mgt_bt_ap_3g;
                refreshFGHeader(R.id.mgt_bt_ap_3g);
                fNetTest = new FragmentNetTest();
                fNetTest.reqPolling = req;
                Bundle bundle = new Bundle();
                bundle.putInt("arg", 100);
                fNetTest.setArguments(bundle);

                showFragment(fNetTest, TAG_NETTEST);
                break;

            //柜体和锁
            case R.id.mgt_bt_ap_boxlock:
                nowTestId = R.id.mgt_bt_ap_boxlock;
                refreshFGHeader(R.id.mgt_bt_ap_boxlock);
                fBoxLock = (FragmentBoxLock) fm.findFragmentByTag(TAG_BOXLOCK);
                if (fBoxLock == null) {
                    fBoxLock = new FragmentBoxLock();
                    fBoxLock.reqPolling = req;
                }
                showFragment(fBoxLock, TAG_BOXLOCK);
                break;

            //其他
            case R.id.mgt_bt_ap_other:
                nowTestId = R.id.mgt_bt_ap_other;
                refreshFGHeader(R.id.mgt_bt_ap_other);
                fPolOther = (FragmentPolOther) fm.findFragmentByTag(TAG_POLOTHER);
                if (fPolOther == null) {
                    fPolOther = new FragmentPolOther();
                    fPolOther.reqPolling = req;
                }
                showFragment(fPolOther, TAG_POLOTHER);
                break;


            //清洁柜体
            case R.id.mgt_tv_ap_1:
                nowTestId = R.id.mgt_bt_ap_clean;
                refreshFGHeader(R.id.mgt_bt_ap_clean);
                fCleanBox = (FragmentCleanBox) fm.findFragmentByTag(TAG_CLEAN);
                if (fCleanBox == null) {
                    fCleanBox = new FragmentCleanBox();
                    fCleanBox.reqPolling = req;
                }
                showFragment(fCleanBox, TAG_CLEAN);
                break;

            //显示屏
            case R.id.mgt_tv_ap_2:
                nowTestId = R.id.mgt_bt_ap_screen;
                refreshFGHeader(R.id.mgt_bt_ap_screen);
                fScreenTest = (FragmentScreenTest) fm.findFragmentByTag(TAG_SCREEN);
                if (fScreenTest == null) {
                    fScreenTest = new FragmentScreenTest();
                    fScreenTest.reqPolling = req;
                }
                showFragment(fScreenTest, TAG_SCREEN);
                break;

            //触摸屏
            case R.id.mgt_tv_ap_3:
                nowTestId = R.id.mgt_bt_ap_touch;
                refreshFGHeader(R.id.mgt_bt_ap_touch);
                fTouchTest = (FragmentTouchTest) fm.findFragmentByTag(TAG_TOUCH);
                if (fTouchTest == null) {
                    fTouchTest = new FragmentTouchTest();
                    fTouchTest.reqPolling = req;
                }
                showFragment(fTouchTest, TAG_TOUCH);
                break;

            //供电系统
            case R.id.mgt_tv_ap_4:
                nowTestId = R.id.mgt_bt_ap_electric;
                refreshFGHeader(R.id.mgt_bt_ap_electric);
                fElectri = (FragmentElectri) fm.findFragmentByTag(TAG_ELECTRIC);
                if (fElectri == null) {
                    fElectri = new FragmentElectri();
                    fElectri.reqPolling = req;
                }
                showFragment(fElectri, TAG_ELECTRIC);
                break;

            //监控
            case R.id.mgt_tv_ap_5:
                nowTestId = R.id.mgt_bt_ap_monitor;
                refreshFGHeader(R.id.mgt_bt_ap_monitor);
                fMonitor = (FragmentMonitor) fm.findFragmentByTag(TAG_MONITOR);
                if (fMonitor == null) {
                    fMonitor = new FragmentMonitor();
                    fMonitor.reqPolling = req;
                }
                showFragment(fMonitor, TAG_MONITOR);
                break;

            //3G
            case R.id.mgt_tv_ap_6:
                nowTestId = R.id.mgt_bt_ap_3g;
                refreshFGHeader(R.id.mgt_bt_ap_3g);
                fNetTest = new FragmentNetTest();
                fNetTest.reqPolling = req;
                Bundle bundle2 = new Bundle();
                bundle2.putInt("arg", 100);
                fNetTest.setArguments(bundle2);

                showFragment(fNetTest, TAG_NETTEST);
                break;

            //柜体和锁
            case R.id.mgt_tv_ap_7:
                nowTestId = R.id.mgt_bt_ap_boxlock;
                refreshFGHeader(R.id.mgt_bt_ap_boxlock);
                fBoxLock = (FragmentBoxLock) fm.findFragmentByTag(TAG_BOXLOCK);
                if (fBoxLock == null) {
                    fBoxLock = new FragmentBoxLock();
                    fBoxLock.reqPolling = req;
                }
                showFragment(fBoxLock, TAG_BOXLOCK);
                break;

            //其他
            case R.id.mgt_tv_ap_8:
                nowTestId = R.id.mgt_bt_ap_other;
                refreshFGHeader(R.id.mgt_bt_ap_other);
                fPolOther = (FragmentPolOther) fm.findFragmentByTag(TAG_POLOTHER);
                if (fPolOther == null) {
                    fPolOther = new FragmentPolOther();
                    fPolOther.reqPolling = req;
                }
                showFragment(fPolOther, TAG_POLOTHER);
                break;

        }

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


    /**
     * 刷新头部
     */
    private void refreshFGHeader(int id) {
        switch (id) {
            //清洁柜体
            case R.id.mgt_bt_ap_clean:
                tvFtitle.setText(R.string.mgt_p_clean);
                tvFdes.setText(R.string.mgt_p_clean_des);
                break;
            //显示屏
            case R.id.mgt_bt_ap_screen:
                tvFtitle.setText(R.string.mgt_p_screen);
                tvFdes.setText(R.string.mgt_p_screen_des);
                break;

            //触摸屏
            case R.id.mgt_bt_ap_touch:
                tvFtitle.setText(R.string.mgt_p_touch);
                tvFdes.setText(R.string.mgt_p_touch_des);
                break;

            //供电系统
            case R.id.mgt_bt_ap_electric:
                tvFtitle.setText(R.string.mgt_p_electric);
                tvFdes.setText(R.string.mgt_p_electric_des);
                break;

            //监控
            case R.id.mgt_bt_ap_monitor:
                tvFtitle.setText(R.string.mgt_p_monitor);
                tvFdes.setText(R.string.mgt_p_monitor_des);
                break;

            //3G
            case R.id.mgt_bt_ap_3g:
                tvFtitle.setText(R.string.mgt_p_3g);
                tvFdes.setText(R.string.mgt_p_3g_des);
                break;

            //柜体和锁
            case R.id.mgt_bt_ap_boxlock:
                tvFtitle.setText(R.string.mgt_p_boxlock);
                tvFdes.setText(R.string.mgt_p_boxlock_des);
                break;

            //其他
            case R.id.mgt_bt_ap_other:
                tvFtitle.setText(R.string.mgt_p_other);
                tvFdes.setText(R.string.mgt_p_other_des);
                break;

        }
        HardAnimUtils.headerAnim(tvFtitle, tvFdes);
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

    /*业务层*/
    private boolean clean_state;
    private boolean screen_state;
    private boolean touch_state;
    private boolean elec_state;
    private boolean monitor_state;
    private boolean net_state;
    private boolean boxlock_state;
    private boolean other_state;


    /**
     * 清除巡检记录
     */
    private void cleanPollingSP() {
        editor.putBoolean(TAG_CLEAN, false);
        editor.putBoolean(TAG_SCREEN, false);
        editor.putBoolean(TAG_TOUCH, false);
        editor.putBoolean(TAG_ELECTRIC, false);
        editor.putBoolean(TAG_MONITOR, false);
        editor.putBoolean(TAG_NETTEST, false);
        editor.putBoolean(TAG_BOXLOCK, false);
        editor.putBoolean(TAG_POLOTHER, false);
        editor.commit();
    }

    /**
     * 初始化测试精灵
     */

    private void initSpirit() {

        clean_state = sp.getBoolean(TAG_CLEAN, false);
        screen_state = sp.getBoolean(TAG_SCREEN, false);
        touch_state = sp.getBoolean(TAG_TOUCH, false);
        elec_state = sp.getBoolean(TAG_ELECTRIC, false);
        monitor_state = sp.getBoolean(TAG_MONITOR, false);
        net_state = sp.getBoolean(TAG_NETTEST, false);
        boxlock_state = sp.getBoolean(TAG_BOXLOCK, false);
        other_state = sp.getBoolean(TAG_POLOTHER, false);

        //判断是否完成测试
        isOver();
    }


    /**
     * 处理测试成功
     */
    private void doTestOK() {
        switch (nowTestId) {
            //完成首次配置
            //清洁柜体
            case R.id.mgt_bt_ap_clean:
                test1TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                clean_state = true;
                break;

            //显示屏
            case R.id.mgt_bt_ap_screen:
                test2TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                screen_state = true;
                break;
            //触摸屏
            case R.id.mgt_bt_ap_touch:
                test3TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                touch_state = true;
                break;
            //供电系统
            case R.id.mgt_bt_ap_electric:
                test4TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                elec_state = true;
                break;
            //监控
            case R.id.mgt_bt_ap_monitor:
                test5TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                monitor_state = true;
                break;
            //3G
            case R.id.mgt_bt_ap_3g:
                test6TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                net_state = true;
                break;
            //柜体和锁
            case R.id.mgt_bt_ap_boxlock:
                test7TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                boxlock_state = true;
                break;

            //其他
            case R.id.mgt_bt_ap_other:
                test8TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                other_state = true;
                break;

        }

        //判断是否完成测试
        isOver();
    }

    private boolean flag;   //全部完成巡检判断

    public void isOver() {
        int clean_num = 0;
        int screen_num = 0;
        int touch_num = 0;
        int elec_num = 0;
        int monitor_num = 0;
        int net_num = 0;
        int boxlock_num = 0;
        int other_num = 0;


        if (clean_state && screen_state && touch_state && elec_state && monitor_state && net_state && boxlock_state && other_state) {
            flag = true;
            overBT.setVisibility(View.INVISIBLE);
        } else {
            flag = false;
        }

        if (clean_state) {
            editor.putBoolean(TAG_CLEAN, true);
            test1TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            clean_num = 1;
        }
        if (screen_state) {
            editor.putBoolean(TAG_SCREEN, true);
            test2TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            screen_num = 1;
        }
        if (touch_state) {
            editor.putBoolean(TAG_TOUCH, true);
            test3TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            touch_num = 1;
        }
        if (elec_state) {
            editor.putBoolean(TAG_ELECTRIC, true);
            test4TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            elec_num = 1;
        }
        if (monitor_state) {
            editor.putBoolean(TAG_MONITOR, true);
            test5TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            monitor_num = 1;
        }
        if (net_state) {
            editor.putBoolean(TAG_NETTEST, true);
            test6TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            net_num = 1;
        }
        if (boxlock_state) {
            editor.putBoolean(TAG_BOXLOCK, true);
            test7TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            boxlock_num = 1;
        }
        if (other_state) {
            editor.putBoolean(TAG_POLOTHER, true);
            test8TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
            other_num = 1;
        }
        editor.commit();
        int total = clean_num + screen_num + touch_num + elec_num + monitor_num + net_num + boxlock_num + other_num;
        LogUtil.i("Polling" + total);
        notestTV.setText("" + total);
        //全部完成
        if (flag) {
            HardAnimUtils.shootOverBT(overBT);
            cleanPollingSP();
            //将巡检信息上传到服务器
            ReqPolling req=PollingStore.getPollBean();
            LogUtil.i("PollingActivity:" + JsonSerializeUtil.bean2Json(req));
            PollingRequest pollingRequest=new PollingRequest(req, new ResponseEventHandler<BaseRsp>() {
                @Override
                public void onResponseSuccess(BaseRsp result) {
                    LogUtil.i("PollingActivity:"+JsonSerializeUtil.bean2Json(result));
                }
                @Override
                public void onResponseError(VolleyError error) {
                    LogUtil.i("PollingActivity:"+error.getMessage());
                }
            });
            executeRequest(pollingRequest);
            //将本地sp保存的文件清除
            PollingStore.clear();
        }

    }


    //巡检结束就保存巡检结果到本地数据库中
    private void savePollingJson(ReqPolling mPollingModel) {
        Gson mGson = new Gson();
        String json = mGson.toJson(mPollingModel);
        LogUtil.i("Polling:开始进行数据保存" + json);
        PollingFileOp.writeJsonToFile(json, "polling.json");
    }

}
