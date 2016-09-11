package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class ReserveBoxInfo implements Serializable {

    private static final long serialVersionUID = -8976715323241636942L;
    private Integer box_size;
    private Integer number;

    public Integer getBoxSize() {
        return box_size;
    }

    public void setBoxSize(Integer box_size) {
        this.box_size = box_size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


}
