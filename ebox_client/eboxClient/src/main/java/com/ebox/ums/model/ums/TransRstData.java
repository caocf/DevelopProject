package com.ebox.ums.model.ums;


import java.io.Serializable;


 /**
 * 交易返回信息
 * *RspCode ;	获取返回码2位
 *RspChin;	获取返回中文提示40byte，最长20个中文
 *Bankcode;	获取银行行号4位
 *CardNo;	获取卡号20位
 *Exp;	获取有效期4位
 *Trace;	获取流水号6
 *Batch;	获取批次号6
 *Amount;	获取金额12
 *MchtId;	获取商户号15
 *TermId;	获取终端号8
 *Reference;	获取系统参考号12
 *TransDate;	获取交易日期4
 *TransTime;	获取交易时间6
 *AuthNo;	获取授权号6
 *SettleDate;	获取结算日期4
 *AppendField;	获取返回的附加域内容参考下面说明，后续
 *PrintInfo;	获取返回的补充打印信息格式如下：待后续补充
 * 
 */
public class TransRstData implements Serializable
{
	private static final long serialVersionUID = 4456908089194766266L;
	private String RstCode=""; 
	private String RspChin="";
	private String Bankcode="";
    private String cardNo="";
    private String Exp="";
    private String Trace="";
    private String Batch="";
    private String Amount="";
    private String MchtId="";
    private String TermId="";
    private String Reference="";
    private String TransDate="";
    private String TransTime="";
    private String AuthNo="";
    private String SettleDate="";
    private String AppendField="";
    private String PrintInfo="";
    private String error_msg="";
	private int initCard = 0;
	private int trade_type=0;
	private String noncestr="";
	private String timestamp="";
	
    public String getRstCode(){
    	return RstCode;
    }
    
    public void setRstCode(String a){
    	this.RstCode=a;
    }
    
    public String getRspChin(){
    	return RspChin;
    }
    
    public void setRspChin(String a){
    	this.RspChin=a;
    }
    
    public String getBankcode(){
    	return Bankcode;
    }
    
    public void setBankcode(String a){
    	this.Bankcode=a;
    }
    
    public String getTrace(){
    	return Trace;
    }
    
    public void setTrace(String a){
    	this.Trace=a;
    }
    
    public String getCardNo(){
    	return cardNo;
    }
    
    public void setCardNo(String a){
    	this.cardNo=a;
    }
    
    
    public String getTermId(){
    	return TermId;
    }
    
    public void setTermId(String a){
    	this.TermId=a;
    }
    
    public String getReference(){
    	return Reference;
    }
    
    public void setReference(String a){
    	this.Reference=a;
    }
    
    public String getTransDate(){
    	return TransDate;
    }
    
    public void setTransDate(String a){
    	this.TransDate=a;
    }
    
    public String getExp(){
    	return Exp;
    }
    
    public void setExp(String a){
    	this.Exp=a;
    }
    public String getAmount(){
    	return Amount;
    }
    
    public void setAmount(String a){
    	this.Amount=a;
    }
    
    public String getMchtId(){
    	return MchtId;
    }
    
    public void setMchtId(String a){
    	this.MchtId=a;
    }
    
    public String getBatch(){
    	return Batch;
    }
    
    public void setBatch(String a){
    	this.Batch=a;
    }
    
    public String getTransTime(){
    	return TransTime;
    }
    
    public void setTransTime(String a){
    	this.TransTime=a;
    }
    
    
    
    
    public void setAuthNo(String a){
    	this.AuthNo=a;
    }
    
    public String getAuthNo(){
    	return AuthNo;
    }
    
    public void setSettleDate(String a){
    	this.SettleDate=a;
    }
    
    public String getSettleDate(){
    	return SettleDate;
    }
    
    public void setAppendField(String a){
    	this.AppendField=a;
    }
    
    public String getAppendField(){
    	return AppendField;
    }
    
    public String getPrintInfo(){
    	return PrintInfo;
    }
    public void setPrintInfo(String a){
    	this.PrintInfo=a;
    }
    
    public String getErrorMsg(){
    	return error_msg;
    }
    public void setErrorMsg(String a){
    	this.error_msg=a;
    }
    
    public int getInitCard(){
    	return initCard;
    }
    
    
    public void setInitCard(int result){
    	this.initCard=result;
    }
    public int getTradeType(){
    	return trade_type;
    }
    
    public void setTradeType(int a){
    	this.trade_type=a;
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