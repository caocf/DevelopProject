package com.moge.ebox.phone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.utils.ToastUtil;

public class FragmentSetFeedback extends Fragment{
	
	
	private EditText medContent;
	private Button mbtnConfim;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		setEvent();
		return view;
	}

	private void setEvent() {
		mbtnConfim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String m=medContent.getText().toString();
				if (m.length()>0) {
					ToastUtil.showToastShort("提交成功");
					medContent.getEditableText().clear();
				}else {
					ToastUtil.showToastShort("请输入您的宝贵意见");
				}
			}
		});
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_set_feed_back, null);
		mbtnConfim=(Button) view.findViewById(R.id.btn_confim);
		medContent=(EditText) view.findViewById(R.id.et_content);
		
		return view;
	}
}
