package com.ebox.ex.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.DotType;
import com.ebox.ex.ui.fragment.FragmentAdvMain;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.WebPageActivity;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.websocket.helper.WebSocketManager;
import com.ebox.st.model.GetNoticeReq;
import com.ebox.st.model.GetNoticeRsp;
import com.ebox.st.model.GetNoticeRspModel;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.IdCardActivity;
import com.ebox.st.ui.LicenceMainActivity;
import com.ebox.st.ui.WebActivity;
import com.ebox.yc.ui.AnnounceMainActivity;
import com.ebox.yc.ui.WorkWinActivity;

import java.util.ArrayList;

/**
 * 应用主界面
 */
public class TopActivity extends TopActivityGroup implements OnClickListener{

    /*主題*/
    public int mTheme = R.style.AppTheme_normal;
	/*
	normal
	 */
	private Button pickup_btn_normal;
	private Button query_btn_normal;
	private Button service_btn_normal;
//	private Button huozhan_btn_normal;
	private Button oper_btn_normal;
	private Button help_btn_normal;
	private Button btn_web;
    private Button btn_help;
	

    private EditText et_pressed_key;
	
	public static final String BROADCAST_GEGE="com.ebox.ui.TopActivity.gege";

	/*
	 * lg
	 */
	private ImageView st_btn_kuaidi;//智能快递
	private ImageView st_btn_dj;//党建信息
	private ImageView st_btn_jiaofei;//便民缴费
	private ImageView st_btn_zjbl;//证件办理
	private ImageView st_btn_rkxx;//人口信息录入
	private ImageView st_btn_xxgk;//信息公开
	private ImageView st_btn_jyw; //家园网
	private ImageView st_btn_sqzx;//社区咨询
	private ImageView st_btn_zxzx; //在线咨询
	private ImageView st_btn_sqfw; //社区服务
	private TextView tv_noti;
	private String NoticeStr ="深圳市龙岗区横岗街道怡锦社区智慧社区综合服务站欢迎您的使用！";

	//银川
	private Button yc_btn_kuaidi;
	private Button yc_btn_jiaofei;
	private Button yc_btn_sqgg;
	private Button yc_btn_sqbs;
	private Button yc_btn_hj;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (savedInstanceState == null) {
            mTheme = getTheme(GlobalField.config.getTheme());
        } else {
            mTheme = savedInstanceState.getInt("theme");
        }
        setTheme(mTheme);

        super.onCreate(savedInstanceState);
        if (GlobalField.config.getDot() == DotType.HENGXI)
        {
            setContentView(R.layout.hx01_activity_top);
			MGViewUtil.scaleContentView(this,R.id.rootView);
            initViewByHx01();
        }
		else if (GlobalField.config.getDot() == DotType.LONGGANG)
		{
			setContentView(R.layout.pub_lg_activity_top);
			MGViewUtil.scaleContentView(this, R.id.rootView);
			LginitView();
			LginitData();
		}
		else if (GlobalField.config.getDot() == DotType.YINCHUAN)
		{
			setContentView(R.layout.pub_yc_activity_top);
			MGViewUtil.scaleContentView(this,R.id.rootView);
			ycInitView();
		}
        else
        {
            setContentView(R.layout.pub_activity_top);
			MGViewUtil.scaleContentView(this,R.id.rootView);
            initView();
//            registerBroadcast();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int theme= getTheme(GlobalField.config.getTheme());
        if (mTheme != theme)
        {
			mTheme=theme;
			//recreate() 方法需要延迟1ms调用
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					recreate();
				}
			}, 2);
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		// 读取最新门状态
		BoxCtrlTask.readBoxStatus();
		if (GlobalField.config.getDot() ==DotType.LONGGANG)
		{
			getNotice();
		}
