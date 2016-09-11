package com.moge.gege.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sam on 2014/12/2.
 */
public class AppServiceReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (AppService.instance() == null)
        {
            context.startService(new Intent(context, AppService.class));
        }

//        if (intent.getAction().equals(GlobalConfig.BROADCAST_ACTION_DESTROY_SERVICE))
//        {
//            if (AppService.instance() == null)
//            {
//                context.startService(new Intent(context, AppService.class));
//            }
//        }

    }

}
