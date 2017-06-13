package com.example.kaixin.elive.activity.Diary;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IDiaryModel {
    void openDiaryFile(String filename,String strContent);
    void addDiaryDB(String strDate, String strCity, String strWeather, String strContent);
}
