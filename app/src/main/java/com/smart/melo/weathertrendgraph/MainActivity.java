package com.smart.melo.weathertrendgraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WeatherTrendView chart1;
    private WeatherTrendView chart2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView()
    {
        chart1= (WeatherTrendView) findViewById(R.id.weather_char1);
        chart2= (WeatherTrendView) findViewById(R.id.weather_char2);
        initData();
    }
    private void initData()
    {
        ArrayList<WeatherItem> list= new ArrayList<WeatherItem>();
        list.add(new WeatherItem("",18));
        list.add(new WeatherItem("",40));
        list.add(new WeatherItem("", -1));
        list.add(new WeatherItem("",1));
        list.add(new WeatherItem("",6));
        list.add(new WeatherItem("",2));
        list.add(new WeatherItem("", 33));
        chart1.SetTuView(list, "最高温度：");//单位: 摄氏度
        ArrayList<WeatherItem> list1= new ArrayList<WeatherItem>();
        list1.add(new WeatherItem("7.10",1));
        list1.add(new WeatherItem("7.11",15));
        list1.add(new WeatherItem("7.12", -6));
        list1.add(new WeatherItem("7.13",-2));
        list1.add(new WeatherItem("7.14", 3));
        list1.add(new WeatherItem("7.15",-1));
        list1.add(new WeatherItem("7.16",11));
        chart2.SetTuView(list1, "最低温度：");
    }

}
