package com.ebox.st.model;

import java.util.List;

public class Workflow {
	private Integer id;
	private String terminal_code;
	private List<WorkStep> workflow;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTerminal_code() {
		return terminal_code;
	}
	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}
	public List<WorkStep> getWorkflow() {
		return workflow;
	}
	public void setWorkflow(List<WorkStep> workflow) {
		this.workflow = workflow;
	}
}
