package com.ebox.ums.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaums.mis.sst.transshell.CardState;
import com.chinaums.mis.sst.transshell.CommonDataDef;
import com.chinaums.mis.sst.transshell.TransControl;
import com.chinaums.mis.sst.transshell.TransReq;
import com.chinaums.mis.sst.transshell.TransResult;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mall.warehouse.model.enums.PayResultType;
import com.ebox.mall.warehouse.model.enums.PayType;
import com.ebox.ex.network.model.enums.TimeNum;
import com.ebox.ex.network.model.enums.TranRstType;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.customview.Title.ZCTitleListener;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.TimeCount;
import com.ebox.pub.utils.TimeCount.TimeOutCallback;
import com.ebox.pub.utils.Tip;
import com.ebox.ums.database.payRst.PayRstOp;
import com.ebox.ums.database.payRst.PayRstTable;
import com.ebox.ums.database.payRst.TradingFinishOrderData;
import com.ebox.ums.model.enums.UmsErrorCode;
import com.ebox.ums.model.enums.UmsTranType;
import com.ebox.ums.model.ums.TransData;
import com.ebox.ums.model.ums.TransRstData;

import java.io.UnsupportedEncodingException;



public class PayCenterActivity extends CommonActivity implements OnClickListener{
	private Title title;
	private TitleData titleData;
	private Tip tip;
	private DialogUtil dialogUtil ;
	private TextView tv_timer;
	
	//支付提示
	private RelativeLayout step1;
	private RelativeLayout iv_insert_card ;
	private ImageView card ;
	
	//刷卡
	private RelativeLayout step2;
	private TextView tv_input;
	private Button btn_card_confirm;
	
	//支付提示
	private RelativeLayout step3;
	private TextView result;
	
	private boolean cardInit=false;
	private boolean cardExist=false;
	private boolean pinInit=false;
	private boolean userCancel=false;
	private boolean userOperTimeOut=false;
	private boolean inputPwdFinish=false;
	private String cardNo;
	
	//thread
	private Thread initCardThread=null;
	private Thread readCardStateThread=null;
	private Thread initPinThread=null;
	private Thread inputPinThread=null;
	private Thread tranThread=null;
	
	
	
	//交易
	private TransControl transControl ;
	private TransReq transReq = new TransReq();
	private TransResult transResp = new TransResult();
	private TransData trdata ;
	private TransRstData tranRst = new TransRstData() ;
	private Intent resultIntent1 ;
	private TradingFinishOrderData tradeData = new TradingFinishOrderData();
	
	private TimeCount cardTimeCount;
	private TimeCount pinpadTimeCount;
	
