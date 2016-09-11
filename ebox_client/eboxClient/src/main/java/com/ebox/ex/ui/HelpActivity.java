package com.ebox.ex.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 帮助界面
 */
public class HelpActivity extends CommonActivity implements CompoundButton.OnCheckedChangeListener {

    private Title title;
    private Title.TitleData data;
    private TextView tv_title;
    private ImageView iv_title;

    private RadioButton rb_pickup, rb_deliver, rb_faq, rb_call;
    private RelativeLayout fl_content;

    private int mCurPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        MGViewUtil.scaleContentView(this, R.id.rootView);

        initTitle();
        initViews();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        rb_pickup = (RadioButton) findViewById(R.id.rb_pickup);
        rb_deliver = (RadioButton) findViewById(R.id.rb_deliver);
        rb_faq = (RadioButton) findViewById(R.id.rb_faq);
        rb_call = (RadioButton) findViewById(R.id.rb_call);

        rb_pickup.setOnCheckedChangeListener(this);
        rb_deliver.setOnCheckedChangeListener(this);
        rb_faq.setOnCheckedChangeListener(this);
        rb_call.setOnCheckedChangeListener(this);

        fl_content = (RelativeLayout) findViewById(R.id.fl_content);

        rb_pickup.setChecked(true);
        changePage(0);

    }

    //0 用户取件   1 快递员派件   2常见问题   3人工求助
    private void changePage(int page) {
        mCurPage = page;
        ImageView contentIV = new ImageView(HelpActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fl_content.removeAllViews();
        switch (mCurPage) {
            case 0:
                contentIV.setImageResource(R.drawable.ex_help_pickup_content);
                fl_content.addView(contentIV, params);
                break;

            case 1:
                contentIV.setImageResource(R.drawable.ex_help_deliver_content);
                fl_content.addView(contentIV, params);
                break;

            case 2:
                TextView tv = new TextView(HelpActivity.this);
                tv.setText("常见问题");
                fl_content.addView(tv, params);
                break;

            case 3:
                TextView tv2 = new TextView(HelpActivity.this);
                tv2.setText("人工求助");
                fl_content.addView(tv2, params);
                break;

            default:

                break;


        }
    }

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.ex_help_pickup);
        data.ivIcon = R.drawable.ex_help_pickup;
        data.tvVisibility = true;
        title.setData(data, this);
        tv_title = title.tv;
        iv_title = title.iv_title;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        String title = (String) compoundButton.getText();
        switch (compoundButton.getId()) {
            case R.id.rb_pickup:
                if (isChecked) {
                    changePage(0);
                    updateTitle(title, R.drawable.ex_help_pickup);
                }
                break;

            case R.id.rb_deliver:
                if (isChecked) {
                    changePage(1);
                    updateTitle(title, R.drawable.ex_help_deliver);
                }
                break;

            case R.id.rb_faq:
                if (isChecked) {
                    changePage(2);
                    updateTitle(title, R.drawable.ex_help_faq);
                }
                break;

            case R.id.rb_call:
                if (isChecked) {
                    changePage(3);
                    updateTitle(title, R.drawable.ex_help_call);
                }
                break;
        }
    }

    private void updateTitle(String title, int ivIcon) {
        if (title != null && tv_title != null) {
            tv_title.setText(title);
            iv_title.setImageResource(ivIcon);
        }
    }


}
