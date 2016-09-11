package com.ebox.st.ui.lic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
import com.ebox.pub.ui.customview.Title.ZCTitleListener;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.AttaModel;
import com.ebox.st.model.SubmitCertificationReq;
import com.ebox.st.model.SubmitCertificationRsp;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.AddImagActivity;
import com.ebox.st.ui.IdCardActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ebox.pub.boxctl.BoxOp.resultListener;

public class LnydPutFileActivity extends CommonActivity implements
	OnClickListener,resultListener {
	private static final String TAG = "LnydPutFileFragment";
	private Button bt_box;
	private Button bt_put_item;
	private EditText et_telephone;
	private DialogUtil dialogUtil;
	private KeyboardUtil keyBoard;
	private Tip tip;
	private Context mContext;
	private boolean canOp = true; // 解决bt_box和bt_put_item同时点击造成的BUG
	
	private BoxInfoType checkBox = null;
	private static Handler handler;
	private static Runnable run;
	public final static int REQUEST_CODE_1 = 1;
	// 快递员不点击确认，自动重试
	private boolean hasItem = false;
	
	private Long boxFee = 0L;
	private int chkeckTime=0;
	
	private CancelDeliverDialog cancelDialog;
	
	private Title title;
	private TitleData titleData;
	private Boolean isEdit = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = LnydPutFileActivity.this;
        setContentView(R.layout.st_lnyd_putfile);
        MGViewUtil.scaleContentView(this,R.id.rootView);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        initView();
		RingUtil.playRingtone(RingUtil.st026);
	}
	

	public void onResume() {
		super.onResume();
		title.showTimer();
		
		et_telephone.requestFocus();
		keyBoard = new KeyboardUtil(this,mContext, et_telephone);
		keyBoard.showKeyboard();
		
		handler = new MyHandler();
		//显示选择的格子
		showCheckBox();
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
		// 关闭摄像头
		AppApplication.getInstance().getCc().release();
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
	
	private void initView() {
		
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_putfile);
		title.setData(titleData, this);
		
		title.setZCTitleListner(new ZCTitleListener() {
			
			@Override
			public void onOperateBtnClick(int index) 
			{
				if(isEdit)
				{
					int step = GlobalField.refuseData.getStepNo();
					if(step>1)
					{
						GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
					}
					
				}
				finish();
				
			}
		});
		
		et_telephone = (EditText) findViewById(R.id.et_telephone);
		EditTextUtil.limitCount(et_telephone, 11, null);
		KeyboardUtil.hideInput(this, et_telephone);
		keyBoard = new KeyboardUtil(this,mContext, et_telephone);
		keyBoard.showKeyboard();
		
		bt_box = (Button) findViewById(R.id.bt_box);
		bt_put_item = (Button) findViewById(R.id.bt_put_item);
		
		bt_box.setOnClickListener(this);
		bt_put_item.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
	}
	
	
	@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_box:
			if (canOp) {
                Intent intent = new Intent(this,SelectBoxActivity.class);
                intent.putExtra("isAccount",false);
                startActivityForResult(intent, REQUEST_CODE_1);
				setCantOperate();
			}
			break;
		case R.id.bt_put_item:
			
			if (canOp && checkPara()) {
				BoxInfo box = BoxUtils.getBoxByCode(checkBox.getBoxCode());
		
				if (BoxUtils.getBoxLocalState(box)
						.equals(DoorStatusType.unknow)) {
					Log.e(TAG, "Box state error:" + checkBox.getBoxCode());
					tip = new Tip(mContext, getResources()
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
			Log.i(GlobalField.tag, "SlectBox:" + checkBox.getBoxCode()
		            + " boxFee:" + boxFee);
		}
		setCanOperate();
	}
	
	
	private void showPrompt(int resId) {
		tip = new Tip(LnydPutFileActivity.this, getResources().getString(
				resId), null);
		tip.show(0);
	}
	
	private boolean checkPara() {
		if (et_telephone.getText() == null
				|| !FunctionUtil.validMobilePhone(et_telephone.getText().toString())) {
			showPrompt(R.string.pub_telephone_error);
			return false;
		}
		
		if (checkBox == null) {
            showPrompt(R.string.ex_select_box);
			return false;
		}
		return true;
	}
	
	
	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				this.postDelayed(run, 500);
			}
		}
	}
	
	
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
	
	public void saveParams() {
		if (hasItem) {
			saveOrderReq();
		}
	}
	
	private void resetData(){
		setCanOperate();
		checkBox = null;
		et_telephone.setText("");	
		bt_box.setText(getResources().getString(R.string.ex_select_box));
	}
	
	
	private void saveOrderReq(){
		
		Workflow workFlow = new Workflow();
		
		workFlow.setId(Integer.valueOf(GlobalField.licData.getType()));
		workFlow.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		workFlow.setWorkflow(new ArrayList<WorkStep>());
		
		// 第一步： 办理人身份证和手机号码
		WorkStep step1 = new WorkStep();
		step1.setId(1);
		step1.setFields(new ArrayList<WorkFields>());
		
		WorkFields fields1 = new WorkFields();
		WorkField field1 = new WorkField();
		field1.setKey("realname");
		field1.setType("idcard");
		field1.setValue(GlobalField.licData.getLnyd().getIdcard());
		fields1.setFields(new ArrayList<WorkField>());
		fields1.getFields().add(field1);
		
		WorkField field2 = new WorkField();
		field2.setKey("telephone");
		field2.setType("String");
		field2.setValue(et_telephone.getText().toString());
		fields1.getFields().add(field2);
		step1.getFields().add(fields1);
		
		workFlow.getWorkflow().add(step1);
		
		// 第2/3/4/5步： 证件扫描
		List<AttaModel> attaList = GlobalField.licData.getLnyd().getAtta();
		
		for(int i = 0; i < attaList.size(); i++)
		{
			WorkStep step = new WorkStep();
			step.setId(i+2);
			step.setFields(new ArrayList<WorkFields>());
			
			WorkFields fields = new WorkFields();
			WorkField field = new WorkField();
			field.setKey(attaList.get(i).getKey());
			field.setType("attachment");
			field.setValue(attaList.get(i).getAttachmentid());
			fields.setFields(new ArrayList<WorkField>());
			fields.getFields().add(field);
			step.getFields().add(fields);
			workFlow.getWorkflow().add(step);
		}
		
		// 第6步：存入照片回执
		WorkStep step6 = new WorkStep();
		step6.setId(6);
		step6.setFields(new ArrayList<WorkFields>());
		
		WorkFields fields = new WorkFields();
		WorkField field = new WorkField();
		field.setKey("boxId");
		field.setType("String");
		field.setValue(checkBox.getBoxCode());
		fields.setFields(new ArrayList<WorkField>());
		fields.getFields().add(field);
		step6.getFields().add(fields);
		workFlow.getWorkflow().add(step6);

		SubmitCertificationReq req = new SubmitCertificationReq();
		req.setData(workFlow);
		ApiClient.submitCertification(this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				SubmitCertificationRsp rsp = (SubmitCertificationRsp) data;
				dialogUtil.closeProgressDialog();
				dealSubmitCfSuccess(rsp);
			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
				dealSubmitCfFailed();
			}
		});
	}

	private void dealSubmitCfSuccess(SubmitCertificationRsp rsp)
	{
		if (rsp!=null && rsp.getSuccess())
		{
			saveOrder();
			resetData();
			RingUtil.playRingtone(RingUtil.st017);
			tip = new Tip(this, getResources().getString(
					R.string.st_lnyd_putfile_success),
					new Tip.onDismissListener() {
						@Override
						public void onDismiss() {
							finish();
						}

					});
			tip.show(0);
		}
		else
		{
			tip = new Tip(this,
					getResources().getString(R.string.st_contact_admin),null);
			tip.show(0);
		}
	}

	private void dealSubmitCfFailed()
	{
		new Dialog(this,
				getResources().getString(R.string.pub_sure_to_retry),
				new Dialog.onClickListener() {
					@Override
					public void onOk(Object value) {
						saveOrderReq();
					}

					@Override
					public void onCancel(Object value) {
						if (isEdit) {
							finish();
						} else {
							cancelDeliver();
						}

					}
				},
				null);
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
	
	
	@Override
	public void onResult(final int result) {
		dialogUtil.closeProgressDialog();

		if (result == RstCode.Success) {
			Log.i(GlobalField.tag, "openDoor success");
			hasItem = true;
			// 播放音效
			//RingUtil.playRingtone(RingUtil.deliver_id);
			BoxInfo info = BoxUtils.getBoxByCode(checkBox.getBoxCode());
			RingUtil.playChooseDoorHaveOpened(info.getBoardNum(), info.getBoxNum(),false);

			String boxId=checkBox.getBoxCode();
			cancelDialog = new CancelDeliverDialog(this,
					BoxUtils.getBoxDesc(checkBox.getBoxCode()),
					new CancelDeliverDialog.onClickListener() {
						@Override
						public void onCancel(Object value) {
							setCanOperate();
							handler.removeCallbacks(run);
							if (checkBox!=null)
							{
								cancelDeliver();
								//设置更改的checkbox
								GlobalField.refuseData.setCheckBox(null);
							}else {
								bt_box.setText(getResources().getString(R.string.ex_select_box));
							}
							Log.i(GlobalField.tag, "onCancel");
						}

						@Override
						public void onOk(Object value) {
							setCanOperate();
							handler.removeCallbacks(run);
							Log.i(GlobalField.tag, "onOk");
//							saveOrderReq();
							gotoCommit();
						}
					}, null);
			cancelDialog.setShowData(et_telephone.getText().toString(),"");
			cancelDialog.notShowItem();
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
								RingUtil.playRingtone(RingUtil.st034);
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
	
	private void gotoCommit(){
		Intent intent = new Intent(this,LnydCommitActivity.class);
		Bundle mBundle = new Bundle();
		intent.putExtra("isEdit",isEdit);
		hasItem =false;
		if(isEdit)
		{
			int step = GlobalField.refuseData.getStepNo();
			Workflow workFlow = GlobalField.refuseData.getWorkFlow();
			WorkStep step1 = workFlow.getWorkflow().get(step-1);
			WorkFields fields1 = step1.getFields().get(0);
			WorkField field1 = fields1.getFields().get(0);
			if(step1.getId() == 6)
			{
				GlobalField.refuseData.setCheckBox(checkBox);
				field1.setValue(checkBox.getBoxCode());
				mBundle.putSerializable("checkBox", checkBox);
			}
			if(workFlow.getWorkflow().size() > step)
			{
				GlobalField.refuseData.setStepNo(step+1);
				int stepno2 = GlobalField.refuseData.getStepNo();
				WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
				gotoFrag(step2.getId());
			} 
			else 
			{
				resetData();
			}
			
		}
		else 
		{
			GlobalField.licData.getLnyd().setBoxId(checkBox.getBoxCode());
			GlobalField.licData.getLnyd().setTelephone(et_telephone.getText().toString());
			mBundle.putSerializable("checkBox", checkBox);
			resetData();

		}
		startActivity(intent);
		finish();
	}


	private void dealImage(int imageType){
		Intent intent = new Intent(this, AddImagActivity.class);
		intent.putExtra("add_image_type",imageType);
		intent.putExtra("isEdit", true);
		startActivity(intent);
	}
	
	private void dealIdcard(String workTye,int idcardType){
		Intent intent = new Intent(this, IdCardActivity.class);
		intent.putExtra("idcard_type",idcardType);
		intent.putExtra("work_type",workTye);
		intent.putExtra("isEdit",true);
		startActivity(intent);
	}
	

	private void gotoFrag(int step)
	{
		switch (step) {
		case 1:
			dealIdcard(WorkType.Lnyd,IdCardActivity.type_4);
			break;
		case 2:
			dealImage(AddImagActivity.type_0);
			break;
		case 3:
			dealImage(AddImagActivity.type_1);
			break;
		case 4:
			dealImage(AddImagActivity.type_2);
			break;
		case 5:
			dealImage(AddImagActivity.type_3);
			break;
		case 6:
		{
			Intent intent = new Intent(this,LnydPutFileActivity.class);
			intent.putExtra("isEdit",true);
			startActivity(intent);
			break;
		}
		case 7:
			dealIdcard(WorkType.Jhsy,IdCardActivity.type_4);
			break;
		case 8:
		{
			Intent intent = new Intent(this,JhsyMarryStateActivity.class);
			intent.putExtra("who",0);
			intent.putExtra("isEdit",true);
			startActivity(intent);
			break;
		}
			
		case 9:
			dealIdcard(WorkType.Jhsy,IdCardActivity.type_9);
			break;
		case 10:
		{
			Intent intent = new Intent(this,JhsyMarryStateActivity.class);
			intent.putExtra("who", 1);
			intent.putExtra("isEdit", true);
			startActivity(intent);
			break;
		}
			
		case 11:
			dealImage(AddImagActivity.type_4);
			break;
		case 12:
			dealImage(AddImagActivity.type_5);
			break;
		case 13:
			dealImage(AddImagActivity.type_7);
			break;
		case 14:
		{
			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit",true);
			startActivity(intent);
			break;
		}
		case 15:
			dealImage(AddImagActivity.type_13);
			break;
		case 16:
			dealImage(AddImagActivity.type_14);
			break;

		default:
			break;
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

