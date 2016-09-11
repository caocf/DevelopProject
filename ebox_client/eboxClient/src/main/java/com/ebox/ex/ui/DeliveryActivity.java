package com.ebox.ex.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.ex.database.deliver.DeliverOp;
import com.ebox.ex.database.deliver.DeliverTable;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.OrderImageType;
import com.ebox.ex.ui.bar.CancelDeliverDialog;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOp.resultListener;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.camera.CameraData;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.service.task.report.AutoReportManager;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.ui.keyboard.KeyboardUtil.onKeyListener;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.TimeUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.pub.utils.Tip.onDismissListener;

public class DeliveryActivity extends CommonActivity implements
		OnClickListener, onKeyListener, resultListener {
	private static final String TAG = "DeliveryActivity";
	private TextView et_barcode;
	private EditText et_telephone;
	private EditText et_telephone_again;
	private Button bt_box;
	private Button bt_put_item;
	private DialogUtil dialogUtil;
	private CancelDeliverDialog cancelDialog;

	private BoxInfoType checkBox = null;
	private  Handler handler;
	private static Runnable run;
	public final static int REQUEST_CODE_1 = 1;
	private CameraData cameraData;
	private String barcode;
	private KeyboardUtil keyBoard;

	// 快递员不点击确认，自动重试
	private boolean hasItem = false;
	private Dialog mDialog = null;
	private boolean canOp = true; // 解决bt_box和bt_put_item同时点击造成的BUG

	private Tip tip;
	private Long boxFee = 0L;
	private int chkeckTime=0;
    private String operatorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		barcode = getIntent().getStringExtra("barcode");
        operatorId = getIntent().getStringExtra("operatorId");
		setContentView(R.layout.ex_activity_deliver);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
		// 开启摄像头
		AppApplication.getInstance().getCc().start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}

	@Override
	protected void onDestroy() {
		// 关闭摄像头
		AppApplication.getInstance().getCc().release();
		if (handler != null) {
			handler.removeCallbacks(run);
			handler = null;
		}

		if (mDialog != null) {
			mDialog.closeDialog();
		}

		if (cancelDialog != null) {
			cancelDialog.closeDialog();
		}

		dialogUtil.closeProgressDialog();

		if (tip != null) {
			tip.closeTip();
		}
		checkBox=null;
		super.onDestroy();
	}

	private void initView() {
		et_barcode = (TextView) findViewById(R.id.et_barcode);
		et_telephone = (EditText) findViewById(R.id.et_telephone);
		et_telephone.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				keyBoard = new KeyboardUtil(DeliveryActivity.this,
						DeliveryActivity.this, et_telephone);
				keyBoard.showKeyboard();
				keyBoard.setonKeyListener(DeliveryActivity.this);
				keyBoard.setNumberAudio(true);
				return false;
			}
		});
		et_telephone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (et_telephone.hasFocus()) {
					et_telephone
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					et_telephone.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}

		});
		KeyboardUtil.hideInput(this, et_telephone);
		et_telephone
				.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

		et_telephone_again = (EditText) findViewById(R.id.et_telephone_again);
		et_telephone_again.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				keyBoard = new KeyboardUtil(DeliveryActivity.this,
						DeliveryActivity.this, et_telephone_again);
				keyBoard.showKeyboard();
				keyBoard.setonKeyListener(null);
				keyBoard.setNumberAudio(true);
				return false;
			}
		});
		KeyboardUtil.hideInput(this, et_telephone_again);

		keyBoard = new KeyboardUtil(DeliveryActivity.this,
				DeliveryActivity.this, et_telephone);
		keyBoard.showKeyboard();
		keyBoard.setonKeyListener(DeliveryActivity.this);
		keyBoard.setNumberAudio(true);
		bt_box = (Button) findViewById(R.id.bt_box);
		bt_put_item = (Button) findViewById(R.id.bt_put_item);

		// 单号最多30位
		et_barcode.setText(barcode);
		// 电话号码最多11位
		EditTextUtil.limitCount(et_telephone, 11, null);
		EditTextUtil.limitCount(et_telephone_again, 11, null);
		et_telephone.requestFocus();

		bt_box.setOnClickListener(this);
		bt_put_item.setOnClickListener(this);

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);

		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_op_delivery);
		data.tvVisibility=true;
		title.setData(data, this);
	}

	private boolean checkPara() {
		if (et_barcode.getText() == null
				|| et_barcode.getText().toString().length() == 0) {
			tip = new Tip(DeliveryActivity.this, getResources().getString(
					R.string.ex_input_barcode), null);
			tip.show(0);
			return false;
		}

		if (checkBox == null) {
			tip = new Tip(DeliveryActivity.this, getResources().getString(
					R.string.ex_select_box), null);
			tip.show(0);
			return false;
		}

		if (et_telephone.getText() == null
				|| !FunctionUtil.validMobilePhone(et_telephone.getText().toString())) {
			tip = new Tip(DeliveryActivity.this, getResources().getString(
					R.string.pub_telephone_error), null);
			tip.show(0);
			return false;
		}

		if (et_telephone_again.getText() == null
				|| !et_telephone_again.getText().toString()
						.equals(et_telephone.getText().toString())) {
			tip = new Tip(DeliveryActivity.this, getResources().getString(
					R.string.pub_input_not_same), null);
			tip.show(0);
			return false;
		}

		return true;
	}

	private void ConfirmDelivery() {


		saveOrder();

		tip = new Tip(DeliveryActivity.this, getResources().getString(
				R.string.ex_delivery_success), new onDismissListener() {
			@Override
			public void onDismiss() {
//			//上报订单
			AutoReportManager.instance().reportConfirmDelivery();
				finish();
			}
		});
		tip.getDialog().setCancelable(false);
		tip.show(0);
	}
	
	private void setCantOperate()
	{
		bt_put_item.setClickable(false);
		bt_box.setClickable(false);
		canOp = false;
	}
	
	private void setCanOperate()
	{
		bt_put_item.setClickable(true);
		bt_box.setClickable(true);
		canOp = true;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_box:
			if (canOp) {
				Intent intent = new Intent(DeliveryActivity.this,
						SelectBoxActivity.class);
                intent.putExtra("operatorId",operatorId);
				startActivityForResult(intent, REQUEST_CODE_1);
				setCantOperate();
			}
			break;
		case R.id.bt_put_item:
			
			if (canOp && checkPara()) {
				BoxInfo box = BoxUtils.getBoxByCode(checkBox.getBoxCode());

				if (BoxUtils.getBoxLocalState(box).equals(DoorStatusType.unknow))
				{
					Log.e(TAG, "Box state error:" + checkBox.getBoxCode());
					tip = new Tip(DeliveryActivity.this, getResources()
							.getString(R.string.ex_system_err), null);
					tip.show(0);
				} else {
					Log.i(GlobalField.tag, "openDoor:" + checkBox.getBoxCode());
					//记录关门检测次数为0次
					chkeckTime=0;
					// 开门
					BoxOp op = new BoxOp();
					op.setOpId(BoxOpId.OpenDoor);
					op.setOpBox(box);
					op.setReport(2);//不用存入告警数据库
					op.setLock(2);//锁定箱门
					op.setListener(this);
					BoxCtrlTask.addBoxCtrlQueue(op);
					dialogUtil.showProgressDialog();
					setCantOperate();
				}
			}
			break;
		}
	}

	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				this.postDelayed(run, 500);
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_1) {
			if (resultCode == Activity.RESULT_CANCELED) {
			}
			else
			{
				if (data != null)
				{
					checkBox = (BoxInfoType) data.getSerializableExtra("box");
					boxFee = data.getLongExtra("fee", 0L);

					if (checkBox != null)
					{
						bt_box.setText(BoxUtils.getBoxSizeDesc(checkBox)
								+ BoxUtils.getBoxDesc(checkBox.getBoxCode()));
						Log.i(GlobalField.tag, "Select BoxCode:" + checkBox.getBoxCode()+ " boxFee:" + boxFee);
					}

					//播放选择箱门
					//String boxId = checkBox.getBoxCode();
					//BoxInfo info = BoxUtils.getBoxByCode(boxId);
					//RingUtil.playChooseDoor(info.getBoardNum(), info.getBoxNum());
				}
			}
			setCanOperate();
		}
	}

	private void cancelDeliver() {
		Log.i(GlobalField.tag, "cancel deliver openDoor:" + checkBox.getBoxCode());
		// 开门
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(checkBox.getBoxCode()));
		BoxCtrlTask.addBoxCtrlQueue(op);
		// 播放音效
		RingUtil.playRingtone(RingUtil.cancel_id);

		checkBox = null;
		hasItem = false;
		bt_box.setText(getResources().getString(R.string.ex_select_box));
		setCanOperate();
	}

	@Override
	public void onKey(int primaryCode) {
		if (et_telephone.getText() != null
				&& et_telephone.getText().toString().length() >= 11) {
			et_telephone_again.requestFocus();
			keyBoard = new KeyboardUtil(DeliveryActivity.this,
					DeliveryActivity.this, et_telephone_again);
			keyBoard.showKeyboard();
			keyBoard.setonKeyListener(null);
			keyBoard.setNumberAudio(true);

			// et_telephone.setInputType(InputType.TYPE_CLASS_TEXT |
			// InputType.TYPE_TEXT_VARIATION_PASSWORD);
		} else {
			// et_telephone.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}

	public void saveParams() {
		if (hasItem) {
			saveOrder();
		}
	}

	/**
	 *本地存储订单
	 */
	private void saveOrder() {

		try {
//			String boxCode=checkBox.getBoxCode();
//			//预防坏件处理后，本地订单未删除
//			OrderLocalInfoOp.deleteLocalOrder(boxCode);

			hasItem = false;
			// 本地存储订单
			Deliver deliver = new Deliver();
			String order_id = TimeUtil.orderTime() + AppApplication.getInstance().getTerminal_code();
			deliver.setOrder_id(order_id);

			deliver.setBox_code(checkBox.getBoxCode());
			deliver.setItemId(et_barcode.getText().toString());
			if (operatorId==null||operatorId.equals(""))
			{
				operatorId=OperatorHelper.mPhone;
			}
			deliver.setOperatorId(operatorId);
			deliver.setTelephone(et_telephone.getText().toString());
			deliver.setState(DeliverTable.STATE_0);
			deliver.setFee(boxFee.intValue());
			deliver.setTime(System.currentTimeMillis()+"");
			//添加到数据
			DeliverOp.CreateDeliver(deliver);

			//更新本地的对应box的状态state为已占用
			//BoxDynSyncOp.boxLock(boxCode);

			Log.i(GlobalField.tag, "save order, orderId:" + order_id + ",boxCode" + checkBox.getBoxCode());

			//update balance
			Long balance = OperatorHelper.getOperatorBalance(operatorId)-boxFee;
			OperatorOp.updateOperatorBalance(operatorId, balance, 0);

			// 拍照
			cameraData = new CameraData();
			cameraData.setOrderId(order_id);
			cameraData.setItemId(et_barcode.getText().toString());
			cameraData.setImageType(OrderImageType.STORE);
			AppApplication.getInstance().getCc().takePicture(cameraData, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResult(int result) {
		dialogUtil.closeProgressDialog();
		if (result == RstCode.Success)
		{
			Log.i(GlobalField.tag, "openDoor success "+checkBox.getBoxCode());
			hasItem = true;
			// 播放音效
			//RingUtil.playRingtone(RingUtil.deliver_id);
			BoxInfo info = BoxUtils.getBoxByCode(checkBox.getBoxCode());
			RingUtil.playChooseDoorHaveOpened(info.getBoardNum(), info.getBoxNum());
			
			String  phone=et_telephone.getText().toString();
			String itemId=et_barcode.getText().toString();

			cancelDialog = new CancelDeliverDialog(DeliveryActivity.this,
					BoxUtils.getBoxDesc(checkBox.getBoxCode()),
					new com.ebox.ex.ui.bar.CancelDeliverDialog.onClickListener() {
						@Override
						public void onCancel(Object value) {
							setCanOperate();
							handler.removeCallbacks(run);
							if (checkBox!=null)
							{
								cancelDeliver();
							} else {
								bt_box.setText(getResources().getString(R.string.ex_select_box));
							}
							title.showTimer();
							Log.i(GlobalField.tag, "onCancel");
						}

						@Override
						public void onOk(Object value) {
							setCanOperate();
							handler.removeCallbacks(run);
							ConfirmDelivery();
						}
					}, null);
			cancelDialog.setShowData(phone,itemId);
			cancelDialog.cantOp();
			handler = new MyHandler();
			// 定时检测箱门状态
			run = new Runnable() {
				@Override
				public void run() {
					if (checkBox != null) {
						BoxInfo box = BoxUtils
								.getBoxByCode(checkBox.getBoxCode());

						// 获取箱门状态
						BoxOp op = new BoxOp();
						op.setOpId(BoxOpId.GetDoorsStatus);
						op.setOpBox(box);
						BoxCtrlTask.addBoxCtrlQueue(op);

						// 箱门关闭
						if (BoxUtils.getBoxLocalState(box) == DoorStatusType.close) 
						{
							chkeckTime++;
							Log.i(GlobalField.tag, "box close " +box.getBoardNum() +box.getBoxNum());
							if (chkeckTime<2) {
								handler.sendEmptyMessage(0);
							}else{//检测到关门两次后让用户存件
								RingUtil.playRingtone(RingUtil.confirm_id);
								cancelDialog.canOp();
								setCanOperate();
								handler.removeCallbacks(run);
							}
						} else {
							handler.sendEmptyMessage(0);
						}
					}
				}
			};
			handler.sendEmptyMessageDelayed(0, 500);
		} else {
			Log.i(GlobalField.tag, "open door failed!");
			// 播放音效
			RingUtil.playRingtone(RingUtil.choose_id);
			checkBox = null;
			hasItem = false;
			bt_box.setText(getResources().getString(R.string.ex_select_box));
			setCanOperate();
		}
	}
}
