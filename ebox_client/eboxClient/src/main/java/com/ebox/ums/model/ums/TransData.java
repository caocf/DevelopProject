package com.ebox.ums.model.ums;

import java.io.Serializable;


 /**
 * 交易信息
 */
public class TransData implements Serializable
{
	private static final long serialVersionUID = 4760908089194766266L;
	private String appType;
	private String tranType;
    private String apendcode;
    private String amount;
    private int tradeType;//0:水费;1：燃气 费;2：电费 3：手机充值;5：查询余额;6：信用卡还款;7:消费
    private String content="";
	private String phone="";
	private String payId;
	private int pay_type;
	private String noncestr;
	private String timestamp;
	
    
    
    public String getApendCode(){
    	return apendcode;
    }
    
    public int getTradeType(){
    	return tradeType;
    }
    public String getAmount(){
    	return amount;
    }
    
    
    public void setApendCode(String a){
    	this.apendcode=a;
    }
    
    public void setTradeType(int a){
    	this.tradeType=a;
    }
    
    public void setAmount(String a){
    	this.amount=a;
    }
    
    public String getContent() {
		return content;
	}
	public void setcontent(String boxId) {
		this.content = boxId;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String boxId) {
		this.phone = boxId;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}