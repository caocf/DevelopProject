package com.ebox.ex.database.boxDyn;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.BoxStateType;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BoxDynSyncOp {


	private static ArrayList<BoxInfo> getAllBoxInfoByCursor(Cursor cursor)
	{
		if (cursor != null) {
			ArrayList<BoxInfo> box = null;
			try{
				int indexBoardNum = cursor.getColumnIndex(BoxDynTable.BOAED_NUM);
				int indexBoxNum = cursor.getColumnIndex(BoxDynTable.BOX_NUM);
				int indexDoorState = cursor.getColumnIndex(BoxDynTable.DOOR_STATE);
				// Box的状态
				int indexBoxState = cursor.getColumnIndex(BoxDynTable.BOX_STATE);
				int indexBoxSize = cursor.getColumnIndex(BoxDynTable.BOX_SIZE);
				int indexBoxUserState = cursor
						.getColumnIndex(BoxDynTable.BOX_USER_STATE);
				int indexState=cursor.getColumnIndex(BoxDynTable.STATE);
				int indexRackType=cursor.getColumnIndex(BoxDynTable.RACK_TYPE);
				cursor.moveToFirst();
				box =new ArrayList<BoxInfo>();
				while (!cursor.isAfterLast())
				{
					BoxInfo one = new BoxInfo();
					one.setBoardNum(cursor.getInt(indexBoardNum));
					one.setBoxNum(cursor.getInt(indexBoxNum));
					one.setDoorState(cursor.getInt(indexDoorState));
					int boxSize = cursor.getInt(indexBoxSize);
					one.setBoxSize(boxSize== 0 ? 1 : boxSize);//默认全部是小
					one.setBoxState(cursor.getInt(indexBoxState));
					one.setBoxUserState(cursor.getInt(indexBoxUserState));
					one.setState(cursor.getInt(indexState));
					one.setRackType(cursor.getInt(indexRackType));
					box.add(one);
					cursor.moveToNext();
				}
			}catch(Exception e)
			{

			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return box;
		}

		return null;
	}

	private static ArrayList<BoxInfo> getBoxInfoByCursor(Cursor cursor) {
		if (cursor != null) {
			ArrayList<BoxInfo> box = null;
			try{

				int indexBoardNum = cursor.getColumnIndex(BoxDynTable.BOAED_NUM);
				int indexBoxNum = cursor.getColumnIndex(BoxDynTable.BOX_NUM);
				int indexDoorState = cursor.getColumnIndex(BoxDynTable.DOOR_STATE);
				// Box占用状态
				int indexBoxState = cursor.getColumnIndex(BoxDynTable.BOX_STATE);
				// int indexBoxSize = cursor.getColumnIndex(BoxDynTable.BOX_SIZE);
				box=new ArrayList<BoxInfo>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					BoxInfo one = new BoxInfo();
					one.setBoardNum(cursor.getInt(indexBoardNum));
					one.setBoxNum(cursor.getInt(indexBoxNum));
					one.setDoorState(cursor.getInt(indexDoorState));
					one.setBoxState(cursor.getInt(indexBoxState));
					box.add(one);
					cursor.moveToPrevious();
				}
			}catch(Exception e){

			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return box;
		}
		return null;
	}

	private static BoxInfo getExistBoxInfo(BoxInfo box) {
		BoxInfo boxInfo = null;
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(
					BoxDynTable.TABLE_NAME,
					null,
					BoxDynTable.BOAED_NUM + " = '" + box.getBoardNum()
							+ "' AND " + BoxDynTable.BOX_NUM + " = '"
							+ box.getBoxNum() + "'", null, null, null,
					BoxDynTable._ID);

			ArrayList<BoxInfo> list = getAllBoxInfoByCursor(cursor);

			if (list != null &&list.size()>0)
			{
				boxInfo = list.get(0);
			}
			else
			{
				Log.i("box","box not exist:" + box.getBoardNum() + " " + box.getBoxNum());
			}
			return boxInfo;
		} catch (Exception e) {
			LogUtil.e(e.getMessage());
			return boxInfo;
		}
	}

	/**
	 * 锁定箱门
	 * @param boardNum
	 * @param boxNum
	 */
	public static void boxLock(int boardNum,int boxNum){

		ContentValues values = new ContentValues();
		values.put(BoxDynTable.BOX_STATE,BoxStateType.locked);
		try {

			AppApplication.getInstance().getExDs().getDb().update(BoxDynTable.TABLE_NAME, values,
					BoxDynTable.BOAED_NUM + " = '" + boardNum
							+ "' AND " + BoxDynTable.BOX_NUM + " = '"
							+ boxNum + "'", null);
			//LogUtil.i("lock by order box [boardNum:" + boardNum+" boxNum:"+boxNum+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 锁定箱门
	 * @param boxCode
	 */
	public static void boxLock(String boxCode)
	{
		BoxInfo box = BoxUtils.getBoxByCode(boxCode);
		boxLock(box.getBoardNum(),box.getBoxNum());
	}


	/**
	 * 释放箱门
	 * @param boardNum 组号
	 * @param boxNum 门号
	 */
	public static void boxRelease(int boardNum,int boxNum){

		ContentValues values = new ContentValues();
		values.put(BoxDynTable.BOX_STATE,BoxStateType.empty);
		try {

			AppApplication.getInstance().getExDs().getDb().update(BoxDynTable.TABLE_NAME, values,
					BoxDynTable.BOAED_NUM + " = '" + boardNum
							+ "' AND " + BoxDynTable.BOX_NUM + " = '"
							+ boxNum + "'", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放箱门
	 * @param boxCode
	 */
	public static void boxRelease(String boxCode){
		BoxInfo box = BoxUtils.getBoxByCode(boxCode);
		boxRelease(box.getBoardNum(), box.getBoxNum());
	}

	/**
	 * 获取所有需要同步的Box，即BoxDynTable.STATE>0
	 * 
	 * @return
	 */
	public static ArrayList<BoxInfo> getSyncBoxList() {
		try {
			Cursor cursor;
			//重启系统后或重启APK后，上报所有箱门状态
			if (!SharePreferenceHelper.getSyncBoxDoorState())
			{
				cursor = AppApplication.getInstance().getExDs().getDb().query(BoxDynTable.TABLE_NAME, null, null, null, null, null, BoxDynTable._ID);
				LogUtil.i("upload all box door state");
			}else
			 cursor = AppApplication.getInstance().getExDs().getDb().query(BoxDynTable.TABLE_NAME, null, BoxDynTable.STATE
					+ ">0", null, null, null, BoxDynTable._ID);

			return getBoxInfoByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 标识全部Box已经同步
	 */
	public static void setBoxSync() {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + BoxDynTable.TABLE_NAME + " set "
					+ BoxDynTable.STATE + "= 0");
		} catch (Exception e) {
		}
	}


	/**
	 * 查询所以Box的状态
	 * @return
	 */
	public static ArrayList<BoxInfo> getAllSyncBoxList() {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(BoxDynTable.TABLE_NAME, null, null, null,
					null, null, BoxDynTable._ID);

			return getAllBoxInfoByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 *开关门时更新本地箱门状态
	 * @param box
	 */
	public static void updateLocalBoxState(BoxInfo box) {
		ContentValues values = new ContentValues();
		values.put(BoxDynTable.BOAED_NUM, box.getBoardNum());
		values.put(BoxDynTable.BOX_NUM, box.getBoxNum());
		values.put(BoxDynTable.DOOR_STATE, box.getDoorState());
		try {

			BoxInfo checkBox = getExistBoxInfo(box);
			if (checkBox == null)
			{
				values.put(BoxDynTable.STATE, 1);
				AppApplication.getInstance().getExDs().getDb().insert(BoxDynTable.TABLE_NAME, BoxDynTable._ID, values);
			} else {
				// 如果跟记录不同更新门的状态
				if (!checkBox.getDoorState().equals(box.getDoorState()))
				{
					values.put(BoxDynTable.STATE, 1);
					AppApplication.getInstance().getExDs().getDb().update(BoxDynTable.TABLE_NAME, values,
							BoxDynTable.BOAED_NUM + " = '" + box.getBoardNum()
									+ "' AND " + BoxDynTable.BOX_NUM + " = '"
									+ box.getBoxNum() + "'", null);
				}
			}

			return;
		} catch (Exception e) {
			return;
		} finally {
		}
	}

	/**
	 * WebSocket预留箱门或者锁定箱门
	 * @param box
	 * @return
	 */
	public static int syncBoxUserState(BoxInfo box) {
		ContentValues values = new ContentValues();
		values.put(BoxDynTable.BOAED_NUM, box.getBoardNum());
		values.put(BoxDynTable.BOX_NUM, box.getBoxNum());
		values.put(BoxDynTable.BOX_USER_STATE, box.getBoxUserState());
		try {

			BoxInfo checkBox = getExistBoxInfo(box);
			if (checkBox != null)
			{
				AppApplication.getInstance().getExDs().getDb().update(BoxDynTable.TABLE_NAME, values,
						BoxDynTable.BOAED_NUM + " = '" + box.getBoardNum()
								+ "' AND " + BoxDynTable.BOX_NUM + " = '"
								+ box.getBoxNum() + "'", null);
			}else{
				LogUtil.e("reserve box not exist");
			}
		} catch (Exception e) {
			return -1;
		} finally {
		}
		return 1;
	}


	/**
	 * 系统初始化操作更新Box全部信息
	 * @param infoTypes
	 * @return
	 */
	public static int osSyncAllBoxState(List<BoxInfoType> infoTypes){
		for (BoxInfoType infoType : infoTypes)
		{
			BoxInfo box = BoxUtils.getBoxByCode(infoType.getBoxCode());

			box.setBoxState(infoType.getBoxStatus());
			box.setBoxSize(infoType.getBoxSize());
			box.setBoxUserState(infoType.getBoxUserState());
			box.setRackType(infoType.getRackType());

			initBoxState(box);
		}
		return 1;
	}


	private static short initBoxState(BoxInfo box) {
		ContentValues values = new ContentValues();
		values.put(BoxDynTable.BOAED_NUM, box.getBoardNum());
		values.put(BoxDynTable.BOX_NUM, box.getBoxNum());
		values.put(BoxDynTable.BOX_STATE, box.getBoxState());
		values.put(BoxDynTable.BOX_SIZE,box.getBoxSize());
		values.put(BoxDynTable.BOX_USER_STATE, box.getBoxUserState());
		values.put(BoxDynTable.RACK_TYPE, box.getRackType());
		values.put(BoxDynTable.STATE,0);
		try {

			AppApplication.getInstance().getExDs().getDb().update(BoxDynTable.TABLE_NAME, values,
				BoxDynTable.BOAED_NUM + " = '" + box.getBoardNum()
						+ "' AND " + BoxDynTable.BOX_NUM + " = '"
						+ box.getBoxNum() + "'", null);

		} catch (Exception e) {
			return -1;
		} finally {
		}
		return 1;
	}

}
