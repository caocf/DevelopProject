package com.ebox.ex.database.deliver;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

public class DeliverOp {



	public static short CreateDeliver(Deliver deliver) {
		ContentValues values = new ContentValues();
		values.put(DeliverTable.ITEM_ID, deliver.getItemId());
		values.put(DeliverTable.BOX_ID, deliver.getBox_code());
		values.put(DeliverTable.OPERATOR_ID, deliver.getOperatorId());
		values.put(DeliverTable.TELEPHONE, deliver.getTelephone());
		values.put(DeliverTable.STATE, deliver.getState());
		values.put(DeliverTable.FEE, deliver.getFee());
		values.put(DeliverTable.ORDER_ID,deliver.getOrder_id());
		values.put(DeliverTable.TIME,deliver.getTime());
		Cursor cursor = null;
		try {
			AppApplication.getInstance().getExDs().getDb().insert(DeliverTable.TABLE_NAME, DeliverTable._ID, values);
			return 1;
		} catch (Exception e) {
			Log.i(GlobalField.tag,"create local delivery error " +e.getMessage());
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private static Deliver getDeliverByCursor(Cursor cursor) {
		if (cursor != null) {
			Deliver deliver = null;
			try{
				int indexId = cursor.getColumnIndex(DeliverTable._ID);
				int indexItemId = cursor.getColumnIndex(DeliverTable.ITEM_ID);
				int indexBoxId = cursor.getColumnIndex(DeliverTable.BOX_ID);
				int indexOperatorId = cursor
						.getColumnIndex(DeliverTable.OPERATOR_ID);
				int indexTelephone = cursor.getColumnIndex(DeliverTable.TELEPHONE);
				int indexState = cursor.getColumnIndex(DeliverTable.STATE);
				int indexFee = cursor.getColumnIndex(DeliverTable.FEE);
				int index_order_id=cursor.getColumnIndex(DeliverTable.ORDER_ID);
				int index_image=cursor.getColumnIndex(DeliverTable.TIME);
				if (cursor.moveToFirst()) {
					deliver = new Deliver();
					deliver.set_id(cursor.getLong(indexId));
					deliver.setBox_code(cursor.getString(indexBoxId));
					deliver.setItemId(cursor.getString(indexItemId));
					deliver.setOperatorId(cursor.getString(indexOperatorId));
					deliver.setTelephone(cursor.getString(indexTelephone));
					deliver.setState(cursor.getInt(indexState));
					deliver.setFee(cursor.getInt(indexFee));
					deliver.setTime(cursor.getString(index_image));
					deliver.setOrder_id(cursor.getString(index_order_id));
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

	public static Deliver getDeliverByBoxId(String boxId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(DeliverTable.TABLE_NAME, null,
					DeliverTable.BOX_ID + " = '" + boxId + "'", null, null,
					null, DeliverTable._ID);

			return getDeliverByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static Deliver getDeliverByOrderId(String orderId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(DeliverTable.TABLE_NAME, null,
					DeliverTable.ORDER_ID + " = '" + orderId + "'", null, null,
					null, DeliverTable._ID);

			return getDeliverByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	private static ArrayList<Deliver> getAllDeliverByCursor(Cursor cursor) {
		if (cursor != null) {
			ArrayList<Deliver> deliver = null;
			try{
				int indexId = cursor.getColumnIndex(DeliverTable._ID);
				int indexItemId = cursor.getColumnIndex(DeliverTable.ITEM_ID);
				int indexBoxId = cursor.getColumnIndex(DeliverTable.BOX_ID);
				int indexOperatorId = cursor
						.getColumnIndex(DeliverTable.OPERATOR_ID);
				int indexTelephone = cursor.getColumnIndex(DeliverTable.TELEPHONE);
				int indexState = cursor.getColumnIndex(DeliverTable.STATE);
				int indexFee = cursor.getColumnIndex(DeliverTable.FEE);
				int index_order_id=cursor.getColumnIndex(DeliverTable.ORDER_ID);
				int index_image=cursor.getColumnIndex(DeliverTable.TIME);
				deliver = new ArrayList<Deliver>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					Deliver one = new Deliver();
					one.set_id(cursor.getLong(indexId));
					one.setBox_code(cursor.getString(indexBoxId));
					one.setItemId(cursor.getString(indexItemId));
					one.setOperatorId(cursor.getString(indexOperatorId));
					one.setTelephone(cursor.getString(indexTelephone));
					one.setState(cursor.getInt(indexState));
					one.setFee(cursor.getInt(indexFee));
					one.setTime(cursor.getString(index_image));
					one.setOrder_id(cursor.getString(index_order_id));

					deliver.add(one);
					cursor.moveToPrevious();
				}
			}catch(Exception e)
			{

			}
			finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return deliver;
		}
		return null;
	}

	public static ArrayList<Deliver> getAllDeliver() {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(DeliverTable.TABLE_NAME, null, null, null,
					null, null, DeliverTable._ID);

			return getAllDeliverByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ArrayList<Deliver> getAllDeliverByOperator(String operatorID) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(DeliverTable.TABLE_NAME, null, 
					DeliverTable.OPERATOR_ID + " = '" + operatorID + "'", null,
					null, null, DeliverTable._ID);

			return getAllDeliverByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static short deleteDeliver(String boxCode) {
		try {
			AppApplication.getInstance().getExDs().getDb().delete(DeliverTable.TABLE_NAME, DeliverTable.BOX_ID + "='" + boxCode+"'",
					null);
			LogUtil.i("delete local deliver success " + boxCode);
			return 1;
		} catch (Exception e) {
			LogUtil.i("delete local deliver error "+boxCode);
			return -1;
		}
	}

	public static void updateDeliver(Deliver deliver) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + DeliverTable.TABLE_NAME + " set "
					+ DeliverTable.STATE + " =" + deliver.getState() + " where "
					+ DeliverTable._ID + " = " + deliver.get_id());
		} catch (Exception e) {
		}
	}

	public static ArrayList<Deliver> getDeliverByState(Integer state) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(DeliverTable.TABLE_NAME, null,
					DeliverTable.STATE + " = " + state, null, null, null,
					DeliverTable._ID);

			return getAllDeliverByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}
}
