package com.ebox.ex.database.adv;

public class AdvertiseData{
	private Integer adver_id;
	private String content;
	private Integer content_type;
	private Integer type;
	private Integer state;
	private String audio_content;

	public Integer getAdver_id() {
		return adver_id;
	}
	public void setAdver_id(Integer adver_id) {
		this.adver_id = adver_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getContent_type() {
		return content_type;
	}
	public void setContent_type(Integer content_type) {
		this.content_type = content_type;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAudio_content() {
		return audio_content;
	}
	public void setAudio_content(String audio_content) {
		this.audio_content = audio_content;
	}
}
