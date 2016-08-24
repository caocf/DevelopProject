package com.xhl.world.ui.event;

/**
 * Created by Sum on 15/12/3.
 */
public class GlobalEvent<T> {

    private int eventType;//更具类型判断
    private T object;//传递的数据对象

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
