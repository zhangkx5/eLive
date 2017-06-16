package com.example.kaixin.elive.activity.News.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.CheckNetwork;
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.activity.News.presenter.NewsPresenter;
import com.example.kaixin.elive.adapter.NewsAdapter;
import com.example.kaixin.elive.bean.NewsBean;

import java.util.List;


/**
 * Created by kaixin on 2017/5/11.
 */

public class NewsSubFragment extends Fragment implements INewsView{

    private RecyclerView recyclerView;
    private NewsPresenter newsPresenter;
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
        newsPresenter = new NewsPresenter(this);
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
                    newsPresenter.getNews(URLs);
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

        if (!CheckNetwork.isNetworkAvailable(MainActivity.getAppContext())) {
            Toast.makeText(MainActivity.getAppContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        URLs = newsPresenter.getUrl(NewsType);
        newsPresenter.getNews(URLs);
        return view;
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
        newsPresenter.getNews(URLs);
        //new NewsAsyncTask().execute(URLs);
        newsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setNewsAdapter(List<NewsBean> newsBeanList) {
        newsAdapter = new NewsAdapter(NewsSubFragment.this.getActivity(), newsBeanList);
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
    @Override
    public void notifyAdapter(List<NewsBean> newsBeanList) {
        newsAdapter.addMoreItem(newsBeanList);
        newsAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean addOrNew() {
        return (newsAdapter != null && lastVisibleItem+1 == newsAdapter.getItemCount());
    }
}
