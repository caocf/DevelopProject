package com.moge.gege.data.dao;

import android.database.sqlite.SQLiteDatabase;

import com.moge.gege.AppApplication;
import com.moge.gege.data.DataBase;

public abstract class BaseDAO
{
    public SQLiteDatabase mDb;

    BaseDAO()
    {
        this.mDb = DataBase.getSQLiteDatabase(AppApplication.instance());
    }
}