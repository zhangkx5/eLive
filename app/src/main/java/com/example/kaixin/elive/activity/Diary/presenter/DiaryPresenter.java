package com.example.kaixin.elive.activity.Diary.presenter;

import com.example.kaixin.elive.activity.Diary.model.DiaryModel;
import com.example.kaixin.elive.activity.Diary.model.IDiaryModel;
import com.example.kaixin.elive.activity.Diary.view.IDiaryView;

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
    public Boolean addDiary() {
        String strContent = diaryView.getStrContent();
        if (strContent.equals("")) {
            diaryView.showToast("亲，你还没写日记哦..");
            return false;
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
            return true;
        }
    }
}
