package com.ebox.st.ui.lic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.CheckWorkReq;
import com.ebox.st.model.CheckWorkRsp;
import com.ebox.st.model.JhsyCommit;
import com.ebox.st.model.LnydCommit;
import com.ebox.st.model.RefuseModel;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.AddImagActivity;
import com.ebox.st.ui.refuseActivity;

public class AgreementActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	private TextView tv_agreement;
	private Button btn_ok;
	private RelativeLayout tv_top;
	
	private IdcardModel idcard=null;
	// 证照类型
	private String type;
	
	private DialogUtil dialogUtil;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_fg_lic_agreement);
		MGViewUtil.scaleContentView(this, R.id.rootView);
		idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
		type = getIntent().getStringExtra("work_type");
        initView();
        initData();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		title.setData(titleData, this);
		
		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		if(type.equals(WorkType.Jhsy))
		{
			titleData.tvContent = getResources().getString(R.string.jhsy_licenses);
			tv_agreement.setText("请准备好以下材料开始办理：\n 1.您的身份证和户口本\n 2.您配偶的身份证和户口本\n 3.您的孩子的户口本、出生证明、\n    准生证\n 4.离婚协议书（如果您是再婚）");
		}
		else
		{
			titleData.tvContent = getResources().getString(R.string.st_lic_lnyd);
			tv_agreement.setText("请准备好以下材料开始办理：\n 1.身份证和户口本\n 2.居住证\n 3.照片回执");
		}
		
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		tv_top = (RelativeLayout) findViewById(R.id.tv_top);
		tv_top.setVisibility(View.GONE);
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
	}
	
	private void initData(){
		dialogUtil.showProgressDialog();
		checkWork();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		title.showTimer();
		AppService.getIntance().hasOnKeyDown();
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
			//清理下
			GlobalField.licData.clearData();
			
			if(type.equals(WorkType.Jhsy))
			{
				if(GlobalField.licData.getJhsy() == null)
				{
					GlobalField.licData.setJhsy(new JhsyCommit());
				}
                GlobalField.licData.getJhsy().setIdcard(idcard);
				GlobalField.licData.setType(type);
                Intent intent = new Intent(this,JhsyMarryStateActivity.class);
                intent.putExtra("who", 0);
                startActivity(intent);
			}
			else
			{
				if(GlobalField.licData.getLnyd() == null)
				{
					GlobalField.licData.setLnyd(new LnydCommit());
				}
                GlobalField.licData.getLnyd().setIdcard(idcard);
				GlobalField.licData.setType(type);

                Intent intent = new Intent(this,AddImagActivity.class);
                intent.putExtra("add_image_type",AddImagActivity.type_0);
                startActivity(intent);

			}
			break;
		}
		
	}
	
	

    private void checkWork()
    {
        CheckWorkReq req = new CheckWorkReq();
        req.setIdcard(idcard);
        req.setIdentification(type);
        req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
        ApiClient.checkWork(this,req,new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                dialogUtil.closeProgressDialog();
                CheckWorkRsp rsp = (CheckWorkRsp)data;
                dealCheckRst(rsp);
            }

            @Override
            public void onFailed(Object data) {
                dialogUtil.closeProgressDialog();
                finish();
            }
        });
    }


    private void dealCheckRst(CheckWorkRsp rsp)
    {
        if(!rsp.getSuccess())
        {
			tip = new Tip(this,
					getResources().getString(R.string.st_contact_admin),null);
			tip.show(0);

            return;
        }
        int state = rsp.getResult().get(0).getState();
        // 办理中
        if (state == 0) {


            Intent intent = new Intent(this,LicJdcxActivity.class);
            Bundle mBundle = new Bundle();
			mBundle.putSerializable("idcard", idcard);
			intent.putExtras(mBundle);
            intent.putExtra("work_type",type);
            startActivity(intent);
            finish();

        }
        // 审核不通过
        else if (state == 1) {
            //TODO 需要查询目前不通过的项，来办理
            //GlobalField.refuseData =
            GlobalField.refuseData = new RefuseModel();
            Workflow wf = null;

            wf = rsp.getResult().get(0).getData();

            String order_id = "";
            String remark = "";
            order_id = rsp.getResult().get(0).getOrder_id();
            if (order_id == null) {
                order_id = "";
            }
            remark = rsp.getResult().get(0).getRemark();
            if (remark == null) {
                remark = "";
            }
            GlobalField.refuseData.setSn(order_id);

            if (wf != null) {
                GlobalField.refuseData.setWorkFlow(wf);
                LogUtil.d("mf", wf.getWorkflow().size() + "");
                GlobalField.refuseData.setStepNo(1);
                dealRefuse(remark);
            }

        }
        // 不存在
        else {
            tv_top.setVisibility(View.VISIBLE);
        }
    }





	
	private void dealRefuse(String state)
	{
        Intent intent = new Intent(this,refuseActivity.class);
        intent.putExtra("refuse_state",state);
        this.finish();
        startActivity(intent);
	}
	
}
