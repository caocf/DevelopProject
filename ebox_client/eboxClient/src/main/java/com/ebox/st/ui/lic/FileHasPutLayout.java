package com.ebox.st.ui.lic;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.bar.OperMainLayout;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.GetUserFileReq;
import com.ebox.st.model.IdentiOrderModel;
import com.ebox.st.model.QueryUserFilesReq;
import com.ebox.st.model.QueryUserFilesRsp;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.ui.lic.GetfileItemBar.TimeoutItemBarListener;

public class FileHasPutLayout  extends OperMainLayout implements
        TimeoutItemBarListener, Tip.onDismissListener,BoxOp.resultListener {
	private Context context;
	private static final String TAG = "GetfileLayout";
	public ListView item_list;
	private DialogUtil dialogUtil;
	private GetFileItemAdapter adapter;
	private IdentiOrderModel item_deal;
	private Tip tip;
	
	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.st_getfile_layout, this,
				true);
		initView();
		initData();
	}
	
	public void saveParams() {
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	public FileHasPutLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public FileHasPutLayout(Context context) {
		super(context);
		initViews(context);
	}
	

	private void initView()
	{
		item_list = (ListView) findViewById(R.id.item_list);
		adapter = new GetFileItemAdapter((Activity)context);
		item_list.setAdapter(adapter);
		item_list.setCacheColorHint(0);
		item_list.setDivider(null);
		item_list.setSelector(R.color.transparent);
		item_list.setOnItemClickListener(adapter);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity) context);
	}
	
	private void initData()
	{
		getFiles();
	}
	
	private void getFiles() {
		dialogUtil.showProgressDialog();
		QueryUserFilesReq req = new QueryUserFilesReq();
		req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		req.setSource("worker");


		ApiClient.queryUserFiles(context, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				dialogUtil.closeProgressDialog();
				QueryUserFilesRsp rsp = (QueryUserFilesRsp) data;
				showData(rsp);
			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
			}
		});
	}


	private void showData(QueryUserFilesRsp rsp)
	{
        if(rsp!=null && rsp.getSuccess())
        {
            if (rsp.getOrder() == null  || rsp.getOrder().size() == 0)
            {
                tip = new Tip((Activity)context,
                        getResources().getString(R.string.st_getfile_empty),
                        this);
                tip.show(0);
            }
            else
            {
                adapter.setData(rsp.getOrder(), FileHasPutLayout.this);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
			tip = new Tip((Activity)context,
					getResources().getString(R.string.st_contact_admin),null);
			tip.show(0);
        }

	}

	@Override
	public void onDealClick(IdentiOrderModel item) {
		BoxInfo box = BoxUtils.getBoxByCode(item.getBox_id());
		if(BoxUtils.getBoxLocalState(box).equals(DoorStatusType.unknow))
		{
			Log.e(TAG, "Box state error:"+item.getBox_id());
			tip = new Tip((Activity)context, 
					getResources().getString(R.string.ex_system_err),
					null);
			tip.show(0);
		}
		else
		{
			item_deal = item;
			getAFile(item);
		}
	}
	
	private void withdrawItem(){
		Log.i(GlobalField.tag, "withdrawOpenDoor:"+item_deal.getBox_id());
		// 开门
		BoxOp op =  new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(BoxUtils.getBoxByCode(item_deal.getBox_id()));
		op.setListener(this);
		BoxCtrlTask.addBoxCtrlQueue(op);
		dialogUtil.showProgressDialog();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getAFile(IdentiOrderModel item) {
		dialogUtil.showProgressDialog();
		getFileSuccess(item);
	}

	private void getFileSuccess(IdentiOrderModel item) {
		GetUserFileReq req = new GetUserFileReq();
		req.setSource("worker");
		req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		req.setSt_identi_order_id(item.getSt_identi_order_id()+"");
		ApiClient.getUserFile(context, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				dialogUtil.closeProgressDialog();
				withdrawItem();
			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
			}
		});
	}

	private void refreshList()
	{
		adapter.setData(null, this);
		adapter.notifyDataSetChanged();
		initData();
	}

	@Override
	public void onDismiss() {
	}

	@Override
	public void onResult(int result) {
		dialogUtil.closeProgressDialog();
		if(result == RstCode.Success)
		{
			Log.i(GlobalField.tag, "withdrawOpenDoor success:"+item_deal.getBox_id());
			// 播放音效
//			RingUtil.playRingtone(RingUtil.withdraw_id);
			((Activity)context).runOnUiThread(
					new Runnable() {
						@Override
						public void run() {
							tip = new Tip((Activity)context,
									getResources().getString(R.string.ex_withdraw_success),
									new Tip.onDismissListener(){
										@Override
										public void onDismiss() {
											refreshList();
										}
									}
									);
							tip.show(0);
						}
					});
			}
		else
		{
			RingUtil.playRingtone(RingUtil.calladmin_id);
			final String box=BoxUtils.getBoxDesc(item_deal.getBox_id());
			((Activity)context).runOnUiThread(
					new Runnable() {
						@Override
						public void run() {
							tip = new Tip((Activity)context, 
									box+"开门失败，请联系管理员取件",
									new Tip.onDismissListener(){
										@Override
										public void onDismiss() {
											refreshList();
										}
									}
									);
							tip.show(0);
						}
					});
		}
	}
}
