package com.ebox.pub.service.task;

import android.app.Activity;
import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.AlarmCode;
import com.ebox.ex.network.model.enums.AlarmState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.OsInitHelper;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.boxctl.serial.GetDoorsStatusReq;
import com.ebox.pub.boxctl.serial.GetDoorsStatusRsp;
import com.ebox.pub.boxctl.serial.GetSingleBoardInfoReq;
import com.ebox.pub.boxctl.serial.GetSingleBoardInfoRsp;
import com.ebox.pub.boxctl.serial.OpenDoorReq;
import com.ebox.pub.boxctl.serial.OpenDoorRsp;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.database.alarm.Alarm;
import com.ebox.pub.database.alarm.AlarmOp;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;

import java.util.ArrayList;
import java.util.TimerTask;

public class BoxCtrlTask extends TimerTask {
	// 主副板操作任务计数
	private static Long boxCtrlTaskTimes = 29L; 
	private int readVal = 30;
	private TaskData data;
	
	public BoxCtrlTask(TaskData data)
	{
		this.data = data;
	}
	private void boxCtrlSleep(int sec)
	{
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean boxInitSuccess()
	{
		if(GlobalField.boxInfoLocal == null 
				|| GlobalField.boxInfoLocal.size() != GlobalField.config.getCount()
				|| !GlobalField.boxLocalInit)
		{
			return false;
		}
		
		return true;
	}
	
	public static void readBoxStatus()
	{
		// 读取各个副柜门状态,软件启动好只读一次
		if(!boxInitSuccess())
		{
			ArrayList<RackInfo> rackList = new ArrayList<RackInfo>();
			
			for(int i = 1 ; i <= GlobalField.config.getCount(); i++)
			{
				RackInfo rack= new RackInfo();
				GetSingleBoardInfoReq req = new GetSingleBoardInfoReq();
				req.setBoardId(i);
				
				GetSingleBoardInfoRsp rsp = AppApplication.getInstance().getSb().GetSingleBoardInfo(req);
				if(rsp == null
						|| rsp.getRst() != RstCode.Success
						|| rsp.getBoxCount() == 0 )
				{
					continue;
				}
				else
				{
					rack.setBoardNum(i);
					rack.setCount(rsp.getBoxCount());
					rackList.add(rack);
				}
			}
			
			GlobalField.boxInfoLocal = rackList;
			if(GlobalField.boxInfoLocal != null && GlobalField.boxInfoLocal.size() > 0)
			{
				GlobalField.boxLocalInit = true;
			}
		}
		
		ArrayList<RackInfo> rackList = GlobalField.boxInfoLocal;
		
		for(int i = 0; i < rackList.size(); i++)
		{
			RackInfo rack = rackList.get(i);
			
			BoxOp op =  new BoxOp();
			op.setOpId(BoxOpId.GetDoorsStatus);
			BoxInfo box = new BoxInfo();
			box.setBoardNum(rack.getBoardNum());
			op.setOpBox(box);
			BoxCtrlTask.addBoxCtrlQueue(op);
		}
		
		// 检查副板是否通讯都正常了
		boolean isPortOk = true;
		if(GlobalField.portFailCnt > 0 && boxInitSuccess())
		{
			for(int i = 0; i < rackList.size(); i++)
			{
				for(int j = 0; j < rackList.get(i).getCount(); j++)
				{
					if(rackList.get(i).getBoxes() == null
							|| rackList.get(i).getBoxes()[j] == null
							|| rackList.get(i).getBoxes()[j].getDoorState().equals(DoorStatusType.unknow))
					{
						isPortOk = false;
						break;
					}
				}
				
				if(!isPortOk)
					break;
			}
			
			// 都已经正常
			if(isPortOk)
			{
				GlobalField.portFailCnt = 0;
			}
		}
	}

	private void openDoor(final BoxOp op)
	{
		Log.i(GlobalField.tag, "openDoor:"+op.getOpBox().getBoardNum()+op.getOpBox().getBoxNum()+"report:"+op.getReport());
		OpenDoorReq req = new OpenDoorReq();
		req.setBoardId(op.getOpBox().getBoardNum());
		req.setBoxId(op.getOpBox().getBoxNum());
	
		int retryTimes = 0;
		OpenDoorRsp rsp = null;
		int rst = RstCode.Failed;
		while(true)
		{
			rsp = AppApplication.getInstance().getSb().OpenDoor(req);
			
			if(rsp == null || rsp.getRst() != RstCode.Success)
			{
				retryTimes++;
			}
			else
			{
				rst = RstCode.Success;
				break;
			}
			
			if(retryTimes >= 3)
			{
				break;
			}
		}
		
		if(op.getListener() != null)
		{
			if(op.getListener() instanceof Activity)
			{
				final int finalRst = rst;
				((Activity)op.getListener()).runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								op.getListener().onResult(finalRst);
							}
						});
			}
			else
			{
				op.getListener().onResult(rst);
			}
		}
		//开门成功修改数据库箱门状态
		if(rsp.getRst() == RstCode.Success)
		{
			if(op.getLock()==2)//锁定
			{
				BoxDynSyncOp.boxLock(op.getOpBox().getBoardNum(), op.getOpBox().getBoxNum());
			}
			else
			{
				BoxDynSyncOp.boxRelease(op.getOpBox().getBoardNum(), op.getOpBox().getBoxNum());
			}
		}
		
		if(rsp.getRst() != RstCode.Success) {
			if (op.getReport()!=2)
			{
				//添加到告警表中
				Alarm alarm = new Alarm();
				alarm.setBoxId(BoxUtils.generateBoxCode(op.getOpBox().getBoardNum(), op.getOpBox().getBoxNum()));
				alarm.setContent("open door failed");
				alarm.setState(AlarmState.state_0);
				alarm.setAlarmCode(AlarmCode.code_3);
				AlarmOp.addAlarm(alarm);

				//AutoReportManager.instance().reportAlarm();
			}
		}
	}
	
	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
		//Log.e("Timer", "set mBoxCtrlLastRunning:"+AppService.getIntance().mBoxCtrlLastRunning);
