package com.ebox.st.model;

import java.util.List;

public class WorkStep {
	private Integer id;
	private List<WorkFields> fields;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<WorkFields> getFields() {
		return fields;
	}
	public void setFields(List<WorkFields> fields) {
		this.fields = fields;
	}
}
