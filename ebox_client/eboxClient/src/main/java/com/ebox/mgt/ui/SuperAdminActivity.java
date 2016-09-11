package com.ebox.mgt.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.bar.AppIcon;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.SpaceCtrl;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;

import java.util.List;

public class SuperAdminActivity extends CommonActivity implements OnClickListener{
	private Button bt_box_setting;
	private Button bt_box_manage;
	private Button bt_box_test;
	private Button bt_box_init;
	private Button bt_camera_test;
	private Button bt_audio_test;
	private Button bt_led_test;
	private Button bt_sd;
	private Button bt_restartapk;
	private Button bt_reboot;
	private Button bt_querySqlite;
	private List<ResolveInfo> mApps;
	private GridView mGrid;
	private TextView tv_version;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.mgt_activity_superadmin);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
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
	
	
	private void initView()
	{
		bt_box_setting = (Button) findViewById(R.id.bt_box_setting);
		bt_box_setting.setOnClickListener(this);
		bt_box_manage = (Button) findViewById(R.id.bt_box_manage);
		bt_box_manage.setOnClickListener(this);
		bt_box_test = (Button) findViewById(R.id.bt_box_test);
		bt_box_test.setOnClickListener(this);
		bt_box_init = (Button) findViewById(R.id.bt_box_init);
		bt_box_init.setOnClickListener(this);
		bt_camera_test = (Button) findViewById(R.id.bt_camera_test);
		bt_camera_test.setOnClickListener(this);
		bt_audio_test = (Button) findViewById(R.id.bt_audio_test);
		bt_audio_test.setOnClickListener(this);
		bt_led_test = (Button) findViewById(R.id.bt_led_test);
		bt_led_test.setOnClickListener(this);
		bt_sd = (Button) findViewById(R.id.bt_sd);
		bt_sd.setOnClickListener(this);
		bt_restartapk = (Button) findViewById(R.id.bt_restartapk);
		bt_restartapk.setOnClickListener(this);
		bt_reboot = (Button) findViewById(R.id.bt_reboot);
		bt_reboot.setOnClickListener(this);
		bt_querySqlite=(Button) findViewById(R.id.bt_querySqlite);
		bt_querySqlite.setOnClickListener(this);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText(FunctionUtil.getAllAppVersion());
		mGrid = (GridView) findViewById(R.id.apps_list);
        mGrid.setAdapter(new AppsAdapter());

        mGrid.setOnItemClickListener(listener);
        initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.mgt_admin);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	
	private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            ResolveInfo info = mApps.get(position);
            
            //该应用的包名
            String pkg = info.activityInfo.packageName;
            //应用的主activity类
            String cls = info.activityInfo.name;

            FunctionUtil.startApp(SuperAdminActivity.this, pkg, cls);
        }

    };
	
	private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
 
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_box_setting:
		{
			Intent intent = new Intent(SuperAdminActivity.this, BoxSettingActivity.class);
	        startActivity(intent);
			break;
		}
		case R.id.bt_box_manage:
		{
			Intent intent1 = new Intent(SuperAdminActivity.this, BoxManageActivity.class);
	        startActivity(intent1);
			break;
		}
		
		case R.id.bt_camera_test:
		{
			Intent intent1 = new Intent(SuperAdminActivity.this, CameraTestActivity.class);
	        startActivity(intent1);
			break;
		}
		case R.id.bt_audio_test:
		{
			RingUtil.playRingtone(RingUtil.deliver_id);
			break;
		}
		case R.id.bt_led_test:
		{
			Intent intent1 = new Intent(SuperAdminActivity.this, LedTestActivity.class);
	        startActivity(intent1);
			break;
		}
		case R.id.bt_querySqlite:
		{
			Intent intent1 = new Intent(SuperAdminActivity.this, SqliteQueryActivity.class);
	        startActivity(intent1);
	        break;
		}
		case R.id.bt_box_test:
		{
			if(GlobalField.boxLocalInit)
			{
				Toast.makeText(this, "箱门测试开始！", Toast.LENGTH_LONG).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(int i = 0; i < GlobalField.boxInfoLocal.size(); i++)
						{
							RackInfo rack = GlobalField.boxInfoLocal.get(i);

							for(int j = 0; j < rack.getBoxes().length; j++)
							{
								BoxInfo box = rack.getBoxes()[j];
								// 开门
								BoxOp op =  new BoxOp();
								op.setOpId(BoxOpId.OpenDoor);
								op.setOpBox(box);
								BoxCtrlTask.addBoxCtrlQueue(op);
							}
						}
						runOnUiThread(new Runnable()
						{
							@Override
							public void run() {
								Toast.makeText(AppApplication.globalContext, "箱门测试结束！", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();

			}
			else
			{
				Toast.makeText(this, "箱门数据初始化尚未完成！", Toast.LENGTH_LONG).show();
			}
			break;
		}
		
		case R.id.bt_box_init:
		{
			GlobalField.boxLocalInit = false;
			GlobalField.boxInfoLocal = null;
			break;
		}
		case R.id.bt_sd:
		{
			Toast.makeText(this, "space all:" + SpaceCtrl.getAllSize() +
					"M available:"+SpaceCtrl.getAvailaleSize()+
					"M free:"+SpaceCtrl.getFreeSize()+"M", Toast.LENGTH_LONG).show();
			break;
		}
		case R.id.bt_restartapk:
		{
			AppApplication.getInstance().exitApp();
			break;
		}
		case R.id.bt_reboot:
		{
			AppApplication.getInstance().restartOs();
			break;
		}
		}
	}
	
	public class AppsAdapter extends BaseAdapter {
	    public AppsAdapter() {
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	AppIcon icon;

	        if (convertView == null) {
	        	icon =  new AppIcon(SuperAdminActivity.this);
	        } else {
	        	icon = (AppIcon) convertView;
	        }

	        ResolveInfo info = mApps.get(position);
	        icon.setData(info.activityInfo.loadIcon(getPackageManager()), 
	        		info.activityInfo.loadLabel(getPackageManager()).toString());

	        return icon;
	    }

	    public final int getCount() {
	        return mApps.size();
	    }

	    public final Object getItem(int position) {
	        return mApps.get(position);
	    }

	    public final long getItemId(int position) {
	        return position;
	    }
	}
}
