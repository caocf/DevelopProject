package com.ebox.st.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.customview.Title.ZCTitleListener;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.ActivityRstCode;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.lic.AddChildrenActivity;
import com.ebox.st.ui.lic.AgreementActivity;
import com.ebox.st.ui.lic.JhsyCommitActivity;
import com.ebox.st.ui.lic.JhsyMarryStateActivity;
import com.ebox.st.ui.lic.LicJdcxActivity;
import com.ebox.st.ui.lic.LnydCommitActivity;
import com.ebox.st.ui.lic.LnydPutFileActivity;
import com.ebox.st.ui.lic.ZzlqActivity;


public class IdCardActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	private Thread readIdCardThread=null;
	private boolean isStop = false;
	private IdcardModel idcard=null;
	private DialogUtil dialogUtil;
	
	
	private Context mContext;
	private Title title;
	private TitleData titleData;
	private ImageView st_idcard_left;
	private int type = 0;
	private Boolean isEdit = false;
	/* 3：人口信息录入
	 * 4:老年优待证办理 5:在线留言  6: 人口信息录入增加家庭成员 7：进度查询
	 * 8:证照领取 9:配偶身份证 
	 */
	
	public final static int type_0 = 0;
	public final static int type_1 =1;
	public final static int type_2 =2;
	public final static int type_3 =3;
	public final static int type_4 =4;//修改身份证信息
	public final static int type_5 =5;
	public final static int type_6 =6;
	public final static int type_7 =7;
	public final static int type_8 =8;
	public final static int type_9 =9;
	
	private String workType = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = IdCardActivity.this;
        type = getIntent().getIntExtra("idcard_type",0);
        workType = getIntent().getStringExtra("work_type");
        isEdit = getIntent().getBooleanExtra("isEdit",false);
		if(type == type_6)
		{
			RingUtil.playRingtone(RingUtil.st028);
		}
		else if(type == type_9)
		{
			RingUtil.playRingtone(RingUtil.st005);
		}
		else
		{
			RingUtil.playRingtone(RingUtil.st001);
		}

		setContentView(R.layout.st_fg_idcard);
		MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
		initData();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_idcard);
		if(type == type_6)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_pls_blush_your_family_idcard);
		}
		else if(type == type_9)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_pls_blush_your_wife_idcard);
		}
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
		st_idcard_left = (ImageView) findViewById(R.id.st_idcard_left);
		st_idcard_left.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
	}
	
	private void initData()
	{
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.st_idcard_left:
            if (Constants.DEBUG)
            {
                idcard = new IdcardModel();
                idcard.setName("马峰");
                idcard.setIdcard("330621198304104237");
                // 测试使用
                nextStep();
            }

			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		isStop = false;
		myHandler.sendEmptyMessageDelayed(0, 1000);
		title.showTimer();
	}

	
	@Override
	public void onPause() {
		super.onPause();
		isStop = true;
		idcard =null;
		title.stopTimer();
		isStop = true;
		myHandler.removeCallbacks(initCardRunnable);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		myHandler=null;
		
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	
	 @SuppressLint("HandlerLeak")
	   private  Handler myHandler = new Handler() {
			public void handleMessage (Message msg) {
				switch(msg.what) {
				case 0:
					if(idcard==null)
					{
						if(readIdCardThread != null ){
							if(readIdCardThread.isAlive()){
								break;
							}
							
						}
						
						readIdCardThread = new Thread(initCardRunnable);
						readIdCardThread.start();
						
					} 
				break;
				case 1:
					isStop=true;
					if(idcard !=null){
						nextStep();
					}
					
				break;
				case 2:
					myHandler.sendEmptyMessageDelayed(0,1000);
				break;
				}
			}
	 };
	
	private Runnable initCardRunnable = new Runnable() {

		@Override
		public void run() {
			if(myHandler != null)
			{
				Message msg = myHandler.obtainMessage();
				if(!isStop){
					boolean isFind = AppApplication.getInstance().getSi().FindIdCard();
					if(isFind){
						boolean isSelect = AppApplication.getInstance().getSi().SelectIdCard();
						if(isSelect) {
							IdcardModel user = AppApplication.getInstance().getSi().ReadIdCard();
							if(user!=null) {
								idcard =user;
								LogUtil.d("mf", user.getIdcard());
								msg.what=1;
							}  else {
								msg.what=2;
							}
						}  else {
							msg.what=2;
						}
					} else {
						msg.what=2;
					}
						
				} else if(!isStop){
					msg.what=2;
				}
				if(myHandler != null){
					myHandler.sendMessage(msg);
				}
			}
		}  
		
	};
	
	
	private void nextStep()
	{
		switch (type) {
		case type_0:
		{
			break;
		}
		case type_1:
		{
			
			break;
		}
		case type_2:
		{
			break;
		}
			
		case type_3:
		{
			Intent intent = new Intent(this,PopulationActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("idcard",idcard);
			intent.putExtras(mBundle);
			startActivity(intent);
			finish();
			break;
		}
		
		case type_4:
		{

			if(isEdit)
			{
				goAbnormalStep();
				
			} else 
			{
				Intent intent = new Intent(this, AgreementActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("idcard",idcard);
				intent.putExtras(mBundle);
				intent.putExtra("work_type", workType);
				startActivity(intent);
			}
			finish();
			break;
		}

		case type_5:
		{
            Intent intent  = new Intent(this,CallCenterActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("idcard", idcard);
            intent.putExtras(mBundle);
            startActivity(intent);
            finish();
			break;
		}
		
		case type_6:
		{
			Intent intent = new Intent(this, AddPopulationActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("idcard", idcard);
			intent.putExtras(mBundle);
			startActivityForResult(intent, ActivityRstCode.ST_REQUEST_CODE_2);

			break;
		}
		
		case type_7:
		{

			Intent intent = new Intent(this, LicJdcxActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("idcard", idcard);
			intent.putExtras(mBundle);
			intent.putExtra("work_type",workType);
			startActivity(intent);
			finish();
			break;
		}
		
		case type_8:
		{

			Intent intent = new Intent(this, ZzlqActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("idcard", idcard);
			intent.putExtras(mBundle);
			intent.putExtra("work_type", workType);
			startActivity(intent);
			finish();
			break;
		}
		
		case type_9:
		{
			if(isEdit)
			{
				goAbnormalStep();
			}
			else
			{
				Intent intent = new Intent(this, JhsyMarryStateActivity.class);
				GlobalField.licData.getJhsy().setOIdcard(idcard);
				intent.putExtra("who", 1);
				startActivity(intent);
			}
			finish();
		
			
			break;
		}
			
		default:
			break;
		}
	}
	
	/*
	 * 异常流程 下一步操作
	 */
	private void goAbnormalStep()
	{
		int step = GlobalField.refuseData.getStepNo();
		Workflow workFlow = GlobalField.refuseData.getWorkFlow();
		WorkStep step1 = workFlow.getWorkflow().get(step-1);
		WorkFields fields1 = step1.getFields().get(0);
		WorkField field1 = fields1.getFields().get(0);
		field1.setValue(idcard);
		//下一步操作
		if(workFlow.getWorkflow().size() > step)
		{
			GlobalField.refuseData.setStepNo(step+1);
			int stepno2 = GlobalField.refuseData.getStepNo();
			WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
			goToFrag(step2.getId());
		} 
		else
		{
			if(workType.equals(WorkType.Jhsy))
			{
				/*JhsyCommitFragment fg = new JhsyCommitFragment();
				fg.setIsEdit(isEdit);
				TopFragmentActivity.changeFragment(fg);*/
				Intent intent = new Intent(this, JhsyCommitActivity.class);
				intent.putExtra("isEdit",isEdit);
				startActivity(intent);
			}
			
			if(workType.equals(WorkType.Lnyd))
			{

				Intent intent = new Intent(this, LnydCommitActivity.class);
				intent.putExtra("isEdit", isEdit);
				startActivity(intent);
			}
		}
		
	}
	
	private void dealIdcard(String workTye,int idcardType){
/*		IdCardActivity fg = new IdCardActivity();
		fg.setType(idcardType);
		fg.setIsEdit(true);
		fg.setWorkType(workTye);
		TopFragmentActivity.changeFragment(fg);*/

		Intent intent = new Intent(this, IdCardActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("idcard", idcard);
		intent.putExtras(mBundle);
		intent.putExtra("work_type", workType);
		intent.putExtra("idcard_type", idcardType);
		startActivity(intent);
	}
	
	private void dealImage(int imageType){
		Intent intent = new Intent(this, AddImagActivity.class);
		intent.putExtra("add_image_type", imageType);
		intent.putExtra("isEdit", true);
		startActivity(intent);
	}
	
	private void goToFrag(int step)
	{
		switch (step) 
		{
			case 1:
				dealIdcard(WorkType.Lnyd, IdCardActivity.type_4);
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
				Intent intent = new Intent(this, LnydPutFileActivity.class);
				intent.putExtra("isEdit", true);
				startActivity(intent);
				break;
			}
			case 7:
				dealIdcard(WorkType.Jhsy, IdCardActivity.type_4);
				break;
			case 8:
			{
				Intent intent = new Intent(this, JhsyMarryStateActivity.class);
				intent.putExtra("isEdit", true);
				intent.putExtra("who",0);
				startActivity(intent);
				break;
			}
			
			case 9:
				dealIdcard(WorkType.Jhsy, IdCardActivity.type_9);
				break;
			case 10:
			{
				Intent intent = new Intent(this, JhsyMarryStateActivity.class);
				intent.putExtra("isEdit", true);
				intent.putExtra("who", 1);
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
				Intent intent = new Intent(this, AddChildrenActivity.class);
				intent.putExtra("isEdit", true);
				startActivity(intent);
				break;
			}
				
			case 15:
				dealImage(AddImagActivity.type_13);
				break;
			case 16:
				dealImage(AddImagActivity.type_14);
				break;
			
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRstCode.ST_REQUEST_CODE_2)
        {
            if (resultCode == RESULT_OK)
            {
                setResult(RESULT_OK,data);
                finish();
            }
        }
    }
}
