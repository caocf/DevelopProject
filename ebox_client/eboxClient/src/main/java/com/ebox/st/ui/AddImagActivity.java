package com.ebox.st.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.CameraTest;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.AttaModel;
import com.ebox.st.model.ChildrenModel;
import com.ebox.st.model.ImagModel;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.lic.AddChildrenActivity;
import com.ebox.st.ui.lic.JhsyCommitActivity;
import com.ebox.st.ui.lic.JhsyMarryStateActivity;
import com.ebox.st.ui.lic.LnydCommitActivity;
import com.ebox.st.ui.lic.LnydPutFileActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class AddImagActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	private Title title;
	private TitleData titleData;
	
	private String pic;
	private CameraTest cp;
	private Button btn_sm;
	private Button btn_save;
	private Button btn_sm_retry;
	private RelativeLayout step1;
	private RelativeLayout step2;
	private ImageView imageView ;
	private DialogUtil dialogUtil;
	private Context mContext;
	
	private int type = 0;
	
	// 0：老年优待证，扫身份证正面
	public final static int type_0 = 0;
	// 1：老年优待证，扫身份证反面
	public final static int type_1 = 1;
	// 2：老年优待证，居住证正面
	public final static int type_2 = 2;
	// 3：老年优待证，居住证反面
	public final static int type_3 = 3;

	// 4：计划生育服务证，结婚证
	public final static int type_4 = 4;
	// 5：计划生育服务证，户口页首页
	public final static int type_5 = 5;
	// 6：计划生育服务证，户口页身份页
	public final static int type_6 = 6;
	// 7：计划生育服务证，配偶户口页首页
	public final static int type_7 = 7;
	// 8：计划生育服务证，配偶户口页身份页
	public final static int type_8 = 8;
	// 9：计划生育服务证，孩子的户口页首页
	public final static int type_9 = 9;
	// 10：计划生育服务证，孩子的户口页身份页
	public final static int type_10 = 10;
	// 11：计划生育服务证，孩子的出生证明
	public final static int type_11 = 11;
	// 12：计划生育服务证，孩子的准生证
	public final static int type_12 = 12;
	// 13：计划生育服务证，离婚协议书
	public final static int type_13 = 13;
	// 14：计划生育服务证，配偶的离婚协议书
	public final static int type_14 = 14;
	
	private Boolean isEdit= false ;

    private int ChildNum = 0;

	public static final  int CAMERA_START=0;
	public static final  int CAMERA_STOP=1;

	final android.os.Handler mHandler=new android.os.Handler()
	{
		@Override
		public void handleMessage(Message msg) {

			dialogUtil.closeProgressDialog();

			switch (msg.what)
			{
				case CAMERA_START:
					dealNomalImage(msg.arg1);
					break;

				case CAMERA_STOP:
					AddImagActivity.this.finish();
					break;
			}

		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("WebCam", "onCreate()   id:" + Thread.currentThread().getId());
		mContext = AddImagActivity.this;
        setContentView(R.layout.st_fg_imag);
		MGViewUtil.scaleContentView(this, R.id.rootView);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        type = getIntent().getIntExtra("add_image_type",0);
        if (type == type_9)
        {
			ArrayList<ChildrenModel> list = GlobalField.licData.getJhsy().getpList();
			if (list != null)
			{
				ChildNum = GlobalField.licData.getJhsy().getpList().size();
			}
			else
			{
				ChildNum=0;
			}
		}
        initView();
        initData();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_zjsm);
		title.setData(titleData, this);
		title.setZCTitleListner(new Title.ZCTitleListener() {
			
			@Override
			public void onOperateBtnClick(int index) {
				clickBack();
			}
		});
		
		cp =  (CameraTest) findViewById(R.id.cp);
		
		step1 = (RelativeLayout) findViewById(R.id.step1);
		step2 = (RelativeLayout) findViewById(R.id.step2);
		
		btn_save =  (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
		btn_sm =  (Button) findViewById(R.id.btn_sm);
		btn_sm.setOnClickListener(this);
		btn_sm_retry = (Button) findViewById(R.id.btn_sm_retry);
		btn_sm_retry.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);

		//返回上一个界面前需要先将Camera关闭
		title.setZCTitleListner(new Title.ZCTitleListener() {
			@Override
			public void onOperateBtnClick(int index) {
				handMsg(1);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		title.showTimer();
	}
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}

	@Override
	public void onDestroy() {
		if(tip != null)
		{
			tip.closeTip();
		}

		if(dialogUtil !=null){
			dialogUtil.closeProgressDialog();
		}

		super.onDestroy();
	}

	public void handMsg(int type){
		handMsg(type,0);
	}

	/**处理摄像头关闭消息*/
	public void handMsg(final int type , final int fileId){

		Message ms=Message.obtain();
		if (type ==0)
		{
			cp.stopCamera();
			ms.what=CAMERA_START;
			ms.arg1=fileId;
		}
		else if(type==1)
		{
			cp.stopCamera();
			ms.what=CAMERA_STOP;
		}
		mHandler.sendMessage(ms);
	}



	private void showPrompt(int resId)
	{
		tip = new Tip(mContext,
				getResources().getString(resId),
				null);
		tip.show(0);
	}

	private void clickBack(){
		switch (type) {
		case type_0:
		case type_1:
		case type_2:
		case type_3:
		case type_4:
		case type_5:
			if(isEdit)
			{
				int step = GlobalField.refuseData.getStepNo();
				if(step>1)
				{
					GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
				}
			}
			finish();
			break;
		case type_6:
			finish();
			break;
		case type_7:
			if(isEdit)
			{
				int step = GlobalField.refuseData.getStepNo();
				if(step>1)
				{
					GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
				}
			}
			finish();
			break;
		case type_8:
			finish();
			break;
		case type_9:
            ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();

            if (pList.size()>ChildNum)
            {
                pList.remove(pList.size()-2);
            }


			if(isEdit)
			{
				int step = GlobalField.refuseData.getStepNo();
				if(step>1)
				{
					GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
				}
			}
            finish();
			break;
		case type_10:
		case type_11:
		case type_12:
            finish();
			break;
		case type_13:
		case type_14:
			if(isEdit)
			{
				int step = GlobalField.refuseData.getStepNo();
				if(step>1)
				{
					GlobalField.refuseData.setStepNo(GlobalField.refuseData.getStepNo()-1);
				}
			}
            finish();
			break;
		default:
            finish();
			break;
		}
	}
	
	private void initData(){
		if(type == type_0)
		{
			RingUtil.playRingtone(RingUtil.st022);
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_idcard_positive);
		}
		else if(type == type_1)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_idcard_reverse);
			RingUtil.playRingtone(RingUtil.st023);
		}
		else if(type == type_2)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_reside_positive);
			RingUtil.playRingtone(RingUtil.st024);
		}
		else if(type == type_3)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_reside_reverse);
			RingUtil.playRingtone(RingUtil.st025);
		}
		else if(type == type_4)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_marriage);
			RingUtil.playRingtone(RingUtil.st031);
		}
		else if(type == type_5)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_account_page_first);
			RingUtil.playRingtone(RingUtil.st006);
		}
		else if(type == type_6)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_account_page_second);
			RingUtil.playRingtone(RingUtil.st007);
		}
		else if(type == type_7)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_wife_account_page_first);
			RingUtil.playRingtone(RingUtil.st008);
		}
		else if(type == type_8)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_wife_account_page_second);
			RingUtil.playRingtone(RingUtil.st009);
		}
		else if(type == type_9)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_child_account_page_first);
			RingUtil.playRingtone(RingUtil.st011);
		}
		else if(type == type_10)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_child_account_page_second);
			RingUtil.playRingtone(RingUtil.st012);
		}
		else if(type == type_11)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_child_birth_certi);
			RingUtil.playRingtone(RingUtil.st013);
		}
		else if(type == type_12)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_zhunshengzheng);
			RingUtil.playRingtone(RingUtil.st014);
		}
		else if(type == type_13)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_divorce_agreement);
			RingUtil.playRingtone(RingUtil.st015);
		}
		else if(type == type_14)
		{
			titleData.tvContent = mContext.getResources().getString(R.string.st_scan_your_wife_divorce_agreement);
			RingUtil.playRingtone(RingUtil.st016);
		}
		title.setData(titleData, this);
		switchStep(1);
	}
	
	private void switchStep(int a){
		switch (a) {
		case 1:
			step1.setVisibility(View.VISIBLE);
			step2.setVisibility(View.GONE);
			break;
		case 2:
//			Bitmap bitmap = BitmapUtil.readBitmap(pic);
//			imageView.setImageBitmap(bitmap); 
			ImageLoader.getInstance().displayImage("file://" + pic, imageView);
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_save:
			uploadImag();

			break;
		case R.id.btn_sm:
			//拍照
			pic = cp.takePicture();
			if(pic != null)
			{
				switchStep(2);
			}
			break;
		case R.id.btn_sm_retry:
			switchStep(1);
			break;
		}
	}

	private void uploadImag(){
		
		dialogUtil.showProgressDialog();
		File file = new File(pic);
		if(file.exists()){
			ApiClient.upload(this, file, new ApiClient.UploadCallback() {

                @Override
                public void onSuccess(String filePath, int fileId) {
                 //   dialogUtil.closeProgressDialog();

                    if (isEdit) {
                        goToEditNext(fileId);
                    } else {
                        //dealNomalImage(fileId);
						handMsg(0,fileId);
                    }
            
                }

                @Override
                public void onFailed(byte[] data) {
                    showPrompt(R.string.st_connect_failed);
                    dialogUtil.closeProgressDialog();
                }

            });
		}
	}
	/**
	 * 审核不通过刷卡后处理
	 */
	private void goToEditNext(int fileId)
	{
		int step = GlobalField.refuseData.getStepNo();
		Workflow workFlow = GlobalField.refuseData.getWorkFlow();
		WorkStep step1 = workFlow.getWorkflow().get(step-1);
		WorkFields fields1 = step1.getFields().get(0);
		WorkField field1 = fields1.getFields().get(0);
		switch (step1.getId()) 
		{
			case 2:
				if(field1.getKey().equals("idcard_up"))
				{
					field1.setValue(fileId+"");
				}
				if(workFlow.getWorkflow().size() > step){
					GlobalField.refuseData.setStepNo(step+1);
					int stepno2 = GlobalField.refuseData.getStepNo();
					WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
					gotoFrag(step2.getId());
				} 
				else 
				{
					Intent intent = new Intent(this,LnydCommitActivity.class);
					intent.putExtra("isEdit",isEdit);
					startActivity(intent);
				}
				
				break;
			case 3:
				if(field1.getKey().equals("idcard_down")){
					field1.setValue(fileId+"");
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
					Intent intent = new Intent(this,LnydCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
				break;
			case 4:
				if(field1.getKey().equals("juzhu_up")){
					field1.setValue(fileId+"");
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
					Intent intent = new Intent(this,LnydCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
				break;
			case 5:
				if(field1.getKey().equals("juzhu_down")){
					field1.setValue(fileId+"");
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
					Intent intent = new Intent(this,LnydCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
				break;
			
			case 11:
			{
				if(field1.getKey().equals("marriage_cert")){
					field1.setValue(fileId+"");
				}
				if(workFlow.getWorkflow().size() > step){
					GlobalField.refuseData.setStepNo(step+1);
					int stepno2 = GlobalField.refuseData.getStepNo();
					WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
					gotoFrag(step2.getId());
				} 
				else 
				{
					Intent intent = new Intent(this,JhsyCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
				
				break;
			}
			case 12:
			{
				WorkField field2 = fields1.getFields().get(1); 
				if(type == type_5)
				{
					if(field1.getKey().equals("index"))
					{
						field1.setValue(fileId+"");
						dealImage(AddImagActivity.type_6);
					}
				}
				
				if(type == type_6)
				{
					if(field2.getKey().equals("user"))
					{
						field2.setValue(fileId+"");
					}
					//跳转下一步
					if(workFlow.getWorkflow().size() > step){
						GlobalField.refuseData.setStepNo(step+1);
						int stepno2 = GlobalField.refuseData.getStepNo();
						WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
						gotoFrag(step2.getId());
					} 
					else 
					{
						Intent intent = new Intent(this,JhsyCommitActivity.class);
						intent.putExtra("isEdit", isEdit);
						startActivity(intent);
					}
				}
				
				break;
			}
			case 13:
				{
					WorkField field2 = fields1.getFields().get(1); 
					if(type == type_7)
					{
						if(field1.getKey().equals("index"))
						{
							field1.setValue(fileId+"");
							dealImage(AddImagActivity.type_8);
						}
					}
					
					if(type == type_8)
					{
						if(field2.getKey().equals("user"))
						{
							field2.setValue(fileId+"");
						}
						//跳转下一步
						if(workFlow.getWorkflow().size() > step){
							GlobalField.refuseData.setStepNo(step+1);
							int stepno2 = GlobalField.refuseData.getStepNo();
							WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
							gotoFrag(step2.getId());
						} 
						else 
						{
							Intent intent = new Intent(this,JhsyCommitActivity.class);
							intent.putExtra("isEdit", isEdit);
							startActivity(intent);
						}
					}
					
					
					break;
				}
			case 14:
				break;
			case 15:
			{
				if(field1.getKey().equals("divorce_a")){
					field1.setValue(fileId+"");
				}
				if(workFlow.getWorkflow().size() > step){
					GlobalField.refuseData.setStepNo(step+1);
					int stepno2 = GlobalField.refuseData.getStepNo();
					WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
					gotoFrag(step2.getId());
				} 
				else 
				{
					Intent intent = new Intent(this,JhsyCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
				
				break;
			}
			case 16:
			{
				if(field1.getKey().equals("divorce_b")){
					field1.setValue(fileId+"");
				}
				if(workFlow.getWorkflow().size() > step){
					GlobalField.refuseData.setStepNo(step+1);
					int stepno2 = GlobalField.refuseData.getStepNo();
					WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
					gotoFrag(step2.getId());
				} 
				else 
				{
					Intent intent = new Intent(this,JhsyCommitActivity.class);
					intent.putExtra("isEdit", isEdit);
					startActivity(intent);
				}
			}
	
			default:
				break;
		}
        
	
	}
	
	private void dealImage(int imageType){
		cp.stopCamera();
		Intent intent = new Intent(this,AddImagActivity.class);
		intent.putExtra("add_image_type",imageType);
		intent.putExtra("isEdit",true);
		startActivity(intent);
	}
	
	private void dealIdcard(String workTye,int idcardType){
		Intent intent = new Intent(this,IdCardActivity.class);
		intent.putExtra("idcard_type", idcardType);
		intent.putExtra("isEdit", true);
		intent.putExtra("work_type",workTye);
		startActivity(intent);
	}
	

	private void gotoFrag(int step)
	{
		switch (step) {
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
			Intent intent = new Intent(this,LnydPutFileActivity.class);
			intent.putExtra("isEdit", true);
			startActivity(intent);
			break;
		}
		case 7:
			dealIdcard(WorkType.Jhsy, IdCardActivity.type_4);
			break;
		case 8:
		{
			Intent intent = new Intent(this,JhsyMarryStateActivity.class);
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
			Intent intent = new Intent(this,JhsyMarryStateActivity.class);
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
			Intent intent = new Intent(this,AddChildrenActivity.class);
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

		default:
			break;
		}
        
	}
	/**
	 * 处理正常流程的上传结果
	 */
	private void dealNomalImage(int fileId)
	{

		if(type == type_0)
		{
			if(GlobalField.licData.getLnyd().getAtta() == null)
			{
				GlobalField.licData.getLnyd().setAtta(new ArrayList<AttaModel>());
			}
			AttaModel atta = new AttaModel();
			atta.setKey("idcard_up");
			atta.setAttachmentid(fileId + "");
			GlobalField.licData.getLnyd().getAtta().add(atta);
			
			
			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type",AddImagActivity.type_1);
			startActivity(intent);
		}
		else if(type == type_1)
		{
			if(GlobalField.licData.getLnyd().getAtta() == null)
			{
				GlobalField.licData.getLnyd().setAtta(new ArrayList<AttaModel>());
			}
			AttaModel atta = new AttaModel();
			atta.setKey("idcard_down");
			atta.setAttachmentid(fileId + "");
			GlobalField.licData.getLnyd().getAtta().add(atta);

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_2);
			startActivity(intent);
		}
		else if(type == type_2)
		{
			if(GlobalField.licData.getLnyd().getAtta() == null)
			{
				GlobalField.licData.getLnyd().setAtta(new ArrayList<AttaModel>());
			}
			AttaModel atta = new AttaModel();
			atta.setKey("juzhu_up");
			atta.setAttachmentid(fileId + "");
			GlobalField.licData.getLnyd().getAtta().add(atta);
			

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_3);
			startActivity(intent);
		}
		else if(type == type_3)
		{
			if(GlobalField.licData.getLnyd().getAtta() == null)
			{
				GlobalField.licData.getLnyd().setAtta(new ArrayList<AttaModel>());
			}
			AttaModel atta = new AttaModel();
			atta.setKey("juzhu_down");
			atta.setAttachmentid(fileId + "");
			GlobalField.licData.getLnyd().getAtta().add(atta);

			Intent intent = new Intent(this,LnydPutFileActivity.class);
			intent.putExtra("isEdit", false);
			startActivity(intent);
		}
		else if(type == type_4)
		{
			GlobalField.licData.getJhsy().setMarriage_attachmentid(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_5);
			startActivity(intent);
		}
		else if(type == type_5)
		{
			GlobalField.licData.getJhsy().setHouse_index_attachmentid(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_6);
			startActivity(intent);
		}
		else if(type == type_6)
		{
			GlobalField.licData.getJhsy().setHouse_user_attachmentid(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_7);
			startActivity(intent);
		}
		else if(type == type_7)
		{
			GlobalField.licData.getJhsy().setHouse2_index_attachmentid(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_8);
			startActivity(intent);
		}
		else if(type == type_8)
		{
			GlobalField.licData.getJhsy().setHouse2_user_attachmentid(fileId + "");

			Intent intent = new Intent(this,AddChildrenActivity.class);
			intent.putExtra("isEdit", false);
			startActivity(intent);
		}
		else if(type == type_9)
		{
			ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();
			ChildrenModel children = new ChildrenModel();
			children.setIsAdd(false);
			
			children.setHukouFirst(fileId + "");
			pList.add(pList.size() - 1, children);


			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_10);
			startActivity(intent);
		}
		else if(type == type_10)
		{
			ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();
			pList.get(pList.size()-2).setHukouSecond(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_11);
			startActivity(intent);
		}
		else if(type == type_11)
		{
			ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();
			pList.get(pList.size()-2).setChusheng(fileId + "");

			Intent intent = new Intent(this,AddImagActivity.class);
			intent.putExtra("isEdit", false);
			intent.putExtra("add_image_type", AddImagActivity.type_12);
			startActivity(intent);
		}
		else if(type == type_12)
		{
			ArrayList<ChildrenModel> pList = GlobalField.licData.getJhsy().getpList();
			pList.get(pList.size()-2).setZhunsheng(fileId + "");
			
			Intent intent = new Intent(this,AddChildrenActivity.class);
            startActivity(intent);
			
		}
		else if(type == type_13)
		{
			GlobalField.licData.getJhsy().setDivorce_attachmentid(fileId+"");
			
			if(GlobalField.licData.getJhsy().getoMarryState().equals("more"))
			{
				Intent intent = new Intent(this,AddImagActivity.class);
				intent.putExtra("isEdit", false);
				intent.putExtra("add_image_type", AddImagActivity.type_14);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(this,JhsyCommitActivity.class);
				intent.putExtra("isEdit", false);
				startActivity(intent);
			}
		}
		else if(type == type_14)
		{
			GlobalField.licData.getJhsy().setDivorce2_attachmentid(fileId+"");
			
			Intent intent = new Intent(this,JhsyCommitActivity.class);
			intent.putExtra("isEdit", false);
			startActivity(intent);
		}
		else
		{
			ImagModel info = new ImagModel();
			info.setIsAdd(false);
			info.setPath(pic);
			info.setAttachmentid(fileId+"");
//			l.addImag(info);
		}
	}
}
