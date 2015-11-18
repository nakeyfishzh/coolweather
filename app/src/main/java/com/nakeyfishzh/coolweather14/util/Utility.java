package com.nakeyfishzh.coolweather14.util;

import android.text.TextUtils;

import com.nakeyfishzh.coolweather14.db.CoolWeatherDB;
import com.nakeyfishzh.coolweather14.model.City;
import com.nakeyfishzh.coolweather14.model.County;
import com.nakeyfishzh.coolweather14.model.Province;

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

                    // 将解析出来的数据存储至City表
                    coolWeatherDB.saveCounty(county);
                }

                return true;
            }
        }

        return false;
    }
}
