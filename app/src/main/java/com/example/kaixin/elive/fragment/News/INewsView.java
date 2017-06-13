package com.example.kaixin.elive.fragment.News;

import com.example.kaixin.elive.bean.NewsBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface INewsView {
    void setNewsAdapter(List<NewsBean> newsBeanList);
    void notifyAdapter(List<NewsBean> newssBeanList);
    boolean addOrNew();
}
