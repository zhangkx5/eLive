package com.example.kaixin.elive.activity.Marker.presenter;

import android.content.Intent;

import com.example.kaixin.elive.activity.Marker.view.IMarkerView;
import com.example.kaixin.elive.activity.Marker.model.IMarkerModel;
import com.example.kaixin.elive.activity.Marker.model.MarkerModel;
import com.example.kaixin.elive.bean.MarkerBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class MarkerPresenter implements IMarkerPresenter {

    private IMarkerView markerDetailsView;
    private IMarkerModel markerDetailsModel;

    public MarkerPresenter(IMarkerView markerDetailsView) {
        this.markerDetailsView = markerDetailsView;
        markerDetailsModel = new MarkerModel();
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
    public void deleteMark(String event) {
        markerDetailsModel.deleteInDB(event);
    }
    @Override
    public MarkerBean getMarkerDetails(Intent intent) {
        MarkerBean markerBean = (MarkerBean)intent.getSerializableExtra("markday");
        return markerBean;
    }
    public List<MarkerBean> getMarkerLists() {
        List<MarkerBean> markerBeanList = markerDetailsModel.showMarkerLists();
        return markerBeanList;
    }
    public void EventError() {
        markerDetailsView.showToast("事件名称不能为空");
    }
    public void DateError() {
        markerDetailsView.showToast("日期不能为空");
    }
}
