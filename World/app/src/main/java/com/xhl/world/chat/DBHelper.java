package com.xhl.world.chat;

import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Sum on 16/2/29.
 */
public class DBHelper {

    private static final String DB_NAME = "push.db";
    private DbManager.DaoConfig config;

    private static DBHelper helper;

    private DBHelper() {

        config = new DbManager.DaoConfig().
                setDbName(DB_NAME).
                setDbVersion(1).
                setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        Logger.e("on db open");
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                Logger.e("update db old:" + oldVersion + " new:" + newVersion);
            }
        });
    }

    public static DBHelper getInstance() {
        if (helper == null) {
            helper = new DBHelper();
        }
        return helper;
    }

    public void addOneMessage(PushMessage msg) {
        try {
            if (msg == null) {
                return;
            }
            DbManager db = x.getDb(config);
            db.save(msg);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void removeOneMessage(PushMessage msg) {
        try {
            if (msg == null) {
                return;
            }
            DbManager db = x.getDb(config);
            db.delete(msg);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<PushMessage> queryAllMessage() {
        DbManager db = x.getDb(config);
        try {
            //倒序查询全部
            return db.selector(PushMessage.class).orderBy("id",true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

}
