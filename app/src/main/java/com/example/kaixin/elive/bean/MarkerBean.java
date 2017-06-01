package com.example.kaixin.elive.bean;

import java.io.Serializable;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerBean implements Serializable {
    private String event;
    private String date;
    private String notes;


    public void setEvent(String event) {
        this.event = event;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getEvent() {
        return event;
    }
    public String getNotes() {
        return notes;
    }
    public String getDate() {
        return date;
    }
}