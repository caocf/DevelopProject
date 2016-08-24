package com.xhl.bqlh.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**   
* @Description: 总订单类实例
* @author zhanglj
* @date 2015年8月24日 上午11:42:38 
* @version V1.0   
*/ 
public class BlanketOrder implements Serializable{

	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 总订单编码
	 */
	private String orderCode;
	
	/**
	 * 订单类型（1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
	 */
	private String orderType;
	
	/**
	 * 收货方编码
	 */
	private String receivingId;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderPrice;
	
	/**
	 * 应付金额
	 */
	private BigDecimal orderPayPrice;
	
	/**
	 * 下单时间
	 */
	private String orderTime;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 更新时间
	 */
	private String updateTime;
	
	/**
	 * 支付方式
	 */
	private String payType;
	
	/**
	 * 收货地址id
	 */
	private String addressId;
	
	// 是否分单（1：分单，2：未分单）
	private String splitOrNot;
	
	/**
	 * 返回 是否分单（1：分单，2：未分单）
	 * @return 是否分单（1：分单，2：未分单）
	 */
	
	public String getSplitOrNot() {
		return splitOrNot;
	}
	

	/**
	 * 设置 是否分单（1：分单，2：未分单）
	 * @param splitOrNot
	 *            是否分单（1：分单，2：未分单）
	 */
	
	public void setSplitOrNot(String splitOrNot) {
		this.splitOrNot = splitOrNot;
	}
	

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getOrderPayPrice() {
		return orderPayPrice;
	}

	public void setOrderPayPrice(BigDecimal orderPayPrice) {
		this.orderPayPrice = orderPayPrice;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getReceivingId() {
		return receivingId;
	}

	public void setReceivingId(String receivingId) {
		this.receivingId = receivingId;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
