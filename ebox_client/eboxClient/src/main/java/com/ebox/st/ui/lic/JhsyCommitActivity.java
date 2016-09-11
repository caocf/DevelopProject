package com.ebox.st.ui.lic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.TopActivity;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.ChildrenModel;
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

public class JhsyCommitActivity extends CommonActivity implements OnClickListener{
	private Tip tip;

	private Title title;
	private TitleData titleData;
	private Button btn_ok;

	private DialogUtil dialogUtil;
	private Boolean isEdit = false;
	private long curTime=0L;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_jhsy_commit);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        MGViewUtil.scaleContentView(this,R.id.rootView);
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
					if(step > 1)
					{
						GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
					}
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

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_ok:
			dialogUtil.showProgressDialog();
			if (System.currentTimeMillis()-curTime>2000)
			{
				curTime=System.currentTimeMillis();
				if(isEdit)
				{
					saveEditOrder();
				}else {
					commit();
				}
			}

			break;
		}

	}

	private void commit()
	{

		Workflow workFlow = new Workflow();

		workFlow.setId(Integer.valueOf(GlobalField.licData.getType()));
		workFlow.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		workFlow.setWorkflow(new ArrayList<WorkStep>());

		// 第1步： 办理人身份证
		WorkStep step1 = new WorkStep();
		step1.setId(7);
		step1.setFields(new ArrayList<WorkFields>());

		WorkFields fields1 = new WorkFields();
		WorkField field1 = new WorkField();
		field1.setKey("idcard_up");
		field1.setType("idcard");
		field1.setValue(GlobalField.licData.getJhsy().getIdcard());
		fields1.setFields(new ArrayList<WorkField>());
		fields1.getFields().add(field1);
		step1.getFields().add(fields1);
		workFlow.getWorkflow().add(step1);

		// 第2步： 用户A信息录入
		WorkStep step2 = new WorkStep();
		step2.setId(8);
		step2.setFields(new ArrayList<WorkFields>());

		WorkFields fields2 = new WorkFields();
		WorkField field21 = new WorkField();
		field21.setKey("telephone");
		field21.setType("String");
		field21.setValue(GlobalField.licData.getJhsy().getTelephone());
		fields2.setFields(new ArrayList<WorkField>());
		fields2.getFields().add(field21);

		WorkField field22 = new WorkField();
		field22.setKey("marriage_state");
		field22.setType("String");
		field22.setValue(GlobalField.licData.getJhsy().getMarryState());
		fields2.getFields().add(field22);
		step2.getFields().add(fields2);
		workFlow.getWorkflow().add(step2);

		// 第3步： 身份证B
		WorkStep step3 = new WorkStep();
		step3.setId(9);
		step3.setFields(new ArrayList<WorkFields>());


		WorkFields fields31 = new WorkFields();
		WorkField field31 = new WorkField();
		field31.setKey("idcard_up");
		field31.setType("idcard");
		field31.setValue(GlobalField.licData.getJhsy().getOIdcard());
		fields31.setFields(new ArrayList<WorkField>());
		fields31.getFields().add(field31);
		step3.getFields().add(fields31);
		workFlow.getWorkflow().add(step3);

		// 第4步：用户B信息录入
		WorkStep step4 = new WorkStep();
		step4.setId(10);
		step4.setFields(new ArrayList<WorkFields>());


		WorkFields fields4 = new WorkFields();
		WorkField field41 = new WorkField();
		field41.setKey("telephone");
		field41.setType("String");
		field41.setValue(GlobalField.licData.getJhsy().getoTelephone());
		fields4.setFields(new ArrayList<WorkField>());
		fields4.getFields().add(field41);

		WorkField field42 = new WorkField();
		field42.setKey("marriage_state");
		field42.setType("String");
		field42.setValue(GlobalField.licData.getJhsy().getoMarryState());
		fields4.getFields().add(field42);
		step4.getFields().add(fields4);
		workFlow.getWorkflow().add(step4);

		// 第5步：结婚证
		WorkStep step5 = new WorkStep();
		step5.setId(11);
		step5.setFields(new ArrayList<WorkFields>());

		WorkFields fields5 = new WorkFields();
		WorkField field5 = new WorkField();
		field5.setKey("marriage_cert");
		field5.setType("attachment");
		field5.setValue(GlobalField.licData.getJhsy().getMarriage_attachmentid());
		fields5.setFields(new ArrayList<WorkField>());
		fields5.getFields().add(field5);
		step5.getFields().add(fields5);
		workFlow.getWorkflow().add(step5);

		// 第6步：A方户口扫描
		WorkStep step6 = new WorkStep();
		step6.setId(12);
		step6.setFields(new ArrayList<WorkFields>());

		WorkFields fields6 = new WorkFields();
		WorkField field61 = new WorkField();
		field61.setKey("index");
		field61.setType("attachment");
		field61.setValue(GlobalField.licData.getJhsy().getHouse_index_attachmentid());
		fields6.setFields(new ArrayList<WorkField>());
		fields6.getFields().add(field61);

		WorkField field62 = new WorkField();
		field62.setKey("user");
		field62.setType("attachment");
		field62.setValue(GlobalField.licData.getJhsy().getHouse_user_attachmentid());
		fields6.getFields().add(field62);
		step6.getFields().add(fields6);

		workFlow.getWorkflow().add(step6);

		// 第7步：B方户口扫描
		WorkStep step7 = new WorkStep();
		step7.setId(13);
		step7.setFields(new ArrayList<WorkFields>());

		WorkFields fields7 = new WorkFields();
		WorkField field71 = new WorkField();
		field71.setKey("index");
		field71.setType("attachment");
		field71.setValue(GlobalField.licData.getJhsy().getHouse2_index_attachmentid());
		fields7.setFields(new ArrayList<WorkField>());
		fields7.getFields().add(field71);

		WorkField field72 = new WorkField();
		field72.setKey("user");
		field72.setType("attachment");
		field72.setValue(GlobalField.licData.getJhsy().getHouse2_user_attachmentid());
		fields7.getFields().add(field72);
		step7.getFields().add(fields7);

		workFlow.getWorkflow().add(step7);


		// 第8步：孩子信息;有孩子信息才传
		ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();

		if(pList.size() > 1)
		{
			WorkStep step8 = new WorkStep();
			step8.setId(14);
			step8.setFields(new ArrayList<WorkFields>());



			for(int i = 0; i < pList.size(); i++)
			{
				if(!pList.get(i).getIsAdd())
				{
					WorkFields fields8 = new WorkFields();
					WorkField fieldx1 = new WorkField();
					fieldx1.setKey("household_index");
					fieldx1.setType("attachment");
					fieldx1.setValue(pList.get(i).getHukouFirst());
					fields8.setFields(new ArrayList<WorkField>());
					fields8.getFields().add(fieldx1);

					WorkField fieldx2 = new WorkField();
					fieldx2.setKey("household_show");
					fieldx2.setType("attachment");
					fieldx2.setValue(pList.get(i).getHukouSecond());
					fields8.getFields().add(fieldx2);

					WorkField fieldx3 = new WorkField();
					fieldx3.setKey("birth_cert");
					fieldx3.setType("attachment");
					fieldx3.setValue(pList.get(i).getChusheng());
					fields8.getFields().add(fieldx3);

					WorkField fieldx4 = new WorkField();
					fieldx4.setKey("birth_show");
					fieldx4.setType("attachment");
					fieldx4.setValue(pList.get(i).getZhunsheng());
					fields8.getFields().add(fieldx4);

					step8.getFields().add(fields8);
				}
			}

			workFlow.getWorkflow().add(step8);
		}


		// 第9步：离婚协议
		String attach = GlobalField.licData.getJhsy().getDivorce_attachmentid();
		if( attach !=null && !attach.equals("") )
		{
			WorkStep step9 = new WorkStep();
			step9.setId(15);
			step9.setFields(new ArrayList<WorkFields>());

			WorkFields fields9 = new WorkFields();
			WorkField field9 = new WorkField();
			field9.setKey("divorce_a");
			field9.setType("attachment");
			field9.setValue(GlobalField.licData.getJhsy().getDivorce_attachmentid());
			fields9.setFields(new ArrayList<WorkField>());
			fields9.getFields().add(field9);
			step9.getFields().add(fields9);
			workFlow.getWorkflow().add(step9);
		}


		// 第10步：配额离婚协议
		String attach2 = GlobalField.licData.getJhsy().getDivorce2_attachmentid();
		if( attach2 !=null && !attach2.equals("") )
		{
			WorkStep step10 = new WorkStep();
			step10.setId(16);
			step10.setFields(new ArrayList<WorkFields>());

			WorkFields fields10 = new WorkFields();
			WorkField field10 = new WorkField();
			field10.setKey("divorce_b");
			field10.setType("attachment");
			field10.setValue(GlobalField.licData.getJhsy().getDivorce2_attachmentid());
			fields10.setFields(new ArrayList<WorkField>());
			fields10.getFields().add(field10);
			step10.getFields().add(fields10);
			workFlow.getWorkflow().add(step10);
		}

        SubmitCertificationReq req = new SubmitCertificationReq();
        req.setData(workFlow);
        ApiClient.submitCertification(this,req,new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                SubmitCertificationRsp rsp = (SubmitCertificationRsp)data;
                dialogUtil.closeProgressDialog();
                if(rsp!=null && rsp.getSuccess())
                {
                    RingUtil.playRingtone(RingUtil.st017);
                    tip = new Tip(JhsyCommitActivity.this, getResources().getString(
                            R.string.st_jhsy_success),
                            new Tip.onDismissListener() {
                                @Override
                                public void onDismiss() {
                                    finish();
                                    gotoTopActivity();
                                }

                            });
                    tip.show(0);
                }
                else
                {
					tip = new Tip(JhsyCommitActivity.this,
							getResources().getString(R.string.st_contact_admin),null);
					tip.show(0);
                }


            }

            @Override
            public void onFailed(Object data) {
                dialogUtil.closeProgressDialog();
                new Dialog(JhsyCommitActivity.this,
                        getResources().getString(R.string.pub_sure_to_retry),
                        new Dialog.onClickListener() {
                            @Override
                            public void onOk(Object value) {
                                if (isEdit) {
                                    saveEditOrder();
                                } else {
                                    commit();
                                }

                            }

                            @Override
                            public void onCancel(Object value) {
                                finish();
                                gotoTopActivity();
                            }
                        },
                        null);
            }
        });
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

            RingUtil.playRingtone(RingUtil.st017);
            tip = new Tip(this, getResources().getString(
                    R.string.st_jhsy_success),
                    new Tip.onDismissListener()
                    {
                        @Override
                        public void onDismiss() {
                            finish();
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
                        finish();
                        gotoTopActivity();
                    }
                },
                null);
    }

    private void gotoTopActivity()
    {
        Intent intent = new Intent(JhsyCommitActivity.this, TopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
