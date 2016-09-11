package com.ebox.ums.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ebox.AppApplication;
import com.ebox.ums.database.payRst.PayRstTable;

public class UmsDataStorer {
	public static final String DATABASE_NAME = "UmsDb";
	public static final int DATABASE_VERSION = 10;

	private DatabaseHelper mOpenHelper;
	private SQLiteDatabase db;
	
	public SQLiteDatabase getDb()
	{
		return db;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME + "_1", null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(PayRstTable.getCreateStr());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}


		public void onClean(SQLiteDatabase db) {
			db.execSQL(PayRstTable.getDropStr());
		}
	}

	public UmsDataStorer() {
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
