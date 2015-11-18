package com.nakeyfishzh.coolweather14.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nakeyfishzh.coolweather14.service.AutoUpdateService;

/**
 * Created by walker on 15/11/18.
 */
public class AutoUpdateReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent intent1 = new Intent(context, AutoUpdateService.class);
        context.startService(intent1);
    }
}
