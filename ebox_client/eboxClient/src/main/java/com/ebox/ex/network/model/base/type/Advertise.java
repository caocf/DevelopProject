package com.ebox.ex.network.model.base.type;

public class Advertise {
    private Integer adver_id;
    private String content;
    private Integer state;
    private Integer class_id;
    private Integer content_type;
    private String audio_content;

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

    public Integer getAdv_id() {
        return adver_id;
    }

    public void setAdv_id(Integer adv_id) {
        this.adver_id = adv_id;
    }

    public String getAudio_content() {
        return audio_content;
    }

    public void setAudio_content(String audio_content) {
        this.audio_content = audio_content;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
