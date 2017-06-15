package com.example.kaixin.elive.activity.News.model;


import com.example.kaixin.elive.bean.NewsBean;

import java.io.InputStream;
import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface INewsModel {
    String getUrl(int NewsType);
    List<NewsBean> getJsonData(String url);
    String readStream(InputStream is);
}
