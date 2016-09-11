package com.ebox.pub.boxctl;

public class RackInfo {
	private Integer boardNum;
	private Integer count;
	private BoxInfo boxes[];

	public Integer getBoardNum() {
		return boardNum;
	}

	public void setBoardNum(Integer boardNum) {
		this.boardNum = boardNum;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BoxInfo[] getBoxes() {
		return boxes;
	}

	public void setBoxes(BoxInfo[] boxes) {
		this.boxes = boxes;
	}
	
}
