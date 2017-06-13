package com.example.kaixin.elive.fragment.Jokes;

import com.example.kaixin.elive.bean.JokesBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public interface IJokesView {
    void setJokesAdapter(List<JokesBean> jokesBeanList);
    void notifyAdapter(List<JokesBean> jokesBeanList);
    boolean addOrNew();
}
