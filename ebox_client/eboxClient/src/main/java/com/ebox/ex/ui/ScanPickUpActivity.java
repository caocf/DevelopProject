package com.ebox.ex.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.bar.ScanTimeLeftBar;
import com.ebox.ex.utils.ActiveUtil;
import com.ebox.Anetwork.RequestManager;
import com.ebox.ex.network.model.base.type.TerminalActivity;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 扫码开柜
 * 
 * @author Administrator
 * 
 */
public class ScanPickUpActivity extends CommonActivity implements
		OnClickListener {

    private ImageView scanCodeIV;
    private TextView tv_scan_bottom;
    private Button enterBT;
    private Title title;
    private TitleData data;
    private ScanTimeLeftBar timeLeft;
    private static final String QR_IMAGE_NAME = "/qrcode.jpg";

    private TextView timeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_scan_pick_up_new);

        MGViewUtil.scaleContentView(this, R.id.rootView);

        AppApplication.getInstance().getTerminal_code();

        initViews();

        initPickActivity();

    }

    private void initPickActivity() {
        /**
         * 判断是否显示用户取件活动
         */
        TerminalActivity activity = ActiveUtil.getUserActive();
        if (activity != null)
        {
            String url = activity.getUrl();
            Intent intent = new Intent(this, DialogWebActivity.class);
            if (url.contains("?")) {
                intent.putExtra("web_url", url + "&terminal_code="
                        + AppApplication.getInstance().getTerminal_code());
            } else {
                intent.putExtra("web_url", url + "?terminal_code="
                        + AppApplication.getInstance().getTerminal_code());
            }

            intent.putExtra("title", activity.getName());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!timeLeft.isShow) {
            timeLeft.show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        timeLeft.stop();
    }

    private void initViews() {

        scanCodeIV = (ImageView) this.findViewById(R.id.new_iv_scan_code);
        timeTV=(TextView) this.findViewById(R.id.new_tv_scan_time);
        // 判断是否已经有二维码图片
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + QR_IMAGE_NAME);
            if (file.exists())
            {
                // 图片存在
                try {
                    Bitmap bitmap = BitmapFactory
                            .decodeStream(new FileInputStream(file));
                    scanCodeIV.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else
            {
                goPickupActivity();
            }
        }

        // 布局界面
        enterBT = (Button) this.findViewById(R.id.new_bt_scan_enter);
        enterBT.setVisibility(View.GONE);
        enterBT.setOnClickListener(this);

        initTitle();

    }

    private void goPickupActivity() {
        Intent intent = new Intent(this, PickupActivity.class);
        intent.putExtra("isActivity", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.new_bt_scan_enter:

                goPickupActivity();

                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        RequestManager.cancelAll("checkqrtask");
    }




	private void initTitle() {
		title = (Title) findViewById(R.id.title);
		title.tv_timer.setVisibility(View.GONE);

		// timeLeft = new TimeLeftBar(title.tv_timer);
		timeLeft = new ScanTimeLeftBar(timeTV, this, enterBT);
		// timeLeft.show();
		data = title.new TitleData();
		data.backVisibility = 1;
		data.tvContent = getResources().getString(R.string.ex_scanpickup);
		data.tvVisibility = true;
		title.setData(data, this);
	}




}