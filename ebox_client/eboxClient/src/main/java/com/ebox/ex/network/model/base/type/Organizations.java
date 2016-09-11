package com.ebox.ex.network.model.base.type;

import java.util.List;

public class Organizations {
    private List<OrganizationType> organizations;
    private String base_url;


    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }


    public List<OrganizationType> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationType> organizations) {
        this.organizations = organizations;
    }
}