	private int inputNum=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ums_activity_paycenter);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		trdata = (TransData) getIntent().getSerializableExtra(
				"trans");
		if(trdata == null) {
			tip = new Tip(PayCenterActivity.this, 
					getResources().getString(R.string.ums_pay_failed),
					null);
			tip.show(0);
			this.finish();
		}
		initView();
		initData();
	}
	
	
	private void setTitle(TransData trdata){
		switch (trdata.getTradeType()) {
		default:
			titleData.tvContent = getResources().getString(R.string.ums_pay_center);
			break;
		}
	}
	
  
	private void initView()
	{
		title = (Title) findViewById(R.id.title);
		
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		setTitle(trdata);
		title.setData(titleData, this);
		title.setZCTitleListner( new ZCTitleListener(){

			@Override
			public void onOperateBtnClick(int index) {
				// TODO 是否需要释放键盘和读卡器的串口
					dialogUtil.showProgressDialog();
					userCancel=true;
					LogUtil.d("用户取消交易！");
					if(cardTimeCount!=null){
						cardTimeCount.cancel();
					}
					if(pinpadTimeCount!=null){
						pinpadTimeCount.cancel();
					}
					
					if(cardExist){
						RingUtil.playRingtone(RingUtil.id_cancel);
					} 
			}
			
		});
		
		tv_timer = (TextView)findViewById(R.id.tv_timer);
		step1=(RelativeLayout) findViewById(R.id.step1);
		result =  (TextView) findViewById(R.id.result);
		cardTimeCount =new TimeCount(TimeNum.CardTimeUi, 1000,new TimeOutCallback(){
			@Override
			public void timeFinish() {
				dialogUtil.showProgressDialog();
				if(!cardInit){
					PayCenterActivity.this.finish();
				}
				userOperTimeOut=true;
    			if(cardExist){
    				RingUtil.playRingtone(RingUtil.id_failed);
    			}
    			LogUtil.d("操作卡超时！");
			}

			@Override
			public void onTick(long st) {
				tv_timer.setText(st /1000+"");
				
			}
		});
		iv_insert_card =(RelativeLayout) findViewById(R.id.iv_insert_card);
		card = (ImageView) findViewById(R.id.card);
		
		final Animation translateAnimation=new TranslateAnimation(0,0,-20,-66); 
		
		translateAnimation.setDuration(3000);
		translateAnimation.setRepeatCount(10);
		card.setAnimation(translateAnimation);               
        translateAnimation.startNow();                     
		
		step2=(RelativeLayout) findViewById(R.id.step2);
		tv_input =  (TextView) findViewById(R.id.tv_input);
		btn_card_confirm=  (Button) findViewById(R.id.btn_card_confirm);
		btn_card_confirm.setOnClickListener(this);
		
		pinpadTimeCount = new TimeCount(TimeNum.PinpadTimeUi, 1000,new TimeOutCallback(){
			@Override
			public void timeFinish() {
				dialogUtil.showProgressDialog();
				userOperTimeOut=true;
				if(cardExist){
    				RingUtil.playRingtone(RingUtil.id_failed);
    			}
				LogUtil.d("操作密码键盘超时！");
			}

			@Override
			public void onTick(long st) {
				tv_timer.setText(st /1000+"");
				
			}
		});
		
		step3=(RelativeLayout) findViewById(R.id.step3);
		
		tranRst.setCardNo("*");
		tradeData.setCardNo("*");
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
	}
	
	private void initData(){
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
		switchStep(1);
		iv_insert_card.setVisibility(View.VISIBLE);
		dialogUtil.showProgressDialog();
		cardTimeCount.start();
		tranRst.setTradeType(trdata.getTradeType());
		tranRst.setNoncestr(trdata.getNoncestr());
		tranRst.setTimestamp(trdata.getTimestamp());
	}
	
	private void initCard(){
		transControl = new TransControl(this, "", trdata.getAppType());
		LogUtil.d("初始化交易！");
		GlobalField.umsTransExsit=true;
		resultIntent1 =new Intent(PayCenterActivity.this,PayRstActivity.class);
		tranRst.setInitCard(0);
		if(initCardThread==null){
			initCardThread = new Thread(initCardRunnable);
			initCardThread.start();
		}
	}
	
	private void switchStep(int step)
	{
		if(step == 1)
		{
			step1.setVisibility(View.VISIBLE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.GONE);
			iv_insert_card.setVisibility(View.GONE);
		} else if(step == 2){        //输入密码
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.VISIBLE);
			step3.setVisibility(View.GONE);
		}else if(step == 3){        //结果
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.VISIBLE);
		}
		
	}
	

	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.btn_card_confirm:
				if(inputNum!=6) {
					tip = new Tip(PayCenterActivity.this, 
							getResources().getString(R.string.ums_pls_input_six_pwd),
    						null);
    				tip.show(0);
					return;
				} 
				inputPwdFinish = true;
				pinpadTimeCount.cancel();
				switchStep(3);
				titleData = title.new TitleData();
				titleData.backVisibility = 0;
				titleData.tvVisibility = true;
				setTitle(trdata);
				title.setData(titleData, this);
				result.setText(getResources().getString(R.string.ums_order_dealing));
				RingUtil.playRingtone(RingUtil.id_process_wait);
			break;
		}
	}
	
	
	
	
   @Override
	protected void onDestroy(){
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		cleanTrans();
		tradeData=null;
		transReq = null;
		transResp= null;
		cardTimeCount=null;
		pinpadTimeCount=null;	
		resultIntent1=null;
		//thread
		initCardThread=null;
		readCardStateThread=null;
		initPinThread=null;
		inputPinThread=null;
		tranThread=null;
	}
   
   
   @SuppressLint("HandlerLeak")
   private Handler myHandler = new Handler() {
		public void handleMessage (Message msg) {
			switch(msg.what) {
			case 0:
				initCard();
				break;
			case UmsTranType.cardInit:
			   RingUtil.playRingtone(RingUtil.id_insert_card);
               dialogUtil.closeProgressDialog();
               if(readCardStateThread==null){
            	   readCardStateThread = new Thread(readCardStateRunnable);
       			}
               	readCardStateThread.start();
				break;
			case UmsTranType.cardProc:
				cardTimeCount.cancel();
				if(initPinThread==null){
					initPinThread = new Thread(initPinRunnable);
					initPinThread.start();
      			}
				
				break;
			case UmsTranType.pinInit:
				pinpadTimeCount.start();
          	  	dialogUtil.closeProgressDialog();
          	  	switchStep(2);
          	  	RingUtil.playRingtone(RingUtil.id_input_passwd);
          	  	if(inputPinThread==null){
          	  		inputPinThread = new Thread(inputPinRunnable);
          	  		inputPinThread.start();
    			}
          	  	
				break;
			case UmsTranType.inputNum:
				String st= tv_input.getText().toString();
          	  	st+="*";
          	  	tv_input.setText(st);
          	  	if(inputNum == 6){
	          	  	inputPwdFinish = true;
					pinpadTimeCount.cancel();
					switchStep(3);
					titleData = title.new TitleData();
					titleData.backVisibility = 0;
					titleData.tvVisibility = true;
					setTitle(trdata);
					title.setData(titleData, PayCenterActivity.this);
					result.setText(getResources().getString(R.string.ums_order_dealing));
					RingUtil.playRingtone(RingUtil.id_process_wait);
          	  	}
				break;
			case UmsTranType.inputDel:
				String st1= tv_input.getText().toString();
          	  	tv_input.setText(st1.substring(1,st1.length()));
				break;
			case UmsTranType.inputEnt:
				pinpadTimeCount.cancel();
          	  	switchStep(3);
          	  	titleData = title.new TitleData();
				titleData.backVisibility = 0;
				titleData.tvVisibility = true;
				setTitle(trdata);
				title.setData(titleData, PayCenterActivity.this);
				result.setText(getResources().getString(R.string.ums_order_dealing));
				RingUtil.playRingtone(RingUtil.id_process_wait);
				if(tranThread==null){
					tranThread = new Thread(tranRunnable);
      			}
				tranThread.start();
				break;
			case UmsTranType.tranSuccess:
				String chin ="";
				String printinfo="";
				try {
					 chin= new String(transResp.getRspChin().getBytes("gb2312"),"UTF-8");
					 LogUtil.d(transResp.getRspChin());
					 printinfo = new String(transResp.getPrintInfo().getBytes("GBK"),"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tranRst.setRstCode(transResp.getRspCode());
        	  	tranRst.setRspChin(chin);
        	  	tranRst.setBankcode(transResp.getBankcode());
        	  	tranRst.setTrace(transResp.getTrace());
        	  	tranRst.setTermId(transResp.getTermId());
        	  	tranRst.setReference(transResp.getReference());
        	  	tranRst.setTransDate(transResp.getTransDate());
        	  	tranRst.setExp(transResp.getExp());
        	  	tranRst.setAmount(transResp.getAmount());
        	  	tranRst.setMchtId(transResp.getMchtId());
        	  	tranRst.setBatch(transResp.getBatch());
        	  	tranRst.setTransTime(transResp.getTransTime());
        	  	tranRst.setAuthNo(transResp.getAuthNo());
        	  	tranRst.setAppendField(transResp.getAppendField());
        	  	tranRst.setPrintInfo(printinfo);
        	  	
        	  	addFinishOrderPay(tranRst);
        	  	Bundle mBundle1 = new Bundle();  
        	  	mBundle1.putSerializable("tranRst", tranRst);
        	  	resultIntent1.putExtras(mBundle1); 
        	  	startActivity(resultIntent1);
        	  	cleanTrans();
				PayCenterActivity.this.finish();
				break;
			case UmsTranType.tranfailed:
	        	 int error_code= (Integer) msg.obj;
	        	 Intent resultIntent = new Intent(PayCenterActivity.this,CardQuitActivity.class);
	        	 tranRst.setErrorMsg(FunctionUtils.getErrorString(error_code));
	        	 addFinishOrderPay(tranRst);
	        	 LogUtil.d("交易失败，交易结束");
        	  	 Bundle mBundle = new Bundle();  
        	  	 mBundle.putSerializable("tranRst", tranRst);
        	  	 resultIntent.putExtras(mBundle); 
        	  	 startActivity(resultIntent);
        	  	 cleanTrans();
				 PayCenterActivity.this.finish();
				break;
			}
		}
	}; 
	
	private Runnable initCardRunnable = new Runnable() {

		@Override
		public void run() {
			Message msg = myHandler.obtainMessage();
			if (!transControl.initTrans()) {
				LogUtil.d("初始化交易失败");
				msg.what = UmsTranType.tranfailed;
				msg.obj = UmsErrorCode.ums_err_code1;
			} else {
//				transControl.initCardDev(0);
				LogUtil.d("初始化交易成功");
				if (!transControl.initCardDev(CommonDataDef.CardModePop,CommonDataDef.TouchCardType)) {
					msg.what = UmsTranType.tranfailed;
					msg.obj = UmsErrorCode.ums_err_code2;
					LogUtil.d("初始化读卡器失败");
				} else {
					cardInit=true;
					tranRst.setInitCard(1);
					LogUtil.d("初始化读卡器成功");
					msg.what = UmsTranType.cardInit;
				}
			}
			myHandler.sendMessage(msg);
		}  
		
	};
	
	private Runnable readCardStateRunnable = new Runnable() {

		@Override
		public void run() {
			Message msg = myHandler.obtainMessage();
			long now=System.currentTimeMillis();
			while(true){
				if (transControl.readCardStat(CommonDataDef.TouchCardType) == CardState.CARD_IN_DEV) {
					LogUtil.d("读卡器有卡");
					cardExist=true;
					cardNo = transControl.readCardNo(CommonDataDef.TouchCardType);
		      		if (cardNo.equals("0000000000000000000")) {
		      			LogUtil.d("读卡失败");
		      			msg.what = UmsTranType.tranfailed;
		      			msg.obj= UmsErrorCode.ums_err_code3;
		      		}else {
		      			msg.what = UmsTranType.cardProc;
		      			tranRst.setCardNo(cardNo);
		      			tradeData.setCardNo(cardNo);
		      			LogUtil.d("读卡成功,卡号："+cardNo);
		      		}
					break;
				}else {
					LogUtil.d("读卡器内无卡");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
				if (System.currentTimeMillis()-now > 20000) {
					LogUtil.d("读卡器超时");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code3;
					break;
				}
				if(userCancel){
					LogUtil.d("读卡时用户取消！");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code7;
					break;
				}
				if(userOperTimeOut){
					LogUtil.d("用户读卡操作超时！");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code10;
					break;
				}
			}
			myHandler.sendMessage(msg);
		}  
		
	};
	
	
	private Runnable initPinRunnable = new Runnable() {

		@Override
		public void run() {
			Message msg = myHandler.obtainMessage();
			if (!transControl.initPinpad()) {
				LogUtil.d("初始化密码键盘失败");
				msg.what = UmsTranType.tranfailed;
      			msg.obj= UmsErrorCode.ums_err_code4;
			} else {
				LogUtil.d("初始化密码键盘成功");
				pinInit=true;
				msg.what = UmsTranType.pinInit;
			}
			
			myHandler.sendMessage(msg);
		}  
		
	};
	
	private Runnable inputPinRunnable = new Runnable() {

		@Override
		public void run() {
			Message	msg =myHandler.obtainMessage();
			 while (true) {
	  			boolean isExit = false;
	  			switch (transControl.readPinByte()) {
	  			case 0x2A:
	  				LogUtil.d("您按了数字键");
	  				isExit = false;
	  				Message	msg1 =myHandler.obtainMessage();
	  				inputNum++;
	  				msg1.what = UmsTranType.inputNum;
	  				myHandler.sendMessage(msg1);
	  				break;
	  			case 0x08:
	  				LogUtil.d("您按了删除键");
	  				isExit = false;
	  				inputNum=0;
	  				Message	msg2 =myHandler.obtainMessage();
	  				msg2.what = UmsTranType.inputDel;
	  				myHandler.sendMessage(msg2);
	  				break;
	  			case 0x1B:
	  				LogUtil.d("您按了取消键");
	  				isExit = true;
	  				msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code7;
	  				break;
	  			case 0x0D:
	  				LogUtil.d("您按了确认键");
					isExit = true;
	  				msg.what = UmsTranType.inputEnt;
	  				break;
	  			case 0x02:
	  				LogUtil.d("输入密码超时");
	  				isExit = true;
	  				msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code6;
	  				break;
	  			default:
	  				LogUtil.d("请在键盘上输入");
	  				isExit = false;
	  				break;
	  			}
	  			if(userCancel){
	  				LogUtil.d("操作密码键盘时用户取消");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code7;
					break;
				}
	  			if(inputPwdFinish){
	  				msg.what = UmsTranType.inputEnt;
	  				break;
	  			}
	  			if(userOperTimeOut){
	  				LogUtil.d("操作密码键盘时操作超时");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code10;
					break;
				}
	  			
	  			if (isExit)break;	
	  			try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	  		}
			 
			 if(userCancel){
				 	LogUtil.d("操作密码键盘时用户取消");
					msg.what = UmsTranType.tranfailed;
					msg.obj=UmsErrorCode.ums_err_code7;
				}
			 
			
			myHandler.sendMessage(msg);
		}  
		
	};
	
	private Runnable tranRunnable = new Runnable() {

		@Override
		public void run() {
			Message msg = myHandler.obtainMessage();
			if (!transControl.getPinValue()) {
				LogUtil.d("密码键盘加密失败！");
				msg.what = UmsTranType.tranfailed;
				msg.obj=UmsErrorCode.ums_err_code8;
				} else{
					LogUtil.d("密码键盘加密成功！");
					try{
						transReq.setAppType(trdata.getAppType());
						if(!trdata.getAmount().equals("0")){
							transReq.setAmount(trdata.getAmount());
							LogUtil.d("金额：" +trdata.getAmount());
						}
    					transReq.setTransType(trdata.getTranType());
    					
    					if(!trdata.getApendCode().equals("0")){
    						transReq.setAppendFields(trdata.getApendCode());
    					}
    					transResp = transControl.bankall(transReq);
    					LogUtil.d("发起交易成功");
    					msg.what = UmsTranType.tranSuccess;
					} catch  (Exception e) {  
						LogUtil.d("发起交易失败");
						msg.what = UmsTranType.tranfailed;
						msg.obj=UmsErrorCode.ums_err_code9;
					}
				}
			myHandler.sendMessage(msg);
		}  
		
	};
	
	
		
	  private void addFinishOrderPay(TransRstData tranRst)
	  {
	      // "pay_id":"",
	      // "pay_type":1,
	      // "result":0,
	      // "error_msg":'',
	      // "pay_info":{
	      // }
		  	
		  	tradeData.setRstCode(tranRst.getRstCode());
		  	tradeData.setRspChin(tranRst.getRspChin());
		  	tradeData.setBankcode(tranRst.getBankcode());
		  	tradeData.setTrace(tranRst.getTrace());
		  	tradeData.setTermId(tranRst.getTermId());
		  	tradeData.setReference(tranRst.getReference());
		  	tradeData.setTransDate(tranRst.getTransDate());
		  	tradeData.setExp(tranRst.getExp());
		  	tradeData.setAmount(tranRst.getAmount());
		  	tradeData.setMchtId(tranRst.getMchtId());
		  	tradeData.setBatch(tranRst.getBatch());
		  	tradeData.setTransTime(tranRst.getTransTime());
		  	tradeData.setMobile(trdata.getPhone());
		  	tradeData.setPayId(trdata.getPayId());
		  	tradeData.setPayType(PayType.Ums_Pay+"");
		  	if(tranRst.getRstCode().equals("")){
	        	  tradeData.setResult(PayResultType.PAY_CANCEL+"");
	        	  tradeData.setError_msg("用户主动取消");
	          } else if(tranRst.getRstCode().equals("00")){
	        	  tradeData.setResult(PayResultType.PAY_SUCCESS+"");
	        	  tradeData.setError_msg("");
	          } else {
	        	  tradeData.setResult(PayResultType.PAY_FAILED+"");
	        	  tradeData.setError_msg(TranRstType.getRstCodeStr(tranRst.getRstCode()));
	          }
		  	tradeData.setNoncestr(tranRst.getNoncestr());
		  	tradeData.setTimestamp(tranRst.getTimestamp());
		  	tradeData.setState(PayRstTable.STATE_0);
		  	PayRstOp.addTradingFinishOrder(tradeData);
	  }
	  
	  private void cleanTrans(){
			
			if(cardTimeCount!=null){
				cardTimeCount.cancel();
			}
			if(pinpadTimeCount!=null){
				pinpadTimeCount.cancel();
			}
			
			if(cardInit){
					if (!transControl.closeCardDev(CommonDataDef.TouchCardType)) {
						LogUtil.d("关闭读卡器");
						transControl.closeCardDev(CommonDataDef.TouchCardType);
					}
					cardInit=false;
				}
			if(pinInit){
					if (!transControl.closePinpad()) {
						LogUtil.d("关闭密码键盘失败");
					}
					pinInit=false;
				}
			if(transControl!=null){
				transControl.clearTrans();
				LogUtil.d( "支付交易clean");
			}
			transControl=null;
			GlobalField.umsTransExsit=false;
		}
	
}
