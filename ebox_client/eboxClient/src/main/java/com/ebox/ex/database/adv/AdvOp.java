package com.ebox.ex.database.adv;

import android.content.ContentValues;
import android.database.Cursor;

import com.ebox.AppApplication;

import java.util.ArrayList;

public class AdvOp {
	private static AdvertiseData getAdvertiseByCursor(Cursor cursor) {
		if (cursor != null) {
			AdvertiseData deliver = null;
			try{
			int indexId = cursor.getColumnIndex(AdvTable.ADVERID);
			int indexContent = cursor.getColumnIndex(AdvTable.CONTENT);
			int indexContentType = cursor.getColumnIndex(AdvTable.CONTENT_TYPE);
			int indexType = cursor.getColumnIndex(AdvTable.TYPE);
			int indexState = cursor.getColumnIndex(AdvTable.STATE);
			int indexAudioContent = cursor.getColumnIndex(AdvTable.AUDIO_CONTENT);
			
			if (cursor.moveToFirst()) {
				deliver = new AdvertiseData();
				deliver.setAdver_id(cursor.getInt(indexId));
				deliver.setContent(cursor.getString(indexContent));
				deliver.setContent_type(cursor.getInt(indexContentType));
				deliver.setType(cursor.getInt(indexType));
				deliver.setState(cursor.getInt(indexState));
				deliver.setAudio_content(cursor.getString(indexAudioContent));
			}
			}catch(Exception e)
			{
			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}
			return deliver;
		}
		return null;
	}

	public static AdvertiseData getAdvertiseByAdvId(Integer advId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(AdvTable.TABLE_NAME, null,
					AdvTable.ADVERID + " = '" + advId + "'", null, null, null,
					AdvTable.ADVERID);

			return getAdvertiseByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static short addAdvertise(AdvertiseData adv) {
		if (getAdvertiseByAdvId(adv.getAdver_id()) != null) {
			return 1;
		}

		ContentValues values = new ContentValues();
		values.put(AdvTable.ADVERID, adv.getAdver_id());
		values.put(AdvTable.CONTENT, adv.getContent());
		values.put(AdvTable.CONTENT_TYPE, adv.getContent_type());
		values.put(AdvTable.TYPE, adv.getType());
		values.put(AdvTable.STATE, adv.getState());
		values.put(AdvTable.AUDIO_CONTENT, adv.getAudio_content());
		Cursor cursor = null;
		try {

			AppApplication.getInstance().getExDs().getDb().insert(AdvTable.TABLE_NAME, AdvTable.ADVERID, values);
			return 1;
		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private static ArrayList<AdvertiseData> getAllAdvertiseByCursor(Cursor cursor) {
		if (cursor != null) {
			ArrayList<AdvertiseData> deliver = null;
			try{
				int indexId = cursor.getColumnIndex(AdvTable.ADVERID);
				int indexContent = cursor.getColumnIndex(AdvTable.CONTENT);
				int indexContentType = cursor.getColumnIndex(AdvTable.CONTENT_TYPE);
				int indexType = cursor.getColumnIndex(AdvTable.TYPE);
				int indexState = cursor.getColumnIndex(AdvTable.STATE);
				int indexAudioContent = cursor.getColumnIndex(AdvTable.AUDIO_CONTENT);
				deliver = new ArrayList<AdvertiseData>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					AdvertiseData one = new AdvertiseData();
					one.setAdver_id(cursor.getInt(indexId));
					one.setContent(cursor.getString(indexContent));
					one.setContent_type(cursor.getInt(indexContentType));
					one.setType(cursor.getInt(indexType));
					one.setState(cursor.getInt(indexState));
					one.setAudio_content(cursor.getString(indexAudioContent));
					deliver.add(one);
					cursor.moveToPrevious();
				}
			}catch(Exception e){

			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return deliver;
		}
		return null;
	}

	public static ArrayList<AdvertiseData> getAllAdvertise() {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(AdvTable.TABLE_NAME, null, "", null, null,
					null, AdvTable.ADVERID);

			return getAllAdvertiseByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static void deleteAdvertise(AdvertiseData adv) {
		try {
			AppApplication.getInstance().getExDs().getDb().delete(AdvTable.TABLE_NAME,
					AdvTable.ADVERID + "=" + adv.getAdver_id(), null);
		} catch (Exception e) {
		}
	}

	public static ArrayList<AdvertiseData> getAdvertise(int state) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(AdvTable.TABLE_NAME, null, AdvTable.STATE
					+ " = '" + state + "'", null, null, null, AdvTable.ADVERID
					+ " desc");

			return getAllAdvertiseByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据广告类型获取对应的广告
	 * 
	 * @param type
	 * 			0,屏保广告 3，取件广告 4，取件成功广告
	 * @return
	 */
	public static ArrayList<AdvertiseData> getAdvertiseByType(int type) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(AdvTable.TABLE_NAME, null, AdvTable.TYPE
					+ " = '" + type + "'"+" and "+ AdvTable.STATE
					+ " = '" + AdvTable.STATE_1 + "'", null, null, null, AdvTable.ADVERID
					+ " desc");

			return getAllAdvertiseByCursor(cursor);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void updateAdvertise(String content) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + AdvTable.TABLE_NAME + " set "
					+ AdvTable.STATE + "=" + AdvTable.STATE_1 + " where "
					+ AdvTable.CONTENT + "='" + content + "'");
		} catch (Exception e) {
		}
	}
}
