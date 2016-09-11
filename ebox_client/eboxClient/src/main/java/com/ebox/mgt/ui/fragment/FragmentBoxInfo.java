package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.BoxStateType;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.RackType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class FragmentBoxInfo extends Fragment {

	private ListView list;
	private ArrayList<BoxInfo> mInfos;
	private FragBoxAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInfos = BoxDynSyncOp.getAllSyncBoxList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.mgt_fragment_box_state, null);
		list = (ListView) view.findViewById(R.id.list);
		
		mAdapter=new FragBoxAdapter(getActivity());
		mAdapter.addAll(mInfos);
		list.setAdapter(mAdapter);
		return view;
	}

	private class FragBoxAdapter extends BaseListAdapter<BoxInfo> {
		private Context context;

		public FragBoxAdapter(Context context) {
			this.context = context;
		}

		@Override
		public View getView(int position, View layout, ViewGroup parent) {
			viewHolder holder;
			if (layout == null) {
				holder = new viewHolder();
				layout = View.inflate(context, R.layout.ex_item_box_info, null);
				holder.tv_group = (TextView) layout.findViewById(R.id.tv_group);
				holder.tv_door = (TextView) layout.findViewById(R.id.tv_door);
				holder.tv_doorState = (TextView) layout
						.findViewById(R.id.tv_doorState);
				holder.tv_boxState = (TextView) layout
						.findViewById(R.id.tv_boxState);
				holder.tv_boxSize = (TextView) layout
						.findViewById(R.id.tv_boxSize);
				holder.tv_boxUserState = (TextView) layout
						.findViewById(R.id.tv_boxUserState);
				holder.tv_updateState = (TextView) layout
						.findViewById(R.id.tv_updateState);
				layout.setTag(holder);
                MGViewUtil.scaleContentView((ViewGroup)layout);
			} else {
				holder = (viewHolder) layout.getTag();
			}

			BoxInfo boxInfo = list.get(position);
			
			holder.tv_group.setText(getRackType(boxInfo.getRackType()));
			holder.tv_door.setText(getBoxCode(boxInfo.getBoardNum(),boxInfo.getBoxNum()));
			holder.tv_boxSize.setText(getBoxSize(boxInfo.getBoxSize()));
			holder.tv_doorState.setText(getDoorState(boxInfo.getDoorState()));
			holder.tv_boxState.setText(getBoxState(boxInfo.getBoxState()));
			holder.tv_boxUserState.setText(getBoxUserState(boxInfo
					.getBoxUserState()));
			holder.tv_updateState.setText(getUpdateState(boxInfo.getState()));
			return layout;
		}

		private String getBoxCode(Integer a,Integer b){

			return BoxUtils.generateBoxCode(a,b);
		}

		private CharSequence getRackType(Integer type) {

			if (RackType.box_kd==type) {
				return "快递柜";
			}else if(RackType.box_sx==type){
				return "生鲜柜";
			}else if(RackType.box_bw==type){
				return "保温柜";
			}
			return null;
		}

		private CharSequence getBoxSize(Integer boxSize) {
			if (BoxSizeType.small==boxSize) {
				return "小格";
			}else if(BoxSizeType.medium==boxSize) {
				 return "中格";
			}else if(BoxSizeType.large==boxSize){
				return "大格";
			}else if(BoxSizeType.tiny==boxSize){
				return "微格";
			}
			return null;
		}

		private CharSequence getUpdateState(Integer state) {
			if (state==0) {
				return "已同步";
			}else if(state==1){
				return "待同步";
			}
			return null;
		}

		private CharSequence getBoxUserState(Integer boxUserState) {
			if (BoxUserState.alarmLock==boxUserState) {
				return "告警锁定";
			}else if(BoxUserState.hardFault==boxUserState) {
				return "硬件故障";
			}else if(BoxUserState.normal ==boxUserState){
				return "正常使用";
			}else if(BoxUserState.reserve==boxUserState){
				return "预留箱门";
			}
			return "boxUserState:"+boxUserState;
		}

		private CharSequence getBoxState(Integer boxState) {
			if (BoxStateType.empty==boxState) {
				return "无件";
			}
			else if(BoxStateType.locked==boxState)
			{
				return "有件";
			}
			return "BosState:"+boxState;
		}

		private CharSequence getDoorState(Integer state) {
			if (DoorStatusType.close==state) {
				return "关闭";
			}else if(DoorStatusType.open==state){
				return "打开";
			}else if(DoorStatusType.unknow==state){
				return "未知";
			}
			return "Door:"+state;
		}

		class viewHolder {
			TextView tv_group;
			TextView tv_door;
			TextView tv_doorState;
			TextView tv_boxState;
			TextView tv_boxSize;
			TextView tv_boxUserState;
			TextView tv_updateState;
		}

	}

}
