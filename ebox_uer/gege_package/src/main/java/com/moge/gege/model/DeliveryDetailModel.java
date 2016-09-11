package com.moge.gege.model;


import java.util.List;

public class DeliveryDetailModel
{
    String name;
    String company;
    String number;
    String contact;
    String logo;
    int status;
    List<LogisticsModel> logistics;
    DeliveryBoxModel order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<LogisticsModel> getLogistics() {
        return logistics;
    }

    public void setLogistics(List<LogisticsModel> logistics) {
        this.logistics = logistics;
    }

    public DeliveryBoxModel getOrder() {
        return order;
    }

    public void setOrder(DeliveryBoxModel order) {
        this.order = order;
    }
}
