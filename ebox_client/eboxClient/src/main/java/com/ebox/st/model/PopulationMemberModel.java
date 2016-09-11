package com.ebox.st.model;

import java.io.Serializable;

public class PopulationMemberModel implements Serializable {
	private Boolean isAdd;
	private PopulationModel population;
	public Boolean getIsAdd() {
		return isAdd;
	}
	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
	}
	public PopulationModel getPopulation() {
		return population;
	}
	public void setPopulation(PopulationModel population) {
		this.population = population;
	}
}
