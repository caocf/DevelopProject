package com.ebox.ex.database.deliver;

import android.provider.BaseColumns;

public class DeliverTable implements BaseColumns
{
	private DeliverTable()
	{

	}
	public static final String TABLE_NAME = "Deliver";
	public static final String ITEM_ID = "ItemID";
	public static final String BOX_ID = "BoxID";
	public static final String OPERATOR_ID = "OperatorID";
	public static final String TELEPHONE = "Telephone";
	public static final String STATE = "State"; // 0:待同步 1：同步失败，待告警
	public static final String FEE = "Fee";	// 订单金额

	public static final String ORDER_ID = "Order_id";	//客户端生成唯一的订单id
	public static final String TIME = "image";	//订单生成时间

	/**
	 * 待同步 
	 */
	public static final Integer STATE_0 = 0;  
	/**
	 * 同步失败，待告警
	 */
	public static final Integer STATE_1 = 1;  
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE " + DeliverTable.TABLE_NAME + " ("
				+ DeliverTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ DeliverTable.ITEM_ID + " TEXT,"
				+ DeliverTable.BOX_ID + " TEXT,"
				+ DeliverTable.OPERATOR_ID + " TEXT,"
				+ DeliverTable.TELEPHONE + " TEXT,"
				+ DeliverTable.STATE + " INTEGER,"
				+ DeliverTable.FEE + " INTEGER"
				+ ");";
		return sqlStr;
	}

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}
	
	public static String addOrderIdClum()
	{
		String sqlStr = "ALTER TABLE " + DeliverTable.TABLE_NAME + " ADD "
				+ DeliverTable.ORDER_ID + " TEXT"
				+ " AFTER "+DeliverTable.FEE+" ;";
		return sqlStr;
	}

	public static String addTimeClum()
	{
		String sqlStr = "ALTER TABLE " + DeliverTable.TABLE_NAME + " ADD "
				+ DeliverTable.TIME + " TEXT"
				+ " AFTER "+DeliverTable.ORDER_ID+" ;";
		return sqlStr;
	}
}
