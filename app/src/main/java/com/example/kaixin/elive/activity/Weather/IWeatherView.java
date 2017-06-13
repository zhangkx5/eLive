package com.example.kaixin.elive.activity.Weather;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IWeatherView {
    void showWeather();
    void showAir();
    void showList();
    void showNextFiveDay();
    void setVisibility(String where, String timestr, String degr);
    int setWeatherImage(String gif);
}
