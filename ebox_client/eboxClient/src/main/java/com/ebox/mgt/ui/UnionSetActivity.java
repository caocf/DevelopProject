package com.ebox.mgt.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.utils.UmscfgTools;
import com.ebox.mgt.ui.utils.ini.IniOper;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;

import org.ini4j.Ini;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 银联链接配置
 */
public class UnionSetActivity extends CommonActivity {


    private EditText tradCardET; //传统银行卡
    private EditText fullNameET;    //全名付总部
    private EditText cwsET; //江宁水煤气

    private static final String filePath = UmscfgTools.getSdcardDir() + "/umsips/etc/pos.ini";
    private static final String LOGTAG = "TransBasicActivity";
    private static final String CPU = "CUP";
    private static final String PUBLOCAL = "PUBJN";
    private static final String PUBCENT = "PUBCENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgt_activity_union_set);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }

    private void initViews() {
        initTitle();

        tradCardET = (EditText) this.findViewById(R.id.mgt_et_maus_t1);
        fullNameET = (EditText) this.findViewById(R.id.mgt_et_maus_t2);
        cwsET = (EditText) this.findViewById(R.id.mgt_et_maus_t3);

        initData();
    }

   /*界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.union_title);
        data = title.new TitleData();
        data.backVisibility = 2;
        data.tvContent = "银联链接配置";
        data.tvVisibility = true;
        title.setData(data, this);
        title.setZCTitleListner(new Title.ZCTitleListener() {
            @Override
            public void onOperateBtnClick(int index) {
                LogUtil.i("UnionSetActivity++++"+index);
                if (index==1){
                    //左侧点击返回
                    saveConfigIniData();
                    finish();
                }
            }
        });


    }

    private void initData() {
        IniOper ini = new IniOper();
        Ini pos1 = ini.getIni();
        try {
            FileInputStream in = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(in, "utf-8");
            pos1.load(reader);

            // pos.load(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //银行卡
        Ini.Section pos = pos1.get(CPU);
        tradCardET.setText(pos.get("TID"));

        //全名付总部
        Ini.Section pos2 = pos1.get(PUBCENT);
        fullNameET.setText(pos2.get("TID"));


        //江宁水煤
        Ini.Section pos3 = pos1.get(PUBLOCAL);
        cwsET.setText(pos3.get("TID"));

    }

    /**
     * 存储配置文件
     */
    private void saveConfigIniData() {
        IniOper ini = new IniOper();
        Ini pos = ini.getIni();
        try {
            FileInputStream in = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(in, "utf-8");
            pos.load(reader);

            // pos.load(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        pos.put(CPU, "TID", tradCardET.getText().toString());
        pos.put(PUBCENT, "TID", fullNameET.getText().toString());
        pos.put(PUBLOCAL,"TID", cwsET.getText().toString());


        try {

            FileOutputStream out = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
            pos.store(writer);

            //pos.save(filePath);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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


}
