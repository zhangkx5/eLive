package com.example.kaixin.elive.activity.Diary;

import android.view.View;

/**
 * Created by kaixin on 2017/6/13.
 */

public class DiaryPresenter implements IDiaryPresenter{
    private IDiaryModel diaryModel;
    private IDiaryView diaryView;

    public DiaryPresenter(IDiaryView diaryView) {
        this.diaryView = diaryView;
        diaryModel = new DiaryModel();
    }
    @Override
    public void addDiary() {
        String strContent = diaryView.getStrContent();
        if (strContent.equals("")) {
            diaryView.showToast("亲，你还没写日记哦..");
        } else {
            String strDate = diaryView.getStrDate();
            String filename = "Diary_"+strDate;
            diaryModel.openDiaryFile(filename, strContent);
            boolean Update = diaryView.getUpdate();
            if (!Update) {
                String strCity = diaryView.getStrCity();
                String strWeather = diaryView.getStrWeather();
                diaryModel.addDiaryDB(filename, strDate, strCity, strWeather);
            }
        }
    }
}
