package com.example.kaixin.elive.fragment.News;

import android.os.AsyncTask;
import com.example.kaixin.elive.bean.NewsBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class NewsPresenter {
    private INewsView newsView;
    private INewsModel newsModel;

    public NewsPresenter(INewsView newsView) {
        this.newsView = newsView;
        newsModel = new NewsModel();
    }
    public String getUrl(int type) {
        return newsModel.getUrl(type);
    }
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
