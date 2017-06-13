package com.example.kaixin.elive.activity.Marker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.bean.MarkerBean;

/**
 * Created by kaixin on 2017/6/13.
 */

public class MarkerDetailsModel implements IMarkerDetailsModel{

    private static final String DATABASE_NAME = "myApp.db";
    private MyDB myDB = new MyDB(MainActivity.getAppContext(), DATABASE_NAME, null, 2);
    private static final String MARKER_SQL_DELETE = "delete from marker where event = ?";
    private static final String SQL_INSERT = "insert into marker (event, date, notes) values (?, ?, ?)";

    @Override
    public void addInDB(String event, String date, String notes) {
        SQLiteDatabase dbWrite = myDB.getWritableDatabase();
        dbWrite.execSQL(SQL_INSERT, new Object[]{event, date, notes});
        dbWrite.close();
    }
    @Override
    public void updateInDB(String event, String date, String notes, MarkerBean markerBean) {
        SQLiteDatabase dbWrite = myDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event", event);
        values.put("date", date);
        values.put("notes", notes);
        dbWrite.update("marker", values, "event = ? and date = ?",
                new String[]{markerBean.getEvent(), markerBean.getDate()});
    }
    @Override
    public void deleteInDB(String event) {
        SQLiteDatabase dbDelete = myDB.getWritableDatabase();
        dbDelete.execSQL(MARKER_SQL_DELETE, new Object[] {event});
        dbDelete.close();
    }
}
