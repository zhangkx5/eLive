package com.example.kaixin.elive.activity.Diary;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IDiaryView {
    void showToast(String message);
    String getStrDate();
    String getStrCity();
    String getStrWeather();
    String getStrContent();
    Boolean getUpdate();
}
