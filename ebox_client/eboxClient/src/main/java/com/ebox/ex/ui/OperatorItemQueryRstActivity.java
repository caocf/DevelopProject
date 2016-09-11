package com.ebox.ex.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspOperatorQueryItem;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.network.model.base.type.QueryItemInfoType;
import com.ebox.ex.network.request.RequestGetHistory;
import com.ebox.ex.ui.adapter.ItemAdapter;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.HashMap;
import java.util.List;

public class OperatorItemQueryRstActivity extends CommonActivity implements OnClickListener
{
	public ListView item_list;
	private Button bt_last;
	private Button bt_next;
	private TextView tv_page;

	private int page,page_size,page_index,cur_page_index;
	private String start_time,end_time;
	private boolean is_query_last;
	private DialogUtil dialogUtil;
	private ItemAdapter adapter;
	private Tip tip;

	private HashMap<Integer,List<OrderInfo>> mCacheData;
	private QueryItemInfoType cache;
	private String next_cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_operator_item_query_rst);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		start_time = getIntent().getStringExtra("start_time");
		end_time=getIntent().getStringExtra("end_time");
		cache = (QueryItemInfoType) getIntent().getSerializableExtra("cache");
		next_cursor=cache.getNext_cursor();

		page_index=1;//页面索引
		cur_page_index=1;

		page=0;//起点位置
		page_size=9;//从起点位置获取的个数
		mCacheData = new HashMap<Integer, List<OrderInfo>>();

		mCacheData.put(cur_page_index, cache.getItems());

		if (cache.getItems().size() < page_size)
		{
			is_query_last = true;
		} else
		{
			is_query_last=false;
		}

		initView();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
	}
	
	private void initView()
	{
		item_list = (ListView) findViewById(R.id.item_list);
		bt_last = (Button) findViewById(R.id.bt_last);
		bt_next = (Button) findViewById(R.id.bt_next);
		tv_page = (TextView) findViewById(R.id.tv_page);
		
		bt_last.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		adapter = new ItemAdapter(this);
		item_list.setAdapter(adapter);
		item_list.setCacheColorHint(0);
		item_list.setDivider(null);
		item_list.setSelector(R.color.transparent);
		item_list.setOnItemClickListener(adapter);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_i_query);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	
	private void initData()
	{
		show();
	}


	private void query_next() {

		if (page_index == 0)
		{
			query();
		}
		else
		{

			if (!is_query_last && cur_page_index >= page_index)
			{
				query();
			}
			else {
				if (cur_page_index >=page_index)
				{
					cur_page_index=page_index;
				}
				else
				{
					cur_page_index++;
				}
				show();
			}

		}

	}

	private void query_last() {
		if (cur_page_index > 1)
		{
			cur_page_index--;
		} else {
			cur_page_index = 1;
		}
		show();
	}

	private void show()
	{
		btnShow();

		setData();
	}

	private void btnShow() {

		boolean has_last;
		boolean has_next;

		if (cur_page_index == 1)
		{
			has_last = false;
		} else {
			has_last = true;
		}


		if (cur_page_index < page_index) {
			has_next = true;
		} else {
			if (!is_query_last)
			{
				has_next = true;
			} else {
				has_next = false;
			}
		}


		if (has_last)
		{
			bt_last.setBackgroundResource(R.drawable.pub_code_btn_orange);
		} else {
			bt_last.setBackgroundResource(R.drawable.pub_code_btn_orange_disabled);
		}
		bt_last.setEnabled(has_last);

		if (has_next)
		{
			bt_next.setBackgroundResource(R.drawable.pub_code_btn_orange);
		} else {
			bt_next.setBackgroundResource(R.drawable.pub_code_btn_orange_disabled);
		}
		bt_next.setEnabled(has_next);
	}

	private void setData(){

		List<OrderInfo> cache = getCache(cur_page_index);
		if (cache!=null)
		{
			adapter.setData(cache);
			adapter.notifyDataSetChanged();
		}
	}

	private List<OrderInfo> getCache(int page_index) {

		if (mCacheData.containsKey(page_index)) {

			return mCacheData.get(page_index);
		} else {
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_last:
			query_last();
			break;
		case R.id.bt_next:
			query_next();
			break;
		}
	}

	private void query() {

		if (is_query_last)
		{
			return;
		}

		HashMap<String, Object> prams = new HashMap<String, Object>();
		prams.put("page", page);
		prams.put("page_size", page_size);
		prams.put("start_date", start_time);
		prams.put("end_date", end_time);
		prams.put("cursor", next_cursor);
		prams.put("state", 100);

		RequestGetHistory requestGetHistory = new RequestGetHistory(prams, new ResponseEventHandler<RspOperatorQueryItem>() {
			@Override
			public void onResponseSuccess(RspOperatorQueryItem result)
			{
				dialogUtil.closeProgressDialog();
				if (result.isSuccess())
				{
					next_cursor=result.getData().getNext_cursor();
					//返回的集合数小于个数表示已经到最后
					int size = result.getData().getItems().size();
					if (size < page_size)
					{
						is_query_last=true;
					}
					if (is_query_last && size == 0) //下一页没数据
					{
						tip = new Tip(OperatorItemQueryRstActivity.this,"已经是最后一页",null);
						tip.show(0);
					} else {
						page_index++;
						cur_page_index++;
						mCacheData.put(page_index, result.getData().getItems());

						//下一页起点
						//page += page_size;
						page++;
					}

					show();
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				LogUtil.e(error.getMessage());
			}
		});
		RequestManager.addRequest(requestGetHistory, null);
		dialogUtil.showProgressDialog();
	}

	private void showPrompt(int resId)
	{
		tip = new Tip(this,
				getResources().getString(resId),
				null);
		tip.show(0);
	}
}
