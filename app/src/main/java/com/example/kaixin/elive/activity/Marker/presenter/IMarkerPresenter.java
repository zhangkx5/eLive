package com.example.kaixin.elive.activity.Marker.presenter;

import android.content.Intent;

import com.example.kaixin.elive.bean.MarkerBean;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IMarkerPresenter {
    void addMark();
    void updateMark(MarkerBean markerBean);
    void deleteMark(String event);
    MarkerBean getMarkerDetails(Intent intent);
}
