package com.example.kaixin.elive.activity.Jokes.model;

import com.example.kaixin.elive.bean.JokesBean;

import java.io.InputStream;
import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IJokesModel {
    String getUrl();
    List<JokesBean> getJsonData(String url);
    String readStream(InputStream is);
}
