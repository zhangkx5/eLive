package com.example.kaixin.elive.bean;

/**
 * Created by baoanj on 2017/5/18.
 */

public class Exponent {
    private int imageId;
    private String expo;
    private String describ;
    private String details;

    public Exponent(int imageId, String expo, String describ, String details) {
        this.imageId = imageId;
        this.expo = expo;
        this.describ = describ;
        this.details = details;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setExpo(String expo) {
        this.expo = expo;
    }
    public void setDescrib(String describ) {
        this.describ = describ;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public int getImageId() {
        return imageId;
    }
    public String getExpo() {
        return expo;
    }
    public String getDescrib() {
        return describ;
    }
    public String getDetails() {
        return details;
    }
}
