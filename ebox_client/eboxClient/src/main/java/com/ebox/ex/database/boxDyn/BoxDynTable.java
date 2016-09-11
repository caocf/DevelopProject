package com.ebox.ex.database.boxDyn;

import android.provider.BaseColumns;

public class BoxDynTable implements BaseColumns{
	private BoxDynTable()
	{

	}
	public static final String TABLE_NAME = "boxdyn";
	public static final String BOAED_NUM = "BoardNum";
	public static final String BOX_NUM = "BoxNum";
	public static final String DOOR_STATE = "DoorState";
	public static final String BOX_SIZE = "BoxSize";
	public static final String BOX_STATE = "BoxState";//格口占用状态
	public static final String BOX_USER_STATE="BoxUserState";
	public static final String RACK_TYPE = "RackType";
	/**
	 * 0:没变化 1：需要同步
	 */
	public static final String STATE = "state";
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE " + BoxDynTable.TABLE_NAME
				+ " ("
				+ BoxDynTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ BoxDynTable.BOAED_NUM + " INTEGER,"
				+ BoxDynTable.BOX_NUM + " INTEGER ,"
				+ BoxDynTable.DOOR_STATE + " INTEGER,"
				+ BoxDynTable.BOX_STATE + " INTEGER,"
				+ BoxDynTable.STATE + " INTEGER, "
				+ BoxDynTable.BOX_SIZE + " INTEGER,"
				+ BoxDynTable.BOX_USER_STATE + " INTEGER"
				+")";
		return sqlStr;
	}

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}
	public static String addRackTypeStr()
	{
		String sqlStr = "ALTER TABLE " + BoxDynTable.TABLE_NAME + " ADD "
				+ BoxDynTable.RACK_TYPE + " INTEGER "
				+ " AFTER "+BoxDynTable.BOX_USER_STATE+" ;";
		return sqlStr;
	}
//	public static String updateBoxUserStatusStr()
//	{
//		String sqlStr = "ALTER TABLE " + BoxDynTable.TABLE_NAME + " ADD "
//				+ BoxDynTable.BOX_USER_STATE + " INTEGER "
//				+ " AFTER "+BoxDynTable.BOX_SIZE+" ;";
//		return sqlStr;
//	}
}
