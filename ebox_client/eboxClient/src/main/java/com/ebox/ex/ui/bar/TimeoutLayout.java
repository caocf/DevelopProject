package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.OrderImageType;
import com.ebox.ex.ui.adapter.TimeoutItemAdapter;
import com.ebox.ex.ui.bar.TimeoutItemBar.TimeoutItemBarListener;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOp.resultListener;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.camera.CameraData;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.pub.utils.Tip.onDismissListener;

import java.util.ArrayList;

public class TimeoutLayout  extends OperMainLayout  implements 
	TimeoutItemBarListener,resultListener{
	private Context context;
	private static final String TAG = "TimeoutLayout";
	public ListView item_list;
	private DialogUtil dialogUtil;
	private TimeoutItemAdapter adapter;
	
	private OrderLocalInfo item_deal;
	
	private CameraData cameraData;
	private Tip tip;
    private String operatorId;
	
	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_m_timeout_layout, this, 
				true);
		// 开启摄像头
		AppApplication.getInstance().getCc().start();
		initView();
		initData();
	}

	@Override
	public void onUpdate() {
		initData();
	}

	public void saveParams() {
		// 关闭摄像头
		AppApplication.getInstance().getCc().release();
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	public TimeoutLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public TimeoutLayout(Context context,String operatorId) {
		super(context);
        this.operatorId = operatorId;
		initViews(context);
	}
	

	private void initView()
	{
		item_list = (ListView) findViewById(R.id.item_list);
		adapter = new TimeoutItemAdapter((Activity)context);
		item_list.setAdapter(adapter);
		item_list.setCacheColorHint(0);
		item_list.setDivider(null);
		item_list.setSelector(R.color.transparent);
		item_list.setOnItemClickListener(adapter);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);
	}
	
	private void initData()
	{
			//查询本地逾期件
		int i= queryLocalTimeOut();
		if (i==0) 
		{
			tip = new Tip((Activity)context, 
					getResources().getString(R.string.ex_timeout_empty),
					null);
			tip.show(0);
		}
	}
	//查询本地逾期件
	private int queryLocalTimeOut() {

		ArrayList<OrderLocalInfo> list = OrderLocalInfoOp.getAllTimeOutOrderByOperator(operatorId);

		if (list != null&&list.size() > 0)
		{
			adapter.setData(list, this);
			adapter.notifyDataSetChanged();
			return 1;
		}else {
			return 0;
		}
	}

	@Override
	public void onDealClick(OrderLocalInfo item) {
		BoxInfo box = BoxUtils.getBoxByCode(item.getBox_code());
		if(BoxUtils.getBoxLocalState(box).equals(DoorStatusType.unknow))
		{
			Log.e(TAG, "Box state error:"+item.getBox_code());
			tip = new Tip((Activity)context, 
					getResources().getString(R.string.ex_system_err),
					null);
			tip.show(0);
		}
		else
		{
			item_deal = item;
			withdrawExpiredItem();
		}
	}
	
	private void withdrawExpiredItem(){
		Log.i(GlobalField.tag, "start withdraw time out order ,OpenDoor:"+item_deal.getBox_code()+",itemId:"+item_deal.getItem_id());
		// 开门
		BoxOp op =  new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(item_deal.getBox_code()));
		op.setListener(this);
		BoxCtrlTask.addBoxCtrlQueue(op);
		dialogUtil.showProgressDialog();
	}
	
	private void refreshList()
	{
		adapter.setData(null, this);
		adapter.notifyDataSetChanged();
		initData();
	}

	@Override
	public void onResult(int result) {
		dialogUtil.closeProgressDialog();
		if(result == RstCode.Success)
		{

			// 拍照
			cameraData = new CameraData();
			cameraData.setOrderId(item_deal.getOrder_id());
			cameraData.setItemId(item_deal.getItem_id());
			cameraData.setImageType(OrderImageType.PICK_UP);
			AppApplication.getInstance().getCc().takePicture(cameraData, false);

			Log.i(GlobalField.tag, "withdraw time out order success ,OpenDoor:" + item_deal.getBox_code());
			// 播放音效
			RingUtil.playRingtone(RingUtil.withdraw_id);
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tip = new Tip((Activity) context,
							getResources().getString(R.string.ex_withdraw_success), new onDismissListener() {
						@Override
						public void onDismiss() {
							refreshList();
						}
					});
					tip.show(0);
				}
			});


			//设置订单为快递员取出
			item_deal.setUser_type(OrderLocalInfo.operator);

			OrderLocalInfoOp.updateLocalOrderState(item_deal);

			//释放箱门
			//BoxDynSyncOp.boxRelease(item_deal.getBox_code());

			//AutoReportManager.instance().reportPickupOrder();
		}
		else
		{
			Log.i(GlobalField.tag, "withdraw time out order failed ,OpenDoor:"+item_deal.getBox_code());
			//设置成未超期状态
			OrderLocalInfoOp.updateLocalOrderToNotTimeOut(item_deal.getOrder_id());

			// 播放音效
			RingUtil.playRingtone(RingUtil.calladmin_id);
			final String box=BoxUtils.getBoxDesc(item_deal.getBox_code());

			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tip = new Tip((Activity) context,
							box + "开门失败，请联系管理员取件", new onDismissListener() {
						@Override
						public void onDismiss() {
							refreshList();
						}
					});
					tip.show(0);
				}
			});
		}
	}
}
