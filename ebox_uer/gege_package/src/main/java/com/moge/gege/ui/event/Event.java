package com.moge.gege.ui.event;

/**
 * Created by dev on 15/3/26.
 */
public class Event {

    public static class PayEvent{

        private int eventType;
        private int result;
        private Object params;

        public PayEvent(int event, int result, Object params) {
            this.eventType = event;
            this.result = result;
            this.params = params;
        }

        public int getEventType() {
            return eventType;
        }

        public int getResult() {
            return result;
        }

        public Object getParams() {
            return params;
        }

    }

    public static class RefreshEvent{

        private int refreshIndex;

        public RefreshEvent(int index)
        {
            this.refreshIndex = index;
        }

        public int getRefreshIndex()
        {
            return this.refreshIndex;
        }
    }

    public static class PointEvent{

    }

    public static class BalanceEvent{

    }

    public static class DeliverySelectEvent{

        private String name;
        private String type;

        public DeliverySelectEvent(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    public static class LoginEvent{

    }
}
