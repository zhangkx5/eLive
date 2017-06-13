package com.example.kaixin.elive.activity.Diary;

import android.database.sqlite.SQLiteDatabase;

import com.example.kaixin.elive.Utils.MyDB;

import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kaixin on 2017/6/13.
 */

public class DiaryModel implements IDiaryModel{

    private static final String DATABASE_NAME = "myApp.db";
    private static final String SQL_INSERT = "insert into diary (filename, time, city, weather) values (?, ?, ?, ?)";

    @Override
    public void openDiaryFile(String filename, String strContent) {
        try {
            FileOutputStream fos = DiaryActivity.getAppContext().openFileOutput(filename, MODE_PRIVATE);
            fos.write(strContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addDiaryDB(String filename, String strDate, String strCity, String strWeather) {
        MyDB myDB = new MyDB(DiaryActivity.getAppContext(), DATABASE_NAME, null, 2);
        SQLiteDatabase dbWrite = myDB.getWritableDatabase();
        dbWrite.execSQL(SQL_INSERT, new Object[]{filename, strDate, strCity, strWeather});
        dbWrite.close();
    }
}
