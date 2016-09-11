package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.database.operator.OperatorTable;
import com.ebox.ex.network.model.RspLoginOperator;
import com.ebox.ex.network.model.base.type.OperatorType;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.model.enums.OperatorStatus;
import com.ebox.ex.network.model.req.ReqVerifyOperator;
import com.ebox.ex.network.request.LoginRequest;
import com.ebox.mgt.ui.SuperLoginActivity;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.GetCardNo;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.TimeUtil;
import com.ebox.pub.utils.Tip;

public class OperatorLoginByCardActivity extends CommonActivity implements
		OnClickListener,ResponseEventHandler<RspLoginOperator> {
	private static final String TAG = "OperatorLoginByCard";
	private Button btn_switch;
	private DialogUtil dialogUtil;
	private Tip tip;
	private int loginType;// 0:离线登陆 1:网络登陆
	public TextView tv_timer;
	public int clickTimes = 0;
    private Title title;
    private TitleData data;

    private String mCardId =null;
    private String operatorId = null;
    private Thread getCardIdThread=null;

    private boolean isCardUsed = false;
    private boolean isFinished = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_operator_card_login);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		 title.showTimer();
		// 开启摄像头
		AppApplication.getInstance().getCc().start();
        myHandler.sendEmptyMessage(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
        GetCardNo.cancelRead();
        isCardUsed = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		if (tip != null) {
			tip.closeTip();
		}
        isFinished = true;
        GetCardNo.cancelRead();
        isCardUsed = false;
        if(getCardIdThread !=null){
            myHandler.removeCallbacks(getCardIdRunnable);
            getCardIdThread = null;
        }
	}

	private void initView() {
        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(this);

		tv_timer = (TextView) findViewById(R.id.tv_timer);
		tv_timer.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);

		initTitle();
	}

	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_operator_login);
		data.tvVisibility=true;
		title.setData(data, this);
	}



    /*
    获取operator cardId
     */
    private void initCardIdThread()
    {

        if(!isCardUsed)
        {
            isCardUsed = true;
            getCardIdThread = new Thread(getCardIdRunnable);
            getCardIdThread.start();

        }

//        myHandler.sendEmptyMessageDelayed(0,1000);
    }

    private Runnable getCardIdRunnable = new Runnable() {

        @Override
        public void run() {
            mCardId = GetCardNo.getCardNo();
            Message msg = myHandler.obtainMessage();
            if (mCardId!=null && !mCardId.equals(""))
            {
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
            if(!isFinished)
            {
                myHandler.sendEmptyMessage(0);
            }
        }
    };


    private void dealCardRst()
    {
        Log.d(TAG,mCardId+"");

        GetCardNo.cancelRead();
        isCardUsed = false;

        if (!AppApplication.getInstance().isSystemOk().equals("ok")) {
            tip = new Tip(OperatorLoginByCardActivity.this, getResources()
                    .getString(R.string.ex_system_init), null);
            tip.show(0);
            return;
        }

        onLineOperator(mCardId);
    }

    private Handler myHandler = new Handler() {
        public void handleMessage (Message msg) {
            switch(msg.what) {
                case 0:
                    initCardIdThread();
                    break;
                case 1:
                    dealCardRst();
                    break;
            }
        }
    };





	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_switch:
        {
            Intent intent = new Intent(OperatorLoginByCardActivity.this,OperatorLoginActivity.class);
            startActivity(intent);

            break;
        }
		case R.id.tv_timer:
		{
			clickTimes++;
			if(clickTimes >= 5)
			{
				Intent intent = new Intent(OperatorLoginByCardActivity.this,SuperLoginActivity.class);
				startActivity(intent);
			}
			break;
		}
		}
	}

	private void showPrompt(int resId) {
		tip = new Tip(OperatorLoginByCardActivity.this, getResources().getString(
				resId), null);
		tip.show(0);
	}

	// 离线操作
	private void OperatorAfterOnline() {
		dialogUtil.closeProgressDialog();
		// 缓存用户信息
		OperatorInfo info = OperatorOp.getOperatorByCardId(mCardId);
		
		if(info == null) {
			showPrompt(R.string.pub_connect_failed);
			return;
		}

			//删除锁定和未激活的数据
		if(info.getOperatorStatus() == OperatorStatus.Blocked 
				|| info.getOperatorStatus() == OperatorStatus.UnActivate ) {
			OperatorOp.deleteOperatorById(info.getOperatorId());
			showPrompt(R.string.pub_connect_failed);
			return;
		}

		// 计费
		int isAccount = GlobalField.serverConfig.getIsAccount();
		
		if(info.getBalance()<=0 
				&& isAccount == AcountType.is_acount) {
			LogUtil.i(TAG, info.getOperatorId()+",余额不足,"+info.getBalance() +","+ TimeUtil.currentTime());
			showPrompt(R.string.pub_connect_failed);
			return;
		}
		// 账号密码正确，登陆成功
		Log.i(TAG, "offline login success," + TimeUtil.currentTime() + "," + info.getOperatorId());
		loginType = 0;
        goToOperatorMainActivity();
		
	}



	private void onLineOperator(String mCardId)
	{
		ReqVerifyOperator req=new ReqVerifyOperator();
        req.setCard(mCardId);
		LoginRequest request=new LoginRequest(req,this);
		executeRequest(request, this);
		dialogUtil.showProgressDialog();
		loginType = 1;
	}

	private void goToOperatorMainActivity()
	{
		Toast.makeText(AppApplication.globalContext, R.string.ex_login_success,Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(OperatorLoginByCardActivity.this,
				OperatorHomeActivity.class);
		intent.putExtra("loginType", loginType);
        intent.putExtra("operatorId",operatorId);
		startActivity(intent);
		// 读取最新门状态
		BoxCtrlTask.readBoxStatus();
		OperatorLoginByCardActivity.this.finish();
	}

	private void saveOperatorInfo(OperatorType operator) {
		OperatorInfo info = new OperatorInfo();
		info.setOperatorId(operator.getTelephone());
		info.setOperatorName(operator.getOperator_name());
		info.setPassword("");

		info.setReserveStatus(operator.getReserve_status());//预留功能是否可用
		info.setOperatorStatus(operator.getStatus());// 用户的状态
		info.setBalance(operator.getBalance());
		info.setTelephone(operator.getTelephone());
		info.setState(OperatorTable.STATE_1);

		info.setCardId(mCardId);

		// 添加至本地表中
		short s = OperatorOp.addOperator(info);
		if (s == 1) {
			LogUtil.i("add operator success," + info.getOperatorId() + ",balance:" + info.getBalance());
		} else if (s == -1) {
			LogUtil.e("add operator failed," + info.getOperatorId() + ",balance:" + info.getBalance());
		}
	}
	@Override
	public void onResponseSuccess(RspLoginOperator result) {
		if (result.isSuccess()) {
			LogUtil.i("online login success by card");
			dialogUtil.closeProgressDialog();
			OperatorType operatorRsp = result.getData().getUser();
			int operatorState = operatorRsp.getStatus();
			String operator_telephone = operatorRsp.getTelephone();
			//快递员账户状态
			switch (operatorState)
			{
				case OperatorStatus.Activate:
					saveOperatorInfo(operatorRsp);
					//检测快递员激活
					goToOperatorMainActivity();
					break;
				case OperatorStatus.UnActivate:
					showPrompt(R.string.ex_account_not_active);
					//删除本地未激活的快递员信息
					OperatorOp.deleteOperatorById(operator_telephone);
					break;
				case OperatorStatus.Blocked:
					showPrompt(R.string.ex_account_locked);
					//删除本地锁定的快递员信息
					OperatorOp.deleteOperatorById(operator_telephone);
					break;
				default:
					break;
			}
		} else {
			OperatorAfterOnline();
		}
	}

	@Override
	public void onResponseError(VolleyError error) {
		OperatorAfterOnline();
	}
}
