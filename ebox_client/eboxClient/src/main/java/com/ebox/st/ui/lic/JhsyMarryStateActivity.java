package com.ebox.st.ui.lic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.customview.Title.ZCTitleListener;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.AddImagActivity;
import com.ebox.st.ui.IdCardActivity;

public class JhsyMarryStateActivity extends CommonActivity implements
	OnClickListener{
	private Context mContext;
	private Title title;
	private TitleData titleData;
	private KeyboardUtil keyBoard;
	private RadioGroup state;
	private RadioButton first;
	private RadioButton more;
	private EditText et_telephone;
	private Button bt_put_item;
	private Tip tip;
	
	private Boolean isEdit = false;
	
	private int who = 0; // 0: 办理人 1：配偶
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = JhsyMarryStateActivity.this;
        who = getIntent().getIntExtra("who", 0);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
		if(who == 0)
		{
			RingUtil.playRingtone(RingUtil.st003);
		}
		else
		{
			RingUtil.playRingtone(RingUtil.st030);
		}

        setContentView(R.layout.st_jhsy_marry_state);
		MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
	}
	

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
	public void onDestroy() {
		super.onDestroy();
		
		if (tip != null) {
			tip.closeTip();
		}
	}
	
	private void initView() {
		
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		if(who == 0)
		{
			titleData.tvContent = getResources().getString(R.string.st_marrystate);
		}
		else
		{
			titleData.tvContent = getResources().getString(R.string.st_wife_marrystate);
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
		
		et_telephone = (EditText) findViewById(R.id.et_telephone);
		EditTextUtil.limitCount(et_telephone, 11, null);
		KeyboardUtil.hideInput(this, et_telephone);
		keyBoard = new KeyboardUtil(this,mContext, et_telephone);
		keyBoard.showKeyboard();
		
		state=(RadioGroup)findViewById(R.id.state);
		first=(RadioButton) findViewById(R.id.first);
		more=(RadioButton) findViewById(R.id.more);
        if (isEdit)
        {
            first.setClickable(false);
            more.setClickable(false);
            Workflow workFlow = GlobalField.refuseData.getWorkFlow();

			if (null!=workFlow) {
				for (WorkStep step : workFlow.getWorkflow()) {
					if (who == 0) {
						if (step.getId() == 8) {
							WorkFields fields1 = step.getFields().get(0);
							for (WorkField field1 : fields1.getFields()) {
								if (field1.getKey().equals("marriage_state") && field1.getValue().equals("first")) {
									first.setChecked(true);
								} else if (field1.getKey().equals("marriage_state") && field1.getValue().equals("more")) {
									more.setChecked(true);
								} else if (field1.getKey().equals("telephone")) {
									et_telephone.setText(field1.getValue().toString() != null ? field1.getValue().toString() : "");
								}
							}
						}
					} else if (who == 1) {
						if (step.getId() == 10) {
							WorkFields fields1 = step.getFields().get(0);
							for (WorkField field1 : fields1.getFields()) {
								if (field1.getKey().equals("marriage_state") && field1.getValue().equals("first")) {
									first.setChecked(true);
								} else if (field1.getKey().equals("marriage_state") && field1.getValue().equals("more")) {
									more.setChecked(true);
								} else if (field1.getKey().equals("telephone")) {
									et_telephone.setText(field1.getValue().toString() != null ? field1.getValue().toString() : "");
								}
							}
						}
					}
				}
			}
		}
        else
        {
            first.setClickable(true);
            more.setClickable(true);
            first.setChecked(true);
        }

        state.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());


		
		bt_put_item = (Button) findViewById(R.id.bt_put_item);
		bt_put_item.setOnClickListener(this);
	}
	
	private class OnCheckedChangeListenerImp implements OnCheckedChangeListener{

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(first.getId()==checkedId){ 
				if(isEdit)
				{
					/*int step = GlobalField.refuseData.getStepNo();
					Workflow workFlow = GlobalField.refuseData.getWorkFlow();
					WorkStep step1 = workFlow.getWorkflow().get(step - 1);
					WorkFields fields1 = step1.getFields().get(0);
					for(WorkField field1 :fields1.getFields())
                    {
						if(field1.getKey().equals("marriage_state"))
						{
							field1.setValue("first");
						}
					}*/
                    Workflow workFlow = GlobalField.refuseData.getWorkFlow();
                    for (WorkStep step :workFlow.getWorkflow())
                    {
                        if (step.getId() == 8 || step.getId()==10 )
                        {
                            WorkFields fields1 = step.getFields().get(0);
                            for(WorkField field1 :fields1.getFields())
                            {
                                if(field1.getKey().equals("marriage_state") && field1.getValue().equals("first"))
                                {
                                    first.setChecked(true);
                                }
                                else if(field1.getKey().equals("marriage_state") && field1.getValue().equals("more"))
                                {
                                    more.setChecked(true);
                                }
                            }
                        }
                    }
				}

				
				
			}
			else if(more.getId()==checkedId){
				if(isEdit)
				{
					/*int step = GlobalField.refuseData.getStepNo();
					Workflow workFlow = GlobalField.refuseData.getWorkFlow();
					WorkStep step1 = workFlow.getWorkflow().get(step-1);
					WorkFields fields1 = step1.getFields().get(0);
					for(WorkField field1 :fields1.getFields()){
						if(field1.getKey().equals("marriage_state"))
						{
							field1.setValue("more");
						}
					}*/
                    Workflow workFlow = GlobalField.refuseData.getWorkFlow();
                    for (WorkStep step :workFlow.getWorkflow())
                    {
                        if (step.getId() == 8 || step.getId()==10 )
                        {
                            WorkFields fields1 = step.getFields().get(0);
                            for(WorkField field1 :fields1.getFields())
                            {
                                if(field1.getKey().equals("marriage_state") && field1.getValue().equals("first"))
                                {
                                    first.setChecked(true);
                                }
                                else if(field1.getKey().equals("marriage_state") && field1.getValue().equals("more"))
                                {
                                    more.setChecked(true);
                                }
                            }
                        }
                    }
				}

			}
		}
	}
	
	private void showPrompt(int resId) {
		tip = new Tip(this, getResources().getString(
				resId), null);
		tip.show(0);
	}

	private boolean checkPara() {
		if (et_telephone.getText() == null
				|| !FunctionUtil.validMobilePhone(et_telephone.getText().toString())) {
			showPrompt(R.string.pub_telephone_error);
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_put_item:
			if (checkPara()) {
				if(isEdit)
				{
					goAbnormalStep();
				}
				else
				{
					if(who == 0)
					{
                        if(more.getId() == state.getCheckedRadioButtonId())
                        {
                            GlobalField.licData.getJhsy().setMarryState("more");
                        }
                        else
                        {
                            GlobalField.licData.getJhsy().setMarryState("first");
                        }
					
						GlobalField.licData.getJhsy().setTelephone(et_telephone.getText().toString());
                        Intent intent = new Intent(this,IdCardActivity.class);
                        intent.putExtra("idcard_type",IdCardActivity.type_9);
                        startActivity(intent);

					}
					else
					{
						GlobalField.licData.getJhsy().setoTelephone(et_telephone.getText().toString());
                        if(more.getId() == state.getCheckedRadioButtonId())
                        {
                            GlobalField.licData.getJhsy().setoMarryState("more");
                        }
                        else
                        {
                            GlobalField.licData.getJhsy().setoMarryState("first");
                        }
                        Intent intent = new Intent(this,AddImagActivity.class);
                        intent.putExtra("add_image_type",AddImagActivity.type_4);
                        startActivity(intent);
					}
				}
		
			}
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
		for(WorkField field1 :fields1.getFields())
		{
			if(field1.getKey().equals("telephone"))
			{
				field1.setValue(et_telephone.getText().toString());
			}
		}
		if(workFlow.getWorkflow().size() > step)
		{
			GlobalField.refuseData.setStepNo(step+1);
			int stepno2 = GlobalField.refuseData.getStepNo();
			WorkStep step2 = workFlow.getWorkflow().get(stepno2-1);
			goToFrag(step2.getId());
		} 
		else
		{
            Intent intent = new Intent(this,JhsyCommitActivity.class);
            intent.putExtra("isEdit",true);
            startActivity(intent);
		}
		
	}
	
	private void dealIdcard(String workTye,int idcardType){
        Intent intent = new Intent(this,IdCardActivity.class);
        intent.putExtra("isEdit",true);
        intent.putExtra("work_type",workTye);
        intent.putExtra("idcard_type",idcardType);
        startActivity(intent);
	}
	
	private void dealImage(int imageType){
        Intent intent = new Intent(this, AddImagActivity.class);
        intent.putExtra("image_type",imageType);
        intent.putExtra("isEdit",true);
        startActivity(intent);
	}
	
	private void goToFrag(int step)
	{
		switch (step) 
		{
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
                intent.putExtra("who",1);
                intent.putExtra("isEdit",true);
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
			
		}
	}
}
