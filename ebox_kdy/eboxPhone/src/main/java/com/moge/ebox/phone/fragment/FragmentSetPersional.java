package com.moge.ebox.phone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.model.UserInfo;
import com.moge.ebox.phone.bettle.network.http.CommonValue;
import com.moge.ebox.phone.ui.BaseActivity;
import com.moge.ebox.phone.ui.customview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentSetPersional extends Fragment {

	private RoundedImageView iv_head_icon;
	private BaseActivity context;
	private TextView tv_company, tv_name;
	private TextView tv_balance;
	private TextView tv_phone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = (BaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		showData();
		return view;
	}

	private void showData() {
		// Bitmap bitmap = context.getMyCacheBitmap();
		// if (bitmap!=null) {
		// iv_head_icon.setImageBitmap(bitmap);
		// }else {
		String url = EboxApplication.getInstance().getLoginSrc();
		if (!TextUtils.isEmpty(url)) {
			String u = CommonValue.PIC_API + url;
		//	Log.i("main", "头像地址：" + u);
			ImageLoader.getInstance().displayImage(u, iv_head_icon);
		}
		// }

		UserInfo info = EboxApplication.getInstance().getLoginUserInfo();
		tv_company.setText(info.orgnization_name);
		tv_balance.setText(getMoney(info.balance));
		tv_phone.setText(info.telephone);
		tv_name.setText(info.operator_name);
	}

	private CharSequence checkCompany(String orgnization_name) {
		if (orgnization_name.contains("快递")) {
			return orgnization_name;
		}
		return orgnization_name + "快递";
	}

	private String getMoney(String sMoney) {

		Integer money = Integer.valueOf(sMoney);
		return "￥" + money / 100 + "." + String.format("%02d", money % 100);
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_set_persional, null);
		iv_head_icon = (RoundedImageView) view.findViewById(R.id.iv_head_icon);
		tv_company = (TextView) view.findViewById(R.id.tv_company);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_balance = (TextView) view.findViewById(R.id.tv_balance);
		tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		return view;
	}
}
