package com.example.kaixin.elive.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.activity.NewsDetailsActivity;
import com.example.kaixin.elive.adapter.NewsAdapter;
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
 * Created by kaixin on 2017/5/11.
 */

public class NewsSubFragment extends Fragment {

    public static final int NEWS_TYPE_TOP = 0;
    public static final int NEWS_TYPE_AMUSE = 1;
    public static final int NEWS_TYPE_MILITARY = 2;
    public static final int NEWS_TYPE_TECHNOLOGY = 3;
    public static final int NEWS_TYPE_ECONOMICS = 4;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private int NewsType = 0;
    private String URLs ="http://api.dagoogle.cn/news/get-news?tableNum=1&page=1&pagesize=";
    private int lastVisibleItem;
    private int pageNumber = 1;

    public static NewsSubFragment newInstance(int type) {
        Bundle args = new Bundle();
        NewsSubFragment fragment = new NewsSubFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsType = getArguments().getInt("type");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub  
        View view = inflater.inflate(R.layout.fragment_subnews, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.newslv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.getAppContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        final LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == newsAdapter.getItemCount()) {
                    newsAdapter.changeMoreStatus(NewsAdapter.LOADING_MORE);
                    pageNumber += 1;
                    URLs ="http://api.dagoogle.cn/news/get-news?tableNum=1&page="+pageNumber+"&pagesize=10";
                    new NewsAsyncTask().execute(URLs);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearManager.findLastVisibleItemPosition();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
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
        new NewsAsyncTask().execute(URLs);
        return view;
    }
    private List<NewsBean> getJsonData(String url){
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
    private String readStream(InputStream is){
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

    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>{
        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }
        @Override
        protected void onPostExecute(List<NewsBean>nesBeanList) {
            super.onPostExecute(nesBeanList);
            if (newsAdapter != null && lastVisibleItem + 1 == newsAdapter.getItemCount()) {
                newsAdapter.addMoreItem(nesBeanList);
                newsAdapter.notifyDataSetChanged();
            } else {
                newsAdapter = new NewsAdapter(NewsSubFragment.this.getActivity(), nesBeanList);
                newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, NewsBean newsBean) {
                        Intent intent = new Intent(MainActivity.getAppContext(), NewsDetailsActivity.class);
                        intent.putExtra("news", newsBean);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(newsAdapter);
            }
        }
    }
    private void refreshNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new NewsAsyncTask().execute(URLs);
        newsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
