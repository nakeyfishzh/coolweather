package com.nakeyfishzh.coolweather14.model;

/**
 * Created by walker on 15/11/17.
 */
public class City
{
    private int id;
    private int provinceId;
    private String name;
    private String code;

    public int getId()
    {
        return id;
    }

    public int getProvinceId()
    {
        return provinceId;
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

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
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
