package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.ui.fragment.MeFragment;

public class MyCenterActivity extends BaseActivity
{
    private Context mContext;

    /**
     * external flag
     */
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);

        mContext = MyCenterActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        MeFragment meFragment = new MeFragment();
        meFragment.setFromActivity(true);
        fragmentTransaction.add(R.id.fragmentContainer,
                meFragment);

        fragmentTransaction.commit();
    }


}
