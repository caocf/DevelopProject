package com.moge.gege.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.data.dao.PushMsgDAO;

public class ChatSettingActivity extends BaseActivity implements
        OnClickListener, OnCheckedChangeListener
{
    private Context mContext;
    private CheckBox mSwitchCheckBox;
    private RelativeLayout mClearRecordLayout;
    private RelativeLayout mComplainLayout;

    private String mTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatsetting);

        mTag = getIntent().getStringExtra("tag");

        mContext = ChatSettingActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.chat_setting);

        mSwitchCheckBox = (CheckBox) this
                .findViewById(R.id.settingSwitchCheckBox);
        mSwitchCheckBox.setChecked(PushMsgDAO.instance().isNotify(
                AppApplication.getLoginId(), mTag));
        mSwitchCheckBox.setOnCheckedChangeListener(this);
        mClearRecordLayout = (RelativeLayout) this
                .findViewById(R.id.clearRecordLayout);
        mClearRecordLayout.setOnClickListener(this);
        mComplainLayout = (RelativeLayout) this
                .findViewById(R.id.complainLayout);
        mComplainLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.clearRecordLayout:
                break;
            case R.id.complainLayout:
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        PushMsgDAO.instance().setNotifyValue(AppApplication.getLoginId(), mTag,
                isChecked);
    }
}
