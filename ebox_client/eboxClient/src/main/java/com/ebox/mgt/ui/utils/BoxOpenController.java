package com.ebox.mgt.ui.utils;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.mgt.ui.fragment.FragmentBoxTest;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.service.task.BoxCtrlTask;


public class BoxOpenController {

	private Handler handler;
	private Runnable run;

	private static BoxOpenController boxOpenController;
	public BoxInfo box;

	private BoxOpenController() {
	};

	public static BoxOpenController getInstance() {
		if (boxOpenController == null) {
			synchronized (BoxOpenController.class) {
				if (boxOpenController == null) {
					boxOpenController = new BoxOpenController();
				}
			}
		}
		return boxOpenController;
	}

	/**
	 * 信息注入
	 * 
	 * @param mHandler
	 */
	public void init(Handler mHandler) {
		handler = mHandler;
	}

	/**
	 * 打开箱门
	 */
	public void openBox(int boardNum, int boxCode) {

		box = new BoxInfo();
		box.setBoardNum(boardNum);
		box.setBoxNum(boxCode);

		Log.i("tag", "------>" + box.getBoardNum() + "   " + box.getBoxNum());
		
		Toast.makeText(AppApplication.globalContext, "开始打开" + box.getBoardNum() + "组"
				+ box.getBoxNum() + "号门",Toast.LENGTH_LONG).show();

		// 获得箱门信息
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(box);
		op.setListener(new OpenBoxResultListener());
		BoxCtrlTask.addBoxCtrlQueue(op);
	}

	/**
	 * 检测箱门状态
	 * 
	 * @param box
	 */
	public void checkOpenBox() {
		// 开始检测所开箱门状态
		// TODO Auto-generated method stub
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.GetDoorsStatus);
		op.setOpBox(box);
		BoxCtrlTask.addBoxCtrlQueue(op);

		// 如果箱门关闭 打开下一号门 最后一个箱门打开 进入测试报告
		if (BoxUtils.getBoxLocalState(box) == DoorStatusType.close) {
			Log.i("tag", "--------------------BOX_CLOSE");
			handler.sendEmptyMessageDelayed(FragmentBoxTest.BOX_CLOSE,1000);
		}else if (BoxUtils.getBoxLocalState(box) == DoorStatusType.open) {
			handler.sendEmptyMessageDelayed(FragmentBoxTest.BOX_OPEN,1000);
			Log.i("tag", "--------------------BOX  Open");
		}

	}

	/**
	 * 设置开门监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class OpenBoxResultListener implements BoxOp.resultListener {

		@Override
		public void onResult(int result) {
			// TODO Auto-generated method stub
			if (result == RstCode.Success) {
				// 开门成功
				handler.sendEmptyMessage(FragmentBoxTest.BOX_OPEN);
			} else {
				// 开门失败 则返回开门失败的错误给服务器
				handler.sendEmptyMessageDelayed(FragmentBoxTest.BOX_ERROR,1000);
				Log.i("tag", "openstate----error");
			}
		}
	}

}
