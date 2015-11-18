package com.nakeyfishzh.coolweather14.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nakeyfishzh.coolweather14.R;
import com.nakeyfishzh.coolweather14.util.HttpCallbackListener;
import com.nakeyfishzh.coolweather14.util.HttpUtil;
import com.nakeyfishzh.coolweather14.util.Utility;

/**
 * Created by walker on 15/11/18.
 */
public class WeatherActivity extends Activity implements View.OnClickListener
{
    private LinearLayout layout_weather_info;
    private TextView textView_city_name;
    private TextView textView_publish_text;
    private TextView textView_current_date;
    private TextView textView_weather_desp;
    private TextView textView_temp1;
    private TextView textView_temp2;
    private Button button_switch_city;
    private Button button_refresh_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        // 初始化控件
        layout_weather_info = (LinearLayout) findViewById(R.id.layout_weather_info);
        textView_city_name = (TextView) findViewById(R.id.textView_city_name);
        textView_publish_text = (TextView) findViewById(R.id.textView_publish_text);
        textView_weather_desp = (TextView) findViewById(R.id.textView_weather_desp);
        textView_temp1 = (TextView) findViewById(R.id.textView_temp1);
        textView_temp2 = (TextView) findViewById(R.id.textView_temp2);
        textView_current_date = (TextView) findViewById(R.id.textView_current_date);

        button_switch_city = (Button) findViewById(R.id.button_switch_city);
        button_refresh_weather = (Button) findViewById(R.id.button_refresh_weather);
        button_switch_city.setOnClickListener(this);
        button_refresh_weather.setOnClickListener(this);

        String countyCode = getIntent().getStringExtra("countyCode");
        if (!TextUtils.isEmpty(countyCode))
        {
            // 有县级代号时就去查询天气
            textView_publish_text.setText("同步中...");
            layout_weather_info.setVisibility(View.INVISIBLE);
            textView_city_name.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            // 没有就直接显示本地天气
            showWeather();
        }
    }

    /**
     * 查询县级代号所对应的天气代码
     */
    private void queryWeatherCode(String countyCode)
    {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryWeatherFromServer(address, "countyCode");
    }

    /**
     * 查询天气代码所对应的天气
     */
    private void queryWeatherInfo(String weatherCode)
    {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryWeatherFromServer(address, "weatherCode");
    }

    /**
     * 根据地址和类型去向服务器查询天气代号或天气信息
     */
    private void queryWeatherFromServer(final String address, final String type)
    {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
        {
            @Override
            public void onFinish(String response)
            {
                if ("countyCode".equals(type))
                {
                    if (!TextUtils.isEmpty(response))
                    {
                        // 从服务器返回的数据中解析出天气代码
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2)
                        {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type))
                {
                    // 处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        textView_publish_text.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示在界面上
     */
    private void showWeather()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        textView_city_name.setText(sp.getString("cityName", ""));
        textView_temp1.setText(sp.getString("temp1", ""));
        textView_temp2.setText(sp.getString("temp2", ""));
        textView_weather_desp.setText(sp.getString("weatherDesp", ""));
        textView_publish_text.setText("今天"+sp.getString("publishTime", "")+"发布");
        textView_current_date.setText(sp.getString("currentDate", ""));

        layout_weather_info.setVisibility(View.VISIBLE);
        textView_city_name.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("FromWeatherActivity", true);
                startActivity(intent);
                finish();
                break;

            case R.id.button_refresh_weather:
                textView_publish_text.setText("同步中...");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = sp.getString("weatherCode", "");
                if (!TextUtils.isEmpty(weatherCode))
                {
                    queryWeatherInfo(weatherCode);
                }
                break;

            default:
                break;
        }
    }
}
