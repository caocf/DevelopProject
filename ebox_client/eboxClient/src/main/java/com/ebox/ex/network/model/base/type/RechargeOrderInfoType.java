package com.ebox.ex.network.model.base.type;

public class RechargeOrderInfoType {

	/*"pay_type": 9,
        "total_num": 1,
        "order_info": {
            "seller_email": "gegekuaidi%40mgooo.cn",
            "body": "201509181606562902825216",
            "_input_charset": "utf-8",
            "sign": "VkpzpzBtoM4yQ43qeUWj3aNJreLPQ+wJIywfLGGmGq8hbG7CxyP05lDgKthRhvyWsQoBiTOxnWq5hEMHXya3PXBQH1pnMBG4lpXo2SKiolVKMCqDDsC/SdX4/OKiDnBQwzXm/z9Hwiu5akFRiBJSMAvyWfhfEhNzC0+g/icGkDw=",
            "notify_url": "http%3A%2F%2Fpay.dev.aimoge.com%2Fv1%2Fpay%2Falipay%2Fnotify%2Fdirect",
            "partner": "2088911291851397",
            "subject": "201509181606562902825216",
            "service": "create_direct_pay_by_user",
            "qr_pay_mode": 1,
            "it_b_pay": "30m",
            "out_trade_no": "201509181606562902825216",
            "params": "https://mapi.alipay.com/gateway.do?_input_charset=utf-8&body=201509181606562902825216&it_b_pay=30m&notify_url=http%3A%2F%2Fpay.dev.aimoge.com%2Fv1%2Fpay%2Falipay%2Fnotify%2Fdirect&out_trade_no=201509181606562902825216&partner=2088911291851397&payment_type=1&qr_pay_mode=1&return_url=http%3A%2F%2Fm.dev.aimoge.com%2Ftrading%2Fpay%2Falipay%2F201509181606562902825216%2FL3RyYWRpbmcvb3JkZXIvMjAxNTA5MTgxMDM4MDE1NDgxNDEyMzc2%3D%3D&seller_email=gegekuaidi%40mgooo.cn&service=create_direct_pay_by_user&subject=201509181606562902825216&total_fee=0.01&sign=VkpzpzBtoM4yQ43qeUWj3aNJreLPQ%2BwJIywfLGGmGq8hbG7CxyP05lDgKthRhvyWsQoBiTOxnWq5hEMHXya3PXBQH1pnMBG4lpXo2SKiolVKMCqDDsC%2FSdX4%2FOKiDnBQwzXm%2Fz9Hwiu5akFRiBJSMAvyWfhfEhNzC0%2Bg%2FicGkDw%3D&sign_type=RSA",
            "payment_type": 1,
            "total_fee": 0.01,
            "sign_type": "RSA",
            "return_url": "http%3A%2F%2Fm.dev.aimoge.com%2Ftrading%2Fpay%2Falipay%2F201509181606562902825216%2FL3RyYWRpbmcvb3JkZXIvMjAxNTA5MTgxMDM4MDE1NDgxNDEyMzc2%3D%3D"
        },
        "total_fee": 1,
        "order_ids": [
            "00000000492"
        ],
        "pay_id": "201509181606562902825216"
*/

	public String seller_email;
	public String body;
	public String _input_charset;
	public String sign;
	public String notify_url;
	public String partner;
	public String subject;
	public String service;
	public String it_b_pay;
	public String out_trade_no;
	public String params;
    public String code_url;
	public int payment_type;
	public String total_fee;
	public String sign_type;
	public String return_url;
	public int qr_pay_mode;



}
