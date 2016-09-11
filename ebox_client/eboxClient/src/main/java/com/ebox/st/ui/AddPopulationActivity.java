package com.ebox.st.ui;


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
import android.widget.Spinner;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.AppService;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.PopulationMemberModel;
import com.ebox.st.model.PopulationModel;

import java.util.Timer;
import java.util.TimerTask;

public class AddPopulationActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	private Title title;
	private TitleData titleData;
	
	private TextView et_name;
	private EditText et_relation;
	private EditText et_telephone;
	private Spinner sp_employment;
	private ArrayAdapter adapter;
	private String employment;
	private Button bt_commit;

	private PopulationMemberModel info = null;
	private IdcardModel idcard=null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.st_polulation);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		RingUtil.playRingtone(RingUtil.st029);
        idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
        info = (PopulationMemberModel)getIntent().getSerializableExtra("member");
		initView();
		initData();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_add_family);
		title.setData(titleData, this);
		
		et_name =  (TextView) findViewById(R.id.et_name);
		et_name.setText(idcard.getName());
		et_relation =  (EditText) findViewById(R.id.et_relation);
		et_relation.requestFocus();
		et_telephone =  (EditText) findViewById(R.id.et_telephone);
		EditTextUtil.limitCount(et_telephone, 11, null);
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
		
		openKeyBoard();
	}
	
	private void initData()
	{
		if(info != null)
		{
			et_name.setText(info.getPopulation().getIdcard().getName());
			et_relation.setText(info.getPopulation().getRelation());
			et_telephone.setText(info.getPopulation().getTelephone());
		}
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
		if(tip != null) {
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
			
			if(et_relation.getText() == null ||  
					et_relation.getText().toString().length() == 0)
			{
				showPrompt(R.string.st_input_relation);
				et_relation.requestFocus();
				return;
			}

			boolean isNew = false;
			if(info == null)
			{
				info = new PopulationMemberModel();
				isNew = true;
			}
			info.setIsAdd(false);
			PopulationModel p = new PopulationModel();
			p.setIdcard(idcard);
			p.setRelation(et_relation.getText().toString());
			p.setTelephone(et_telephone.getText().toString());
			info.setPopulation(p);

			Intent data = new Intent();
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("populationMember", info);
			mBundle.putBoolean("isNew", isNew);
			data.putExtras(mBundle);
            setResult(RESULT_OK,data);
			//隐藏输入法
			KeyboardUtil.hideInput(this,et_relation);
			KeyboardUtil.hideInput(this,et_telephone);
			this.finish();
			
			break;
		}
	}
	

	private void openKeyBoard()
	{
		Timer timer = new Timer();
	    timer.schedule(new TimerTask()
	    {
	         public void run() 
	         {
	             InputMethodManager inputManager =
	                 (InputMethodManager)et_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	             inputManager.showSoftInput(et_name, 0);
	         }
	     },  
	     200);
//		InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
//		imm.showSoftInput(et_name,InputMethodManager.SHOW_FORCED);
	}
	
	private void closeKeyBoard()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_name.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
}
