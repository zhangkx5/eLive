package com.example.kaixin.elive.activity.Marker;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IMarkerDetailsView {
    void showToast(String text);
    void showMarkerDetails();
    String getEvent();
    String getDate();
    String getNotes();
}
