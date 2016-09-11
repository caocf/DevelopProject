package com.ebox.ex.network.model.base.type;

public class OrganizationType {
    private Long orgnization_id;
    private String orgnization_name;
    private String telephone;
    private String slug;

    public Long getOrgnization_id() {
        return orgnization_id;
    }

    public void setOrgnization_id(Long orgnization_id) {
        this.orgnization_id = orgnization_id;
    }

    public String getOrgnization_name() {
        return orgnization_name;
    }

    public void setOrgnization_name(String orgnization_name) {
        this.orgnization_name = orgnization_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