//		AppService.getIntance().checkTask();
		//Log.i("synchronized", "run start:"+System.currentTimeMillis());
		synchronized(GlobalField.boxCtrlQueue)
		{
			while(GlobalField.boxCtrlQueue.size() > 0)
			{
				data.setLastRunning(System.currentTimeMillis());
				int opIndex = 0;
				int j =0;
				// 优先处理开门
				for(j = 0; j < GlobalField.boxCtrlQueue.size(); j++)
				{
					if(GlobalField.boxCtrlQueue.get(j).getOpId().equals(BoxOpId.OpenDoor))
					{
						break;
					}
				}
				
				if(j >= GlobalField.boxCtrlQueue.size())
				{
					opIndex = 0;
				}
				else
				{
					opIndex = j;
				}
				BoxOp op = GlobalField.boxCtrlQueue.get(opIndex);
				
				if(op.getOpId().equals(BoxOpId.GetDoorsStatus))
				{
					// 更新内存中状态信息
					ArrayList<RackInfo> rackList = GlobalField.boxInfoLocal;
					for(int i = 0; i < rackList.size(); i++)
					{
						RackInfo rack = rackList.get(i);
						
						if(rack.getBoardNum().equals(op.getOpBox().getBoardNum()))
						{
							getBoxInfo(rack);
						}
					}
				}
				else if(op.getOpId().equals(BoxOpId.OpenDoor))
				{
					openDoor(op);
				}	
				GlobalField.boxCtrlQueue.remove(opIndex);
				boxCtrlSleep(200);
			}
		}
		//Log.i("synchronized", "run stop:"+System.currentTimeMillis());
		
		boxCtrlTaskTimes++;
		if(boxCtrlTaskTimes >= 65535)
		{
			boxCtrlTaskTimes = 1L;
		}
		
		// 30秒读取一次读取门状态
		if(boxCtrlTaskTimes%readVal == 0 &&
				(!boxInitSuccess() || AppService.getIntance().isSysIdle()))
		{
			readBoxStatus();
		}
	}
	
	public static void addBoxCtrlQueue(BoxOp op)
	{
		//Log.i("synchronized", "addBoxCtrlQueue start:"+System.currentTimeMillis());
		synchronized(GlobalField.boxCtrlQueue)
		{
			GlobalField.boxCtrlQueue.add(op);
			
			if(op.getOpId().equals(BoxOpId.OpenDoor))
			{
				// 本地缓存状态改为打开
				op.getOpBox().setDoorState(DoorStatusType.open);
				BoxUtils.setBoxLocalState(op.getOpBox());
			}
		}

		//Log.i("synchronized", "addBoxCtrlQueue stop:"+System.currentTimeMillis());
	}

	public static void getBoxInfo(RackInfo rack)
	{
		GetDoorsStatusReq req = new GetDoorsStatusReq();
		req.setBoardId(rack.getBoardNum());
		
		GetDoorsStatusRsp rsp = AppApplication.getInstance().getSb().GetDoorsStatus(req);
		
		//遍历是否存在未知箱门，存在重新读一次
		for (int i = 0; i < rack.getCount(); i++) {
			if(rsp == null || rsp.getRst() != RstCode.Success ||
					rsp.getBoxState() == null ||
					rsp.getBoxState().length <= i)
			{
				rsp=AppApplication.getInstance().getSb().GetDoorsStatus(req);
				break;
			}
		}
		
		ArrayList<BoxInfo> boxList = new ArrayList<BoxInfo>();
		
		for(int j= 0; j < rack.getCount(); j++)
		{
			BoxInfo box = new BoxInfo();
			box.setBoardNum(rack.getBoardNum());
			box.setBoxNum(j+1);
			if(rsp == null || rsp.getRst() != RstCode.Success ||
					rsp.getBoxState() == null ||
					rsp.getBoxState().length <= j)
			{
				box.setDoorState(DoorStatusType.unknow);
			}
			else
			{
				box.setDoorState(Integer.valueOf(rsp.getBoxState()[j]));
			}
			boxList.add(box);
		}
		
		// TODO boxList可以和rack中的boxList做对比，如果箱门打开非程序控制则告警！
		rack.setBoxes(boxList.toArray(new BoxInfo[boxList.size()]));
		// 更新BoxDynTable
		for(int i= 0; i < boxList.size(); i++)
		{
			BoxDynSyncOp.updateLocalBoxState(boxList.get(i));
		}
		if (!GlobalField.deviceInit)
		{
			GlobalField.deviceInit=true;
			//下拉服务器订单
			BoxInfoHelper.pullServiceOrder();

			if (SharePreferenceHelper.isReInstall())
			{
				new OsInitHelper(null).checkLocalConfig().updateBoxState();
			}
		}
	}
}
