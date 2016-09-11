package com.ebox.st.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.RefuseModel;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.lic.AddChildrenActivity;
import com.ebox.st.ui.lic.JhsyMarryStateActivity;
import com.ebox.st.ui.lic.LnydPutFileActivity;

public class refuseActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	private TextView tv_refuse_text;
	
	private Button st_btn_perf;

	private DialogUtil dialogUtil;
	
	private String state;
	
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = refuseActivity.this;
		setContentView(R.layout.st_fg_refuse);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        state = getIntent().getStringExtra("refuse_state");
        initView();
		initData();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = "审批不通过";
		title.setData(titleData, this);

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
		tv_refuse_text = (TextView) findViewById(R.id.tv_refuse_text);
		st_btn_perf = (Button) findViewById(R.id.st_btn_perfor);
		st_btn_perf.setOnClickListener(this);
	}
	

	public void initData(){
		tv_refuse_text.setText(state);
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
		case R.id.st_btn_perfor:
			goToPerfer();
			break;
		}
		
	}
	
	private void goToPerfer()
	{
		if(GlobalField.refuseData!=null && GlobalField.refuseData.getWorkFlow()!=null
				&& GlobalField.refuseData.getWorkFlow().getWorkflow()!=null
				&& GlobalField.refuseData.getWorkFlow().getWorkflow().size()> 0 )
		{
			gotoNext(GlobalField.refuseData);
		}else {
			tip = new Tip(mContext, getResources().getString(
					R.string.st_contact_admin),null);
			tip.show(0);
		}
		
	}
	
	private void gotoNext(RefuseModel re)
	{
		WorkStep step = GlobalField.refuseData.getWorkFlow().getWorkflow().get(0);
		gotoFrag(step.getId());
	}
	
	private void dealIdcard(String workTye,int idcardType)
	{
		Intent intent = new Intent(this,IdCardActivity.class);
		intent.putExtra("idcard_type", idcardType);
		intent.putExtra("isEdit", true);
		intent.putExtra("work_type", workTye);
		startActivity(intent);
	}
	
	private void dealImage(int imageType)
	{
		Intent intent = new Intent(this,AddImagActivity.class);
		intent.putExtra("add_image_type", imageType);
		intent.putExtra("isEdit", true);
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
			intent.putExtra("who", 0);
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
	
}
