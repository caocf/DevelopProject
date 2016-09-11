package com.ebox.mgt.ui;

import android.os.Bundle;
import android.view.View;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 日志导出
 */
public class ExportLogActivity extends CommonActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_log);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }

    /*界面层*/
    private Title title;
    private Title.TitleData data;
    private void initTitle() {
        title=(Title) findViewById(R.id.title);
        data=title.new TitleData();
        data.backVisibility=1;
        data.tvContent=getResources().getString(R.string.mgt_export_log);
        data.tvVisibility=true;
        title.setData(data, this);
    }

    private void initViews() {
        initTitle();
    }


    @Override
    public void onClick(View view) {

    }

    /*业务层*/
}
