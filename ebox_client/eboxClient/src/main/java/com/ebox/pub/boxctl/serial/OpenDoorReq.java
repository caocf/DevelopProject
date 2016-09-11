package com.ebox.pub.boxctl.serial;

public class OpenDoorReq {
	private int boardId;
	private int boxId;

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public int getBoxId() {
		return boxId;
	}
	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}
	
}
