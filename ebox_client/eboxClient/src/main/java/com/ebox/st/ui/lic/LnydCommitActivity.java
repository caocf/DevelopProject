package com.ebox.st.ui.lic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.TopActivity;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.AttaModel;
import com.ebox.st.model.SubmitCertificationReq;
import com.ebox.st.model.SubmitCertificationRsp;
import com.ebox.st.model.SubmitEditCertificationReq;
import com.ebox.st.model.SubmitEditCertificationRsp;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.network.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class LnydCommitActivity extends CommonActivity implements OnClickListener,BoxOp.resultListener {
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	private Button btn_ok;
	
	private DialogUtil dialogUtil;
	
	private BoxInfoType checkBox = null;
	private Boolean isEdit = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_lnyd_commit);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		isEdit = getIntent().getBooleanExtra("isEdit",false);
		checkBox = (BoxInfoType)getIntent().getSerializableExtra("checkBox");
        initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = "确认提交";
		title.setData(titleData, this);
		title.setZCTitleListner(new Title.ZCTitleListener() {
			
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
					if(GlobalField.refuseData.getCheckBox()!=null)
					{
						cancelDeliverByEdit();
					}
					
				}
				else 
				{
					cancelDeliver();
				}
				finish();
			}
		});

		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
		isEdit = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		AppService.getIntance().hasOnKeyDown();
		title.showTimer();
	}
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}

	private long curTime;
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_ok:
			dialogUtil.showProgressDialog();
			if (System.currentTimeMillis()-curTime>2000) {
				curTime=System.currentTimeMillis();
				if(isEdit)
				{
					saveEditOrder();
				} else
				{
					saveOrderReq();
				}
			}

			break;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		field2.setValue(GlobalField.licData.getLnyd().getTelephone());
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
		field.setValue(GlobalField.licData.getLnyd().getBoxId());
		fields.setFields(new ArrayList<WorkField>());
		fields.getFields().add(field);
		step6.getFields().add(fields);
		workFlow.getWorkflow().add(step6);


        SubmitCertificationReq req = new SubmitCertificationReq();
        req.setData(workFlow);
        ApiClient.submitCertification(this,req,new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                SubmitCertificationRsp rsp = (SubmitCertificationRsp)data;
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
            if (checkBox != null) {
                saveOrder(checkBox);
            }

            RingUtil.playRingtone(RingUtil.st017);
            tip = new Tip(this, getResources().getString(
                    R.string.st_lnyd_putfile_success),
                    new Tip.onDismissListener() {
                        @Override
                        public void onDismiss() {
                           gotoTopActivity();
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
                           gotoTopActivity();
                        } else {
                            cancelDeliver();
                        }

                    }
                },
                null);
    }

	
	/*
	 * 更新订单接口
	 */

	private void saveEditOrder()
	{
        GlobalField.refuseData.getWorkFlow().setTerminal_code(AppApplication.getInstance().getTerminal_code());
        SubmitEditCertificationReq req = new SubmitEditCertificationReq();
        req.setData(GlobalField.refuseData.getWorkFlow());
        req.setOrder_id(GlobalField.refuseData.getSn());
        ApiClient.submitEditCertification(this,req,new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                SubmitEditCertificationRsp rsp = (SubmitEditCertificationRsp)data;
                dialogUtil.closeProgressDialog();
                dealSubmitEditCfSuccess(rsp);
            }

            @Override
            public void onFailed(Object data) {
                dealSubmitEditCfFailed();
            }
        });

	}


    private void dealSubmitEditCfSuccess(SubmitEditCertificationRsp rsp)
    {
        if (rsp!=null && rsp.getSuccess())
        {
            if(GlobalField.refuseData.getCheckBox() != null)
            {
                saveOrder(GlobalField.refuseData.getCheckBox());
            }
            RingUtil.playRingtone(RingUtil.st017);
            tip = new Tip(this, getResources().getString(
                    R.string.st_lnyd_putfile_edit_success),
                    new Tip.onDismissListener()
                    {
                        @Override
                        public void onDismiss() {
                            gotoTopActivity();
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

    private void dealSubmitEditCfFailed()
    {
        new Dialog(this,
                getResources().getString(R.string.pub_sure_to_retry),
                new Dialog.onClickListener(){
                    @Override
                    public void onOk(Object value) {
                        saveEditOrder();
                    }

                    @Override
                    public void onCancel(Object value) {
                        if(GlobalField.refuseData.getCheckBox() !=null)
                        {
                            cancelDeliverByEdit();
                        }
                        gotoTopActivity();
                    }
                },
                null);
    }
	
	private void cancelDeliver() {
		String boxId = GlobalField.licData.getLnyd().getBoxId();
		Log.i(GlobalField.tag, "openDoor:" + boxId);
		// 开门
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(boxId));
		BoxCtrlTask.addBoxCtrlQueue(op);
	}
	
	private void cancelDeliverByEdit() {
		String boxId = GlobalField.refuseData.getCheckBox().getBoxCode();
		Log.i(GlobalField.tag, "openDoor:" + boxId);
		// 开门
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(boxId));
		BoxCtrlTask.addBoxCtrlQueue(op);
	}
	
	/**
	*保存订单
	*/
	private void saveOrder(BoxInfoType boxinfo) {
	
//		String boxId=GlobalField.licData.getLnyd().getBoxCode();
		String boxId=boxinfo.getBoxCode();

		//保存本地的对应box的状态status为已占用
		BoxDynSyncOp.boxLock(boxId);
	}

	@Override
	public void onResult(final int result) {
		dialogUtil.closeProgressDialog();

        if (result == RstCode.Success) {
            Log.i(GlobalField.tag, "openDoor success");
            // 播放音效
            //RingUtil.playRingtone(RingUtil.deliver_id);
            BoxInfo info = BoxUtils.getBoxByCode(checkBox.getBoxCode());
            RingUtil.playChooseDoorHaveOpened(info.getBoardNum(), info.getBoxNum(),false);
            finish();

        }
		
	}

    private void gotoTopActivity()
    {
        Intent intent = new Intent(this, TopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
