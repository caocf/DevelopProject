package com.ebox.ex.utils;

import com.ebox.ex.network.model.base.type.TerminalActivity;
import com.ebox.pub.service.global.GlobalField;

import java.util.List;

public class ActiveUtil {

    //快递柜活动类型
    public static int ACTIVITY_DELIVER=1;	//快递员活动
    public static int ACTIVITY_PICKUP=2;	//用户取件活动

    /**用户活动*/
    public static TerminalActivity getUserActive()
    {
        return getActive(ACTIVITY_PICKUP);
    }
    /**快递员活动*/
    public static TerminalActivity getOperatorActive()
    {
        return getActive(ACTIVITY_DELIVER);
    }


    private static TerminalActivity getActive(int mType)
    {
        if ( GlobalField.serverConfig == null || GlobalField.serverConfig.getActivity() == null)
        {
            return null;
        }
        List<TerminalActivity> activitys = GlobalField.serverConfig.getActivity();

        if (activitys != null && activitys.size() > 0)
        {
            for (TerminalActivity activity : activitys) {

                int type = activity.getType();

                if (mType == type)
                {
                    return activity;
                }
            }
        }
        return null;
    }

}
