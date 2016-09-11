package com.ebox.ex.database.operator;

import android.provider.BaseColumns;

public class OperatorTable implements BaseColumns
{
	private OperatorTable()
	{

	}
	public static final String TABLE_NAME = "OperatorTable";
	public static final String OPERATOR_ID = "OperatorId";
    public static final String OPERATOR_CARD = "CardId";
	public static final String OPERATOR_PASSWORD = "Password";
	public static final String OPERATOR_NAME = "OperatorName";
	public static final String OPERATOR_STATUS = "OperatorStatus";
	public static final String RESERVE_STATUS = "ReserveStatus";
	public static final String TELEPHONE = "Telephone";
	public static final String BALANCE = "Balance";
	public static final String STATE = "State"; // 0:待同步 1：已同步  2：同步失败，待告警
	
	/**
	 * 待同步 
	 */
	public static final Integer STATE_0 = 0; 
	/**
	 *已经同步
	 */
	public static final Integer STATE_1 = 1;  
	/**
	 * 同步失败，待告警
	 */
	public static final Integer STATE_2 = 2;
	
	public static String getCreateStr()
	{

		String sqlStr = "CREATE TABLE IF NOT EXISTS " + OperatorTable.TABLE_NAME + " ("
				+ OperatorTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ OperatorTable.OPERATOR_NAME + " TEXT,"
				+ OperatorTable.OPERATOR_ID + " TEXT,"
				+ OperatorTable.OPERATOR_PASSWORD + " TEXT,"
				+ OperatorTable.OPERATOR_STATUS + " INTEGER,"
				+ OperatorTable.RESERVE_STATUS + " INTEGER,"
				+ OperatorTable.TELEPHONE + " TEXT, "
				+ OperatorTable.BALANCE + " INTEGER,"
				+ OperatorTable.STATE + " INTEGER,"
                + OperatorTable.OPERATOR_CARD + " TEXT"
				+ ");";
		return sqlStr;
	}

    /**
     * 向订单表中增加CardId字段
     *
     * @return
     */
    public static String updateCardId() {
        String sqlStr = "ALTER TABLE " + OperatorTable.TABLE_NAME + " ADD "
                + OperatorTable.OPERATOR_CARD + " TEXT ;";
        return sqlStr;
    }

	public static String getDropStr()
	{
		String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
		return sqlStr;
	}

}
