package com.ebox.st.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.pub.utils.Tip.onDismissListener;
import com.ebox.st.model.PopulationMemberModel;
import com.ebox.st.model.PopulationModel;
import com.ebox.st.model.SubmitPopulationReq;
import com.ebox.st.model.SubmitPopulationRsp;
import com.ebox.st.model.enums.ActivityRstCode;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.adapter.MemberAdatpter;
import com.ebox.st.ui.adapter.MemberAdatpter.onCancelClickListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PopulationActivity extends CommonActivity implements OnClickListener
	,onCancelClickListener {
	private Tip tip;
	private Title title;
	private TitleData titleData;
	private TextView bt_back;
	
	private TextView et_name;
	private EditText et_telephone;
	private EditText et_home;
	private Spinner sp_employment;
	private ArrayAdapter adapter;
	private String employment;
	private Button bt_commit;
	private GridView gv_population;
	private MemberAdatpter mAdapter;
	
	private IdcardModel idcard;
	private ArrayList<PopulationMemberModel> pList = new ArrayList<PopulationMemberModel>();
	
	private DialogUtil dialogUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RingUtil.playRingtone(RingUtil.st002);
		setContentView(R.layout.st_activity_population);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
		initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_population);
		title.setData(titleData, this);
		
		bt_back =  (TextView) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		
		et_name =  (TextView) findViewById(R.id.et_name);
		et_name.setText(idcard.getName());
		et_telephone =  (EditText) findViewById(R.id.et_telephone);
		EditTextUtil.limitCount(et_telephone, 11, null);
		et_telephone.requestFocus();
		et_home =  (EditText) findViewById(R.id.et_home);
		sp_employment =  (Spinner) findViewById(R.id.sp_employment);
		//将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this, R.array.employment, R.layout.st_simple_spinner_item);
 
        //设置下拉列表的风格 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter2 添加到spinner中
		sp_employment.setAdapter(adapter);
        //添加事件Spinner事件监听  
		sp_employment.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        
		bt_commit =  (Button) findViewById(R.id.bt_commit);
		bt_commit.setOnClickListener(this);
		
		gv_population = (GridView) findViewById(R.id.gv_population);
		mAdapter=new MemberAdatpter(this);
		if(pList.size() <= 0)
		{
			PopulationMemberModel member = new PopulationMemberModel();
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
				PopulationMemberModel info = pList.get(position);
				startDeal(info);
			}
		});
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
		openKeyBoard();
	}
	
	//使用XML形式操作
    class SpinnerXMLSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	employment = (String)adapter.getItem(arg2);
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
             
        }
         
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(dialogUtil!=null){
			dialogUtil.closeProgressDialog();
		}
		if(tip != null)
		{
			tip.closeTip();
		}
		closeKeyBoard();
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
	
	private void showPrompt(int resId)
	{
		tip = new Tip(this,
				getResources().getString(resId),
				null);
		tip.show(0);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_commit:
			if(et_name.getText() == null ||  
					et_name.getText().toString().length() == 0)
			{
				showPrompt(R.string.st_input_name);
				et_name.requestFocus();
				return;
			}
			
			if(et_telephone.getText() == null ||  
					et_telephone.getText().toString().length() != 11
                    || !FunctionUtil.validMobilePhone(et_telephone.getText().toString()))
			{
				showPrompt(R.string.st_input_telephone);
				et_telephone.requestFocus();
				return;
			}
			
			if(et_home.getText() == null ||  
					et_home.getText().toString().length() == 0)
			{
				showPrompt(R.string.st_input_home);
				et_home.requestFocus();
				return;
			}
			
			inputPopulation();
			break;
		}
	}
	
	private void inputPopulation()
	{
		dialogUtil.showProgressDialog();

		SubmitPopulationReq req = new SubmitPopulationReq();
		req.setTerminal_code(GlobalField.config.getTerminal_code());
		req.setIdcard(idcard);
		req.setAddress(et_home.getText().toString());
		req.setTelephone(et_telephone.getText().toString());
		ArrayList<PopulationModel> populationList = new ArrayList<PopulationModel>();

		for(int i = 0; i < pList.size(); i++)
		{
			if(!pList.get(i).getIsAdd())
			{
				populationList.add(pList.get(i).getPopulation());
			}
		}
		req.setPopulation(populationList);
		ApiClient.submitPopulation(this, req, new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                dialogUtil.closeProgressDialog();
                SubmitPopulationRsp rsp = (SubmitPopulationRsp) data;
                if (rsp != null && rsp.getSuccess()) {
                    tip = new Tip(PopulationActivity.this,
                            getResources().getString(R.string.st_population_ok),
                            new onDismissListener() {
                                @Override
                                public void onDismiss() {
                                    finish();
                                }
                            });
                    tip.show(0);
                }
            }

            @Override
            public void onFailed(Object data) {
                dialogUtil.closeProgressDialog();
                finish();
            }
        });
	}
	

	@Override
	public void OnCancelClickListener(PopulationMemberModel info) {
		pList.remove(info);
		mAdapter.clear();
		mAdapter.addAll(pList);
		mAdapter.notifyDataSetChanged();
	}
	
	private void startDeal(PopulationMemberModel info)
	{
		if(info.getIsAdd())
		{
			Intent intent = new Intent(this,IdCardActivity.class);
			intent.putExtra("idcard_type",IdCardActivity.type_6);
			startActivityForResult(intent, ActivityRstCode.ST_REQUEST_CODE_2);
		}
		else
		{
			Intent intent = new Intent(this,AddPopulationActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("member",info);
			mBundle.putSerializable("idcard",idcard);
            intent.putExtras(mBundle);
			startActivityForResult(intent, ActivityRstCode.ST_REQUEST_CODE_4);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ActivityRstCode.ST_REQUEST_CODE_2 || requestCode == ActivityRstCode.ST_REQUEST_CODE_4) {
			if (resultCode == Activity.RESULT_OK)
			{
                Bundle mBundle = data.getExtras();
                PopulationMemberModel info = (PopulationMemberModel)mBundle.getSerializable("populationMember");
                Boolean isNew = mBundle.getBoolean("isNew");
                addPopulation(info,isNew);
			}
		}
	}

	public void addPopulation(PopulationMemberModel info, boolean isNew) {
		
		if(isNew)
		{
			pList.add(pList.size()-1, info);
		}
		else
		{
			for(int i = 0; i < pList.size(); i++)
			{
				if(pList.get(i).getPopulation().getIdcard().getIdcard().equals(info.getPopulation().getIdcard().getIdcard()))
				{
					pList.set(i, info);
					break;
				}
			}
		}
		mAdapter.clear();
		mAdapter.addAll(pList);
		mAdapter.notifyDataSetChanged();
    }
	
	private void openKeyBoard()
	{
		Timer timer = new Timer();
	    timer.schedule(new TimerTask()
	    {
	         public void run() 
	         {
	             InputMethodManager inputManager =
	                 (InputMethodManager)et_telephone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	             inputManager.showSoftInput(et_telephone, 0);
	         }
	     },  
	     200);
//		InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
//		imm.showSoftInput(et_name,InputMethodManager.SHOW_FORCED);
	}
	
	private void closeKeyBoard()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_telephone.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
}
