package com.ebox.ums.database.payRst;


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
public class TradingFinishOrderData 
{
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
    private String result="";
    private String mobile="";
    private String pay_id;
    private String pay_type;
    private String error_msg;
    private String noncestr;
    private String timestamp;
    private int state; //0:未同步 1:已同步
	
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
    
    
    
    public String getResult(){
    	return result;
    }
    public void setResult(String a){
    	this.result=a;
    }
    
    
    
    
    public void setMobile(String a){
    	this.mobile=a;
    }
    
    public String getMobile(){
    	return mobile;
    }
    
    public void setPayId(String a){
    	this.pay_id=a;
    }
    
    public String getPayId(){
    	return pay_id;
    }
    
    public String getPayType(){
    	return pay_type;
    }
    public void setPayType(String a){
    	this.pay_type=a;
    }

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
    
    
    
}