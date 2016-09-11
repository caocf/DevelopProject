package com.ebox.ex.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ebox.AppApplication;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoTable;
import com.ebox.ex.database.adv.AdvTable;
import com.ebox.ex.database.boxDyn.BoxDynTable;
import com.ebox.ex.database.deliver.DeliverTable;
import com.ebox.ex.database.operator.OperatorTable;

public class ExDataStorer {
	public static final String DATABASE_NAME = "EboxDb";
	public static final int DATABASE_VERSION = 13;

	private DatabaseHelper mOpenHelper;
	private SQLiteDatabase db;

	public synchronized SQLiteDatabase getDb()
	{
		return db;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME + "_1", null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(AdvTable.getCreateStr());
            db.execSQL(BoxDynTable.getCreateStr());
            db.execSQL(DeliverTable.getCreateStr());

            db.execSQL(OperatorTable.getCreateStr());

			V1_4_1(db);

			V1_4_2(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (oldVersion <=10 && newVersion == DATABASE_VERSION)
            {
                V1_4_0(db);
            }
			else if (oldVersion == 11 && newVersion == DATABASE_VERSION)
			{
				V1_4_1(db);
			}
			else if (oldVersion == 12 && newVersion == DATABASE_VERSION)
			{
				V1_4_2(db);
			}


		}

		private void V1_4_2(SQLiteDatabase db){
			//创建新的订单表
			db.execSQL(OrderLocalInfoTable.getCreateStr());
			//本地Delivery表中增加OrderId 列
			db.execSQL(DeliverTable.addOrderIdClum());
			//本地Delivery表中增加Time列
			db.execSQL(DeliverTable.addTimeClum());
		}

		private void V1_4_1(SQLiteDatabase db) {
            //box表格中添加rackType 字段
			db.execSQL(BoxDynTable.addRackTypeStr());
		}

		private void V1_4_0(SQLiteDatabase db) {
			// OperatorTable中增加CardId字段
			db.execSQL(OperatorTable.updateCardId());
		}

		public void onClean(SQLiteDatabase db) {
		//	db.execSQL(OrderTable.getDropStr());
			db.execSQL(DeliverTable.getDropStr());
			db.execSQL(AdvTable.getDropStr());
			db.execSQL(BoxDynTable.getDropStr());
			db.execSQL(OperatorTable.getDropStr());
			//db.execSQL(ItemDeliveryTable.getDropStr());
		}
	}

	public ExDataStorer() {
		if (mOpenHelper != null)
			mOpenHelper.close();
		mOpenHelper = new DatabaseHelper(AppApplication.globalContext);
		db = mOpenHelper.getWritableDatabase();
	}

	public void finalize() throws Throwable {
		mOpenHelper.close();
	}

	public void cleanDB() {
		mOpenHelper.onClean(db);
	}

	public short logout() {
		try {
			if (db != null)
				db.close();
			if (mOpenHelper != null)
				mOpenHelper.close();
		} catch (Exception e) {

		}
		return 1;
	}
}