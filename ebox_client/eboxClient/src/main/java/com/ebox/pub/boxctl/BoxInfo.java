package com.ebox.pub.boxctl;

public class BoxInfo {
	private Integer boardNum;
	private Integer boxNum;
	private Integer doorState;//门的状态
	private Integer boxState;//Box的状态
	private Integer boxSize;//Box的大小
	private Integer boxUserState;//使用状态
	private Integer state;//更新状态
	private Integer rackType;
	public Integer getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(Integer boardNum){
		this.boardNum = boardNum;
	}
	public Integer getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}
	public Integer getDoorState() {
		return doorState;
	}
	public void setDoorState(Integer doorState) {
		this.doorState = doorState;
	}
	public Integer getBoxState() {
		return boxState;
	}
	public void setBoxState(Integer boxState) {
		this.boxState = boxState;
	}
	public Integer getBoxSize() {
		return boxSize;
	}
	public void setBoxSize(Integer boxSize) {
		this.boxSize = boxSize;
	}

	public Integer getBoxUserState() {
		return boxUserState;
	}
	public void setBoxUserState(Integer boxUserState) {
		this.boxUserState = boxUserState;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRackType() {
		return rackType;
	}

	public void setRackType(Integer rackType) {
		this.rackType = rackType;
	}
}
