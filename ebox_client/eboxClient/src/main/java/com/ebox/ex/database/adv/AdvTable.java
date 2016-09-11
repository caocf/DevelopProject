package com.ebox.ex.database.adv;

import android.provider.BaseColumns;

public class AdvTable implements BaseColumns
{
	private AdvTable()
	{

	}
	public static final String TABLE_NAME = "AdvTable";
	public static final String ADVERID = "AdverId";
	public static final String CONTENT = "Content";
	public static final String AUDIO_CONTENT = "AudioContent";
	public static final String CONTENT_TYPE = "ContentType";
	public static final String TYPE = "Type";
	public static final String STATE = "State"; // 0:创建 1：已下载
	
	public static final Integer STATE_0 = 0; // 0:创建
	public static final Integer STATE_1 = 1; // 1：已下载
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE " + AdvTable.TABLE_NAME + " ("
				+ AdvTable._ID + " INTEGER," 
				+ AdvTable.ADVERID+ " LONG PRIMARY KEY," 
				+ AdvTable.CONTENT + " TEXT,"
				+ AdvTable.CONTENT_TYPE + " INTEGER,"
				+ AdvTable.TYPE + " INTEGER," 
				+ AdvTable.STATE + " INTEGER,"
                + AdvTable.AUDIO_CONTENT + " TEXT"+ ");";
		return sqlStr;
	}

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}
	
	public static String addAudioContent()
	{
		String sqlStr = "ALTER TABLE " + AdvTable.TABLE_NAME + " ADD "
				+ AdvTable.AUDIO_CONTENT + " TEXT "
				+ " AFTER "+AdvTable.STATE+" ;";
		return sqlStr;
	}
}