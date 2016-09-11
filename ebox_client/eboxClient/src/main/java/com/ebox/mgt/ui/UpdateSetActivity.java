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
import com.ebox.mgt.ui.fragment.FragmentBasicSet;
import com.ebox.mgt.ui.fragment.FragmentExtendsSet;
import com.ebox.mgt.ui.fragment.FragmentInit;
import com.ebox.mgt.ui.fragment.FragmentUnionSet;
import com.ebox.mgt.ui.utils.HardAnimUtils;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 更新配置
 */
public class UpdateSetActivity extends CommonActivity implements View.OnClickListener, View.OnHoverListener {

    //界面属性
    private Button btBasic;
    private Button btExtends;
    private Button btUnion;
    private Button btInit;

    private TextView test1TV, test2TV, test3TV, test4TV;   //测试精灵

    private FragmentBasicSet fragmentBasicSet;
    private FragmentExtendsSet fragmentExtendsSet;
    private FragmentUnionSet fragmentUnionSet;
    private FragmentInit fragmentInit;

    private TextView tvFtitle;
    private TextView tvFdes;

    private Button overBT;
    private TextView notestTV;
    private Button singleBT;

    //其他属性
    private FragmentManager fm;
    private FragmentTransaction ts;

    private String TAG_BASIC = "basic_set";
    private String TAG_EXTENDS = "extends_set";
    private String TAG_UNION = "union_set";
    private String TAG_INIT = "fragmentInit";

