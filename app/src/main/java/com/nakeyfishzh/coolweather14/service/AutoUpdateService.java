package com.nakeyfishzh.coolweather14.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nakeyfishzh.coolweather14.util.HttpCallbackListener;
import com.nakeyfishzh.coolweather14.util.HttpUtil;
import com.nakeyfishzh.coolweather14.util.Utility;

/**
 * Created by walker on 15/11/18.
 */
public class AutoUpdateService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // 服务启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("AutoUpdateService", "updateWeather()");
                updateWeather();
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = sp.getString("weatherCode", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
        {
            @Override
            public void onFinish(String response)
            {
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e)
            {
                e.printStackTrace();
            }
        });
    }

}
