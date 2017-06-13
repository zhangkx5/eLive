package com.example.kaixin.elive.activity.Marker;

import com.example.kaixin.elive.bean.MarkerBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IMarkerDetailsModel {
    void addInDB(String event, String date, String notes);
    void updateInDB(String event, String date, String notes, MarkerBean markerBean);
    void deleteInDB(String event);
    List<MarkerBean> showMarkerLists();
}