//		else if (GlobalField.config.getDot() == DotType.NANJING)
//		{
//			if (GlobalField.serverConfig!=null)
//			{
//				Integer ge = GlobalField.serverConfig.getIsOpenGeGe();
//				if (null != ge && ge == GegeType.is_show)
//				{
//					huozhan_btn_normal.setVisibility(View.VISIBLE);
//				} else {
//					huozhan_btn_normal.setVisibility(View.GONE);
//				}
//			}
//		}
	}

    private void initView()
    {
		btn_web= (Button) findViewById(R.id.web_view);
		btn_web.setOnClickListener(this);
        pickup_btn_normal = (Button) findViewById(R.id.pickup_btn_normal);
        pickup_btn_normal.setOnClickListener(this);
        query_btn_normal = (Button) findViewById(R.id.query_btn_normal);
        query_btn_normal.setOnClickListener(this);
        service_btn_normal = (Button) findViewById(R.id.service_btn_normal);
        service_btn_normal.setOnClickListener(this);
//        huozhan_btn_normal = (Button) findViewById(R.id.huozhan_btn_normal);
//        huozhan_btn_normal.setOnClickListener(this);
        oper_btn_normal = (Button) findViewById(R.id.oper_btn_normal);
        oper_btn_normal.setOnClickListener(this);
		help_btn_normal= (Button) findViewById(R.id.help_view);
		help_btn_normal.setOnClickListener(this);
        serviceShow(false);
		webShow(false);
//        //默认不显示格格货栈
//        huozhan_btn_normal.setVisibility(View.GONE);
        startAdvFragment();
    }

    private void initViewByHx01()
    {
        pickup_btn_normal = (Button) findViewById(R.id.pickup_btn_normal);
        pickup_btn_normal.setOnClickListener(this);
        query_btn_normal = (Button) findViewById(R.id.query_btn_normal);
        query_btn_normal.setOnClickListener(this);
        oper_btn_normal = (Button) findViewById(R.id.oper_btn_normal);
        oper_btn_normal.setOnClickListener(this);
        btn_help = (Button) findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        et_pressed_key = (EditText)findViewById(R.id.et_pressed_key);
        et_pressed_key.requestFocus();

        // 遇到回车自动访问服务端
        et_pressed_key.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				LogUtil.d("mf", "keycode " + keyCode);
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					//存件
					changeTab(2);
					return true;
				}

				if (KeyEvent.KEYCODE_PAGE_UP == keyCode) {
					//取件
					changeTab(0);
					return true;
				}


				return false;
			}

		});
        KeyboardUtil.hideInput(this, et_pressed_key);
        startAdvFragment();
    }

	private void LginitView(){
		st_btn_kuaidi = (ImageView) findViewById(R.id.st_btn_kuaidi);
		st_btn_kuaidi.setOnClickListener(this);
		st_btn_dj = (ImageView) findViewById(R.id.st_btn_dj);
		st_btn_dj.setOnClickListener(this);
		st_btn_jiaofei = (ImageView) findViewById(R.id.st_btn_jiaofei);
		st_btn_jiaofei.setOnClickListener(this);
		st_btn_zjbl = (ImageView) findViewById(R.id.st_btn_zjbl);
		st_btn_zjbl.setOnClickListener(this);
		st_btn_rkxx = (ImageView) findViewById(R.id.st_btn_rkxx);
		st_btn_rkxx.setOnClickListener(this);
		st_btn_xxgk = (ImageView) findViewById(R.id.st_btn_xxgk);
		st_btn_xxgk.setOnClickListener(this);
		st_btn_jyw = (ImageView) findViewById(R.id.st_btn_jyw);
		st_btn_jyw.setOnClickListener(this);
		st_btn_sqzx = (ImageView) findViewById(R.id.st_btn_sqzx);
		st_btn_sqzx.setOnClickListener(this);
		st_btn_zxzx = (ImageView) findViewById(R.id.st_btn_zxzx);
		st_btn_zxzx.setOnClickListener(this);
		st_btn_sqfw = (ImageView) findViewById(R.id.st_btn_sqfw);
		st_btn_sqfw.setOnClickListener(this);
		tv_noti = (TextView) findViewById(R.id.tv_noti);
	}

	private void LginitData(){
		tv_noti.setText(NoticeStr);
		tv_noti.requestFocus();
	}

	private void ycInitView(){
		yc_btn_kuaidi = (Button) findViewById(R.id.yc_btn_kuaidi);
		yc_btn_kuaidi.setOnClickListener(this);
		yc_btn_jiaofei = (Button) findViewById(R.id.yc_btn_jiaofei);
		yc_btn_jiaofei.setOnClickListener(this);
		yc_btn_sqgg = (Button) findViewById(R.id.yc_btn_sqgg);
		yc_btn_sqgg.setOnClickListener(this);
		yc_btn_sqbs = (Button) findViewById(R.id.yc_btn_sqbs);
		yc_btn_sqbs.setOnClickListener(this);
		yc_btn_hj = (Button) findViewById(R.id.yc_btn_hj);
		yc_btn_hj.setOnClickListener(this);
	}
	
