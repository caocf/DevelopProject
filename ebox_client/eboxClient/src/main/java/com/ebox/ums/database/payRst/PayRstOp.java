package com.ebox.ums.database.payRst;

import android.content.ContentValues;
import android.database.Cursor;

import com.ebox.AppApplication;

import java.util.ArrayList;

public class PayRstOp {
	private static TradingFinishOrderData getTradingFinishOrderByCursor(Cursor cursor) {
		if (cursor != null) {
			TradingFinishOrderData tranRst = null;
			try{
			int indexAmount = cursor.getColumnIndex(PayRstTable.AMOUNT);
			int indexAppendField = cursor
					.getColumnIndex(PayRstTable.APPENDFIELD);
			int indexAuthNo = cursor.getColumnIndex(PayRstTable.AUTHNO);
			int indexBankCode = cursor.getColumnIndex(PayRstTable.BANKCODE);
			int indexBatch = cursor.getColumnIndex(PayRstTable.BATCH);
			int indexCardNo = cursor.getColumnIndex(PayRstTable.CATDNO);
			int indexExp = cursor.getColumnIndex(PayRstTable.EXP);
			int indexMchtId = cursor.getColumnIndex(PayRstTable.MCHTID);
			int indexMobile = cursor.getColumnIndex(PayRstTable.MOBILE);
			int indexPayId = cursor.getColumnIndex(PayRstTable.PAYID);
			int indexPayType = cursor.getColumnIndex(PayRstTable.PAYTYPE);
			int indexPrintInfo = cursor.getColumnIndex(PayRstTable.PRINTINFO);
			int indexRefence = cursor.getColumnIndex(PayRstTable.REFERENCE);
			int indexResult = cursor.getColumnIndex(PayRstTable.RESULT);
			int indexRspChin = cursor.getColumnIndex(PayRstTable.RSPCHIN);
			int indexRstCode = cursor.getColumnIndex(PayRstTable.RSTCODE);
			int indexSettleDate = cursor.getColumnIndex(PayRstTable.SETTLEDATE);
			int indexTermId = cursor.getColumnIndex(PayRstTable.TERMID);
			int indexTrace = cursor.getColumnIndex(PayRstTable.TRACE);
			int indexTransDate = cursor.getColumnIndex(PayRstTable.TRANSDATE);
			int indexTransTime = cursor.getColumnIndex(PayRstTable.TRANSTIME);
			int error_msg = cursor.getColumnIndex(PayRstTable.ERRORMSG);
			int indexNoncestr = cursor.getColumnIndex(PayRstTable.NONCESTR);
			int indexTimestamp = cursor.getColumnIndex(PayRstTable.TIMESTAMP);
			int indexState = cursor.getColumnIndex(PayRstTable.STATE);

			if (cursor.moveToFirst()) {
				tranRst = new TradingFinishOrderData();
				tranRst.setAmount(cursor.getString(indexAmount));
				tranRst.setAppendField(cursor.getString(indexAppendField));
				tranRst.setAuthNo(cursor.getString(indexAuthNo));
				tranRst.setBankcode(cursor.getString(indexBankCode));
				tranRst.setBatch(cursor.getString(indexBatch));
				tranRst.setCardNo(cursor.getString(indexCardNo));
				tranRst.setExp(cursor.getString(indexExp));
				tranRst.setMchtId(cursor.getString(indexMchtId));
				tranRst.setPrintInfo(cursor.getString(indexPrintInfo));
				tranRst.setReference(cursor.getString(indexRefence));
				tranRst.setRspChin(cursor.getString(indexRspChin));
				tranRst.setRstCode(cursor.getString(indexRstCode));
				tranRst.setSettleDate(cursor.getString(indexSettleDate));
				tranRst.setTermId(cursor.getString(indexTermId));
				tranRst.setTrace(cursor.getString(indexTrace));
				tranRst.setTransDate(cursor.getString(indexTransDate));
				tranRst.setTransTime(cursor.getString(indexTransTime));
				tranRst.setResult(cursor.getString(indexResult));
				tranRst.setMobile(cursor.getString(indexMobile));
				tranRst.setPayId(cursor.getString(indexPayId));
				tranRst.setPayType(cursor.getString(indexPayType));
				tranRst.setError_msg(cursor.getString(error_msg));
				tranRst.setNoncestr(cursor.getString(indexNoncestr));
				tranRst.setTimestamp(cursor.getString(indexTimestamp));
				tranRst.setState(cursor.getInt(indexState));
			}
			}catch(Exception e)
			{

			}finally{
				if(cursor != null)
				{
					cursor.close();
				}
			}

			return tranRst;
		}
		return null;
	}

