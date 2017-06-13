package com.example.kaixin.elive.activity.News.presenter;

import android.os.AsyncTask;

import com.example.kaixin.elive.activity.News.model.INewsModel;
import com.example.kaixin.elive.activity.News.model.NewsModel;
import com.example.kaixin.elive.activity.News.view.INewsView;
import com.example.kaixin.elive.bean.NewsBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class NewsPresenter implements INewsPresenter{
    private INewsView newsView;
    private INewsModel newsModel;

    public NewsPresenter(INewsView newsView) {
        this.newsView = newsView;
        newsModel = new NewsModel();
    }
    @Override
    public String getUrl(int type) {
        return newsModel.getUrl(type);
    }
    @Override
    public void getNews(String url) {
        new NewsAsyncTask().execute(url);
    }

    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {
        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return newsModel.getJsonData(params[0]);
        }
        @Override
        protected void onPostExecute(List<NewsBean>nesBeanList) {
            super.onPostExecute(nesBeanList);
            if (newsView.addOrNew()) {
                newsView.notifyAdapter(nesBeanList);
            } else {
                newsView.setNewsAdapter(nesBeanList);
            }
        }
    }
}
