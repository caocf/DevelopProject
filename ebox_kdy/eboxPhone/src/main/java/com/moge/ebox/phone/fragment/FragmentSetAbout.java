package com.moge.ebox.phone.fragment;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.utils.PackageUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentSetAbout extends Fragment{

	private TextView tv_version;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set_about, null);
		tv_version=(TextView) view.findViewById(R.id.tv_version);
		tv_version.setText("V "+PackageUtil.getVersionName(getActivity()));
		return view;
	}
}
