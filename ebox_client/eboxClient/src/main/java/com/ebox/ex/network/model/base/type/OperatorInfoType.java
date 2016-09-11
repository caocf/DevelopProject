package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class OperatorInfoType  implements Serializable{
	private static final long serialVersionUID = 6790006590866136314L;
	private Long id;
	private String operator_id; 
	private String operator_name; 
	private OrganizationType orgnization_info;
	private String telephone;
	private Long balance;
	private Integer reserve_status;
	private Integer status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public String getOperatorId() {
		return operator_id;
	}
	public void setOperatorId(String operator_id) {
		this.operator_id = operator_id;
	}
	public String getOperatorName() {
		return operator_name;
	}
	public void setOperatorName(String operator_name) {
		this.operator_name = operator_name;
	}
	public OrganizationType getOrgnizationInfo() {
		return orgnization_info;
	}
	public void setOrgnizationInfo(OrganizationType orgnization_info) {
		this.orgnization_info = orgnization_info;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Long getBalance() {
		return balance;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	public Integer getReserveStatus() {
		return reserve_status;
	}
	public void setReserveStatus(Integer reserve_status) {
		this.reserve_status = reserve_status;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
