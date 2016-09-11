package com.moge.gege.model.enums;

public interface PayType
{
//        '1':alipay_mobile.AliPay,
//        '2':BankPay,
//        '3':alipay_wap.AliPay,
//        '4':weixinpay_weixin.WeixinPay,
//        '5':weixinpay_mobile.WeixinPay,
//        '6':CashDeliverPay,
//         7余额支付wap 8 余额支付


    public static final int Ali_Pay = 1;
    public static final int Bank_Pay = 2;
    public static final int Ali_Wap_Pay = 3;
    public static final int Weixin_Wap_Pay = 4;
    public static final int Weixin_Pay = 5;
    public static final int Cod_Pay = 6;
    public static final int Balance_Wap_Pay = 7;
    public static final int Balance_Pay = 8;

    public static final int Card_Pay = 99;
}
