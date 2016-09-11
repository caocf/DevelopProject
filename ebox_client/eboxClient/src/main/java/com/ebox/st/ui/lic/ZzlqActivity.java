package com.ebox.st.ui.lic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOp.resultListener;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.TakeCertificationReq;
import com.ebox.st.model.TakeCertificationRsp;
import com.ebox.st.network.http.ApiClient;

public class ZzlqActivity extends CommonActivity implements OnClickListener,resultListener {
	private TextView tv_prop;
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	
	private IdcardModel idcard;
	
	private String boxId;
	private Context mContext;
	
	// 证照类型
	private String type;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ZzlqActivity.this;
		setContentView(R.layout.st_fg_zjlq_rst);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
		type = getIntent().getStringExtra("work_type");
		boxId = getIntent().getStringExtra("boxId");
		initView();

	}

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.lic_get);
		title.setData(titleData, this);
		tv_prop = (TextView) findViewById(R.id.tv_prop);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		
	}
	
	private void initData(){
		getLicBoxId();
	}
	
	
	/*
	 * 取件申请开哪个门
	 */
	
	private void getLicBoxId() {

		TakeCertificationReq req = new TakeCertificationReq();
		req.setIdcard(idcard);
		req.setIdentification(type);
		req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		ApiClient.takeCertification(this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				dialogUtil.closeProgressDialog();
                TakeCertificationRsp rsp = (TakeCertificationRsp)data;
				showData(rsp);
			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
			}
		});
		
	}

	private void showData(TakeCertificationRsp rsp)
	{
		if (rsp!= null && rsp.getSuccess())
		{
			if (rsp.getResult().size() > 0) {
				boxId = rsp.getResult().get(0).getBox_code();
				if (boxId != null) {
					openDoor(boxId);
					tv_prop.setText("您的证件在" + BoxUtils.getBoxDesc(boxId)
							+ "," + getResources().getString(R.string.st_pickup_success));
				}
			} else {
				tip = new Tip(mContext, "没有您需要领取的证件", new Tip.onDismissListener() {
					@Override
					public void onDismiss() {
						finish();
					}
				});
				tip.show(0);
			}
		}
		else
		{
            tip = new Tip(mContext, rsp.getMessage(), new Tip.onDismissListener() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
            tip.show(0);

		}
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(tip != null)
		{
			tip.closeTip();
		}
		if(dialogUtil !=null){
			dialogUtil.closeProgressDialog();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AppService.getIntance().setTimeoutLeft(15);
		title.showTimer();
		initData();
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
			
			break;
		}
	}
	
	private void openDoor(String boxId)
	{
		Log.i(GlobalField.tag, "openDoor:"+boxId);
		// 开门
		BoxOp op =  new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(boxId));
		op.setListener(this);
		BoxCtrlTask.addBoxCtrlQueue(op);
	}
	
	
	@Override
	public void onResult(final int result) {
		dialogUtil.closeProgressDialog();
		
		if(result == RstCode.Success && boxId!= null)
		{
			Log.i(GlobalField.tag, "openDoor success");
				dealPickupItem(boxId);
			//BoxDynSyncOp.boxRelease(boxId);
		}
		else
		{

			tip = new Tip(mContext,
					"开门失败，请联系管理员取件",
					null);
			tip.show(0);
			if(boxId!= null){
				Log.i(GlobalField.tag, "onLine openDoor failed,orderId:"+boxId);
			}
			// 播放音效
			RingUtil.playRingtone(RingUtil.calladmin_id);
		}
	}
	
	private void dealPickupItem(String boxId)
	{
		// 播放音效
		BoxInfo info = BoxUtils.getBoxByCode(boxId);
		RingUtil.playPickUpDoorOpened(info.getBoardNum(), info.getBoxNum());
	}
}
