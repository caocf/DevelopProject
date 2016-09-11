package com.ebox.ex.database.operator;

import android.content.ContentValues;
import android.database.Cursor;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.base.type.OperatorType;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

public class OperatorOp {
	/**
	 * 添加一个本地缓存用户
	 *
	 * @param operator
	 * @return
	 */
	public static short addOperator(OperatorInfo operator) {
		ContentValues values = new ContentValues();
		values.put(OperatorTable.OPERATOR_NAME, operator.getOperatorName());
		values.put(OperatorTable.OPERATOR_ID, operator.getOperatorId());
		values.put(OperatorTable.OPERATOR_PASSWORD, operator.getPassword());
		values.put(OperatorTable.OPERATOR_STATUS, operator.getOperatorStatus());
		values.put(OperatorTable.RESERVE_STATUS, operator.getReserveStatus());
		values.put(OperatorTable.TELEPHONE, operator.getTelephone());
		values.put(OperatorTable.BALANCE, operator.getBalance());
		values.put(OperatorTable.STATE, operator.getState());
		values.put(OperatorTable.OPERATOR_CARD, operator.getCardId());
		try {
			OperatorInfo info = getOperatorById(operator.getOperatorId());
			/*if (info!=null)
			{
				deleteOperatorById(operator.getOperatorId());
			}*/

			if (info == null)
			{
				AppApplication.getInstance().getExDs().getDb().insert(OperatorTable.TABLE_NAME, null, values);
			} else {
				AppApplication.getInstance().getExDs().getDb().update(OperatorTable.TABLE_NAME, values, OperatorTable.OPERATOR_ID + "='" + operator.getOperatorId() + "'", null);
			}
//			AppApplication.getInstance().getExDs().getDb().insert(OperatorTable.TABLE_NAME, null, values);
			return 1;
		} catch (Exception e) {
			LogUtil.e("add operator to local table failed," + operator.getOperatorId());
			return -1;
		}
	}

