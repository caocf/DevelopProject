package com.xhl.bqlh.business.Db;

import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.DbManager;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Sum on 16/4/18.
 */
public class DbTaskHelper {

    private final DbManager mDb;

    private final Executor trimExecutor = new PriorityExecutor(1, true);

    private static final int LIMIT_COUNT = 5000; // 限制最多5000条数据

    private long lastTrimTime = 0L;

    private static final long TRIM_TIME_SPAN = 1000;

    private static final String DB_NAME = "task.db";

    private static DbTaskHelper helper;

    private DbTaskHelper() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().
                setDbName(DB_NAME).
                setDbVersion(1).
                setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        Logger.v("on db open");
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                Logger.v("update db old:" + oldVersion + " new:" + newVersion);
            }
        });

        mDb = x.getDb(daoConfig);
    }

    public static DbTaskHelper getInstance() {
        if (helper == null) {
            helper = new DbTaskHelper();
        }
        return helper;
    }

    /**
     * 先清理后保存全部新的店铺数据
     */
    public void addTaskShop(List<TaskShop> shops) {
        //先清理全部的店铺信息
        clearAllTaskShop();
        try {
            mDb.save(shops);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void addTaskRecord(List<TaskRecord> shops) {
        //先清理全部信息
        clearAllTaskRecord();
        try {
            mDb.save(shops);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加会员信息
     */
    public void addMember(List<Member> members) {
        //先清理全部信息
        clearAllMember();
        try {
            mDb.save(members);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取非输入天数的其他天的店铺信息
     *
     * @param day 输入的天数
     * @return
     */
    public List<TaskShop> getTaskShopNotInDay(int day) {
        try {
//            return mDb.selector(TaskShop.class).where("week", "!=", day).orderBy("index").findAll();
            return mDb.selector(TaskShop.class).orderBy("index").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取输入天数的店铺信息
     *
     * @param day 输入的天数
     * @return
     */
    public List<TaskShop> getTaskShopByDay(int day) {
        try {
            return mDb.selector(TaskShop.class).where("week", "=", day).orderBy("index").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaskShop> getAllTaskShop() {
        try {
            return mDb.selector(TaskShop.class).orderBy("index").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaskRecord> getAllRecordTask() {
        try {
            return mDb.selector(TaskRecord.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Member> getAllMember() {
        try {
            return mDb.selector(Member.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存一个会员，如果存在则更新状态（状态为3的时候触发）
     * 不存在添加
     *
     * @param member 会员信息
     */
    public void saveOneMember(Member member) {
        try {
            Member first = mDb.selector(Member.class).where("retailerId", "=", member.getRetailerId()).findFirst();
            if (first != null) {
                first.setState(member.getState());
                mDb.saveOrUpdate(first);
                Logger.v("update member :" + member.getState());
            } else {
                mDb.save(member);
                Logger.v("add member :" + member.getState());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个临时任务
     * 保证同一个店铺当天只能添加一次
     *
     * @param shopId
     * @return 0:添加成功 1:任务已经开始 2：任务已经完成
     */
    public Date addTemporaryTask(String shopId) {
//        TaskRecord exitRecord = getTodayRecordByType(shopId);
        TaskRecord record = new TaskRecord();
        record.setShopId(shopId);
        Date date = new Date();
        record.setTaskTime(date);
        record.setTaskType(TaskType.TYPE_Temporary);
        record.setTaskState(TaskType.STATE_START);
        saveOneTask(record);
        return date;
    }

    public int getTemporaryTaskId(Date date) {
        try {
            TaskRecord first = mDb.selector(TaskRecord.class).where("taskTime", "=", date).findFirst();
            if (first != null) {
                return first.getId();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 保存一个临时任务完成
     *
     * @param shopId
     */
    public void saveTemporaryTask(String shopId) {
        TaskRecord record = getTodayRecordByType(shopId);
        //存在的临时任务
        if (record != null) {
            record.setTaskTime(new Date());
            record.setTaskState(TaskType.STATE_FINISH);
            record.setTaskType(TaskType.TYPE_Temporary);
            try {
                mDb.saveOrUpdate(record);
                Logger.v("saveTemporaryTask record:" + shopId);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteTaskRecord(int taskId) {
        try {
            mDb.deleteById(TaskRecord.class, taskId);
            Logger.v("delete record:" + taskId);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存一个正常任务的完成
     *
     * @param shopId
     */
    public void saveNormalTask(String shopId) {
        TaskRecord record = new TaskRecord();
        record.setShopId(shopId);
        record.setTaskTime(new Date());
        record.setTaskType(TaskType.TYPE_Normal);
        record.setTaskState(TaskType.STATE_FINISH);
        saveOneTask(record);
    }

    private void saveOneTask(TaskRecord record) {
        try {
            mDb.save(record);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当天的固定门店的临时任务
     */
    private TaskRecord getTodayRecordByType(String shopId) {
        //凌晨开始
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        Date start = instance.getTime();
        //当前时间点结束
        Date end = new Date();
        try {
            return mDb.selector(TaskRecord.class)
                    .where("taskTime", "between", new Date[]{start, end})
                    .and("taskType", "=", TaskType.TYPE_Temporary)
                    .and("shopId", "=", shopId)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定时间内的任务记录
     */
    private List<TaskRecord> getRecordBetweenTime(Date start, Date end) {
        try {
            return mDb.selector(TaskRecord.class).where("taskTime", "between", new Date[]{start, end}).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取结束日期当天完成的任务数量
     */
    public long getFinishTaskByEndTime(Date end) {
        //结束日期的凌晨开始
        Calendar instance = Calendar.getInstance();
        instance.setTime(end);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        Date start = instance.getTime();
        try {
            return mDb.selector(TaskRecord.class)
                    .where("taskTime", "between", new Date[]{start, end})
                    .and("taskState", "=", TaskType.STATE_FINISH)
                    .count();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从结束日期一天的普通任务记录
     */
    public List<TaskRecord> getRecordByEndTime(Date end) {
        //结束日期的凌晨开始
        Calendar instance = Calendar.getInstance();
        instance.setTime(end);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        Date start = instance.getTime();
        return getRecordBetweenTime(start, end);
    }

    /**
     * 获取当天的临时任务记录
     */
    public List<TaskRecord> getTemporaryByEndTime() {
        return getTemporaryByEndTime(new Date());
    }

    /**
     * 从结束日期一天的临时任务记录
     */
    public List<TaskRecord> getTemporaryByEndTime(Date end) {
        //结束日期的凌晨开始
        Calendar instance = Calendar.getInstance();
        instance.setTime(end);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        Date start = instance.getTime();
        try {
            //1461081600523 1461143694523
            List<TaskRecord> records = mDb.selector(TaskRecord.class)
                    .where("taskTime", "between", new Date[]{start, end})
                    .and("taskType", "=", TaskType.TYPE_Temporary)
                    .orderBy("taskState", false)
                    .findAll();
            if (records != null) {
                //设置临时任务是店铺数据
                for (TaskRecord record : records) {
                    record.setTaskShop(record.getShop(mDb));
                }
            }
            return records;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 清理全部任务记录
     */
    private void clearAllTaskRecord() {
        try {
            mDb.delete(TaskRecord.class);
            Logger.v("clear task table");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理全部店铺数据
     */
    private void clearAllTaskShop() {
        try {
            mDb.delete(TaskShop.class);
            Logger.v("clear shop table");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理全部会员数据
     */
    private void clearAllMember() {
        try {
            mDb.delete(Member.class);
            Logger.v("clear member table");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void trimSize() {
        trimExecutor.execute(new Runnable() {
            @Override
            public void run() {

                long current = System.currentTimeMillis();
                if (current - lastTrimTime < TRIM_TIME_SPAN) {
                    return;
                } else {
                    lastTrimTime = current;
                }
                // trim by limit count
                try {
                    int count = (int) mDb.selector(TaskRecord.class).count();
                    if (count > LIMIT_COUNT + 10) {
                        List<TaskRecord> rmList = mDb.selector(TaskRecord.class)
                                .orderBy("id", false)
                                .limit(count - LIMIT_COUNT).findAll();
                        if (rmList != null) {
                            mDb.delete(rmList);
                        }
                    }
                } catch (Throwable ex) {
                    LogUtil.e(ex.getMessage(), ex);
                }
            }
        });
    }


}
