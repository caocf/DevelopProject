package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.mgt.ui.adapter.FragOrderAdapter;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class FragmentOrder extends Fragment {

	private ListView list;
	private FragOrderAdapter mAdapter;
	private ArrayList<OrderLocalInfo> mData;
	private TextView tv_hint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = OrderLocalInfoOp.getAllOrderLocals();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);

		return view;
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.mgt_fragment_sqlite_query, null);
        MGViewUtil.scaleContentView((ViewGroup)view);
		list = (ListView) view.findViewById(R.id.list);
		tv_hint = (TextView) view.findViewById(R.id.tv_hint);
		mAdapter = new FragOrderAdapter(getActivity());
		mAdapter.addAll(mData);
		list.setAdapter(mAdapter);
		if (mData == null || mData.size() == 0) {
			tv_hint.setVisibility(View.VISIBLE);
		} else {
			tv_hint.setVisibility(View.GONE);
		}
		return view;
	}

}
