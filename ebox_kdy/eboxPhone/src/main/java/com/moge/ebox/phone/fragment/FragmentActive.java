package com.moge.ebox.phone.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import tools.AppException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.model.AcitveModel;
import com.moge.ebox.phone.ui.FastRechargeActivity;
import com.moge.ebox.phone.ui.adapter.ActiveAdapter;

public class FragmentActive extends Fragment {

	private GridView mGridView;
	private ActiveAdapter mAdapter;
	private FastRechargeActivity mContext;
	private ArrayList<AcitveModel> mData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=(FastRechargeActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		getAcitve();
		return view;
	}

	private void getAcitve() 
	{
		Map params = new HashMap();
//		String dot_id = EboxApplication.getInstance().getLoginUserInfo().dot_id;
//		params.put("dot_id", dot_id);
		ApiClient.getActive(EboxApplication.getInstance(), params, new ClientCallback() {
			
			@Override
			public void onSuccess(JSONArray data,int code) {
				showData(data);
			}
			
			@Override
			public void onFailed(byte[] data,int code) {
			}
		});
	}

	protected void showData(JSONArray data) {
		try {
			AcitveModel acitveModel=new AcitveModel();
			ArrayList<AcitveModel> list = acitveModel.parseList(data.getJSONObject(0).getJSONArray("activities").toString());
			mAdapter=new ActiveAdapter(mContext);
			mAdapter.addAll(list);
			mData=list;
			mGridView.setAdapter(mAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_activite, null);
		mGridView = (GridView) view.findViewById(R.id.GridView);
		return view;
	}

 
}