    private int nowTestId = R.id.mgt_bt_aus_basic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_set);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }



    /*界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_update_set);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initViews() {
        initTitle();

        btBasic = (Button) this.findViewById(R.id.mgt_bt_aus_basic);
        btExtends = (Button) this.findViewById(R.id.mgt_bt_aus_extends);
        btUnion = (Button) this.findViewById(R.id.mgt_bt_aus_union);
        btInit = (Button) this.findViewById(R.id.mgt_bt_afi_init);

        test1TV = (TextView) this.findViewById(R.id.mgt_tv_aus_1);
        test2TV = (TextView) this.findViewById(R.id.mgt_tv_aus_2);
        test3TV = (TextView) this.findViewById(R.id.mgt_tv_aus_3);
        test4TV = (TextView) this.findViewById(R.id.mgt_tv_aus_4);

        test1TV.setOnClickListener(this);
        test2TV.setOnClickListener(this);
        test3TV.setOnClickListener(this);
        test4TV.setOnClickListener(this);


        tvFtitle = (TextView) this.findViewById(R.id.tv_fh_title);
        tvFdes = (TextView) this.findViewById(R.id.tv_fh_prompt);

        overBT = (Button) this.findViewById(R.id.mgt_bt_aus_over);
        overBT.setOnClickListener(this);
        notestTV = (TextView) this.findViewById(R.id.mgt_tv_aus_ok);
        singleBT = (Button) this.findViewById(R.id.mgt_bt_aus_ok);
        singleBT.setOnClickListener(this);

        btBasic.setOnClickListener(this);
        btExtends.setOnClickListener(this);
        btUnion.setOnClickListener(this);
        btInit.setOnClickListener(this);

        btBasic.setOnHoverListener(this);
        btExtends.setOnClickListener(this);
        btUnion.setOnClickListener(this);
        btInit.setOnHoverListener(this);

        fm = getFragmentManager();
        fragmentBasicSet = new FragmentBasicSet();
        showFragment(fragmentBasicSet, TAG_BASIC);
        tvFtitle.setText(R.string.mgt_us_baisc);
        tvFdes.setText(R.string.mgt_us_baisc_des);

        HardAnimUtils.resultTVAnim(test1TV, test2TV, test3TV, test4TV);
    }

    private void showFragment(Fragment fragment, String tag) {
        ts = fm.beginTransaction();
//        ts.addToBackStack(null);
        ts.replace(R.id.mgt_fl_aus_content, fragment, tag).commit();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //完成单次安装
            case R.id.mgt_bt_aus_ok:
                doTestOK();
                break;

            //完成配置更新
            case R.id.mgt_bt_aus_over:
                finish();
                break;

            //基本配置
            case R.id.mgt_bt_aus_basic:
                nowTestId = R.id.mgt_bt_aus_basic;
                refreshFGHeader(R.id.mgt_bt_aus_basic);
                fragmentBasicSet = (FragmentBasicSet) fm.findFragmentByTag(TAG_BASIC);
                if (fragmentBasicSet == null) {
                    fragmentBasicSet = new FragmentBasicSet();
                }
                showFragment(fragmentBasicSet, TAG_BASIC);
                break;

            //扩展配置
            case R.id.mgt_bt_aus_extends:
                nowTestId = R.id.mgt_bt_aus_extends;
                refreshFGHeader(R.id.mgt_bt_aus_extends);
                fragmentExtendsSet = (FragmentExtendsSet) fm.findFragmentByTag(TAG_EXTENDS);
                if (fragmentExtendsSet == null) {
                    fragmentExtendsSet = new FragmentExtendsSet();
                }
                showFragment(fragmentExtendsSet, TAG_EXTENDS);
                break;


            //银联测试
            case R.id.mgt_bt_aus_union:
                nowTestId = R.id.mgt_bt_aus_union;
                refreshFGHeader(R.id.mgt_bt_aus_union);
                fragmentUnionSet = (FragmentUnionSet) fm.findFragmentByTag(TAG_UNION);
                if (fragmentUnionSet == null) {
                    fragmentUnionSet = new FragmentUnionSet();
                }
                showFragment(fragmentUnionSet, TAG_UNION);
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


            //基本配置
            case R.id.mgt_tv_aus_1:
                nowTestId = R.id.mgt_bt_aus_basic;
                refreshFGHeader(R.id.mgt_bt_aus_basic);
                fragmentBasicSet = (FragmentBasicSet) fm.findFragmentByTag(TAG_BASIC);
                if (fragmentBasicSet == null) {
                    fragmentBasicSet = new FragmentBasicSet();
                }
                showFragment(fragmentBasicSet, TAG_BASIC);
                break;

            //扩展配置
            case R.id.mgt_tv_aus_2:
                nowTestId = R.id.mgt_bt_aus_extends;
                refreshFGHeader(R.id.mgt_bt_aus_extends);
                fragmentExtendsSet = (FragmentExtendsSet) fm.findFragmentByTag(TAG_EXTENDS);
                if (fragmentExtendsSet == null) {
                    fragmentExtendsSet = new FragmentExtendsSet();
                }
                showFragment(fragmentExtendsSet, TAG_EXTENDS);
                break;


            //银联测试
            case R.id.mgt_tv_aus_3:
                nowTestId = R.id.mgt_bt_aus_union;
                refreshFGHeader(R.id.mgt_bt_aus_union);
                fragmentUnionSet = (FragmentUnionSet) fm.findFragmentByTag(TAG_UNION);
                if (fragmentUnionSet == null) {
                    fragmentUnionSet = new FragmentUnionSet();
                }
                showFragment(fragmentUnionSet, TAG_UNION);
                break;
            //系统初始化
            case R.id.mgt_tv_aus_4:
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

            //基本配置
            case R.id.mgt_bt_aus_basic:
                tvFtitle.setText(R.string.mgt_us_baisc);
                tvFdes.setText(R.string.mgt_us_baisc_des);
                break;

            //扩展配置
            case R.id.mgt_bt_aus_extends:
                tvFtitle.setText(R.string.mgt_us_extends);
                tvFdes.setText(R.string.mgt_us_extends_des);
                break;

            //银联配置
            case R.id.mgt_bt_aus_union:
                tvFtitle.setText(R.string.mgt_us_union);
                tvFdes.setText(R.string.mgt_us_union_des);
                break;
            //系统初始化
            case R.id.mgt_bt_afi_init:
                tvFtitle.setText("4.系统初始化");
                tvFdes.setText(R.string.mgt_fi_init_des);
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

    private boolean basic_state;
    private boolean extends_state;
    private boolean union_state;
    private boolean init_state;

    /**
     * 处理测试成功
     */
    private void doTestOK() {
        switch (nowTestId) {
            //完成基本配置
            case R.id.mgt_bt_aus_basic:
                fragmentBasicSet.saveConfig();
                test1TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                basic_state = true;
                break;

            //扩展配置
            case R.id.mgt_bt_aus_extends:
                fragmentExtendsSet.saveConfig();
                test2TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                extends_state = true;
                break;

            //银联配置
            case R.id.mgt_bt_aus_union:
                fragmentUnionSet.saveConfig();
                test3TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                union_state = true;
                break;

            //系统初始化
            case R.id.mgt_bt_afi_init:
                test4TV.setBackgroundColor(getResources().getColor(R.color.pub_blue));
                init_state = true;
                break;
        }

        //判断是否完成测试
        isOver();
    }

    public void isOver() {
        int basic_num = 0;
        int extends_num = 0;
        int uniono_num = 0;
        int init_num=0;

        boolean flag = false;
        if (basic_state && extends_state && union_state&&init_state) {
            flag = true;
        } else {
            flag = false;
            overBT.setVisibility(View.INVISIBLE);
        }

        if (basic_state) {
            basic_num = 1;
        }
        if (extends_state) {
            extends_num = 1;
        }
        if (union_state) {
            uniono_num = 1;
        }
        if (init_state){
            init_num=1;
        }

        notestTV.setText("" + (basic_num + extends_num + uniono_num+init_num));

        //全部完成
        if (flag) {
            HardAnimUtils.shootOverBT(overBT);
        }
    }


}
