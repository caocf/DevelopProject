package com.ebox.ums.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaums.mis.sst.transshell.CardState;
import com.chinaums.mis.sst.transshell.CommonDataDef;
import com.chinaums.mis.sst.transshell.TransControl;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.network.model.enums.AlarmCode;
import com.ebox.ex.network.model.enums.AlarmState;
import com.ebox.ex.network.model.enums.AlarmType;
import com.ebox.ex.network.model.enums.TimeNum;
import com.ebox.ex.network.model.enums.TranRstType;
import com.ebox.pub.database.alarm.Alarm;
import com.ebox.pub.database.alarm.AlarmOp;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.customview.Title.ZCTitleListener;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.TimeCount;
import com.ebox.pub.utils.TimeCount.TimeOutCallback;
import com.ebox.pub.utils.Tip;
import com.ebox.ums.model.ums.TransRstData;

import java.util.Date;

public class PayRstActivity extends CommonActivity {
	private Title title;
	private TitleData titleData;
	private TimeCount cardTimeCount;
	private TextView tv_timer;
	private TransControl transControl ;
	private boolean cardInit=false;
	private boolean canQuit=false;
	private boolean timeOut=false;
	private boolean cardExsit=false;
	private Tip tip;
	private String card="";
	private TransRstData tranRst;

	private TextView result;
	private ImageView image;
	private Thread SwallowingThread=null;

	private DialogUtil dialogUtil ;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ums_activity_payrst);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		tranRst = (TransRstData) getIntent().getSerializableExtra(
				"tranRst");
		card = tranRst.getCardNo();
		initView(7);
		initData(tranRst);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView(int i)
	{
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.ums_consume);
		title.setData(titleData, this);
		title.setZCTitleListner( new ZCTitleListener(){

			@Override
			public void onOperateBtnClick(int index) {
				if(canQuit){
					PayRstActivity.this.finish();
				} else {
						tip = new Tip(PayRstActivity.this,
								getResources().getString(R.string.ums_getaway_yourcard),
								null);
						tip.show(0);

				}
			}

		});
		result =  (TextView) findViewById(R.id.result);
		//image =  (ImageView) findViewById(R.id.image);
		tv_timer = (TextView)findViewById(R.id.tv_timer);
		cardTimeCount =new TimeCount(TimeNum.CardTimeUi, 1000,new TimeOutCallback(){
			@Override
			public void timeFinish() {
				if(cardExsit){
					timeOut=true;
					tip = new Tip(PayRstActivity.this,
							getResources().getString(R.string.ums_card_retained),
							null);
					tip.show(1000);
					dialogUtil.showProgressDialog();
					while(true){
						if(canQuit){
							PayRstActivity.this.finish();
							break;
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} else {
					PayRstActivity.this.finish();
				}

			}

			@Override
			public void onTick(long st) {
				tv_timer.setText(st /1000+"");

			}
		});

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
	}

	private void initData(TransRstData tranRst){
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				while(GlobalField.umsTransExsit){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				myHandler.sendEmptyMessage(0);
			}

		});
		th.start();
		if(tranRst.getRstCode().equals("00")){
			RingUtil.playRingtone(RingUtil.id_sucess);
		//	image.setImageDrawable(getResources().getDrawable(R.drawable.ums_rst_success));
			result.setText(getResources().getString(R.string.ums_pay_success));
	   } else {
		   RingUtil.playRingtone(RingUtil.id_failed);
		//   image.setImageDrawable(getResources().getDrawable(R.drawable.ums_rst_failed));
		   result.setText(getResources().getString(R.string.ums_pay_failed)+":"+TranRstType.getRstCodeStr(tranRst.getRstCode()));
	   }
		cardTimeCount.start();

	}


	private Runnable SwallowingRunnable  = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {

			if (!transControl.initTrans()) {
				canQuit=true;
			} else {
				if (!transControl.initCardDev(CommonDataDef.CardModeNone,CommonDataDef.TouchCardType)) {
					canQuit=true;
				} else {
					cardInit=true;
					while(true){
						if (transControl.readCardStat(CommonDataDef.TouchCardType) == CardState.CARD_BEFORE_DEV) {
							cardExsit=true;
							//超时吞卡操作
							if(timeOut){
								if (transControl.initCardDev(CommonDataDef.CardModePull,CommonDataDef.TouchCardType)) {
									//TODO 记录数据库
									Alarm alarm = new Alarm();
									alarm.setAlarm_type(AlarmType.type_service);
									alarm.setAlarmCode(AlarmCode.code_9);
									java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							        String s = format1.format(new Date());
							        alarm.setContent("格格货栈吞卡告警：卡号为：" +card +" 时间：" +s);
							        alarm.setState(AlarmState.state_0);
							        AlarmOp.addAlarm(alarm);
									cardInit=true;
								}
								canQuit=true;
								break;
							}

							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}else {
							cardExsit=false;
							canQuit=true;
							break;
						}

					}
				}

			}
		}};


	@Override
	protected void onDestroy(){
		super.onDestroy();
		cardTimeCount.cancel();
		dialogUtil.closeProgressDialog();
		cleanTrans();
		if(tip != null)
		{
			tip.closeTip();
		}
	}

	private void readCardThread(){

		transControl = new TransControl(this, "",  CommonDataDef.cupBasicApp);
		if(tranRst.getInitCard()==1 ){
			cardExsit = true;
			canQuit=false;
			if(SwallowingThread == null){
				SwallowingThread = new Thread(SwallowingRunnable);
			}
			SwallowingThread.start();
		} else {
			cardExsit = false;
			canQuit=true;
		}
	}

	@SuppressLint("HandlerLeak")
	   private Handler myHandler = new Handler() {
			public void handleMessage (Message msg) {
				switch(msg.what) {
				case 0:
					readCardThread();
					break;
				}
			}
	 };

	 private void cleanTrans(){
			if(cardInit){
				if (!transControl.closeCardDev(CommonDataDef.TouchCardType)) {
					transControl.closeCardDev(CommonDataDef.TouchCardType);
				}
				cardInit=false;
			}
			if(transControl!=null){
				transControl.clearTrans();
			}
			transControl=null;
			GlobalField.umsTransExsit=false;
		}

}
