package com.xhl.bqlh.business.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.R;

public class IndexActivity extends Activity {

    private View name;
    private static final int TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        name = findViewById(R.id.image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!((AppDelegate) getApplication()).isLogin()) {
                    startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(IndexActivity.this, HomeActivity.class));
                }

                overridePendingTransition(R.anim.anim_right_open_enter, R.anim.anim_right_open_exit);

                finish();
            }
        }, TIME);

    }
}
