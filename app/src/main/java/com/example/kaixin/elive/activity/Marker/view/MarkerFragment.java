package com.example.kaixin.elive.activity.Marker.view;


import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.activity.Marker.presenter.MarkerDetailsPresenter;
import com.example.kaixin.elive.adapter.MarkerAdapter;
import com.example.kaixin.elive.bean.MarkerBean;

import java.util.List;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerFragment extends Fragment implements IMarkerDetailsView{
    private ListView marklv;
    private FloatingActionButton fab;
    private List<MarkerBean> markerBeanList;
    private MarkerAdapter markerAdapter;
    private MarkerDetailsPresenter markerDetailsPresenter;
    String event, date;

    @Override
    public void onResume() {
        super.onResume();
        markerBeanList = markerDetailsPresenter.getMarkerLists();
        markerAdapter = new MarkerAdapter(MainActivity.getAppContext(), markerBeanList);
        marklv.setAdapter(markerAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker, container, false);
        markerDetailsPresenter = new MarkerDetailsPresenter(this);
        marklv = (ListView)view.findViewById(R.id.marklv);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        markerBeanList = markerDetailsPresenter.getMarkerLists();

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
                date = day.getText().toString();
                Intent intent = new Intent(MainActivity.getAppContext(), MarkerDetailsActivity.class);
                intent.putExtra("markday", markerBean);
                intent.putExtra("long", date);
                startActivity(intent);
            }
        });
        marklv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
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
                        event = marker_select_event.getText().toString();
                        markerDetailsPresenter.deleteMark(event);
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
    @Override
    public void showToast(String text) {}
    @Override
    public String getEvent() {
        return event;
    }
    @Override
    public String getDate() {
        return date;
    }
    @Override
    public String getNotes() {
        return "";
    }
    @Override
    public void showMarkerDetails() {}
}