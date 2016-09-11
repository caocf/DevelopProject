package com.ebox.ex.utils;

/**
 * Created by Android on 2015/8/27.
 */

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoTable;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.FasterSelectMode;
import com.ebox.ex.network.model.RspFirstUpdateLocalOrder;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.base.type.ExpressCharge;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.BoxStateType;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.RackType;
import com.ebox.ex.network.request.FirstUpdateLocalOrderRequest;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BoxInfoHelper {

    //关闭箱门选择后清空数据
    private static ArrayList<BoxInfoType> mBoxs;
    private static boolean isCanUser;

    public static void setBoxData(ArrayList<BoxInfoType> boxs, boolean canUser) {
        isCanUser = canUser;
        mBoxs = boxs;
    }

    public static void clearBoxData() {
        if (mBoxs != null) {
            mBoxs.clear();
            mBoxs = null;
        }
        isCanUser = false;
    }

    public static int getCountByRackType(int rackType) {
        int count = 0;
        ArrayList<BoxInfoType> type = getBoxDataByRackType(rackType);

        for (BoxInfoType info : type) {

            if (info.getDoorStatus() != DoorStatusType.unknow
                    && info.getBoxStatus() == BoxStateType.empty) {
                count++;
            }
        }
        return count;
    }

    public static List<FasterSelectMode> getFaster() {

        mBoxs=null;

        List<FasterSelectMode> modes = new ArrayList<FasterSelectMode>();

        ArrayList<BoxInfoType> kd = getBoxDataByRackType(RackType.box_kd);
        if (kd.size()>0)
        {
            modes.add(getOneFaster(kd, BoxSizeType.large, RackType.box_kd));
            modes.add(getOneFaster(kd, BoxSizeType.medium, RackType.box_kd));
            modes.add(getOneFaster(kd, BoxSizeType.small, RackType.box_kd));
            modes.add(getOneFaster(kd, BoxSizeType.tiny, RackType.box_kd));
        }

        ArrayList<BoxInfoType> sx = getBoxDataByRackType(RackType.box_sx);
        if (sx.size()>0)
        {
            modes.add(getOneFaster(sx, BoxSizeType.large, RackType.box_sx));
            modes.add(getOneFaster(sx, BoxSizeType.medium, RackType.box_sx));
            modes.add(getOneFaster(sx, BoxSizeType.small, RackType.box_sx));
            modes.add(getOneFaster(sx, BoxSizeType.tiny, RackType.box_sx));
        }
        ArrayList<BoxInfoType> bw = getBoxDataByRackType(RackType.box_bw);
        if (bw.size()>0)
        {
            modes.add(getOneFaster(bw, BoxSizeType.large, RackType.box_bw));
            modes.add(getOneFaster(bw, BoxSizeType.medium, RackType.box_bw));
            modes.add(getOneFaster(bw, BoxSizeType.small, RackType.box_bw));
            modes.add(getOneFaster(bw, BoxSizeType.tiny, RackType.box_bw));
        }

        return  modes;
    }

    public static long getBoxMoney(int size){
        List<ExpressCharge> expressCharge;
        if (GlobalField.serverConfig != null && GlobalField.serverConfig.getExpressCharge() != null)
        {
            expressCharge = GlobalField.serverConfig.getExpressCharge();
        }else
        {
            expressCharge= OperatorHelper.getExpressChargeByServer();
        }
        if (expressCharge!=null)
        {
            for (ExpressCharge charge : expressCharge) {

                if (size == charge.getBoxSize())
                {
                    return charge.getFee();
                }
            }
        }
        return 0L;
    }

    public static String getMoney(long balance){
        String re;
        if(balance < 0) {
            balance = Math.abs(balance);
            re =balance/100+"."+String.format("%02d",balance%100)+"元";
        } else {
            re =balance/100+"."+String.format("%02d",balance%100)+"元";
        }
        return  re;
    }

    public static long getBoxMoney(BoxInfoType boxInfoType){

        Integer boxSize = boxInfoType.getBoxSize();
        return getBoxMoney(boxSize);
    }

    private static  FasterSelectMode getOneFaster(List<BoxInfoType> infoTypes,int type,int rackType) {

        //全部数据
        List<BoxInfoType> boxByType = getBoxByType(infoTypes, type);
        if (boxByType==null)
        {
            return null;
        }
        FasterSelectMode mode = new FasterSelectMode();
        mode.setBox_infos(boxByType);
        mode.setBox_count(boxByType.size());
        mode.setBox_type(getRack(rackType, type));
        //背景
        mode.setBox_bg_normal(R.drawable.ex_box_code_normal);
        mode.setBox_bg_select(R.color.ex_box_select);
        mode.setBox_bg_disable(R.color.ex_box_disable);
        //字体
        mode.setBox_text_normal(R.color.ex_text_normal);
        mode.setBox_text_select(R.color.ex_text_select);
        mode.setBox_text_disable(R.color.ex_text_disable);
        //费用
        mode.setMoney(getMoney(getBoxMoney(type)));
        mode.setMoney_color_normal(R.color.ex_text_grey);
        mode.setMoney_color_select(R.color.ex_red);

        if (boxByType.size()==0)
        {
            mode.setBox_state(3);
        }else
        {
            mode.setBox_state(0);
        }

        if (type == BoxSizeType.large)
        {
            mode.setBox_image_nomal(R.drawable.ex_box_big_normal);
            mode.setBox_image_select(R.drawable.ex_box_big_select);
            mode.setBox_image_disable(R.drawable.ex_box_big_disable);
        }
        else if(type==BoxSizeType.medium)
        {
            mode.setBox_image_nomal(R.drawable.ex_box_medium_normal);
            mode.setBox_image_select(R.drawable.ex_box_medium_select);
            mode.setBox_image_disable(R.drawable.ex_box_medium_disable);
        }
        else if(type==BoxSizeType.small)
        {
            mode.setBox_image_nomal(R.drawable.ex_box_small_normall);
            mode.setBox_image_select(R.drawable.ex_box_small_select);
            mode.setBox_image_disable(R.drawable.ex_box_small_disable);
        }
        else if(type==BoxSizeType.tiny)
        {
            mode.setBox_image_nomal(R.drawable.ex_box_tiny_normal);
            mode.setBox_image_select(R.drawable.ex_box_tiny_select);
            mode.setBox_image_disable(R.drawable.ex_box_tiny_disable);
        }

        return mode;
    }

    private static String getRack(int rackType,int type) {
        if (rackType==RackType.box_kd)
        {
            if (type==BoxSizeType.large) {
                return "大号";
            }else if (type==BoxSizeType.medium) {
                return "中号";
            }else if (type==BoxSizeType.small) {
                return "小号";
            }else if (type==BoxSizeType.tiny) {
                return "微号";
            }
        }
        else if (rackType == RackType.box_sx)
        {
            if (type==BoxSizeType.large) {
                return "生鲜大";
            }else if (type==BoxSizeType.medium) {
                return "生鲜中";
            }else if (type==BoxSizeType.small) {
                return "生鲜小";
            }else if (type==BoxSizeType.tiny) {
                return "生鲜微";
            }
        }
        else if (rackType == RackType.box_bw)
        {
            if (type==BoxSizeType.large) {
                return "保温大";
            }else if (type==BoxSizeType.medium) {
                return "保温中";
            }else if (type==BoxSizeType.small) {
                return "保温小";
            }else if (type==BoxSizeType.tiny) {
                return "保温微";
            }
        }
        return "未知";
    }


    private static List<BoxInfoType> getBoxByType(List<BoxInfoType> datas, int box_size)
    {
        ArrayList<BoxInfoType> list = new ArrayList<BoxInfoType>();
        for (BoxInfoType info : datas)
        {
            if (info.getBoxSize() == box_size) {
                //正常空闲箱门
                if (info.getBoxStatus()==BoxStateType.empty&&info.getBoxUserState()==BoxUserState.normal)
                {
                    list.add(info);
                }
            }
        }
        return list;
    }


    /**
     * 获取相同rack类型全部数据
     *
     * @param rackType 0:快递柜；1：生鲜柜；2：保温柜
     * @return
     */
    public static ArrayList<BoxInfoType> getBoxDataByRackType(int rackType) {

        ArrayList<BoxInfoType> boxs = new ArrayList<BoxInfoType>();

        ArrayList<BoxInfoType> data = getBoxData();

        for (BoxInfoType info : data) {

            if (info.getRackType().intValue() == rackType) {
                boxs.add(info);
            }
        }
        return boxs;
    }

    public static int getBoxCountBySize(int size){
        ArrayList<BoxInfoType> data = getBoxData();
        int count = 0;
        for (BoxInfoType info : data) {

            if (info.getBoxStatus() != BoxStateType.locked && info.getBoxUserState() == BoxUserState.normal) {
                if (info.getBoxSize()==size)
                {
                    count++;
                }
            }
        }
        return count;
    }

    private static ArrayList<BoxInfoType> getBoxData(){
        if (mBoxs == null)
        {
            //从数据库中读取箱门状态
            ArrayList<BoxInfoType> box;
            box = getAllBoxInfo(isCanUser);
            mBoxs = box;
            Log.d("box", "读取箱门信息:"+mBoxs.size());
        }
        return mBoxs;
    }


    /**
     * 获取一组箱门数据
     *
     * @param rackIndex
     * @return
     */
    public static ArrayList<BoxInfoType> getBoxDataByRack(int rackIndex) {
        rackIndex++;
        ArrayList<BoxInfoType> boxs = new ArrayList<BoxInfoType>();
        BoxInfo boxByCode;
        if (mBoxs == null) {
            //从数据库中读取箱门状态
            ArrayList<BoxInfoType> box;
            if (isCanUser) {
                box = getAllBoxInfo(true);
            } else {
                box = getAllBoxInfo(false);
            }
            mBoxs = box;
        }
        for (BoxInfoType type : mBoxs) {
            boxByCode = BoxUtils.getBoxByCode(type.getBoxCode());
            if (boxByCode.getBoardNum() == rackIndex) {
                boxs.add(type);
            }
        }
        return boxs;
    }


    /**
     * 从数据库中获取box信息
     *
     * @param canUseReserveBox 是否能使用预留的格子
     * @return
     */
    public static ArrayList<BoxInfoType> getAllBoxInfo(Boolean canUseReserveBox) {
        ArrayList<BoxInfo> boxList = BoxDynSyncOp.getAllSyncBoxList();
        ArrayList<BoxInfoType> boxs = new ArrayList<BoxInfoType>();
        for (BoxInfo box : boxList) {
            BoxInfoType infoType = new BoxInfoType();
            String boxId = BoxUtils.generateBoxCode(box.getBoardNum(),box.getBoxNum());
            infoType.setBoxCode(boxId);
            infoType.setBoxSize(box.getBoxSize());
            infoType.setDoorStatus(box.getDoorState());
            infoType.setBoxUserState(box.getBoxUserState());
            infoType.setBoxStatus(box.getBoxState());
            infoType.setRackType(box.getRackType());
//			if (box.getBoxState()!= BoxStateType.empty)
//			{
//				infoType.setBoxUserState(BoxUserState.Local_lock);
//			}
            //不是正常，都锁定
            if (box.getBoxUserState() != BoxUserState.normal) {
                infoType.setBoxStatus(BoxStateType.locked);
            }

            //处理预留的格子
            if (canUseReserveBox) {
                if (box.getBoxUserState() == BoxUserState.reserve) {
                    infoType.setBoxStatus(BoxStateType.empty);
                    Log.i(GlobalField.tag, "reserve unlocked ：" + infoType.getBoxCode());
                }
            }
            boxs.add(infoType);
        }

        return boxs;
    }


    /**
     * 升级需要将本地订单格口锁定
     */
    public static void syncLocalOrderToBox() {
        ArrayList<OrderLocalInfo> allOrderLocals = OrderLocalInfoOp.getAllOrderLocals();

        for (OrderLocalInfo order : allOrderLocals) {
            Integer order_state = order.getOrder_state();
            //所有未取走订单
            if (order_state == OrderLocalInfoTable.STATE_CREATE) {
                //锁定未取走订单箱门
                BoxDynSyncOp.boxLock(order.getBox_code());
                Log.i(GlobalField.tag, "lock order info " + order.toString());
            }
        }
    }


    //获取服务端所有订单
    public static void pullServiceOrder() {
        if (!SharePreferenceHelper.getSyncOrderState()) {
            //如果箱门未初始化不去下拉订单
            if (!GlobalField.deviceInit) {
                Log.e("box", "box info not write to sql");
                return;
            }
            updateServiceOrder();
        }
    }


    private static void updateServiceOrder() {
        FirstUpdateLocalOrderRequest request = new FirstUpdateLocalOrderRequest(new ResponseEventHandler<RspFirstUpdateLocalOrder>() {
            @Override
            public void onResponseSuccess(RspFirstUpdateLocalOrder result) {
                if (result != null && result.isSuccess()) {
                    LogUtil.i("update service order to local");
                    SharePreferenceHelper.saveSyncOrderState(true);
                    List<OrderInfo> data = result.getData();
                    for (OrderInfo order : data) {
                        OrderLocalInfoOp.addLocalOrder(order);
                    }

                    //锁定订单格口
                    BoxInfoHelper.syncLocalOrderToBox();
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                LogUtil.e(error.getMessage());
            }
        });
        RequestManager.addRequest(request, null);
    }

}

