package com.example.kaixin.elive.bean;

/**
 * Created by baoanj on 2017/5/18.
 */

public class WeatherBean {
    private String date;
    private String weather;
    private String temperature;
    private int img;

    public WeatherBean(String date, String temperature, int img, String weather) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.img = img;
    }

    public String getDate() {
        return date;
    }
    public String getWeather() {
        return weather;
    }
    public String getTemperature() {
        return temperature;
    }
    public int getImg() {
        return img;
    }
}
