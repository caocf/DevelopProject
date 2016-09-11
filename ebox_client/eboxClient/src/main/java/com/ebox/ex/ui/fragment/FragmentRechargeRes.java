package com.ebox.ex.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.OperatorHomeActivity;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.QRImageUtil;

public class FragmentRechargeRes extends Fragment {

	private String mUrl;
	private String mMoney;
	private String mPhone;

	private ImageView iv_Code;
	private TextView tv_phone;
	private TextView tv_money;
	private Button bt_cancle;

	public static FragmentRechargeRes instance(String order, String money,
			String phone) {
		FragmentRechargeRes res = new FragmentRechargeRes();
		Bundle bundle = new Bundle();
		bundle.putString("url", order);
		bundle.putString("money", money);
		bundle.putString("phone", phone);
		res.setArguments(bundle);
		return res;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mUrl = bundle.getString("url", "");
		mMoney = bundle.getString("money", "money");
		mPhone = bundle.getString("phone", "phone");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = init(inflater);
		initdata();
		return view;
	}

	private void initdata() {
		//mUrl = "https://qr.alipay.com/gdyiwmznyb9gmf1g81";
		tv_money.setText(mMoney + "元");
		tv_phone.setText(mPhone);
		Bitmap image = QRImageUtil.createQRImage(mUrl,BitmapFactory.decodeResource(getResources(), R.drawable.ex_paysafe_alipay));
		iv_Code.setImageBitmap(image);
	}

	private View init(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.ex_fragment_recharge, null);
		MGViewUtil.scaleContentView((ViewGroup) view);
		tv_money = (TextView) view.findViewById(R.id.tv_charge);
		tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		iv_Code = (ImageView) view.findViewById(R.id.iv_code);
		bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
		bt_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		return view;
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		AppApplication.getInstance().sendBroadcast(new Intent(OperatorHomeActivity.BROADCAST_UPDATE_OPERATOR));
	}
	
}