	/**
	 * 用于同步更新本地用户信息
	 *
	 * @param operator
	 * @return
	 */
	public static short updateOperator(OperatorInfo operator) {
		ContentValues values = new ContentValues();
		values.put(OperatorTable.OPERATOR_ID, operator.getOperatorId());
		values.put(OperatorTable.OPERATOR_STATUS, operator.getOperatorStatus());
		values.put(OperatorTable.RESERVE_STATUS, operator.getReserveStatus());
		//	values.put(OperatorTable.TELEPHONE, operator.getTelephone());
		values.put(OperatorTable.BALANCE, operator.getBalance());
		values.put(OperatorTable.STATE, OperatorTable.STATE_1);
		try {
			OperatorInfo info = getOperatorById(operator.getOperatorId());
			if (info == null) {
				AppApplication.getInstance().getExDs().getDb().insert(OperatorTable.TABLE_NAME, null, values);
			} else {
				AppApplication.getInstance().getExDs().getDb().update(OperatorTable.TABLE_NAME, values, OperatorTable.OPERATOR_ID + "='" + operator.getOperatorId() + "'", null);
			}
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 用于同步更新本地用户所有信息
	 *
	 * @param operatorId
	 * @param operator
	 * @return
	 */
	public static void updateOperatorInfo(String operatorId, OperatorType operator) {

		ContentValues values = new ContentValues();
		values.put(OperatorTable.OPERATOR_STATUS, operator.getStatus());
		values.put(OperatorTable.RESERVE_STATUS, operator.getReserve_status());
		values.put(OperatorTable.TELEPHONE, operator.getTelephone());
		values.put(OperatorTable.BALANCE, operator.getBalance());
		values.put(OperatorTable.OPERATOR_NAME, operator.getOperator_name());
		values.put(OperatorTable.OPERATOR_PASSWORD, operator.getPassword());
		values.put(OperatorTable.STATE, OperatorTable.STATE_1);
		try {
			AppApplication.getInstance().getExDs().getDb().update(OperatorTable.TABLE_NAME, values, OperatorTable.OPERATOR_ID + "='" + operatorId + "'", null);
		} catch (Exception e) {
		}
	}

	/**
	 * 根据用户id获取用户信息
	 *
	 * @param operatorId 用户id
	 * @return 用户信息
	 */
	public static OperatorInfo getOperatorById(String operatorId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OperatorTable.TABLE_NAME, null,
					OperatorTable.OPERATOR_ID + " = '" + operatorId + "'",
					null, null, null, null);
			return getOperatorByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据用户id获取用户信息
	 *
	 * @param OperatorCardId 用户id
	 * @return 用户信息
	 */
	public static OperatorInfo getOperatorByCardId(String OperatorCardId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OperatorTable.TABLE_NAME, null,
					OperatorTable.OPERATOR_CARD + " = '" + OperatorCardId + "'",
					null, null, null, null);
			return getOperatorByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer getOperatorStateById(String operatorId) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OperatorTable.TABLE_NAME, null,
					OperatorTable.OPERATOR_ID + " = '" + operatorId + "'",
					null, null, null, null);
			if (cursor != null) {
				try {
					int index = cursor.getColumnIndex(OperatorTable.STATE);
					if (cursor.moveToFirst()) {
						return cursor.getInt(index);
					}
					return OperatorTable.STATE_0;
				} catch (Exception e) {

				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}

			}
			return OperatorTable.STATE_0;
		} catch (Exception e) {
			return OperatorTable.STATE_0;
		}
	}

	/**
	 * 根据手机号获取用户信息
	 *
	 * @param phone 手机号
	 * @return 用户信息
	 */
	public static OperatorInfo getOperatorByPhone(String phone) {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OperatorTable.TABLE_NAME, null,
					OperatorTable.TELEPHONE + " = '" + phone + "'", null, null,
					null, null);

			return getOperatorByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 更新用户的权限状态
	 *
	 * @param operatorId
	 * @param status
	 */
	public static void updateOperatorStatus(String operatorId, Integer status) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + OperatorTable.TABLE_NAME + " set "
					+ OperatorTable.OPERATOR_STATUS + "=" + status + ","
					+ " where " + OperatorTable.OPERATOR_ID + " = "
					+ operatorId);
		} catch (Exception e) {
		}
	}

	/**
	 * @param operatorId
	 * @param reserveStatus 预留柜子的是否可用
	 */
	public static void updateOperatorReserveStatus(String operatorId, Integer reserveStatus) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + OperatorTable.TABLE_NAME + " set "
					+ OperatorTable.RESERVE_STATUS + "=" + reserveStatus + ","
					+ " where " + OperatorTable.OPERATOR_ID + " = '"
					+ operatorId + "'");
		} catch (Exception e) {
		}
	}

	/**
	 * 更新用户的密码
	 *
	 * @param operatorId
	 * @param pwd
	 */
	public static void updateOperatorPwd(String operatorId, String pwd) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + OperatorTable.TABLE_NAME + " set "
					+ OperatorTable.OPERATOR_PASSWORD + " = '"
					+ pwd + "' , " + OperatorTable.STATE + "="
					+ OperatorTable.STATE_0 + " where "
					+ OperatorTable.OPERATOR_ID + " = '"
					+ operatorId + "'");
		} catch (Exception e) {
		}
	}

	/**
	 * @param operatorId 用户Id
	 * @param balance    用户余额
	 */
	public static short updateOperatorBalance(String operatorId, Long balance, Integer state) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + OperatorTable.TABLE_NAME + " set "
					+ OperatorTable.BALANCE + " = " + balance + ","
					+ OperatorTable.STATE + " = " + state
					+ " where " + OperatorTable.OPERATOR_ID + "='" + operatorId + "'");
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 根据Id删除用户信息
	 *
	 * @param operatorId
	 */
	public static short deleteOperatorById(String operatorId) {
		try {
			AppApplication.getInstance().getExDs().getDb().delete(OperatorTable.TABLE_NAME, OperatorTable.OPERATOR_ID + "='"
					+ operatorId + "'", null);
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}


	/**
	 * @param operatorId
	 * @param state
	 */
	public static void updateOperatorState(String operatorId, Integer state) {
		try {
			AppApplication.getInstance().getExDs().getDb().execSQL("update " + OperatorTable.TABLE_NAME + " set "
					+ OperatorTable.STATE + "=" + state
					+ " where " + OperatorTable.OPERATOR_ID + "='"
					+ operatorId + "'");
		} catch (Exception e) {
		}
	}

	private static OperatorInfo getOperatorByCursor(Cursor cursor) {
		if (cursor != null) {
			OperatorInfo info = null;
			try {
				int indexId = cursor.getColumnIndex(OperatorTable._ID);
				int indexOperatorName = cursor
						.getColumnIndex(OperatorTable.OPERATOR_NAME);
				int indexOperatorID = cursor
						.getColumnIndex(OperatorTable.OPERATOR_ID);
				int indexOperatorPwd = cursor
						.getColumnIndex(OperatorTable.OPERATOR_PASSWORD);
				int indexOpertorStatus = cursor
						.getColumnIndex(OperatorTable.OPERATOR_STATUS);
				int indexReserveStatus = cursor
						.getColumnIndex(OperatorTable.RESERVE_STATUS);
				int indexBalance = cursor.getColumnIndex(OperatorTable.BALANCE);
				int indexState = cursor.getColumnIndex(OperatorTable.STATE);
				int indexPhone = cursor.getColumnIndex(OperatorTable.TELEPHONE);
				int indexCardId = cursor.getColumnIndex(OperatorTable.OPERATOR_CARD);
				if (cursor.moveToFirst()) {
					info = new OperatorInfo();
					info.set_id(cursor.getLong(indexId));
					info.setOperatorName(cursor.getString(indexOperatorName));
					info.setOperatorId(cursor.getString(indexOperatorID));
					info.setPassword(cursor.getString(indexOperatorPwd));
					info.setReserveStatus(cursor.getInt(indexReserveStatus));
					info.setOperatorStatus(cursor.getInt(indexOpertorStatus));
					info.setTelephone(cursor.getString(indexPhone));
					info.setBalance(cursor.getLong(indexBalance));
					info.setState(cursor.getInt(indexState));
					info.setCardId(cursor.getString(indexCardId));
				}
			} catch (Exception e) {

			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}

			return info;
		}
		return null;
	}

	/**
	 * @return 获取所以本地缓存用户
	 */
	public static ArrayList<OperatorInfo> getAllOperator() {
		try {
			Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OperatorTable.TABLE_NAME, null, "", null,
					null, null, null);

			return getAllOperatorByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	private static ArrayList<OperatorInfo> getAllOperatorByCursor(Cursor cursor) {
		if (cursor != null) {
			ArrayList<OperatorInfo> Operator = null;
			try {
				int indexId = cursor.getColumnIndex(OperatorTable._ID);
				int indexOperatorName = cursor
						.getColumnIndex(OperatorTable.OPERATOR_NAME);
				int indexOperatorID = cursor
						.getColumnIndex(OperatorTable.OPERATOR_ID);
				int indexOperatorPwd = cursor
						.getColumnIndex(OperatorTable.OPERATOR_PASSWORD);
				int indexOpertorStatus = cursor
						.getColumnIndex(OperatorTable.OPERATOR_STATUS);
				int indexReserveStatus = cursor
						.getColumnIndex(OperatorTable.RESERVE_STATUS);

				int indexBalance = cursor.getColumnIndex(OperatorTable.BALANCE);
				int indexState = cursor.getColumnIndex(OperatorTable.STATE);
				int indexPhone = cursor.getColumnIndex(OperatorTable.TELEPHONE);
				int indexCardId = cursor.getColumnIndex(OperatorTable.OPERATOR_CARD);
				Operator = new ArrayList<OperatorInfo>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					OperatorInfo info = new OperatorInfo();
					info.set_id(cursor.getLong(indexId));
					info.setOperatorName(cursor.getString(indexOperatorName));
					info.setOperatorId(cursor.getString(indexOperatorID));
					info.setPassword(cursor.getString(indexOperatorPwd));
					info.setOperatorStatus(cursor.getInt(indexOpertorStatus));
					info.setReserveStatus(cursor.getInt(indexReserveStatus));
					info.setTelephone(cursor.getString(indexPhone));
					info.setBalance(cursor.getLong(indexBalance));
					info.setState(cursor.getInt(indexState));
					info.setCardId(cursor.getString(indexCardId));
					Operator.add(info);
					cursor.moveToPrevious();
				}
			} catch (Exception e) {

			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}

			return Operator;
		}

		return null;
	}
}
