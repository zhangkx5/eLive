package com.example.kaixin.elive.activity.Jokes.view;

import android.content.Context;
import android.os.Bundle;
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
import com.example.kaixin.elive.activity.Jokes.presenter.JokesPresenter;
import com.example.kaixin.elive.activity.Main.MainActivity;
import com.example.kaixin.elive.adapter.JokesAdapter;
import com.example.kaixin.elive.bean.JokesBean;

import java.util.List;

/**
 * Created by kaixin on 2017/5/12.
 */

public class JokesFragment extends Fragment implements IJokesView{
    String url = "http://japi.juhe.cn/joke/content/text.from";
    String apikey = "e0e928f4afcc0997f574aece6c351bde";
    private RecyclerView jokeslv;
    private JokesAdapter jokesAdapter;
    private int lastVisibleItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNumber = 1;
    private JokesPresenter jokesPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, container, false);

        jokesPresenter = new JokesPresenter(this);
        jokeslv = (RecyclerView)view.findViewById(R.id.jokeslv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.getAppContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        jokeslv.setLayoutManager(linearLayoutManager);

        if (!CheckNetwork.isNetworkAvailable(MainActivity.getAppContext())) {
            Toast.makeText(MainActivity.getAppContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        jokeslv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == jokesAdapter.getItemCount()) {
                    pageNumber += 1;
                    String URLs ="http://japi.juhe.cn/joke/content/text.from?key=" + apikey +
                            "&page="+pageNumber+"&pagesize=20";
                    jokesPresenter.getJokes(URLs);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshJokes();
            }
        });
        url = jokesPresenter.getNewUrl();
        jokesPresenter.getJokes(url);
        return view;
    }

    public void refreshJokes() {
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
        jokesPresenter.getJokes(url);
        jokesAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void setJokesAdapter(List<JokesBean> jokesBeanList) {
        jokesAdapter = new JokesAdapter(MainActivity.getAppContext(), jokesBeanList);
        jokeslv.setAdapter(jokesAdapter);
    }
    @Override
    public void notifyAdapter(List<JokesBean> jokesBeanList) {
        jokesAdapter.addMoreJokes(jokesBeanList);
        jokesAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean addOrNew() {
        return (jokesAdapter != null && lastVisibleItem+1 == jokesAdapter.getItemCount());
    }
}
