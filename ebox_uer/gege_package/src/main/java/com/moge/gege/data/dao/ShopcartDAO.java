package com.moge.gege.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.moge.gege.data.DBConstants;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.TradingBaseModel;
import com.moge.gege.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopcartDAO extends BaseDAO
{
    public static final String TAG = ShopcartDAO.class.getName();

    private ArrayList<TradingBaseModel> mSelectedGoods = new ArrayList<TradingBaseModel>();

    private static ShopcartDAO instance = null;

    public static ShopcartDAO instance()
    {
        if (instance == null)
        {
            instance = new ShopcartDAO();
        }
        return instance;
    }

    private String attachmentToString(AttachmentModel attachment)
    {
        StringBuilder sb = new StringBuilder();
        for (String image : attachment.getImages())
        {
            sb.append(image + ",");
        }

        if (sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private AttachmentModel stringToAttachment(String content)
    {
        AttachmentModel attachment = new AttachmentModel();

        List<String> list = new ArrayList<String>();

        String[] itemArray = content.split(";");
        for (String item : itemArray)
        {
            list.add(item);
        }

        attachment.setImages(list);

        return attachment;
    }

    private boolean insertGoods(TradingBaseModel model, String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.SHOPCART.ID, model.get_id());
        values.put(DBConstants.SHOPCART.ATTACHMENTS,
                attachmentToString(model.getAttachments()));
        values.put(DBConstants.SHOPCART.DISCOUNT_PRICE,
                model.getDiscount_price());
        values.put(DBConstants.SHOPCART.NUM, model.getNum() - model.getSale_num());
        values.put(DBConstants.SHOPCART.TITLE, model.getTitle());
        values.put(DBConstants.SHOPCART.PRICE, model.getPrice());
        values.put(DBConstants.SHOPCART.IS_SELECTED,
                model.isSelected() ? 1 : 0);
        values.put(DBConstants.SHOPCART.BUY_NUM, model.getBuyNum());
        values.put(DBConstants.SHOPCART.USER_ACCOUNT_ID, userAccountId);
        values.put(DBConstants.SHOPCART.MERCHANT_ID, model.getAuthor_uid());
        values.put(DBConstants.SHOPCART.PROMOTION_ID, model.getPromotion_id());

        long rowId = mDb.insert(DBConstants.Table_Names.SHOPCART, null, values);
        String debug = String.format("插入商品：商品id=%s,", model.get_id())
                + String.format("商品名称=%s,", model.getTitle())
                + String.format("商品数量=%d,", model.getNum())
                + String.format("用户id=%s", userAccountId);
        LogUtil.d(TAG, debug);

        return rowId != -1 ? true : false;
    }

    public boolean updateGoods(TradingBaseModel model, String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.SHOPCART.BUY_NUM, model.getBuyNum());
        values.put(DBConstants.SHOPCART.DISCOUNT_PRICE, model.getDiscount_price());
        values.put(DBConstants.SHOPCART.NUM, model.getNum() - model.getSale_num());
        values.put(DBConstants.SHOPCART.TITLE, model.getTitle());
        values.put(DBConstants.SHOPCART.PRICE, model.getPrice());
        values.put(DBConstants.SHOPCART.MERCHANT_ID, model.getAuthor_uid());
        values.put(DBConstants.SHOPCART.PROMOTION_ID, model.getPromotion_id());

        int row = mDb.update(DBConstants.Table_Names.SHOPCART, values,
                DBConstants.SHOPCART.ID + "=? and "
                        + DBConstants.SHOPCART.USER_ACCOUNT_ID + "=?",
                new String[] { model.get_id(), userAccountId });

        String debug = String.format("更新商品：商品id=%s,", model.get_id())
                + String.format("商品购买数量=%s,", model.getBuyNum())
                + String.format("用户id=%s", userAccountId);
        LogUtil.d(TAG, debug);

        return row > 0 ? true : false;
    }

    public void updateGoods(List<TradingBaseModel> list, String userAccountId)
    {
        mDb.beginTransaction();
        for (TradingBaseModel model : list)
        {
            updateGoods(model, userAccountId);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public boolean deleteGoods(TradingBaseModel model, String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        int row = mDb.delete(DBConstants.Table_Names.SHOPCART,
                DBConstants.SHOPCART.ID + "=? and "
                        + DBConstants.SHOPCART.USER_ACCOUNT_ID + "=?",
                new String[] { model.get_id(), userAccountId });

        String debug = String.format("删除商品：商品id=%s,", model.get_id())
                + String.format("商品名称=%s,", model.getTitle())
                + String.format("用户id=%s", userAccountId);
        LogUtil.d(TAG, debug);

        return row > 0 ? true : false;
    }

    public void deleteGoods(List<TradingBaseModel> list, String userAccountId)
    {
        if (list == null || list.size() == 0)
        {
            return;
        }

        mDb.beginTransaction();
        for (TradingBaseModel model : list)
        {
            deleteGoods(model, userAccountId);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public boolean selectGoods(TradingBaseModel model, String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.SHOPCART.IS_SELECTED,
                model.isSelected() ? 1 : 0);
        int row = mDb.update(DBConstants.Table_Names.SHOPCART, values,
                DBConstants.SHOPCART.ID + "=? and "
                        + DBConstants.SHOPCART.USER_ACCOUNT_ID + "=?",
                new String[] { model.get_id(), userAccountId });

        String debug = String.format("选中商品：商品id=%s,", model.get_id())
                + String.format("商品名称=%s,", model.getTitle())
                + String.format("用户id=%s", userAccountId);
        LogUtil.d(TAG, debug);

        return row > 0 ? true : false;
    }

    public void selectAllGoods(String userAccountId, boolean isSelected)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.SHOPCART.IS_SELECTED, isSelected ? 1 : 0);
        mDb.update(DBConstants.Table_Names.SHOPCART, values,
                DBConstants.SHOPCART.USER_ACCOUNT_ID + "=?",
                new String[] { userAccountId });

        String debug = String.format("用户id=%s", userAccountId);
        LogUtil.d(TAG, debug);

        return;
    }

    public List<TradingBaseModel> getGoodsList(String goodsId,
            String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        List<TradingBaseModel> goodsList = new ArrayList<TradingBaseModel>();
        String[] columns = { DBConstants.SHOPCART.ID,
                DBConstants.SHOPCART.ATTACHMENTS,
                DBConstants.SHOPCART.DISCOUNT_PRICE, DBConstants.SHOPCART.NUM,
                DBConstants.SHOPCART.TITLE, DBConstants.SHOPCART.PRICE,
                DBConstants.SHOPCART.IS_SELECTED, DBConstants.SHOPCART.BUY_NUM,
                DBConstants.SHOPCART.MERCHANT_ID, DBConstants.SHOPCART.PROMOTION_ID };

        String selection = new StringBuffer().append(DBConstants.SHOPCART.ID)
                .append("=? and ").append(DBConstants.SHOPCART.USER_ACCOUNT_ID)
                .append("=?").toString();

        String[] selectionArgs = { goodsId, userAccountId };

        Cursor cursor = mDb.query(DBConstants.Table_Names.SHOPCART, columns,
                selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            TradingBaseModel goods = new TradingBaseModel();
            goods.set_id(cursor.getString(0));
            goods.setAttachments(stringToAttachment(cursor.getString(1)));
            goods.setDiscount_price(cursor.getInt(2));
            goods.setNum(cursor.getInt(3));
            goods.setTitle(cursor.getString(4));
            goods.setPrice(cursor.getInt(5));
            goods.setSelected(cursor.getInt(6) == 1 ? true : false);
            goods.setBuyNum(cursor.getInt(7));
            goods.setAuthor_uid(cursor.getString(8));
            goods.setPromotion_id(cursor.getString(9));
            goodsList.add(goods);
        }
        cursor.close();
        return goodsList;
    }

    public void updateUserGoods(String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        List<TradingBaseModel> tempTradings = getShopcart("");
        if (tempTradings == null || tempTradings.size() == 0)
        {
            return;
        }

        insertShopcart(tempTradings, userAccountId);
        clearShopcart("");

        //        ContentValues values = new ContentValues();
        //        values.put(DBConstants.SHOPCART.USER_ACCOUNT_ID, userAccountId);
        //        mDb.update(DBConstants.Table_Names.SHOPCART, values,
        //                DBConstants.SHOPCART.USER_ACCOUNT_ID + "=?",
        //                new String[] { "0" });
    }

    public boolean insertShopcart(TradingBaseModel model, String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        boolean result = false;

        List<TradingBaseModel> goodsList = getGoodsList(model.get_id(),
                userAccountId);
        if (goodsList.size() == 0)
        {
            result = insertGoods(model, userAccountId);
        }
        else
        {
            int newBuyNum = model.getBuyNum() + goodsList.get(0).getBuyNum();
            model.setBuyNum(newBuyNum);
            result = updateGoods(model, userAccountId);
        }

        return result;
    }

    public void insertShopcart(List<TradingBaseModel> list,
            String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        mDb.beginTransaction();
        for (TradingBaseModel goods : list)
        {
            insertShopcart(goods, userAccountId);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public List<TradingBaseModel> getShopcart(String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        List<TradingBaseModel> goodsList = new ArrayList<TradingBaseModel>();
        String[] columns = { DBConstants.SHOPCART.ID,
                DBConstants.SHOPCART.ATTACHMENTS,
                DBConstants.SHOPCART.DISCOUNT_PRICE, DBConstants.SHOPCART.NUM,
                DBConstants.SHOPCART.TITLE, DBConstants.SHOPCART.PRICE,
                DBConstants.SHOPCART.IS_SELECTED, DBConstants.SHOPCART.BUY_NUM,
                DBConstants.SHOPCART.MERCHANT_ID, DBConstants.SHOPCART.PROMOTION_ID };

        String selection = new StringBuffer()
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append("=?")
                .toString();

        String[] selectionArgs = { userAccountId };

        Cursor cursor = mDb.query(DBConstants.Table_Names.SHOPCART, columns,
                selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            TradingBaseModel goods = new TradingBaseModel();
            goods.set_id(cursor.getString(0));
            goods.setAttachments(stringToAttachment(cursor.getString(1)));
            goods.setDiscount_price(cursor.getInt(2));
            goods.setNum(cursor.getInt(3));
            goods.setTitle(cursor.getString(4));
            goods.setPrice(cursor.getInt(5));
            goods.setSelected(cursor.getInt(6) == 1 ? true : false);
            goods.setBuyNum(cursor.getInt(7));
            goods.setAuthor_uid(cursor.getString(8));
            goods.setPromotion_id(cursor.getString(9));
            goodsList.add(goods);
        }
        cursor.close();
        return goodsList;
    }

    public int getShopcartSize(String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        String[] columns = { "SUM(" + DBConstants.SHOPCART.BUY_NUM + ")" };

        String selection = new StringBuffer()
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append("=?")
                .toString();

        String[] selectionArgs = { userAccountId };

        Cursor cursor = mDb.query(DBConstants.Table_Names.SHOPCART, columns,
                selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getGoodBuyNum(String userAccountId, String id)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }

        String[] columns = { DBConstants.SHOPCART.BUY_NUM };

        String selection = new StringBuffer()
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append("=?")
                .append(" and ").append(DBConstants.SHOPCART.ID)
                .append("=?").toString();

        String[] selectionArgs = { userAccountId, id };

        Cursor cursor = mDb.query(DBConstants.Table_Names.SHOPCART, columns,
                selection, selectionArgs, null, null, null);

        int count = 0;
        if(cursor.moveToFirst())
        {
           count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void clearShopcart(String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }
        String selection = new StringBuffer()
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append("=?")
                .toString();
        String[] selectionArgs = { userAccountId };
        mDb.delete(DBConstants.Table_Names.SHOPCART, selection, selectionArgs);
        LogUtil.d(TAG, "清空购物车");
    }

    public void clearSelectedShopcart(String userAccountId)
    {
        if (TextUtils.isEmpty(userAccountId))
        {
            userAccountId = "0";
        }
        String selection = new StringBuffer()
                .append(DBConstants.SHOPCART.USER_ACCOUNT_ID).append("=?")
                .append(" and ").append(DBConstants.SHOPCART.IS_SELECTED)
                .append("=?").toString();
        String[] selectionArgs = { userAccountId, "1" };
        mDb.delete(DBConstants.Table_Names.SHOPCART, selection, selectionArgs);
        LogUtil.d(TAG, "清空checked购物车");
    }

    public void addSelectedGoods(ArrayList<TradingBaseModel> selectedGoods)
    {
        mSelectedGoods.clear();
        mSelectedGoods.addAll(selectedGoods);
    }

    public ArrayList<TradingBaseModel> getSelectedGoods()
    {
        return mSelectedGoods;
    }
}
