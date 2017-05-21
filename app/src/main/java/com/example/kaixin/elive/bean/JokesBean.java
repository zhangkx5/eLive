package com.example.kaixin.elive.bean;

/**
 * Created by kaixin on 2017/5/12.
 */

public class JokesBean {
    private String jokesContent;
    private String jokesPtime;
    public void setJokesContent(String content) {
        this.jokesContent = content;
    }
    public void setJokesPtime(String time) {
        this.jokesPtime = time;
    }
    public String getJokesContent() {
        return jokesContent;
    }
    public String getJokesPtime() {
        return jokesPtime;
    }
}
