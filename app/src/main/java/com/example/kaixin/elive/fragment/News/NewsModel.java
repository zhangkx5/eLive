package com.example.kaixin.elive.fragment.News;

import com.example.kaixin.elive.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class NewsModel implements INewsModel{
    public static final int NEWS_TYPE_TOP = 0;
    public static final int NEWS_TYPE_AMUSE = 1;
    public static final int NEWS_TYPE_MILITARY = 2;
    public static final int NEWS_TYPE_TECHNOLOGY = 3;
    public static final int NEWS_TYPE_ECONOMICS = 4;
    private String URLs ="http://api.dagoogle.cn/news/get-news?tableNum=1&page=1&pagesize=";
    private int pageNumber = 1;
    @Override
    public String getUrl(int NewsType) {
        switch(NewsType) {
            case NEWS_TYPE_TOP:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=1&page="+pageNumber+"&pagesize=10";
                break;
            case NEWS_TYPE_AMUSE:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=2&page="+pageNumber+"&pagesize=10";
                break;
            case NEWS_TYPE_MILITARY:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=3&page="+pageNumber+"&pagesize=10";
                break;
            case NEWS_TYPE_TECHNOLOGY:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=4&page="+pageNumber+"&pagesize=10";
                break;
            case NEWS_TYPE_ECONOMICS:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=5&page="+pageNumber+"&pagesize=10";
                break;
            default:
                URLs ="http://api.dagoogle.cn/news/get-news?tableNum=1&page="+pageNumber+"&pagesize=10";
                break;
        }
        return URLs;
    }
    @Override
    public List<NewsBean> getJsonData(String url){
        List<NewsBean> nesBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openConnection().getInputStream());
            JSONObject jsonObject;
            NewsBean newsBean;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0 ; i < jsonArray.length(); i++ ){
                    jsonObject = jsonArray.getJSONObject(i);
                    newsBean = new NewsBean();
                    newsBean.setNewsId(jsonObject.getString("news_id"));
                    newsBean.setNewsTitle(jsonObject.getString("title"));
                    newsBean.setTopImg(jsonObject.getString("top_image"));
                    newsBean.setTextImg0(jsonObject.getString("text_image0"));
                    newsBean.setTextImg1(jsonObject.getString("text_image1"));
                    newsBean.setNewsSource(jsonObject.getString("source"));
                    newsBean.setNewsContent(jsonObject.getString("content"));
                    newsBean.setNewsDigest(jsonObject.getString("digest"));
                    newsBean.setNewsTime(jsonObject.getString("edit_time"));
                    nesBeanList.add(newsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nesBeanList;
    }

    @Override
    public String readStream(InputStream is){
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line=br.readLine()) != null ){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
