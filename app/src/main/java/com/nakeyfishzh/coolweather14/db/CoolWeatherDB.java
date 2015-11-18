package com.nakeyfishzh.coolweather14.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nakeyfishzh.coolweather14.model.City;
import com.nakeyfishzh.coolweather14.model.County;
import com.nakeyfishzh.coolweather14.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/11/17.
 * 封装常用数据库操作
 */
public class CoolWeatherDB
{
    // 数据库名
    public static final String DB_NAME = "cool_weather";

    // 数据库版本
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    // 将构造方法私有化
    private CoolWeatherDB(Context context)
    {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    // 获取CoolWeatherDB的实例
    public synchronized static CoolWeatherDB getInstance(Context context)
    {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    // 将 Province 实例存储到数据库
    public void saveProvince(Province province)
    {
        if (province != null)
        {
            String strSQL = "insert into Province(name, code) values ("
                    + "'" + province.getName() + "', "
                    + "'" + province.getCode() + "')";
            db.execSQL(strSQL);
        }
    }

    // 将 City 实例存储到数据库
    public void saveCity(City city)
    {
        if (city != null)
        {
            db.execSQL("insert into City(name, code, province_id) values ("
                    + "'" + city.getName() + "', '"
                    + city.getCode() + "', "
                    + city.getProvinceId()
                    + ")");
        }
    }

    // 将 City 实例存储到数据库
    public void saveCounty(County county)
    {
        if (county != null)
        {
            db.execSQL("insert into County(name, code, city_id) values ("
                    + "'" + county.getName() + "', '"
                    + county.getCode() + "', "
                    + county.getCityId()
                    + ")");
        }
    }

    // 从数据库读取中国所有省份信息
    public List<Province> loadProvinces()
    {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Province", null);
        if (cursor.moveToFirst())
        {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("code")));
                list.add(province);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    // 从数据库读取某省下所有城市信息
    public List<City> loadCities(int provinceId)
    {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from City where province_id = " + provinceId, null);
        if (cursor.moveToFirst())
        {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    // 从数据库读取某城市下所有县信息
    public List<County> loadCounties(int cityId)
    {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from County where city_id = " + cityId, null);
        if (cursor.moveToFirst())
        {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setName(cursor.getString(cursor.getColumnIndex("name")));
                county.setCode(cursor.getString(cursor.getColumnIndex("code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                list.add(county);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

}
