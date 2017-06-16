package com.example.kaixin.elive.activity.Marker.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.bean.MarkerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class MarkerModel implements IMarkerModel {

    private static final String DATABASE_NAME = "myApp.db";
    private MyDB myDB = new MyDB(MainActivity.getAppContext(), DATABASE_NAME, null, 2);
    private static final String MARKER_SQL_DELETE = "delete from marker where event = ?";
    private static final String SQL_INSERT = "insert into marker (event, date, notes) values (?, ?, ?)";
    private static final String MARKER_SQL_SELECTALL = "select * from marker";

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

    public List<MarkerBean> showMarkerLists() {
        List<MarkerBean> markerBeanList = new ArrayList<>();
        SQLiteDatabase dbRead = myDB.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MARKER_SQL_SELECTALL, null);
        while (cursor.moveToNext()) {
            MarkerBean markerBean = new MarkerBean();
            markerBean.setEvent(cursor.getString(cursor.getColumnIndex("event")));
            markerBean.setDate(cursor.getString(cursor.getColumnIndex("date")));
            markerBean.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
            markerBeanList.add(markerBean);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(markerBeanList);
        return markerBeanList;
    }
}
