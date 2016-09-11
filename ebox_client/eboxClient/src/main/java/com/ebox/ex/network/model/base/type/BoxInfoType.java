package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class BoxInfoType implements Serializable {
    private static final long serialVersionUID = -9126590145994741064L;
    private String  box_code;
    private Integer type;
    private Integer box_state;
    private Integer box_user_state;
    private Integer door_state;
    private Integer rack_type;
    private Integer id;

    public Integer getRackType() {
        return rack_type;
    }

    public void setRackType(Integer rack_type) {
        this.rack_type = rack_type;
    }

    public String getBoxCode() {
        return box_code;
    }

    public void setBoxCode(String box_id) {
        this.box_code = box_id;
    }

    public Integer getBoxSize() {
        return type;
    }

    public void setBoxSize(Integer box_size) {
        this.type = box_size;
    }

    public Integer getBoxStatus() {
        return box_state;
    }

    public void setBoxStatus(Integer box_status) {
        this.box_state = box_status;
    }

    public Integer getDoorStatus() {
        return door_state;
    }

    public void setDoorStatus(Integer door_status) {
        this.door_state = door_status;
    }

    public Integer getBoxUserState() {
        return box_user_state;
    }

    public void setBoxUserState(Integer box_user_state) {
        this.box_user_state = box_user_state;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
