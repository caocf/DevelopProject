package com.ebox.pub.database.alarm;

import android.provider.BaseColumns;


public class AlarmTable implements BaseColumns
{
	private AlarmTable()
	{

	}
	public static final String TABLE_NAME = "alarm";
	public static final String BOXID = "boxId";
	public static final String ALARMCODE = "alarmCode";
	public static final String CONTENT = "content";
	public static final String ALARMTYPE = "type";
	public static final String ALARMSTATE = "state";
	
	
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE " + AlarmTable.TABLE_NAME + " ("
				+ AlarmTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ AlarmTable.BOXID + " TEXT,"
				+ AlarmTable.CONTENT + " TEXT,"
				+ AlarmTable.ALARMTYPE + " INTEGER,"
				+ AlarmTable.ALARMSTATE + " INTEGER,"
				+ AlarmTable.ALARMCODE + " INTEGER)";
		return sqlStr;
	}
	
	public static String dropCardnoStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " DROP cardno;";
				
		return sqlStr;
	}
	public static String dropAddDateStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " DROP add_date;";
				
		return sqlStr;
	}
	
	public static String AddColumnBoxIdStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " ADD " 
		             + AlarmTable.BOXID + " TEXT "
				+ " AFTER "+AlarmTable._ID+" ;";
				
		return sqlStr;
	}
	public static String AddColumnAlarmCodeStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " ADD " 
		             + AlarmTable.ALARMCODE + " TEXT "
				+ " AFTER "+AlarmTable.BOXID+" ;";
				
		return sqlStr;
	}
	
	public static String AddColumnAlarmTypeCodeStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " ADD " 
		             + AlarmTable.ALARMTYPE + " TEXT "
				+ " AFTER "+AlarmTable.ALARMCODE+" ;";
				
		return sqlStr;
	}
	
	
	public static String AddColumnContentStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " ADD " 
		             + AlarmTable.CONTENT + " TEXT "
				+ " AFTER "+AlarmTable.ALARMTYPE+" ;";
				
		return sqlStr;
	}
	
	public static String AddColumnAlarmStateStr()
	{

		String sqlStr = "ALTER TABLE " + AlarmTable.TABLE_NAME  + " ADD " 
		             + AlarmTable.ALARMSTATE + " TEXT "
				+ " AFTER "+AlarmTable.CONTENT+" ;";
				
		return sqlStr;
	}

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}
}
