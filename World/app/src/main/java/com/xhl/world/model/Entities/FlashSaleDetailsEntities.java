package com.xhl.world.model.Entities;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/14.
 */
public class FlashSaleDetailsEntities implements Serializable {

    private int sale_state;//抢购状态

    private String query_condition;//抢购查询条件

    private BaseTargetEntities base_entities;

    private String show_time;

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }


    public String getQuery_condition() {
        return query_condition;
    }

    public void setQuery_condition(String query_condition) {
        this.query_condition = query_condition;
    }

    public int getSale_state() {
        return sale_state;
    }

    public void setSale_state(int sale_state) {
        this.sale_state = sale_state;
    }

    public BaseTargetEntities getBase_entities() {
        return base_entities;
    }

    public void setBase_entities(BaseTargetEntities base_entities) {
        this.base_entities = base_entities;
    }
}
