package com.ebox.ums.database.payRst;

import android.provider.BaseColumns;

public final class PayRstTable implements BaseColumns
{
	private PayRstTable()
	{

	}
	public static final String TABLE_NAME = "LocalPayRst";
	public static final String PAYID = "PayId";
	public static final String PAYTYPE = "PayType";
	public static final String MOBILE = "mobile";
	public static final String RSTCODE = "rstCode"; 
	public static final String AMOUNT = "amount";
	public static final String RESULT = "result";
	public static final String APPENDFIELD = "appendField";
	public static final String AUTHNO = "authNo";
	public static final String BANKCODE = "bankCode";
	public static final String BATCH = "batch";
	public static final String CATDNO = "cardNo";
	public static final String EXP = "exp";
	public static final String MCHTID = "mchtId"; 
	public static final String PRINTINFO = "printInfo";
	public static final String REFERENCE = "reference";
	public static final String RSPCHIN = "rspchin";
	public static final String SETTLEDATE = "settleDate";
	public static final String TERMID = "termId";
	public static final String TRACE = "trace";
	public static final String TRANSDATE = "transDate";
	public static final String TRANSTIME = "transTime"; 
	public static final String ERRORMSG = "error_msg";
	public static final String NONCESTR = "noncestr";
	public static final String TIMESTAMP = "timestamp";
	public static final String STATE = "state";
	public static final int STATE_0 = 0;//未同步
	public static final int STATE_1 = 1;//已同步
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE " + PayRstTable.TABLE_NAME + " ("
				+ PayRstTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ PayRstTable.PAYID+ " TEXT," 
				+ PayRstTable.PAYTYPE + " TEXT,"
				+ PayRstTable.MOBILE + " TEXT,"
				+ PayRstTable.RSTCODE + " TEXT," 
				+ PayRstTable.AMOUNT+ " TEXT," 
				+ PayRstTable.APPENDFIELD + " TEXT,"
				+ PayRstTable.RESULT + " TEXT,"
				+ PayRstTable.AUTHNO + " TEXT,"
				+ PayRstTable.BANKCODE + " TEXT," 
				+ PayRstTable.BATCH+ " TEXT," 
				+ PayRstTable.CATDNO + " TEXT,"
				+ PayRstTable.EXP + " TEXT,"
				+ PayRstTable.MCHTID + " TEXT," 
				+ PayRstTable.PRINTINFO+ " TEXT," 
				+ PayRstTable.REFERENCE + " TEXT,"
				+ PayRstTable.RSPCHIN + " TEXT,"
				+ PayRstTable.SETTLEDATE + " TEXT," 
				+ PayRstTable.TERMID+ " TEXT," 
				+ PayRstTable.TRACE + " TEXT,"
				+ PayRstTable.TRANSDATE + " TEXT,"
				+ PayRstTable.ERRORMSG + " TEXT,"
				+ PayRstTable.NONCESTR + " TEXT,"
				+ PayRstTable.TIMESTAMP + " TEXT,"
				+ PayRstTable.STATE + " INTEGER,"
				+ PayRstTable.TRANSTIME + " TEXT"
				+ ");";
		return sqlStr;
	}
	
	public static String updatePayRstStr()
	{
		String sqlStr = "ALTER TABLE " + PayRstTable.TABLE_NAME + " ADD "
				+ PayRstTable.ERRORMSG + " TEXT "
				+ " AFTER "+PayRstTable.TRANSDATE+" ;";
		return sqlStr;
	}
	
	public static String addNoncestrStr()
	{
		String sqlStr = "ALTER TABLE " + PayRstTable.TABLE_NAME + " ADD "
				+ PayRstTable.NONCESTR + " TEXT "
				+ " AFTER "+PayRstTable.ERRORMSG+" ;";
		return sqlStr;
	}
	
	public static String addTimestampStr()
	{
		String sqlStr = "ALTER TABLE " + PayRstTable.TABLE_NAME + " ADD "
				+ PayRstTable.TIMESTAMP + " TEXT "
				+ " AFTER "+PayRstTable.NONCESTR+" ;";
		return sqlStr;
	}
	
	public static String addStateStr()
	{
		String sqlStr = "ALTER TABLE " + PayRstTable.TABLE_NAME + " ADD "
				+ PayRstTable.STATE + " TEXT "
				+ " AFTER "+PayRstTable.TIMESTAMP+" ;";
		return sqlStr;
	}

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}
}
