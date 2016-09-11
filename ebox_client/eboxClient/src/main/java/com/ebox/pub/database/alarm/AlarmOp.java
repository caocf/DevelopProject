package com.ebox.pub.database.alarm;

import android.content.ContentValues;
import android.database.Cursor;

import com.ebox.AppApplication;

import java.util.ArrayList;

public class AlarmOp {
	public static short addAlarm(Alarm alarm) {
		ContentValues values = new ContentValues();
		values.put(AlarmTable.ALARMCODE, alarm.getAlarmCode());
		values.put(AlarmTable.BOXID, alarm.getBoxId());
		values.put(AlarmTable.CONTENT, alarm.getContent());
		values.put(AlarmTable.ALARMTYPE, alarm.getAlarm_type());
		values.put(AlarmTable.ALARMSTATE, alarm.getAlarm_type());
		Cursor cursor = null;
		try {
			AppApplication.getInstance().getPubDs().getDb().insert(AlarmTable.TABLE_NAME, AlarmTable._ID, values);
			return 1;
		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private static Alarm getAlarmByCursor(Cursor cursor) {
		if (cursor != null) {
			Alarm alarm = null;
			try{
				int indexid = cursor.getColumnIndex(AlarmTable._ID);
				int indexBoxId = cursor.getColumnIndex(AlarmTable.BOXID);
				int indexalarmCode = cursor.getColumnIndex(AlarmTable.ALARMCODE);
				int indexContent = cursor.getColumnIndex(AlarmTable.CONTENT);
				int indexAlarmType = cursor.getColumnIndex(AlarmTable.ALARMTYPE);
				int indexAlarmState = cursor.getColumnIndex(AlarmTable.ALARMSTATE);

				if (cursor.moveToFirst()) {
					alarm = new Alarm();
					alarm.set_id(cursor.getLong(indexid));
					alarm.setBoxId(cursor.getString(indexBoxId));
					alarm.setAlarmCode(cursor.getInt(indexalarmCode));
					alarm.setContent(cursor.getString(indexContent));
					alarm.setAlarm_type(cursor.getInt(indexAlarmType));
					alarm.setAlarm_type(cursor.getInt(indexAlarmState));
				}
			}catch(Exception e)
			{
			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return alarm;
		}
		return null;
	}

	public static Alarm getAlarm() {
		try {
			Cursor cursor = AppApplication.getInstance().getPubDs().getDb().query(AlarmTable.TABLE_NAME, null, null, null,
					null, null, AlarmTable._ID);

			return getAlarmByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	private static ArrayList<Alarm> getAllAlarmByCursor(Cursor cursor) {
		if (cursor != null) {
			ArrayList<Alarm> alarm  = null;
			try{

				int indexid = cursor.getColumnIndex(AlarmTable._ID);
				int indexBoxId = cursor.getColumnIndex(AlarmTable.BOXID);
				int indexalarmCode = cursor.getColumnIndex(AlarmTable.ALARMCODE);
				int indexContent = cursor.getColumnIndex(AlarmTable.CONTENT);
				int indexAlarmType = cursor.getColumnIndex(AlarmTable.ALARMTYPE);
				int indexAlarmState = cursor.getColumnIndex(AlarmTable.ALARMSTATE);
				alarm = new ArrayList<Alarm>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					Alarm one = new Alarm();
					one.set_id(cursor.getLong(indexid));
					String boxId = cursor.getString(indexBoxId);
					if (boxId==null)
					{
						boxId="don't exist boxId";
					}
					one.setBoxId(boxId);
					one.setAlarmCode(cursor.getInt(indexalarmCode));
					one.setContent(cursor.getString(indexContent));
					one.setAlarm_type(cursor.getInt(indexAlarmType));
					one.setAlarm_type(cursor.getInt(indexAlarmState));
					alarm.add(one);
					cursor.moveToPrevious();
				}
			}
			catch(Exception e)
			{

			}
			finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}
			return alarm;
		}

		return null;
	}

	public static ArrayList<Alarm> getAllAlarm() {
		try {
			Cursor cursor = AppApplication.getInstance().getPubDs().getDb().query(AlarmTable.TABLE_NAME, null, "", null,
					null, null, AlarmTable._ID);

			return getAllAlarmByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static void deleteAlarm(long oid) {
		try {
			AppApplication.getInstance().getPubDs().getDb().delete(AlarmTable.TABLE_NAME, AlarmTable._ID + "=" + oid, null);
		} catch (Exception e) {
		}
	}
	
	public static void clearAlarm(){
		try {
			AppApplication.getInstance().getPubDs().getDb().execSQL("delete from "+AlarmTable.TABLE_NAME);
		} catch (Exception e) {
		}
	}
}
