package com.ebox.yc.model;

public class Content {
	private Long contentId;
	private String title;
	private String createDate;
	public Long getConentId() {
		return contentId;
	}
	public void setConentId(Long conentId) {
		this.contentId = conentId;
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
}
