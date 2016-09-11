package com.ebox.st.ui.lic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.st.model.ChildrenModel;
import com.ebox.st.model.WorkField;
import com.ebox.st.model.WorkFields;
import com.ebox.st.model.WorkStep;
import com.ebox.st.model.Workflow;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.AddImagActivity;
import com.ebox.st.ui.IdCardActivity;
import com.ebox.st.ui.adapter.ChildrenAdapter;

import java.util.ArrayList;

public class AddChildrenActivity extends CommonActivity implements OnClickListener,ChildrenAdapter.onCancelClickListener {
	private Title title;
	private TitleData titleData;
	private Button bt_commit;
	private GridView gv_population;
	private ChildrenAdapter mAdapter;
	private ArrayList<ChildrenModel> pList;
	
	private boolean isEdit = false ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isEdit=getIntent().getBooleanExtra("isEdit",false);

		if(isEdit)
		{
			pList =new ArrayList<ChildrenModel> ();
			ChildrenModel member = new ChildrenModel();
			member.setIsAdd(true);
			pList.add(member);
			int step = GlobalField.refuseData.getStepNo();
			Workflow workFlow = GlobalField.refuseData.getWorkFlow();
			WorkStep step1 = workFlow.getWorkflow().get(step-1);
			
			//WorkField field1 = fields1.getFields().get(0);
			if(step1.getId() == 14)
			{
				for(WorkFields fields1 :step1.getFields())
				{
					ChildrenModel model = new ChildrenModel();
					for(WorkField field1 :fields1.getFields())
					{
						model.setIsAdd(false);
						if(field1.getKey().equals("household_index"))
						{
							model.setHukouFirst((String) field1.getValue());
						}
						
						if(field1.getKey().equals("household_show"))
						{
							model.setHukouSecond((String) field1.getValue());
						}
						
						if(field1.getKey().equals("birth_cert"))
						{
							model.setChusheng((String) field1.getValue());
						}
						if(field1.getKey().equals("birth_show"))
						{
							model.setZhunsheng((String) field1.getValue());
						}
					}
					pList.add(0,model);
				}
			}
		}
		else
		{
			if(GlobalField.licData.getJhsy().getpList() == null)
			{
				GlobalField.licData.getJhsy().setpList(new ArrayList<ChildrenModel>());
			}
			pList = GlobalField.licData.getJhsy().getpList();
		}

        setContentView(R.layout.st_add_children);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 2;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_children_info);
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
				}
				finish();
				
			}
		});
		
		bt_commit =  (Button) findViewById(R.id.bt_commit);
		bt_commit.setOnClickListener(this);
		
		if(pList.size() > 1)
		{
			bt_commit.setText(getResources().getString(R.string.st_commit));
		}
		else
		{
			RingUtil.playRingtone(RingUtil.st010);
		}
		
		gv_population = (GridView) findViewById(R.id.gv_population);
		mAdapter=new ChildrenAdapter(this);
		if(pList.size() <= 0)
		{
			ChildrenModel member = new ChildrenModel();
			member.setIsAdd(true);
			pList.add(member);
		}
		mAdapter.addAll(pList);
		mAdapter.setListener(this);
		gv_population.setAdapter(mAdapter);
		gv_population.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChildrenModel info = pList.get(position);
				startDeal(info);
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
		super.onDestroy();
	}
	
	private void startDeal(ChildrenModel info)
	{
		if(info.getIsAdd())
		{
            Intent intent = new Intent(this, AddImagActivity.class);
            intent.putExtra("add_image_type",AddImagActivity.type_9);
            startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_commit:
			if(isEdit)
			{
				goAbnormalStep();
			}
			else 
			{
				if(GlobalField.licData.getJhsy().getMarryState().equals("more"))
				{
                    Intent intent = new Intent(this, AddImagActivity.class);
                    intent.putExtra("add_image_type",AddImagActivity.type_13);
                    startActivity(intent);
				}
				else if(GlobalField.licData.getJhsy().getoMarryState().equals("more"))
				{
                    Intent intent = new Intent(this, AddImagActivity.class);
                    intent.putExtra("add_image_type",AddImagActivity.type_14);
                    startActivity(intent);
				}
				else
				{
                    Intent intent = new Intent(this, JhsyCommitActivity.class);
                    startActivity(intent);
				}
			}
			break;
		}
	}

	@Override
	public void OnCancelClickListener(ChildrenModel info) {
		pList.remove(info);
		mAdapter.clear();
		mAdapter.addAll(pList);
		mAdapter.notifyDataSetChanged();
		if(pList.size() == 1)
		{
			bt_commit.setText(getResources().getString(R.string.pub_skip));
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
		if(step1.getId() == 14)
		{
			
			step1.getFields().clear();
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
					
					step1.getFields().add(fields8);
				}
			}
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

            Intent intent = new Intent(this, LnydCommitActivity.class);
            intent.putExtra("isEdit", true);
            startActivity(intent);
			
			/*LnydCommitActivity fg = new LnydCommitActivity();
			fg.setIsEdit(true);
			TopFragmentActivity.changeFragment(fg);*/
		}
		
	}
	
	private void dealImage(int imageType){
//		AddImagFragment fg = new AddImagFragment();
//		fg.setType(imageType);
//		fg.setIsEdit(true);
//		TopFragmentActivity.changeFragment(fg);
        Intent intent = new Intent(this, AddImagActivity.class);
        intent.putExtra("add_imag_type",imageType);
        intent.putExtra("isEdit",true);
        startActivity(intent);
	}
	private void dealIdcard(String workTye,int idcardType){
		/*IdCardFragment fg = new IdCardFragment();
		fg.setType(idcardType);
		fg.setIsEdit(true);
		fg.setWorkType(workTye);
		TopFragmentActivity.changeFragment(fg);*/

        Intent intent = new Intent(this, IdCardActivity.class);
        intent.putExtra("idcard_type",idcardType);
        intent.putExtra("isEdit",true);
        intent.putExtra("work_type",workTye);
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
//			LnydPutFileActivity fg  = new LnydPutFileActivity();
//			fg.setIsEdit(true);
//			TopFragmentActivity.changeFragment(fg);
            Intent intent = new Intent(this, LnydPutFileActivity.class);
            intent.putExtra("isEdit",true);
            startActivity(intent);
			break;
		}
		case 7:
			dealIdcard(WorkType.Jhsy,IdCardActivity.type_4);
			break;
		case 8:
		{
			/*JhsyMarryStateActivity delivery = new JhsyMarryStateActivity();
			delivery.setWho(0);
			delivery.setIsEdit(true);
			TopFragmentActivity.changeFragment(delivery);*/
            Intent intent = new Intent(this, JhsyMarryStateActivity.class);
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
			/*JhsyMarryStateActivity delivery = new JhsyMarryStateActivity();
			delivery.setWho(1);
			delivery.setIsEdit(true);
			TopFragmentActivity.changeFragment(delivery);*/
            Intent intent = new Intent(this, JhsyMarryStateActivity.class);
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
			/*AddChildrenActivity fg = new AddChildrenActivity();
			fg.setEdit(true);
			TopFragmentActivity.changeFragment(fg);*/

            Intent intent = new Intent(this, AddChildrenActivity.class);
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