	public static TradingFinishOrderData getTradingFinishOrderByPayId(String payId) {
		try {
			Cursor cursor = AppApplication.getInstance().getUmsDs().getDb().query(PayRstTable.TABLE_NAME, null,
					PayRstTable.PAYID + " = '" + payId + "'", null, null, null,
					PayRstTable.PAYID);

			return getTradingFinishOrderByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static TradingFinishOrderData getTradingFinishOrder() {
		try {
			Cursor cursor = AppApplication.getInstance().getUmsDs().getDb().query(PayRstTable.TABLE_NAME, null, null, null,
					null, null, PayRstTable.PAYID);

			return getTradingFinishOrderByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static TradingFinishOrderData getTradingFinishOrderByState(int state) {
		try {
			Cursor cursor = AppApplication.getInstance().getUmsDs().getDb().query(PayRstTable.TABLE_NAME, null, PayRstTable.STATE + " = '" + state + "'", null,
					null, null, PayRstTable.PAYID);

			return getTradingFinishOrderByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}
	public static void updateTradingFinishOrderState(int state,String payId){
		try {
			AppApplication.getInstance().getUmsDs().getDb().execSQL("update " + PayRstTable.TABLE_NAME + " set "
					+ PayRstTable.STATE + "=" + state + " where "
					+ PayRstTable.PAYID + "='" + payId + "'");
		} catch (Exception e) {
		}
	}

	private static ArrayList<TradingFinishOrderData> getAllTradingFinishOrderByCursor(
			Cursor cursor) {
		if (cursor != null) {
			ArrayList<TradingFinishOrderData> deliver = null;
			try{
				int indexAmount = cursor.getColumnIndex(PayRstTable.AMOUNT);
				int indexAppendField = cursor
						.getColumnIndex(PayRstTable.APPENDFIELD);
				int indexAuthNo = cursor.getColumnIndex(PayRstTable.AUTHNO);
				int indexBankCode = cursor.getColumnIndex(PayRstTable.BANKCODE);
				int indexBatch = cursor.getColumnIndex(PayRstTable.BATCH);
				int indexCardNo = cursor.getColumnIndex(PayRstTable.CATDNO);
				int indexExp = cursor.getColumnIndex(PayRstTable.EXP);
				int indexMchtId = cursor.getColumnIndex(PayRstTable.MCHTID);
				int indexMobile = cursor.getColumnIndex(PayRstTable.MOBILE);
				int indexPayId = cursor.getColumnIndex(PayRstTable.PAYID);
				int indexPayType = cursor.getColumnIndex(PayRstTable.PAYTYPE);
				int indexPrintInfo = cursor.getColumnIndex(PayRstTable.PRINTINFO);
				int indexRefence = cursor.getColumnIndex(PayRstTable.REFERENCE);
				int indexResult = cursor.getColumnIndex(PayRstTable.RESULT);
				int indexRspChin = cursor.getColumnIndex(PayRstTable.RSPCHIN);
				int indexRstCode = cursor.getColumnIndex(PayRstTable.RSTCODE);
				int indexSettleDate = cursor.getColumnIndex(PayRstTable.SETTLEDATE);
				int indexTermId = cursor.getColumnIndex(PayRstTable.TERMID);
				int indexTrace = cursor.getColumnIndex(PayRstTable.TRACE);
				int indexTransDate = cursor.getColumnIndex(PayRstTable.TRANSDATE);
				int indexTransTime = cursor.getColumnIndex(PayRstTable.TRANSTIME);
				int error_msg = cursor.getColumnIndex(PayRstTable.ERRORMSG);
				int indexNoncestr = cursor.getColumnIndex(PayRstTable.NONCESTR);
				int indexTimestamp = cursor.getColumnIndex(PayRstTable.TIMESTAMP);
				int indexState = cursor.getColumnIndex(PayRstTable.STATE);
				deliver = new ArrayList<TradingFinishOrderData>();
				cursor.moveToLast();
				while (!cursor.isBeforeFirst()) {
					TradingFinishOrderData tranRst = new TradingFinishOrderData();
					tranRst.setAmount(cursor.getString(indexAmount));
					tranRst.setAppendField(cursor.getString(indexAppendField));
					tranRst.setAuthNo(cursor.getString(indexAuthNo));
					tranRst.setBankcode(cursor.getString(indexBankCode));
					tranRst.setBatch(cursor.getString(indexBatch));
					tranRst.setCardNo(cursor.getString(indexCardNo));
					tranRst.setExp(cursor.getString(indexExp));
					tranRst.setMchtId(cursor.getString(indexMchtId));
					tranRst.setPrintInfo(cursor.getString(indexPrintInfo));
					tranRst.setReference(cursor.getString(indexRefence));
					tranRst.setRspChin(cursor.getString(indexRspChin));
					tranRst.setRstCode(cursor.getString(indexRstCode));
					tranRst.setSettleDate(cursor.getString(indexSettleDate));
					tranRst.setTermId(cursor.getString(indexTermId));
					tranRst.setTrace(cursor.getString(indexTrace));
					tranRst.setTransDate(cursor.getString(indexTransDate));
					tranRst.setTransTime(cursor.getString(indexTransTime));
					tranRst.setResult(cursor.getString(indexResult));
					tranRst.setMobile(cursor.getString(indexMobile));
					tranRst.setPayId(cursor.getString(indexPayId));
					tranRst.setPayType(cursor.getString(indexPayType));
					tranRst.setError_msg(cursor.getString(error_msg));
					tranRst.setNoncestr(cursor.getString(indexNoncestr));
					tranRst.setTimestamp(cursor.getString(indexTimestamp));
					tranRst.setState(cursor.getInt(indexState));
					deliver.add(tranRst);
					cursor.moveToPrevious();
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

	public static void deleteTradingFinishOrder(String payId) {
		try {
			AppApplication.getInstance().getUmsDs().getDb().delete(PayRstTable.TABLE_NAME, PayRstTable.PAYID + "='" + payId
					+ "'", null);
		} catch (Exception e) {

		}
	}

	public static ArrayList<TradingFinishOrderData> getTradingFinishOrders() {
		try {
			Cursor cursor = AppApplication.getInstance().getUmsDs().getDb().query(PayRstTable.TABLE_NAME, null, null, null,
					null, null, PayRstTable.PAYID + " desc");

			return getAllTradingFinishOrderByCursor(cursor);
		} catch (Exception e) {
			return null;
		}
	}

	public static short addTradingFinishOrder(TradingFinishOrderData adv) {
		if (getTradingFinishOrderByPayId(adv.getPayId()) != null) {
			return 1;
		}
		ContentValues values = new ContentValues();
		values.put(PayRstTable.AMOUNT, adv.getAmount());
		values.put(PayRstTable.APPENDFIELD, adv.getAppendField());
		values.put(PayRstTable.AUTHNO, adv.getAuthNo());
		values.put(PayRstTable.BANKCODE, adv.getBankcode());
		values.put(PayRstTable.BATCH, adv.getBatch());
		values.put(PayRstTable.CATDNO, adv.getCardNo());
		values.put(PayRstTable.EXP, adv.getExp());
		values.put(PayRstTable.MCHTID, adv.getMchtId());
		values.put(PayRstTable.MOBILE, adv.getMobile());
		values.put(PayRstTable.PAYID, adv.getPayId());
		values.put(PayRstTable.PAYTYPE, adv.getPayType());
		values.put(PayRstTable.PRINTINFO, adv.getPrintInfo());
		values.put(PayRstTable.REFERENCE, adv.getReference());
		values.put(PayRstTable.RESULT, adv.getResult());
		values.put(PayRstTable.RSPCHIN, adv.getRspChin());
		values.put(PayRstTable.RSTCODE, adv.getRstCode());
		values.put(PayRstTable.SETTLEDATE, adv.getSettleDate());
		values.put(PayRstTable.TERMID, adv.getTermId());
		values.put(PayRstTable.TRACE, adv.getTrace());
		values.put(PayRstTable.TRANSDATE, adv.getTransDate());
		values.put(PayRstTable.TRANSTIME, adv.getTransTime());
		values.put(PayRstTable.ERRORMSG, adv.getError_msg());
		values.put(PayRstTable.NONCESTR, adv.getNoncestr());
		values.put(PayRstTable.TIMESTAMP, adv.getTimestamp());
		values.put(PayRstTable.STATE, adv.getState());
		Cursor cursor = null;
		try {
			AppApplication.getInstance().getUmsDs().getDb().insert(PayRstTable.TABLE_NAME, PayRstTable._ID, values);
			return 1;
		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
