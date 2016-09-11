package com.ebox.ex.database.operator;

public class OperatorInfo {

	private Long _id;
	private String operatorId;
    private String cardId;
	private String password;
	private String operatorName;
	private String telephone;
	private Long balance;
	private Integer operatorStatus;//用户的状态，判断是否被锁定
	private Integer reserveStatus;//判断是否可以使用预留格子
	private Integer state;//记录是否已经同步服务器状态
	public Long get_id() {
		return _id;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public String getPassword() {
		return password;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public String getTelephone() {
		return telephone;
	}
	public Long getBalance() {
		return balance;
	}
	public Integer getState() {
		return state;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getOperatorStatus() {
		return operatorStatus;
	}
	public void setOperatorStatus(Integer operatorStatus) {
		this.operatorStatus = operatorStatus;
	}
	public Integer getReserveStatus() {
		return reserveStatus;
	}
	public void setReserveStatus(Integer reserveStatus) {
		this.reserveStatus = reserveStatus;
	}
    public String getCardId(){return cardId;}
    public void setCardId(String cardId){this.cardId = cardId;}
	
}
