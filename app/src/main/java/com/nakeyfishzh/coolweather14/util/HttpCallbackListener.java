package com.nakeyfishzh.coolweather14.util;

/**
 * Created by walker on 15/11/9.
 */
public interface HttpCallbackListener
{
    void onFinish(String response);
    void onError(Exception e);
}
