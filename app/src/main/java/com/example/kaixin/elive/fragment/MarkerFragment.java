package com.example.kaixin.elive.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.activity.MainActivity;
import com.example.kaixin.elive.activity.Marker.MarkerActivity;
import com.example.kaixin.elive.activity.Marker.MarkerDetailsActivity;
import com.example.kaixin.elive.adapter.MarkerAdapter;
import com.example.kaixin.elive.bean.MarkerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerFragment extends Fragment {
    private ListView marklv;
    private FloatingActionButton fab;
    private List<MarkerBean> markerBeanList;
    private MarkerAdapter markerAdapter;
    private MyDB myDB = new MyDB(MainActivity.getAppContext(), DATABASE_NAME, null, 2);

    private static final String DATABASE_NAME = "myApp.db";
    private static final String MARKER_SQL_SELECTALL = "select * from marker";
    private static final String MARKER_SQL_SELECTONE = "select event from marker where ctime = ?";
    private static final String MARKER_SQL_DELETE = "delete from marker where event = ?";

    @Override
    public void onResume() {
        super.onResume();
        init();
        markerAdapter = new MarkerAdapter(MainActivity.getAppContext(), markerBeanList);
        marklv.setAdapter(markerAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker, container, false);
        marklv = (ListView)view.findViewById(R.id.marklv);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getAppContext(), MarkerActivity.class);
                startActivity(intent);
            }
        });
        marklv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MarkerBean markerBean = markerAdapter.getItem(i);
                TextView day = (TextView)view.findViewById(R.id.mark_day);
                Intent intent = new Intent(MainActivity.getAppContext(), MarkerDetailsActivity.class);
                intent.putExtra("markday", markerBean);
                intent.putExtra("long", day.getText().toString());
                startActivity(intent);
            }
        });
        marklv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                final TextView marker_select_date = (TextView)view.findViewById(R.id.mark_day);
                final TextView marker_select_event = (TextView)view.findViewById(R.id.mark_event);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("是否删除？");
                builder.setMessage("此操作不可逆！");
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
                        dbDelete.execSQL(MARKER_SQL_DELETE, new Object[] {
                                marker_select_event.getText().toString()});
                        dbDelete.close();
                        markerBeanList.remove(pos);
                        markerAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        return view;
    }

    private void init() {
        markerBeanList = new ArrayList<>();
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
    }
}