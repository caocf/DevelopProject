package com.ebox.mgt.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.bar.BoxBar;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

public class BoxStatusActivity extends CommonActivity {
	private int boardNum;
	private GridView box_list;
	private RackInfo rack;
	private BoxesAdapter adapter;
	private Handler refreshHandler;
	static private Runnable run;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgt_activity_box_status);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		// 箱体数据
		boardNum = getIntent().getIntExtra("boardNum", 0);
		RackInfo rackInList;
		if(GlobalField.boxInfoLocal != null)
		{
			for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
			{
				rackInList = GlobalField.boxInfoLocal.get(i);
				if(rackInList.getBoardNum().equals(boardNum))
				{
					rack = rackInList;
					break;
				}
			}
		}
		initView();
		refreshHandler = new MyHandler();
		run = new Runnable() {
			@Override
			public void run() {
				refreshView();
				if(refreshHandler != null)
				{
					refreshHandler.sendEmptyMessage(0);
				}
			}
		};
		refreshHandler.sendEmptyMessage(0);
	}
	
	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				this.postDelayed(run, 1000);
			}
		}
	};
	
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
		refreshHandler.removeCallbacks(run);
		refreshHandler = null;
	}
	
	private void initView()
	{
		box_list = (GridView) findViewById(R.id.box_list);
		adapter = new BoxesAdapter();
		box_list.setAdapter(adapter);
		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.mgt_box_status);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	
	public class BoxesAdapter extends BaseAdapter {
	    public BoxesAdapter() {
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	BoxBar bt;

	        if (convertView == null) {
	        	bt = new BoxBar(BoxStatusActivity.this);
                MGViewUtil.scaleContentView(bt);
	        } else {
	        	bt = (BoxBar) convertView;
	        }
	        bt.setData(rack.getBoxes()[position]);
			
	        return bt;
	    }

	    public final int getCount() {
	        return rack.getBoxes().length;
	    }

	    public final Object getItem(int position) {
	        return rack.getBoxes()[position];
	    }

	    public final long getItemId(int position) {
	        return position;
	    }
	}
	
	private void refreshView()
	{
		// 获取箱门状态
		BoxOp op =  new BoxOp();
		op.setOpId(BoxOpId.GetDoorsStatus);
		op.setOpBox(rack.getBoxes()[0]);
		BoxCtrlTask.addBoxCtrlQueue(op);
		adapter.notifyDataSetChanged();
	}
}
