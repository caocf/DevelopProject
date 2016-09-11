package com.moge.gege.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.moge.gege.util.LogUtil;

public final class DataBase extends SQLiteOpenHelper
{
    private static final String TAG = "DataBase";

    private static DataBase instance;

    private static SQLiteDatabase mDb = null;

    private DataBase(Context context)
    {
        super(context, DBConstants.NAME, null, DBConstants.VERSION);
    }

    public static SQLiteDatabase getSQLiteDatabase(Context paramContext)
    {
        if (mDb == null)
        {
            if (instance == null)
            {
                instance = new DataBase(paramContext);
            }
            mDb = instance.getWritableDatabase();
        }
        return mDb;
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
        try
        {
            super.finalize();
        }
        catch (Throwable e)
        {
            LogUtil.logException(e);
        }
    }

    private void createShoppingCartTable(SQLiteDatabase db)
    {
        db.beginTransaction();

        db.execSQL(new StringBuilder(128).append("create table if not exists ")
                .append(DBConstants.Table_Names.SHOPCART).append("(")
                .append(DBConstants.SHOPCART.ID).append(" text,")
                .append(DBConstants.SHOPCART.ATTACHMENTS).append(" text,")
                .append(DBConstants.SHOPCART.DISCOUNT_PRICE)
                .append(" integer,").append(DBConstants.SHOPCART.NUM)
                .append(" integer,").append(DBConstants.SHOPCART.TITLE)
                .append(" text,").append(DBConstants.SHOPCART.PRICE)
                .append(" integer,").append(DBConstants.SHOPCART.IS_SELECTED)
                .append(" interger,").append(DBConstants.SHOPCART.BUY_NUM)
                .append(" interger,")
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append(" text,")
                .append(DBConstants.SHOPCART.MERCHANT_ID).append(" text,")
                .append(DBConstants.SHOPCART.PROMOTION_ID)
                .append(" text, primary key(_id,usercount_id))")
                .toString());

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void createPushMsgTable(SQLiteDatabase db)
    {
        db.beginTransaction();

        db.execSQL(new StringBuilder(128).append("create table if not exists ")
                .append(DBConstants.Table_Names.PUSHMSG).append("(")
                .append(DBConstants.PushMsg.UID).append(" text ,")
                .append(DBConstants.PushMsg.TAG).append(" text,")
                .append(DBConstants.PushMsg.MSGTYPE).append(" integer,")
                .append(DBConstants.PushMsg.HAVEREAD).append(" integer,")
                .append(DBConstants.PushMsg.AVATAR).append(" text,")
                .append(DBConstants.PushMsg.COUNT).append(" integer,")
                .append(DBConstants.PushMsg.TITLE).append(" text,")
                .append(DBConstants.PushMsg.CONTENT).append(" text,")
                .append(DBConstants.PushMsg.TIME).append(" integer,")
                .append(DBConstants.PushMsg.IS_NOTIFY)
                .append(" integer, primary key(uid,tag))").toString());

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createShoppingCartTable(db);
        createPushMsgTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        LogUtil.d(TAG, "database udpate. oldVersion : " + oldVersion
                + ", newVersion : " + newVersion);

        String columns = getColumnNames(db, DBConstants.Table_Names.SHOPCART);
        if (TextUtils.isEmpty(columns))
        {
            return;
        }

        updateTable(db, DBConstants.Table_Names.SHOPCART, columns);
    }

    private void updateTable(SQLiteDatabase db, String tableName,
            String columns)
    {
        try
        {
            db.beginTransaction();

            // rename the table
            String tempTable = tableName + "_temp";
            db.execSQL("alter table " + tableName + " rename to " + tempTable);

            // drop the oldtable
            db.execSQL("DROP TABLE IF EXISTS " + tableName);

            // creat table
            createShoppingCartTable(db);

            // load data
            db.equals("insert into " + tableName + " (" + columns + ") "
                    + "select " + columns + " from "
                    + tempTable);

            // drop temp table
            db.execSQL("DROP TABLE IF EXISTS " + tempTable);

            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            LogUtil.i(e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
    }

    private String getColumnNames(SQLiteDatabase db, String tableName)
    {
        StringBuffer sb = null;
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (null != cursor)
            {
                int columnIndex = cursor.getColumnIndex("name");
                if (-1 == columnIndex)
                {
                    return null;
                }

                int index = 0;
                sb = new StringBuffer(cursor.getCount());
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext())
                {
                    sb.append(cursor.getString(columnIndex));
                    sb.append(",");
                    index++;
                }

                if (sb.length() > 0)
                {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }

        return sb != null ? sb.toString() : "";
    }
}
