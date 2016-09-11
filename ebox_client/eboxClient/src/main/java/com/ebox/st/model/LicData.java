package com.ebox.st.model;


public class LicData {
	private String type;
	private LnydCommit lnyd;
	private JhsyCommit jhsy;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LnydCommit getLnyd() {
		return lnyd;
	}

	public void setLnyd(LnydCommit lnyd) {
		this.lnyd = lnyd;
	}

	public JhsyCommit getJhsy() {
		return jhsy;
	}

	public void setJhsy(JhsyCommit jhsy) {
		this.jhsy = jhsy;
	}
	public void clearData(){
		lnyd = null;
		jhsy = null;
	}
}
