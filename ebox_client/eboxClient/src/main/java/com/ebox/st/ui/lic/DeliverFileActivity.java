package com.ebox.st.ui.lic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.SelectBoxActivity;
import com.ebox.ex.ui.bar.CancelDeliverDialog;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.CarryCertificationReq;
import com.ebox.st.model.CarryCertificationRsp;
import com.ebox.st.model.IdentiOrderModel;
import com.ebox.st.network.http.ApiClient;

public class DeliverFileActivity extends CommonActivity implements
	OnClickListener,BoxOp.resultListener {
	private static final String TAG = "DeliverFileFragment";
	private Button bt_box;
	private Button bt_put_item;
	private TextView tv_order;
	private DialogUtil dialogUtil;
	private Tip tip;
	private boolean canOp = true; // 解决bt_box和bt_put_item同时点击造成的BUG
	
	private BoxInfoType checkBox = null;
	private static Handler handler;
	private static Runnable run;
	public final static int REQUEST_CODE_1 = 1;
	// 快递员不点击确认，自动重试
	private boolean hasItem = false;
	
	private IdentiOrderModel mIdentiOrder=null;

	private int chkeckTime=0;
	
	private CancelDeliverDialog cancelDialog;
	private String barcode;
	
	private Title title;
	private TitleData titleData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_deliver_file_fragment);
        mIdentiOrder = (IdentiOrderModel)getIntent().getSerializableExtra("IdentiOrderModel");
        barcode = getIntent().getStringExtra("barcode");
        initView();
        MGViewUtil.scaleContentView(this,R.id.rootView);
	}
	

	private void initView()
	{
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = "存文件";
		title.setData(titleData, this);
		
		bt_box = (Button) findViewById(R.id.bt_box);
		bt_put_item = (Button) findViewById(R.id.bt_put_item);
		tv_order = (TextView) findViewById(R.id.tv_order);
		
		bt_box.setOnClickListener(this);
		bt_put_item.setOnClickListener(this);

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
	}

	
	public void onResume() {
		super.onResume();
		handler = new MyHandler();
		//显示选择的格子
		showCheckBox();
		title.showTimer();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}
	
	@Override
	public void onDestroy() {
		//保存未点击确认的订单
		saveParams();

		if (handler != null) {
			handler.removeCallbacks(run);
			handler = null;
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
	
	public void saveParams() {
		if (hasItem) {
			saveOrderReq();
		}
	}
	
	private boolean checkPara() {
		if (checkBox == null) {
			tip = new Tip(this, getResources().getString(
					R.string.ex_select_box), null);
			tip.show(0);
			return false;
		}

		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_box:
			if (canOp)
            {
                Intent intent = new Intent(this,
                        SelectBoxActivity.class);
                intent.putExtra("isAccount",false);
                startActivityForResult(intent, REQUEST_CODE_1);
				setCantOperate();
			}
			break;
		case R.id.bt_put_item:
			
			if (canOp&& checkPara()) {
				BoxInfo box = BoxUtils.getBoxByCode(checkBox.getBoxCode());

				if (BoxUtils.getBoxLocalState(box)
						.equals(DoorStatusType.unknow)) {
					Log.e(TAG, "Box state error:" + checkBox.getBoxCode());
					tip = new Tip(this, getResources()
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
					op.setListener(this);
					BoxCtrlTask.addBoxCtrlQueue(op);
					dialogUtil.showProgressDialog();
					setCantOperate();
				}
			}
			break;
		
		}
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
	

    private void showCheckBox(){
    	
    	if (checkBox != null) {
			bt_box.setText(BoxUtils.getBoxSizeDesc(checkBox)
					+ BoxUtils.getBoxDesc(checkBox.getBoxCode()));
			Log.i(GlobalField.tag, "SlectBox:" + checkBox.getBoxCode());
		}
		setCanOperate();
		if(mIdentiOrder!=null){
			tv_order.setText("办理人身份证号：\n" + mIdentiOrder.getIdcard().getIdcard());
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

	private void cancelDeliver() {
		title.showTimer();
		Log.i(GlobalField.tag, "openDoor:" + checkBox.getBoxCode());
		// 开门
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(checkBox.getBoxCode()));
		BoxCtrlTask.addBoxCtrlQueue(op);

		checkBox = null;
		hasItem = false;
		bt_box.setText(getResources().getString(R.string.ex_select_box));
		setCanOperate();
	}
	
	private void resetData(){
		setCanOperate();
		checkBox = null;
		mIdentiOrder = null;
		tv_order.setText("");
		bt_box.setText(getResources().getString(R.string.ex_select_box));
	}
	
	
	private void saveOrderReq(){
		
        CarryCertificationReq req = new CarryCertificationReq();
        req.setBarcode(barcode);
        req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
        req.setBoxId(checkBox.getBoxCode());
        ApiClient.carryCertification(this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				CarryCertificationRsp rsp = (CarryCertificationRsp) data;
				dialogUtil.closeProgressDialog();
				if (rsp.getSuccess()) {
					saveOrder();
					resetData();
					tip = new Tip(DeliverFileActivity.this, getResources().getString(
							R.string.ex_delivery_success), new Tip.onDismissListener() {
						@Override
						public void onDismiss() {
							finish();
						}
					});
					tip.show(0);
				} else {
					tip = new Tip(DeliverFileActivity.this,
							getResources().getString(R.string.st_contact_admin), null);
					tip.show(0);

				}

			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
				cancelDeliver();
			}
		});
	}

	/**
	 *保存订单
	 */
	private void saveOrder() {
		
		String boxId=checkBox.getBoxCode();
		hasItem = false;
		
		//保存本地的对应box的状态status为已占用
		BoxDynSyncOp.boxLock(boxId);

	}
	
	private void ConfirmDelivery() {
		
		saveOrderReq();
	}

	@Override
	public void onResult(final int result) {
		dialogUtil.closeProgressDialog();

        if (result == RstCode.Success) {
            Log.i(GlobalField.tag, "openDoor success");
            hasItem = true;
            // 播放音效
            //RingUtil.playRingtone(RingUtil.deliver_id);
            BoxInfo info = BoxUtils.getBoxByCode(checkBox.getBoxCode());
            RingUtil.playChooseDoorHaveOpened(info.getBoardNum(), info.getBoxNum(), false);

            String itemId=barcode;
            String boxId=checkBox.getBoxCode();
            cancelDialog = new CancelDeliverDialog(DeliverFileActivity.this,
                    BoxUtils.getBoxDesc(checkBox.getBoxCode()),
                    new CancelDeliverDialog.onClickListener() {
                        @Override
                        public void onCancel(Object value) {
                            setCanOperate();
                            handler.removeCallbacks(run);
                            if (checkBox!=null)
                            {
                                cancelDeliver();
                            }else {
                                bt_box.setText(getResources().getString(R.string.ex_select_box));
                            }
                            Log.i(GlobalField.tag, "onCancel");
							title.showTimer();
                        }

                        @Override
                        public void onOk(Object value) {
                            setCanOperate();
                            handler.removeCallbacks(run);
                            Log.i(GlobalField.tag, "onOk");
                            ConfirmDelivery();
                        }
                    }, null);
            cancelDialog.setShowData(mIdentiOrder.getTelephone(),itemId);
            cancelDialog.cantOp();
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
                            Log.i(GlobalField.tag, "Box close");
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
            Log.i(GlobalField.tag, "openDoor failed!");
            // 播放音效
            RingUtil.playRingtone(RingUtil.choose_id);
            checkBox = null;
            hasItem = false;
            bt_box.setText(getResources().getString(R.string.ex_select_box));
            setCanOperate();
        }
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == Activity.RESULT_CANCELED) {
            } else {
                if (data != null) {
                    checkBox = (BoxInfoType) data.getSerializableExtra("box");

                    if (checkBox != null) {
                        bt_box.setText(BoxUtils.getBoxSizeDesc(checkBox)
                                + BoxUtils.getBoxDesc(checkBox.getBoxCode()));
                    }
                    Long  boxFee = data.getLongExtra("fee", 0L);

                    Log.i(GlobalField.tag, "SlectBox:" + checkBox.getBoxCode()
                            + " boxFee:" + boxFee);
                    //播放选择箱门
                    //String boxId = checkBox.getBoxCode();
                    //BoxInfo info = BoxUtils.getBoxByCode(boxId);
                    //RingUtil.playChooseDoor(info.getBoardNum(), info.getBoxNum());
                }
            }
            setCanOperate();
        }
    }
}
