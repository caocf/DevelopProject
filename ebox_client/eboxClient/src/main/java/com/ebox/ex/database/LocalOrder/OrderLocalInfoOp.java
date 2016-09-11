package com.ebox.ex.database.LocalOrder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.base.type.BoxType;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

public class OrderLocalInfoOp {

    public static final String TAG="OrderLocalInfoOp";

    /**
     * 更新订单超时信息
     * @return
     */
    public static int updateLocalOrderToTimeOutState(String order_id) {
        ContentValues values = new ContentValues();
        values.put(OrderLocalInfoTable.order_id, order_id);
        values.put(OrderLocalInfoTable.time_out, 1);
        return updateOrderTimeOutState(order_id, values);
    }

    /**
     * 将本地订单修改成未超时
     * @param orderId
     * @return
     */
    public static int updateLocalOrderToNotTimeOut(String orderId){
        ContentValues values = new ContentValues();
        values.put(OrderLocalInfoTable.order_id, orderId);
        values.put(OrderLocalInfoTable.time_out, 0);
        return updateOrderTimeOutState(orderId, values);
    }

    private static int updateOrderTimeOutState(String orderId, ContentValues values)
    {
        OrderLocalInfo existOrder = getOrderByOrderId(orderId);
        try {
            if (existOrder != null)
            {
                updateOrderByOrderId(orderId, values);
                LogUtil.i("update local order time out: " + orderId);
            } else {
              //  LogUtil.i("update local order time out order not exist: " + orderId);
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return -1;
        }
        return 1;
    }


    /**
     * 更新本地订单为待同步状态
     * @param localOrder
     */
    public static void updateLocalOrderState(OrderLocalInfo localOrder){

        updateLocalOrderPickUpType(localOrder.getBox_code(),localOrder.getUser_type());
    }


    //更新为订单待同步状态
    private static int updateLocalOrderPickUpType(String boxCode,String pickUpType) {
        ContentValues values = new ContentValues();
        values.put(OrderLocalInfoTable.box_code, boxCode);
        values.put(OrderLocalInfoTable.user_type,pickUpType);
        values.put(OrderLocalInfoTable.order_state,OrderLocalInfoTable.STATE_WAIT_UPLOAD);
        values.put(OrderLocalInfoTable.data1, System.currentTimeMillis()+"");

        OrderLocalInfo existOrder = getOrderByBoxCode(boxCode);

        try {
            if (existOrder != null)
            {
                updateOrder(boxCode, values);
                Log.i(TAG,"update local order pickup type ,boxCode: " + boxCode + " userType:" + pickUpType);
            } else {
                Log.w(TAG,"update local order pickup type order don't exist, boxCode: " + boxCode + " userType:" + pickUpType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }


    public static int addLocalOrder(OrderInfo orderInfo){

        OrderLocalInfo localInfo=new OrderLocalInfo();

        localInfo.setOrder_id(orderInfo.getOrder_id());

        BoxType box = orderInfo.getBox();
        if (box != null) {
            localInfo.setBox_code(box.getCode());
        } else {
            LogUtil.e("add local order boxCode is null");
        }

        localInfo.setCustomer_telephone(orderInfo.getMsisdn());
        localInfo.setOperator_telephone(orderInfo.getOperator_username());
        localInfo.setDelivery_at(orderInfo.getDelivery_at());
        localInfo.setPick_id(orderInfo.getPickup_id());
        localInfo.setItem_id(orderInfo.getItem_id());
        localInfo.setPassword(orderInfo.getPassword());

        localInfo.setOrder_state(OrderLocalInfoTable.STATE_CREATE);
        localInfo.setTime_out(orderInfo.getTimeout());


      return   addLocalOrder(localInfo);
    }


    public static int addLocalOrder(OrderLocalInfo order) {
        ContentValues values = new ContentValues();

        values.put(OrderLocalInfoTable.order_id, order.getOrder_id());
        values.put(OrderLocalInfoTable.box_code, order.getBox_code());
        values.put(OrderLocalInfoTable.customer_telephone, order.getCustomer_telephone());
        values.put(OrderLocalInfoTable.operator_telephone, order.getOperator_telephone());
        values.put(OrderLocalInfoTable.order_state, order.getOrder_state());
        values.put(OrderLocalInfoTable.delivery_at, order.getDelivery_at());
        values.put(OrderLocalInfoTable.password, order.getPassword());
        values.put(OrderLocalInfoTable.pick_id, order.getPick_id());
        values.put(OrderLocalInfoTable.item_id, order.getItem_id());
        values.put(OrderLocalInfoTable.time_out, order.getTime_out());

        try {
            String box_code = order.getBox_code();

            //订单存在更新，不存在插入
            OrderLocalInfo existOrder = getOrderByBoxCode(box_code);
            if (existOrder == null)
            {
                insertOrder(box_code, values);

            } else {
                //更新本地订单是未同步
                if (existOrder.getOrder_state()!=OrderLocalInfoTable.STATE_WAIT_UPLOAD)
                {
                    updateOrder(box_code, values);
                }
                else if (!existOrder.getOrder_id().equals(order.getOrder_id()))//不是同一个订单更新本地
                {
                    updateOrder(box_code, values);
                }
            }

        } catch (Exception e) {
            LogUtil.e("add local order error " + order.getBox_code());
            return -1;
        } finally {
        }
        return 1;
    }


    /**
     * 删除本地订单
     * @param boxCode
     */
    public static void deleteLocalOrder(String boxCode) {
        try {
            AppApplication.getInstance().getExDs().getDb().delete(OrderLocalInfoTable.TABLE_NAME, OrderLocalInfoTable.box_code + " = '" + boxCode + "'", null);
        } catch (Exception e) {
            LogUtil.e("delete local order error " + boxCode);
        }
    }

    private static void insertOrder(String boxCode, ContentValues values) {
        try {
            AppApplication.getInstance().getExDs().getDb().insert(OrderLocalInfoTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            LogUtil.e("insert local order error " + boxCode);
        }
    }

    private static void updateOrderByOrderId(String orderId, ContentValues values) {
        try {
            AppApplication.getInstance().getExDs().getDb().update(OrderLocalInfoTable.TABLE_NAME, values, OrderLocalInfoTable.order_id + " = '" + orderId + "'", null);
        } catch (Exception e) {
            LogUtil.e("update local order error " + orderId);
        }
    }
    private static void updateOrder(String boxCode, ContentValues values) {
        try {
            AppApplication.getInstance().getExDs().getDb().update(OrderLocalInfoTable.TABLE_NAME, values, OrderLocalInfoTable.box_code + " = '" + boxCode + "'", null);
        } catch (Exception e) {
            LogUtil.e("update local order error " + boxCode);
        }
    }


    /**
     * 根据boxCode 获取本地订单信息
     * @param boxCode
     * @return
     */
    public static OrderLocalInfo getOrderByBoxCode(String boxCode) {
        try {
            Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                    OrderLocalInfoTable.box_code + " = '" + boxCode + "'", null, null, null,
                    OrderLocalInfoTable._ID);
            return getOrderByCursor(cursor);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            LogUtil.e("get local order error "+boxCode);
            return null;
        }
    }

    private static OrderLocalInfo getOrderByOrderId(String orderId) {
        try {
            Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                    OrderLocalInfoTable.order_id + " = '" + orderId + "' and "
                    +OrderLocalInfoTable.order_state + " = '" + OrderLocalInfoTable.STATE_CREATE+ "'", null, null, null,
                    OrderLocalInfoTable._ID);
            return getOrderByCursor(cursor);
        } catch (Exception e) {
            LogUtil.e("get local order error "+orderId);
            return null;
        }
    }

    /**
     * 获取一个待同步订单
     * @return
     */
    public static OrderLocalInfo getOneWaitUploadOrder() {
        try {
            Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                    OrderLocalInfoTable.order_state + " = '" + OrderLocalInfoTable.STATE_WAIT_UPLOAD+ "'", null, null, null,
                    OrderLocalInfoTable._ID);
            return getOrderByCursor(cursor);
        } catch (Exception e) {
            LogUtil.e("get wait upload local order error ");
            LogUtil.e(e.getMessage());
            return null;
        }
    }

    /**
     * 根据6位密码获取本地订单未取走数据
     * @param pwd
     * @return
     */
    public static OrderLocalInfo getOrderByPwd(String pwd) {
        try {
            Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                    OrderLocalInfoTable.password + " = '" + pwd + "'"
                            + " and " + OrderLocalInfoTable.order_state + " = " + OrderLocalInfoTable.STATE_CREATE, null, null, null,
                    OrderLocalInfoTable._ID);
            return getOrderByCursor(cursor);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据用户手机号查询所有未取走的超期件
     * @param operator_phone
     * @return
     */
    public static ArrayList<OrderLocalInfo> getAllTimeOutOrderByOperator(String operator_phone) {
        Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                        OrderLocalInfoTable.operator_telephone + " = '" + operator_phone+"'"
                        + " and " + OrderLocalInfoTable.time_out + " = " + 1
                        + " and " + OrderLocalInfoTable.order_state + " = " + OrderLocalInfoTable.STATE_CREATE, null, null,
                null, OrderLocalInfoTable._ID);

        return getAllOrderByCursor(cursor);
    }

    public static ArrayList<OrderLocalInfo> getAllUnTimeOutOrderByOperator(String operator_phone) {
        Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                        OrderLocalInfoTable.operator_telephone + " = '" + operator_phone+"'"
                        + " and " + OrderLocalInfoTable.time_out + " = " + 0
                        + " and " + OrderLocalInfoTable.order_state + " = " + OrderLocalInfoTable.STATE_CREATE, null, null,
                null, OrderLocalInfoTable._ID);

        return getAllOrderByCursor(cursor);
    }

    /**
     * 根据手机号或者订单号查询本地未取走数据
     * @param number
     * @return
     */
    public static ArrayList<OrderLocalInfo> getAllOrderLocalsByMobilePhone(String number) {
        Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,
                "(" + OrderLocalInfoTable.customer_telephone + " = '" + number + "' or "
                        + OrderLocalInfoTable.item_id + " = '" + number + "')"
                        + " and " + OrderLocalInfoTable.order_state + " = " + OrderLocalInfoTable.STATE_CREATE, null, null,
                null, OrderLocalInfoTable._ID);

        return getAllOrderByCursor(cursor);
    }

