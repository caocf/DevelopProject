package com.ebox.mgt.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 扫描器ceshi
 */
public class ScanActivity extends CommonActivity {

    private EditText scanET;
    private Button clearBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }

    private void initViews() {
        initTitle();

        scanET = (EditText) this.findViewById(R.id.mgt_et_fst_scan);
        clearBT = (Button) this.findViewById(R.id.mgt_bt_fst_clean);

        scanET.requestFocus();
        clearBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanET.setText("");
                scanET.requestFocus();
            }
        });
    }

   /*界面层*/

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.scan_title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = "扫描器测试";
        data.tvVisibility = true;
        title.setData(data, this);
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
