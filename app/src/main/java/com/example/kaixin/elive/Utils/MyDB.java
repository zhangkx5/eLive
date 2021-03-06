package com.example.kaixin.elive.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baoanj on 2017/5/12.
 */

public class MyDB extends SQLiteOpenHelper {
    private static final String DIARY_TABLE_NAME = "diary";
    private static final String MARKER_TABLE_NAME = "marker";

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String DIARY_CREATE_TABLE = "create table "
                + DIARY_TABLE_NAME
                + "(id interger primary key, "
                + "filename TEXT, "
                + "time TEXT, "
                + "city TEXT, "
                + "weather TEXT)";
        sqLiteDatabase.execSQL(DIARY_CREATE_TABLE);
        String MARKDAY_CREATE_TABLE = "create table "
                + MARKER_TABLE_NAME
                + "(id interger primary key, "
                + "event TEXT, "
                + "date TEXT, "
                + "notes TEXT)";
        sqLiteDatabase.execSQL(MARKDAY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersin) {
        sqLiteDatabase.execSQL("drop table if exists "+ DIARY_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists "+ MARKER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
