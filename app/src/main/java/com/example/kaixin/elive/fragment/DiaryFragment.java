package com.example.kaixin.elive.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.activity.Diary.DiaryActivity;
import com.example.kaixin.elive.activity.MainActivity;
import com.example.kaixin.elive.activity.Diary.SetDiaryLock;
import com.example.kaixin.elive.adapter.DiaryAdapter;
import com.example.kaixin.elive.bean.DiaryBean;
import com.example.kaixin.elive.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by baoanj on 2017/5/12.
 */

public class DiaryFragment extends Fragment {

    private ListView diarylv;
    private FloatingActionButton fab;
    private LinearLayout ll_diary_pass;
    private EditText et_diary_pass;
    private ImageButton ib_diary_ok;
    private TextView set_diary_pass;

    private DiaryAdapter diaryAdapter;

    private SharedPreferences pref;
    private String lock;
    private Boolean hasSetLock;

    private static final String DATABASE_NAME = "myApp.db";
    private static final String DIARY_SQL_SELECTALL = "select * from diary";
    private static final String DIARY_SQL_DELETE = "delete from diary where time = ?";

    private List<DiaryBean> diaryBeanList;
    private MyDB myDB = new MyDB(MainActivity.getAppContext(), DATABASE_NAME, null, 2);

    @Override
    public void onResume() {
        super.onResume();
        init();
        diaryAdapter = new DiaryAdapter(MainActivity.getAppContext(), diaryBeanList);
        diarylv.setAdapter(diaryAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        diarylv = (ListView) view.findViewById(R.id.diarylv);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        ll_diary_pass = (LinearLayout) view.findViewById(R.id.pass_gone);
        et_diary_pass = (EditText) view.findViewById(R.id.diary_pass);
        ib_diary_ok = (ImageButton) view.findViewById(R.id.diary_ok);
        set_diary_pass = (TextView) view.findViewById(R.id.set_diary_pass);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getAppContext(), DiaryActivity.class);
                startActivity(intent);
            }
        });
        diarylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView diary_select_content = (TextView)view.findViewById(R.id.diary_content);
                TextView diary_select_city = (TextView)view.findViewById(R.id.diary_city);
                TextView diary_select_time = (TextView)view.findViewById(R.id.diary_time);
                TextView diary_select_weather = (TextView)view.findViewById(R.id.diary_weather);
                Bundle bundle = new Bundle();
                bundle.putString("content", diary_select_content.getText().toString());
                bundle.putString("time", diary_select_time.getText().toString());
                bundle.putString("city", diary_select_city.getText().toString());
                bundle.putString("weather", diary_select_weather.getText().toString());

                Intent intent = new Intent(MainActivity.getAppContext(), DiaryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        diarylv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                final TextView diary_select_time = (TextView)view.findViewById(R.id.diary_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("是否删除？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase dbDelete = myDB.getWritableDatabase();
                        dbDelete.execSQL(DIARY_SQL_DELETE, new Object[] {diary_select_time.getText().toString()});
                        MainActivity.getAppContext().deleteFile("Diary_" + diary_select_time.getText().toString());
                        dbDelete.close();
                        diaryBeanList.remove(pos);
                        diaryAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        ib_diary_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = MainActivity.getAppContext().getSharedPreferences("diaryLock", MODE_PRIVATE);
                lock = pref.getString("lock", "");
                hasSetLock = pref.getBoolean("hasSetLock", false);

                if (TextUtils.isEmpty(et_diary_pass.getText().toString())) {
                    Toast.makeText(MainActivity.getAppContext(), "日记锁不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_diary_pass.getText().toString().equals(lock)) {
                    ll_diary_pass.setVisibility(View.GONE);
                    diarylv.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                } else if (!hasSetLock) {
                    Toast.makeText(MainActivity.getAppContext(), "你还没有设置日记锁", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.getAppContext(), "日记锁错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        set_diary_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getAppContext(), SetDiaryLock.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void init() {
        diaryBeanList  = new ArrayList<>();
        SQLiteDatabase dbRead = myDB.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(DIARY_SQL_SELECTALL, null);
        while (cursor.moveToNext()) {
            DiaryBean diaryBean = new DiaryBean();
            String filename = cursor.getString(cursor.getColumnIndex("filename"));
            try {
                FileInputStream fis = MainActivity.getAppContext().openFileInput(filename);
                byte[] content = new byte[fis.available()];
                fis.read(content);
                diaryBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
                diaryBean.setCity(cursor.getString(cursor.getColumnIndex("city")));
                diaryBean.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                diaryBean.setContent(new String(content));
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            diaryBeanList.add(diaryBean);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(diaryBeanList);
    }

}
