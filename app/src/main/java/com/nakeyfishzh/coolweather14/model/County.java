package com.nakeyfishzh.coolweather14.model;

/**
 * Created by walker on 15/11/17.
 */
public class County
{
    private int id;
    private int cityId;
    private String name;
    private String code;

    public int getId()
    {
        return id;
    }

    public int getCityId()
    {
        return cityId;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
