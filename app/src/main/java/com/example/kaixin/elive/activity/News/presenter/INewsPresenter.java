package com.example.kaixin.elive.activity.News.presenter;

/**
 * Created by kaixin on 2017/6/14.
 */

public interface INewsPresenter {
    String getUrl(int type);
    void getNews(String url);
}
