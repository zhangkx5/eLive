package com.example.kaixin.elive.activity.Marker;

import android.content.Intent;

import com.example.kaixin.elive.bean.MarkerBean;

/**
 * Created by kaixin on 2017/6/13.
 */

public class MarkerDetailsPresenter implements IMarkerDetailsPresenter{

    private IMarkerDetailsView markerDetailsView;
    private IMarkerDetailsModel markerDetailsModel;

    public MarkerDetailsPresenter(IMarkerDetailsView markerDetailsView) {
        this.markerDetailsView = markerDetailsView;
        markerDetailsModel = new MarkerDetailsModel();
    }
    @Override
    public void addMark() {
        String event = markerDetailsView.getEvent();
        String date = markerDetailsView.getDate();
        String notes = markerDetailsView.getNotes();
        markerDetailsModel.addInDB(event, date, notes);
    }
    @Override
    public void updateMark(MarkerBean markerBean) {
        String event = markerDetailsView.getEvent();
        String date = markerDetailsView.getDate();
        String notes = markerDetailsView.getNotes();
        markerDetailsModel.updateInDB(event, date, notes, markerBean);
    }
    @Override
    public void deleteMark() {
        String event = markerDetailsView.getEvent();
        markerDetailsModel.deleteInDB(event);
    }
    @Override
    public MarkerBean getMarkerDetails(Intent intent) {
        MarkerBean markerBean = (MarkerBean)intent.getSerializableExtra("markday");
        return markerBean;
    }
    public void EventError() {
        markerDetailsView.showToast("事件名称不能为空");
    }
    public void DateError() {
        markerDetailsView.showToast("日期不能为空");
    }
}
