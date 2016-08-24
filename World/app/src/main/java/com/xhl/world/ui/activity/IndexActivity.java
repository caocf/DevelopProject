package com.xhl.world.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.xhl.world.R;
import com.xhl.world.ui.main.MainActivity;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.network.download.DownloadManager;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_index)
public class IndexActivity extends FragmentActivity {

    private LifeCycleImageView iv_index;

    private Button btn_skip;

    private void download(View view) {
        String url = "http://7xi9d6.com1.z0.glb.clouddn.com/com.ebox.V1.4.5.apk";

        new DownloadManager().setActivity(this).setIsNeedShowView(true).setUrl(url).setIsForceDownload(false).setLabel("测试升级哦！1").start();
    }

    private boolean sKipd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        iv_index = (LifeCycleImageView) findViewById(R.id.iv_index);
        btn_skip = (Button) findViewById(R.id.btn_skip);
        sKipd = false;
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
    }

    private void skip() {
        if (!sKipd) {
            sKipd = true;
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
   /*     UserInfo userInfo = AppApplication.appContext.getLoginUserInfo();
        if (userInfo != null && !TextUtils.isEmpty(userInfo.id)) {
            ChatManager chatManager = ChatManager.getInstance();
            //根据试航的用户唯一ID，用作第三方聊天的唯一ID
            chatManager.setupManagerWithUserId(userInfo.id);
            chatManager.openClient(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    skip();
                }
            });
        } else*/
        {
            btn_skip.postDelayed(new Runnable() {
                @Override
                public void run() {
                    skip();
                }
            }, 500);
        }
    }
}
