package com.ebox.st.ui.lic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.AppService;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.QueryWorkProgressReq;
import com.ebox.st.model.QueryWorkProgressRsp;
import com.ebox.st.model.StateModel;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.adapter.JdcxRstAdapter;

import java.util.ArrayList;

public class LicJdcxActivity extends CommonActivity implements OnClickListener{
	private ListView list;
	private Tip tip;
	private TextView tv_state_top;
	private JdcxRstAdapter mAdapter;
	
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	private ArrayList<StateModel> listState ;
	
	private IdcardModel idcard;
	// 证照类型
	private String type;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_fg_jdcx_rst);
        MGViewUtil.scaleContentView(this,R.id.rootView);
        type = getIntent().getStringExtra("work_type");
        idcard = (IdcardModel)getIntent().getSerializableExtra("idcard");
        initView();
	}
	

	@SuppressLint("SetJavaScriptEnabled") 
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_jdcx);
		title.setData(titleData, this);
		tv_state_top = (TextView) findViewById(R.id.tv_state_top);
		list = (ListView) findViewById(R.id.item_list);
		mAdapter = new JdcxRstAdapter(this);
		listState = new ArrayList<StateModel>();
		mAdapter.addAll(listState);
		list.setAdapter(mAdapter);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		
	}
	
	private void initData(){
		getStateInfo();
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
		title.showTimer();
		AppService.getIntance().hasOnKeyDown();
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
	
	private void getStateInfo() {
        QueryWorkProgressReq req = new QueryWorkProgressReq();
        req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
        req.setIdentification(type);
        req.setIdcard(idcard);
        ApiClient.queryWorkProgress(this,req,new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                dialogUtil.closeProgressDialog();
                QueryWorkProgressRsp rsp = (QueryWorkProgressRsp)data;
                showData(rsp);
            }

            @Override
            public void onFailed(Object data) {
                dialogUtil.closeProgressDialog();
            }
        });
	}
	
	private void showPrompt(int resId) {
		tip = new Tip(LicJdcxActivity.this, getResources().getString(
				resId), null);
		tip.show(0);
	}
	
	
	private void showData(QueryWorkProgressRsp rsp){
        if(rsp!=null && rsp.getSuccess())
        {
            if(rsp.getResult().size() > 0 )
            {
                tv_state_top.setText("当前状态："+ rsp.getResult().get(0).getCurrent_state());

                listState = rsp.getResult();
            } else{
                showPrompt(R.string.not_data);
            }
        }
        else
        {
			tip = new Tip(this,
					getResources().getString(R.string.st_contact_admin),null);
			tip.show(0);
        }
		mAdapter.addAll(listState);
		mAdapter.notifyDataSetChanged();
	}
}
