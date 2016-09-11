package com.moge.ebox.phone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.model.OrderDetailsModel;
import com.moge.ebox.phone.ui.BaseActivity;
import com.moge.ebox.phone.utils.ToastUtil;

public class FragmentDetailsOrder extends Fragment {

	private BaseActivity mContext;
	private OrderDetailsModel model;

	private RelativeLayout rl_back;
	private TextView tv_order_id, tv_state, tv_express_name, tv_express_phone,
			tv_community, tv_community_place, tv_deliver_time, tv_fetch_time,
			tv_charge;

	public static FragmentDetailsOrder getInstance(OrderDetailsModel json) {
		FragmentDetailsOrder detailsOrder = new FragmentDetailsOrder();
		Bundle bundle = new Bundle();
		bundle.putSerializable("json", json);
		detailsOrder.setArguments(bundle);
		return detailsOrder;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mContext = (BaseActivity) getActivity();
		model=(OrderDetailsModel) bundle.getSerializable("json");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = initView(inflater,container);
		setListener();
		setData();
		return view;
	}

	private void setData() {

		if (model == null) {
			ToastUtil.showToastShort("数据错误");
			return;
		}
		setTextVaules(tv_order_id, model.getItem_id());
		setTextVaules(tv_state, ViewUtils.checkState(model.getState()));
//		setTextVaules(tv_express_name, model.get());
		setTextVaules(tv_express_phone, model.getMsisdn());
		setTextVaules(tv_community, model.getTerminal().terminal_name);
		setTextVaules(tv_community_place, "");
		setTextVaules(tv_deliver_time, model.getDelivery_at());
		setTextVaules(tv_fetch_time, model.getFetch_at());
		
//		Integer is_account = model.getIs_account();
//		if (is_account==null)
//		{
//			setTextVaules(tv_charge, ViewUtils.getMoney(model.getCharge()));
//		}
//		else if (is_account==0) //0不计费，1计费
//		{
//			setTextVaules(tv_charge, "不计费");
//		}
//		else {
//		}
		setTextVaules(tv_charge, ViewUtils.getMoney(model.getCharge()));
		
	}

	private void setTextVaules(TextView view, String valuse) {
		if (!TextUtils.isEmpty(valuse)) {
			view.setText(valuse);
		} else {
			view.setText("暂无数据");
		}
	}

	private void setListener() {

		rl_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});

	}

	private View initView(LayoutInflater inflater,ViewGroup container) {

		View view = inflater.inflate(R.layout.fragment_details_order, null);

		rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
		tv_order_id = (TextView) view.findViewById(R.id.tv_order_id);
		tv_state = (TextView) view.findViewById(R.id.tv_state);
		tv_express_name = (TextView) view.findViewById(R.id.tv_express_name);
		tv_express_phone = (TextView) view.findViewById(R.id.tv_express_phone);
		tv_community = (TextView) view.findViewById(R.id.tv_community);
		tv_community_place = (TextView) view
				.findViewById(R.id.tv_community_place);
		tv_deliver_time = (TextView) view.findViewById(R.id.tv_deliver_time);
		tv_fetch_time = (TextView) view.findViewById(R.id.tv_fetch_time);
		tv_charge = (TextView) view.findViewById(R.id.tv_charge);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mContext.setBooleanIsShow(true);
	}
	
	@Override
	public void onDestroy() {
		mContext.setBooleanIsShow(false);
		super.onDestroy();
	}
	

}
