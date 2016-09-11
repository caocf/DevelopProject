package com.moge.ebox.phone.config;

public class GlobalConfig {
	
	 /** The Constant INTENT_ACTION_FINISH. */
    public static final String INTENT_ACTION_FINISH = "com.moge.ebox.phone.intent.action.FINISH";
    /**update balance*/
    public static final String INTENT_ACTION_UPDATE = "com.moge.ebox.phone.intent.action.UPDATE";
    /** order time out */
    public static final String INTENT_ACTION_TIMEOUT_COUNT = "com.moge.ebox.phone.intent.action.TIMEOUT";

    /**
     * activity jump flag
     */
    
    public static final int INTENT_LOGIN = 0;
    public static final int INTENT_SCAND = 1;
    public static final int INTENT_REGISTER = 2;
    public static final int INTENT_TAKE_PIC_CAMEAR = 3;
    public static final int INTENT_TAKE_PIC_DICM = 5;
    public static final int INTENT_TAKE_PIC_SCALE = 6;
    public static final int INTENT_FORGET_PWD= 4;
    
    
    public static final int TAG_CHECK_VERSION= 0;
    public static final int TAG_ABOUT= 1;
    public static final int TAG_CONTACT= 2;
    public static final int TAG_FEED_BACK= 3;
    public static final int TAG_PERSIONAL_CENTER= 4;
    
    public static final String FLAG_TEL="tel";
    public static final String FLAG_PWD="pwd";

    public static final int LOGIC_SELECT_COMPANY=100;
    public static final int LOGIC_SELECT_ORIGINATION=101;

}