    public static ArrayList<OrderLocalInfo> getAllOrderLocals() {

        Cursor cursor = AppApplication.getInstance().getExDs().getDb().query(OrderLocalInfoTable.TABLE_NAME, null,null,null,null,null, OrderLocalInfoTable.box_code);

        return getAllOrderByCursor(cursor);
    }

    private static OrderLocalInfo getOrderByCursor(Cursor cursor) {
        if (cursor != null) {
            OrderLocalInfo order = null;
            try {
                int indexBoxCode = cursor.getColumnIndex(OrderLocalInfoTable.box_code);
                int indexItemId = cursor.getColumnIndex(OrderLocalInfoTable.item_id);
                int indexPassword = cursor.getColumnIndex(OrderLocalInfoTable.password);
                int indexState = cursor.getColumnIndex(OrderLocalInfoTable.order_state);
                int indexOperatorPhone = cursor.getColumnIndex(OrderLocalInfoTable.operator_telephone);
                int indexCustomerPhone = cursor.getColumnIndex(OrderLocalInfoTable.customer_telephone);

                int index_order_id=cursor.getColumnIndex(OrderLocalInfoTable.order_id);
                int index_delivery_at = cursor.getColumnIndex(OrderLocalInfoTable.delivery_at);
                int index_pick_id = cursor.getColumnIndex(OrderLocalInfoTable.pick_id);//取件授权码
                int index_user_type = cursor.getColumnIndex(OrderLocalInfoTable.user_type);//取走人
                int index_time_out = cursor.getColumnIndex(OrderLocalInfoTable.time_out);//超期状态
                int index_pick_time = cursor.getColumnIndex(OrderLocalInfoTable.data1);//取件时间
                if (cursor.moveToFirst())
                {
                    order = new OrderLocalInfo();
                    order.setBox_code(cursor.getString(indexBoxCode));
                    order.setDelivery_at(cursor.getString(index_delivery_at));
                    order.setOrder_id(cursor.getString(index_order_id));
                    order.setItem_id(cursor.getString(indexItemId));
                    order.setPick_id(cursor.getString(index_pick_id));
                    order.setUser_type(cursor.getString(index_user_type));
                    order.setCustomer_telephone(cursor.getString(indexCustomerPhone));
                    order.setOperator_telephone(cursor.getString(indexOperatorPhone));
                    order.setPassword(cursor.getString(indexPassword));

                    order.setTime_out(cursor.getInt(index_time_out));
                    order.setOrder_state(cursor.getInt(indexState));
                    order.setPick_time(cursor.getString(index_pick_time));
                }
                return order;
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return order;
        }
        return null;
    }


    private static ArrayList<OrderLocalInfo> getAllOrderByCursor(Cursor cursor) {
        if (cursor != null) {
            ArrayList<OrderLocalInfo> Operator = null;
            try {
                int indexBoxCode = cursor.getColumnIndex(OrderLocalInfoTable.box_code);
                int indexItemId = cursor.getColumnIndex(OrderLocalInfoTable.item_id);
                int indexPassword = cursor.getColumnIndex(OrderLocalInfoTable.password);
                int indexState = cursor.getColumnIndex(OrderLocalInfoTable.order_state);
                int indexOperatorPhone = cursor.getColumnIndex(OrderLocalInfoTable.operator_telephone);
                int indexCustomerPhone = cursor.getColumnIndex(OrderLocalInfoTable.customer_telephone);

                int index_order_id=cursor.getColumnIndex(OrderLocalInfoTable.order_id);
                int index_delivery_at = cursor.getColumnIndex(OrderLocalInfoTable.delivery_at);
                int index_pick_id = cursor.getColumnIndex(OrderLocalInfoTable.pick_id);//取件授权码
                int index_user_type = cursor.getColumnIndex(OrderLocalInfoTable.user_type);//取走人
                int index_time_out = cursor.getColumnIndex(OrderLocalInfoTable.time_out);//超期状态
                int index_pick_time = cursor.getColumnIndex(OrderLocalInfoTable.data1);//取件时间
                Operator = new ArrayList<OrderLocalInfo>();
                cursor.moveToLast();
                while (!cursor.isBeforeFirst()) {
                    OrderLocalInfo order = new OrderLocalInfo();
                    order.setBox_code(cursor.getString(indexBoxCode));
                    order.setDelivery_at(cursor.getString(index_delivery_at));
                    order.setOrder_id(cursor.getString(index_order_id));
                    order.setItem_id(cursor.getString(indexItemId));
                    order.setPick_id(cursor.getString(index_pick_id));
                    order.setUser_type(cursor.getString(index_user_type));
                    order.setCustomer_telephone(cursor.getString(indexCustomerPhone));
                    order.setOperator_telephone(cursor.getString(indexOperatorPhone));
                    order.setPassword(cursor.getString(indexPassword));

                    order.setTime_out(cursor.getInt(index_time_out));
                    order.setOrder_state(cursor.getInt(indexState));
                    order.setPick_time(cursor.getString(index_pick_time));

                    Operator.add(order);

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
