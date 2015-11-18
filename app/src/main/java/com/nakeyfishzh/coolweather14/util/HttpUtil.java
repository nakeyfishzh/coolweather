package com.nakeyfishzh.coolweather14.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by walker on 15/11/9.
 */
public class HttpUtil
{
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener)
    {
//        if (true)
//        {
//            Toast.makeText(MyApplication.getContext(), "network is unavailable", Toast.LENGTH_SHORT).show();
//        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);    // 默认为true, 设置后可用conn.getInputStream().read()
                    //connection.setDoOutput(true); // 默认为false,设置后可用conn.getOutputStream().write()

                    // POST写法
                    //connection.setRequestMethod("POST");
                    //DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //out.writeBytes("username=admin&password=123456");

                    // 下面对获取到的输入流进行读取
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // 回调 onFinish 方法
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                }
                catch (Exception e)
                {
                    // 回调 onError 方法
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
                finally
                {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
