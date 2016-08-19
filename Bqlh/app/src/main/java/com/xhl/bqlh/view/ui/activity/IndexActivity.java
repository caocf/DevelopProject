package com.xhl.bqlh.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xhl.bqlh.R;


public class IndexActivity extends Activity {

    private View name;
    private static final int TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        name = findViewById(R.id.image);

        name.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastDo();
            }
        }, TIME);

    }

    private void lastDo() {
        startActivity(new Intent(IndexActivity.this, HomeActivity.class));
        finish();
    }
}
