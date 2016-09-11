package com.ebox.ex.network.model.base.type;

import java.util.List;

/**
 * Created by Android on 2015/9/2.
 */
public class ReportTerminalStatus {

    private List<TimeOutOrderType> items;
    private List<BoxInfoType> boxes;
    private ReserveInfoType reserve;
    private List<Integer> terminal_update;


    public List<Integer> getTerminal_update() {
        return terminal_update;
    }

    public void setTerminal_update(List<Integer> terminal_update) {
        this.terminal_update = terminal_update;
    }

    public ReserveInfoType getReserve() {
        return reserve;
    }

    public void setReserve(ReserveInfoType reserve) {
        this.reserve = reserve;
    }

    public List<BoxInfoType> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<BoxInfoType> boxes) {
        this.boxes = boxes;
    }

    public List<TimeOutOrderType> getItems() {
        return items;
    }

    public void setItems(List<TimeOutOrderType> items) {
        this.items = items;
    }
}
