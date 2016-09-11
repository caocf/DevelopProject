package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/6/25.
 */
public class SubmitPopulationReq {
    private ArrayList<PopulationModel> population;

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    private String terminal_code;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public IdcardModel getIdcard() {
        return idcard;
    }

    public void setIdcard(IdcardModel idcard) {
        this.idcard = idcard;
    }

    private IdcardModel idcard;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String telephone;

    private String address;

    public ArrayList<PopulationModel> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<PopulationModel> population) {
        this.population = population;
    }
}
