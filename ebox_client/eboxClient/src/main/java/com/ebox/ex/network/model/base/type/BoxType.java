package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

/**
 * Created by Android on 2015/9/2.
 */
public class BoxType implements Serializable{

    private String code;
    private String name;
    private Integer id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
