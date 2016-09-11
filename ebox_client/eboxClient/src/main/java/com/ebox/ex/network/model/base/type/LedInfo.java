package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class LedInfo implements Serializable{
	private static final long serialVersionUID = -3254845460100636168L;
	private Integer startx;
	private Integer starty;
	private Integer endx;
	private Integer endy;
	private String text;
	private Integer in;
	private Integer out;
	private Integer speed;
	private Integer showTime;
	public Integer getStartx() {
		return startx;
	}
	public void setStartx(Integer startx) {
		this.startx = startx;
	}
	public Integer getStarty() {
		return starty;
	}
	public void setStarty(Integer starty) {
		this.starty = starty;
	}
	public Integer getEndx() {
		return endx;
	}
	public void setEndx(Integer endx) {
		this.endx = endx;
	}
	public Integer getEndy() {
		return endy;
	}
	public void setEndy(Integer endy) {
		this.endy = endy;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getIn() {
		return in;
	}
	public void setIn(Integer in) {
		this.in = in;
	}
	public Integer getOut() {
		return out;
	}
	public void setOut(Integer out) {
		this.out = out;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getShowTime() {
		return showTime;
	}
	public void setShowTime(Integer showTime) {
		this.showTime = showTime;
	}

}
