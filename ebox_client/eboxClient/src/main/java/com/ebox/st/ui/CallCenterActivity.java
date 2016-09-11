package com.ebox.st.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.SubmitCommentsReq;
import com.ebox.st.model.SubmitCommentsRsp;
import com.ebox.st.network.http.ApiClient;

import java.util.Timer;
import java.util.TimerTask;


public class CallCenterActivity extends CommonActivity implements OnClickListener{
	private Tip tip;
	private Title title;
	private TitleData titleData;
	private TextView bt_back;
	private TextView et_name;
	private EditText et_telephone;
	private EditText et_comment;
	private Button bt_commit;
	
	private IdcardModel idcard=null;
	
	private DialogUtil dialogUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.st_activity_callcenter);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
		initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_callcenter);
		title.setData(titleData, this);
		
		bt_back =  (TextView) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		
		et_name =  (TextView) findViewById(R.id.et_name);
		et_name.setText(idcard.getName());
		et_telephone =  (EditText) findViewById(R.id.et_telephone);
		et_telephone.requestFocus();
		EditTextUtil.limitCount(et_telephone, 11, null);
		et_comment =  (EditText) findViewById(R.id.et_comment);
		bt_commit =  (Button) findViewById(R.id.bt_commit);
		bt_commit.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
		openKeyBoard();
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
			
			if(et_comment.getText() == null ||  
					et_comment.getText().toString().length() == 0)
			{
				showPrompt(R.string.st_input_comment);
				et_comment.requestFocus();
				return;
			}
			
			inputComment();
			break;
		}
	}
	
	private void inputComment()
	{
		
		dialogUtil.showProgressDialog();
		/*HashMap<String, Object> parms=new HashMap<String, Object>();
		parms.put("terminal_code", GlobalField.config.getTerminal_code());
		parms.put("idcard", idcard);
		parms.put("telephone", et_telephone.getText().toString());
		parms.put("content", et_comment.getText().toString());
		String action = "submitComments";
		ApiClient.post(this.getActivity(), action, parms, new ClientCallback() {

			@Override
			public void onSuccess(JSONArray data) {
				dialogUtil.closeProgressDialog();
				tip = new Tip(CallCenterActivity.this.getActivity(),
						getResources().getString(R.string.st_comment_ok),
						new onDismissListener() {
							@Override
							public void onDismiss() {
								resetData();
							}

						});
				tip.show(0);
			}

			@Override
			public void onFailed(byte[] data) {
				dialogUtil.closeProgressDialog();
//				showPrompt(R.string.st_connect_failed);
				resetData();
			}
		});*/

		SubmitCommentsReq req = new SubmitCommentsReq();
		req.setTerminal_code(GlobalField.config.getTerminal_code());
		req.setContent(et_comment.getText().toString());
		req.setIdcard(idcard);
		req.setTelephone(et_telephone.getText().toString());
		ApiClient.submitComments(this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				dialogUtil.closeProgressDialog();
				SubmitCommentsRsp rsp = (SubmitCommentsRsp) data;
				if (rsp != null && rsp.getSuccess()) {
					tip = new Tip(CallCenterActivity.this,
							getResources().getString(R.string.st_comment_ok),
							new Tip.onDismissListener() {
								@Override
								public void onDismiss() {
									finish();
								}

							});
					tip.show(0);
				} else {
					tip = new Tip(CallCenterActivity.this,
							getResources().getString(R.string.st_contact_admin), null);
					tip.show(0);
				}
			}

			@Override
			public void onFailed(Object data) {

			}
		});
		
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
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_telephone.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
	public void setIdcard(IdcardModel idcard){
		this.idcard = idcard;
	}
}
