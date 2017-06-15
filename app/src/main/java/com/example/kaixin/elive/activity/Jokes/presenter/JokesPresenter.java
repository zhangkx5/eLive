package com.example.kaixin.elive.activity.Jokes.presenter;

import android.os.AsyncTask;

import com.example.kaixin.elive.activity.Jokes.view.IJokesView;
import com.example.kaixin.elive.activity.Jokes.model.IJokesModel;
import com.example.kaixin.elive.activity.Jokes.model.JokesModel;
import com.example.kaixin.elive.bean.JokesBean;

import java.util.List;

/**
 * Created by kaixin on 2017/6/13.
 */

public class JokesPresenter implements IJokesPresenter{

    private IJokesView jokesView;
    private IJokesModel jokesModel;

    public JokesPresenter(IJokesView jokesView) {
        this.jokesView = jokesView;
        jokesModel = new JokesModel();
    }
    @Override
    public void getJokes(String url) {
        new JokesAsyncTask().execute(url);
    }
    @Override
    public String getNewUrl() {
        return jokesModel.getUrl();
    }

    class JokesAsyncTask extends AsyncTask<String, Void, List<JokesBean>> {
        @Override
        protected List<JokesBean> doInBackground(String... params) {
            return jokesModel.getJsonData(params[0]);
        }
        @Override
        protected void onPostExecute(List<JokesBean> jokesBeanList) {
            super.onPostExecute(jokesBeanList);
            if (jokesView.addOrNew()) {
                jokesView.notifyAdapter(jokesBeanList);
            } else {
                jokesView.setJokesAdapter(jokesBeanList);
            }
        }
    }
}