//	private void registerBroadcast() {
//		IntentFilter filter=new IntentFilter();
//		filter.addAction(BROADCAST_GEGE);//显示格格标签
//		registerReceiver(showReceiver, filter);
//	}

//	//展示标签广播
//	BroadcastReceiver showReceiver=new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action=intent.getAction();
//			if (action.equals(BROADCAST_GEGE)) //是否显示格格货栈标签
//			{
//				Integer ge = GlobalField.serverConfig.getIsOpenGeGe();
//				if (ge!=null)
//				{
//					if (ge==GegeType.is_show)
//					{
//						huozhan_btn_normal.setVisibility(View.VISIBLE);
//					}else {
//						huozhan_btn_normal.setVisibility(View.GONE);
//					}
//				}
//			}
//		}
//	};
	
	@Override
	protected void onDestroy() {
//		if (GlobalField.config.getDot() == DotType.NANJING)
//		{
//			unregisterReceiver(showReceiver);
//		}
		super.onDestroy();
	}
	private void serviceShow(boolean show)
	{
		if(GlobalField.config.getServiceCtrl() == 0)
		{
			service_btn_normal.setVisibility(View.GONE);
		}
		else
		{
            service_btn_normal.setVisibility(View.VISIBLE);
		}
	}

	private void webShow(boolean show) {

		if (GlobalField.config.getWeb_nj()!=null)
		{
			if (GlobalField.config.getWeb_nj()==0)
			{
				show = false;
			} else {
				show = true;
			}
		}

		if (show) {
			btn_web.setVisibility(View.VISIBLE);
		} else {
			btn_web.setVisibility(View.GONE);
		}

	}

	private void changeTab(int index)
	{
		if (index==0)
		{
			boolean state = readDoorState();
			if (state) {
				Intent intent=new Intent(this, CloseDoorActivity.class);
				intent.putExtra(CloseDoorActivity.Flag, CloseDoorActivity.pickUp);
				startActivity(intent);
			}else {
				startAct(index);
			}
		}
		else if(index==2)
		{
			boolean state = readDoorState();
			if (state) {
				Intent intent=new Intent(this, CloseDoorActivity.class);
				intent.putExtra(CloseDoorActivity.Flag, CloseDoorActivity.Login);
				startActivity(intent);
			}else {
				startAct(index);
			}
		}else {
			startAct(index);
		}
	}
	
	private void startAct(int act){
		Intent intent=null;
		
		if (act==0) {
			// 用户取件
			// 检测服务器配置文件中返回的取件模式
			//现在只有南京网点支持扫码取件
			if (GlobalField.config.getDot() == DotType.NANJING)
			{
				if( GlobalField.serverConfig ==null
						||GlobalField.serverConfig.isScanPickup() == null
						|| GlobalField.serverConfig.isScanPickup() == 0)
				{
					// 返回值为0 走原有取件模式
					intent = new Intent(this, PickupActivity.class);
				}
				else if(GlobalField.serverConfig.isScanPickup() == 1)
				{
					// 返回值为1 走扫码开柜
					// 且需要判断长连接在才能进入
					if (WebSocketManager.instance().isConnected())
					{
						intent = new Intent(this, ScanPickUpActivity.class);
					} else {
						intent = new Intent(this, PickupActivity.class);
					}
				}
			}
			else
			{
				intent = new Intent(this, PickupActivity.class);
			}

		}
		else if(act==1)
		{
			intent=new Intent(this, ItemQueryActivity.class);
		}
		else if(act==2) 
		{
            if (GlobalField.config.getDot() == DotType.HENGXI)
            {
                intent=new Intent(this, OperatorLoginByCardActivity.class);
            }
            else
            {
                intent=new Intent(this, OperatorLoginActivity.class);
            }

		}
		else if(act==3) 
		{
			intent=new Intent(this, ServiceActivity.class);
		}
		else if(act==4) 
		{
			Integer service = GlobalField.config.getServiceCtrl();//便民服务,0不显示，1显示
			if (service==0) //便民服务不可用，即银联不可用，货栈显示网页
			{
				intent=new Intent(this, WebPageActivity.class);
				intent.putExtra("web_url", "http://m.aimoge.com/ebox/new_year");
				intent.putExtra("title", this.getResources().getString(R.string.pub_huozhan));
			}
			else {
				//intent=new Intent(this, WebPageActivity.class);
			}
			intent=new Intent(this, GegeHuozhanActivity.class);
		}
        else if (act ==5)
        {
            intent=new Intent(this, WebPageActivity.class);
            intent.putExtra("web_url", "file:///android_asset/help.html");
            intent.putExtra("title", this.getResources().getString(R.string.pub_help));
        }
		else if (act == 6)
		{
			intent=new Intent(this, WebPageActivity.class);
			intent.putExtra("web_url", "https://www.tmall.com/?spm=a21bo.7724922.8455.1.FlYb6Q");
			intent.putExtra("title", this.getResources().getString(R.string.pub_taobao));
		}
		if (intent!=null) 
		{
			startActivity(intent);
			//overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
		}
	}

	@Override
	public void onClick(View v) {
		AppService.getIntance().hasOnKeyDown();
		switch(v.getId())
		{
		case R.id.pickup_btn_normal:
			changeTab(0);
			break;
		case R.id.query_btn_normal:
			changeTab(1);
			break;
		case R.id.oper_btn_normal:
			changeTab(2);
			break;
		case R.id.service_btn_normal:
			changeTab(3);
			break;
//		case R.id.huozhan_btn_normal:
//			changeTab(4);
//			break;
        case R.id.btn_help:
            changeTab(5);
            break;
		case R.id.web_view:
			changeTab(6);
			break;
		case R.id.st_btn_kuaidi:
			//智能快递
			startActivity(new Intent(this,KuaidiActivity.class));
			break;

			case R.id.help_view:
				//帮助
				startActivity(new Intent(this,HelpActivity.class));
				break;

		case R.id.st_btn_jiaofei:
			//便民缴费
			startActivity(new Intent(this, ServiceActivity.class));
			break;

		case R.id.st_btn_zjbl:
			//证件办理
			startActivity(new Intent(this, LicenceMainActivity.class));
			break;
		case R.id.st_btn_zxzx:
		{
			//在线咨询
			Intent intent = new Intent(this, IdCardActivity.class);
			intent.putExtra("idcard_type",IdCardActivity.type_5);
			startActivity(intent);
			break;
		}
		case R.id.st_btn_dj:
		{
			//党建信息
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("url","http://hgdj.snsii.net/index.html");
			intent.putExtra("title", getResources().getString(R.string.st_dangjian));
			startActivity(intent);
			break;
		}
		case R.id.st_btn_xxgk:
		{
			//信息公开
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("url", "http://cms.ebox.gegelaile.cn/cms/public?page=0");
			intent.putExtra("title", getResources().getString(R.string.st_xxgk));
            startActivity(intent);
			break;
		}
		case R.id.st_btn_sqzx:
		{
			//社区资讯
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("url", "http://cms.ebox.gegelaile.cn/cms/news?page=0");
			intent.putExtra("title", getResources().getString(R.string.st_sqzx));
            startActivity(intent);
			break;
		}
		case R.id.st_btn_jyw:
		{
			//家园网
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("url","http://www.gbsq.org/yijin/");
			//intent.putExtra("url","http://m.dev.aimoge.com/ebox/card/index");
			intent.putExtra("title", getResources().getString(R.string.st_jiayuanw));
            startActivity(intent);
			break;
		}
		case R.id.st_btn_sqfw:
			//社区服务
		{
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("url","http://www.gbsq.org/yijin/sqjywmtwo_webmap/sqjywmtwo_sqfw/");
			intent.putExtra("title", getResources().getString(R.string.st_sqfw));
			startActivity(intent);
			break;
		}
		case R.id.st_btn_rkxx:
		{
			//人口信息录入
			Intent intent = new Intent(this, IdCardActivity.class);
			intent.putExtra("idcard_type",IdCardActivity.type_3);
			startActivity(intent);
			break;
		}

		//银川
		case R.id.yc_btn_jiaofei:
		{
			startActivity(new Intent(this, ServiceActivity.class));
			break;
		}
		case R.id.yc_btn_kuaidi:
		{
			startActivity(new Intent(this, KuaidiActivity.class));
			break;
		}
		case R.id.yc_btn_sqgg:
		{
			//社区公告
			startActivity(new Intent(this, AnnounceMainActivity.class));
			break;
		}
		case R.id.yc_btn_sqbs:
		{
			//社区办事
			startActivity(new Intent(this,WorkWinActivity.class));
			break;
		}
		case R.id.yc_btn_hj:
		{
			//环境信息
			Intent intent = new Intent(this, WebActivity.class);
			//intent.putExtra("url","http://zems.zeei.com.cn:9900/LEDHandle.ashx?key=GetSceneTemplate&pLedId=1");
            if (GlobalField.config.getCommunityId().equals("36"))
            {
                intent.putExtra("url","http://42.63.19.197:9030/BCS/Index.html?CommunityName=%E5%8D%8E%E6%96%B0%E7%A4%BE%E5%8C%BA");
            }
            else
            {
                intent.putExtra("url","http://42.63.19.197:9030/BCS/Index.html?CommunityName=%E8%A7%82%E6%B9%96%E7%A4%BE%E5%8C%BA");
            }

			intent.putExtra("title", getResources().getString(R.string.yc_hj));
			startActivity(intent);
			break;
		}
		}

//		overridePendingTransition(R.anim.activity_open_enter,R.anim.activity_open_exit);
	}
	
	
	// 读取数据库缓存的门的信息
	private boolean readDoorState() {
		ArrayList<BoxInfo> list = BoxDynSyncOp.getAllSyncBoxList();
		if (list!=null&&list.size()>0)
		{
			for (BoxInfo boxInfo : list)
			{
				Integer doorState = boxInfo.getDoorState();
				if (boxInfo.getBoxUserState()==BoxUserState.normal)
				{
					if (doorState == DoorStatusType.open) 
					{
						return true;
					} 
				}
			}
		}
		return false;
	}
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mTheme);
    }

    /**
     * 获取配置主题的Id
     * */
    private int getTheme(int config)
    {
        int theme = R.style.AppTheme_normal;
        if(config == 1)
        {
            theme = R.style.AppTheme_hx01;
        }
		//银川主题
		if (GlobalField.config.getDot() == DotType.YINCHUAN)
		{
			theme=R.style.AppTheme_yc;
		}

        return theme;
    }
	
	private void startAdvFragment() 
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fl_content, new FragmentAdvMain()).commit();
	}

	private void getNotice(){
		GetNoticeReq req = new GetNoticeReq();
		req.setTerminal_code(AppApplication.getInstance().getTerminal_code());


		ApiClient.getNotice(TopActivity.this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				GetNoticeRsp rsp = (GetNoticeRsp)data;
				if (rsp.getSuccess())
				{
					ArrayList<GetNoticeRspModel> result = rsp.getResult();
					if (result.size()>0)
					{
						tv_noti.setText(rsp.getResult().get(0).getContent());
					}
				}

			}

			@Override
			public void onFailed(Object data) {

			}
		});
	}
	

}
