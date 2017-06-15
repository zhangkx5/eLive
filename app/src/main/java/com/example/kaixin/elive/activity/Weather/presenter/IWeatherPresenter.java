package com.example.kaixin.elive.activity.Weather.presenter;

import java.util.ArrayList;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IWeatherPresenter {
    ArrayList<String> getResponse();
    void postRequest();
    int setImage(String gif);
}
