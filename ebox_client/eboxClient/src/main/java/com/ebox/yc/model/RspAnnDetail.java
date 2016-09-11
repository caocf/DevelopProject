package com.ebox.yc.model;


public class RspAnnDetail {
	int  result;
	String resultMsg;
	Long id;
	String title;
	String createDate;
	String htmlDetail;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getHtmlDetail() {
		return htmlDetail;
	}
	public void setHtmlDetail(String htmlDetail) {
		this.htmlDetail = htmlDetail;
	}
	
}
