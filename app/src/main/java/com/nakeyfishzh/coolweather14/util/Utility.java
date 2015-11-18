package com.nakeyfishzh.coolweather14.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.nakeyfishzh.coolweather14.db.CoolWeatherDB;
import com.nakeyfishzh.coolweather14.model.City;
import com.nakeyfishzh.coolweather14.model.County;
import com.nakeyfishzh.coolweather14.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by walker on 15/11/18.
 */
public class Utility
{
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] all = response.split(",");
            if (all != null && all.length > 0)
            {
                for (String s : all)
                {
                    String[] array = s.split("\\|");
                    Province province = new Province();
                    province.setCode(array[0]);
                    province.setName(array[1]);

                    // 将解析出来的数据存储至Province表
                    coolWeatherDB.saveProvince(province);
                }

                return true;
            }
        }

        return false;
    }


    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] all = response.split(",");
            if (all != null && all.length > 0)
            {
                for (String s : all)
                {
                    String[] array = s.split("\\|");
                    City city = new City();
                    city.setCode(array[0]);
                    city.setName(array[1]);
                    city.setProvinceId(provinceId);

                    // 将解析出来的数据存储至City表
                    coolWeatherDB.saveCity(city);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] all = response.split(",");
            if (all != null && all.length > 0)
            {
                for (String s : all)
                {
                    String[] array = s.split("\\|");
                    County county = new County();
                    county.setCode(array[0]);
                    county.setName(array[1]);
                    county.setCityId(cityId);

                    // 将解析出来的数据存储至County表
                    coolWeatherDB.saveCounty(county);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharePreferences文件中
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                       String temp1, String temp2, String weatherDesp, String publishTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日");

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("citySelected", true);
        editor.putString("cityName", cityName);
        editor.putString("weatherCode", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weatherDesp", weatherDesp);
        editor.putString("publishTime", publishTime);
        editor.putString("currentDate", simpleDateFormat.format(new Date()));
        editor.commit();
    }
}
