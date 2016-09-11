package com.ebox.ex.utils;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.RspLoginOperator;
import com.ebox.ex.network.model.base.type.ExpressCharge;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.OperatorReserveStatus;
import com.ebox.ex.network.model.req.ReqVerifyOperator;
import com.ebox.ex.network.request.LoginRequest;
import com.ebox.pub.service.global.GlobalField;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/7/4.
 */
public class OperatorHelper {

    public static String mPhone;
    public static boolean mHasTimeoutOrder;
    private static String mPwd;

    //登陆后缓存的Cookie，推出后需要清理掉
    public static String cacheCookie = null;

    public interface  LoginResponseListener{
        void success();
        void failed();
    }

    public static void clear(){
        mPwd=null;
        mPhone=null;
        cacheCookie=null;
        mHasTimeoutOrder=false;
    }

    public static void silenceLogin(final LoginResponseListener l) {

        if (mPhone==null||mPwd==null)
        {
            if (l != null) {
                l.failed();
            }
            return;
        }
        ReqVerifyOperator req = new ReqVerifyOperator();
        req.setOperatorId(mPhone);
        req.setPassword(mPwd);

        LoginRequest request = new LoginRequest(req, new ResponseEventHandler<RspLoginOperator>() {
            @Override
            public void onResponseSuccess(RspLoginOperator result) {
                if (result.isSuccess())
                {
                    if (l != null) {
                        l.success();
                    }
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                if (l != null) {
                    l.failed();
                }
            }
        });

        RequestManager.addRequest(request, null);
    }


    public static boolean isAccount() {

        if (GlobalField.serverConfig == null) {
            return true;
        }
        Integer isAccount = GlobalField.serverConfig.getIsAccount();


        if (isAccount != null && isAccount == AcountType.is_acount) {
            return true;
        } else {
            return false;
        }

    }


    public static void Login(String phone, String pwd, ResponseEventHandler<RspLoginOperator> listener)
    {
        ReqVerifyOperator req=new ReqVerifyOperator();
        req.setOperatorId(phone);
        //c4ca4238a0b923820dcc509a6f75849b
        req.setPassword(pwd);

        //临时保存登陆信息
        mPhone=phone;
        mPwd=pwd;
        LoginRequest request=new LoginRequest(req,listener);

        RequestManager.addRequest(request,listener);
    }


    /*
     **查阅逾期件
     * @param operatorId
     * @return true:有超时件，false：没有超时件
      */
    public static boolean checkTimeOut(String operatorId) {
        if (mHasTimeoutOrder)
        {
            return true;
        }
        ArrayList<OrderLocalInfo> infos = OrderLocalInfoOp.getAllTimeOutOrderByOperator(operatorId);

        if (infos != null && infos.size() > 0) {
            mHasTimeoutOrder=true;
            return true;
        } else {
            return false;
        }
    }

    /*
     * 查阅快递员余额
     * @param operatorId
     */
    public static String getOperatorBalance2St(String OperatorId)
    {
        // 计算快递员余额
        Long balance =0L;
        OperatorInfo mOperatorInfo = OperatorOp.getOperatorById(OperatorId);

        if (mOperatorInfo!=null)
        {
            balance = mOperatorInfo.getBalance();
        }
        String re;
        if(balance < 0) {
            balance = Math.abs(balance);
            re =balance/100+"."+String.format("%02d",balance%100);
        } else {
            re =balance/100+"."+String.format("%02d",balance%100);
        }
        return re;
    }
    public static Long getOperatorBalance(String OperatorId)
    {
        // 计算快递员余额
        Long balance =0L;
        OperatorInfo mOperatorInfo = OperatorOp.getOperatorById(OperatorId);

        if (mOperatorInfo!=null)
        {
            balance = mOperatorInfo.getBalance();
        }
        return balance;
    }

    /*
    ** 获取格子计费金额
     */

    public static ArrayList<ExpressCharge> getExpressChargeByServer()
    {
        ArrayList<ExpressCharge> charge = new ArrayList<ExpressCharge>();

        if (GlobalField.serverConfig!=null&&GlobalField.serverConfig.getExpressCharge()!=null)
        {
            for (int i = 0; i<GlobalField.serverConfig.getExpressCharge().size();i++)
            {
                charge.add(GlobalField.serverConfig.getExpressCharge().get(i));
            }
        }
        else
        {
            // 微
            Long tiny_fee1 = 30L;
            ExpressCharge tiny_fee = new ExpressCharge();
            tiny_fee.setBoxSize(BoxSizeType.tiny);
            tiny_fee.setFee(tiny_fee1);
            // 小
            Long small_fee = 40L;
            ExpressCharge charge_small = new ExpressCharge();
            charge_small.setBoxSize(BoxSizeType.small);
            charge_small.setFee(small_fee);
            // 中
            Long medium_fee = 50L;
            ExpressCharge charge_medium = new ExpressCharge();
            charge_medium.setBoxSize(BoxSizeType.medium);
            charge_medium.setFee(medium_fee);
            // 大
            Long large_fee = 60L;
            ExpressCharge charge_large = new ExpressCharge();
            charge_large.setBoxSize(BoxSizeType.large);
            charge_large.setFee(large_fee);

            charge.add(charge_small);
            charge.add(charge_medium);
            charge.add(charge_large);
            charge.add(tiny_fee);
        }
        return charge;
    }

    /*
    ** 判断是否能使用预留格子
     */

    public static boolean canUseReserveBox(String operatorId)
    {
        Boolean canUse = false;
        OperatorInfo mOperatorInfo = OperatorOp.getOperatorById(operatorId);
        if (mOperatorInfo.getReserveStatus() == OperatorReserveStatus.reserveBox)
        {
            canUse = true;
        }

        return canUse;
    }
}
