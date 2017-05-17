package com.example.kaixin.elive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.bean.JokesBean;

import java.util.List;

/**
 * Created by kaixin on 2017/5/12.
 */

public class JokesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<JokesBean> mlist;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView ptime;
        public ViewHolder(View view) {
            super(view);
            content = (TextView)view.findViewById(R.id.content);
            ptime = (TextView)view.findViewById(R.id.ptime);
        }
    }
    static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView)view.findViewById(R.id.foot_view);
        }
    }
    public JokesAdapter(Context context, List<JokesBean> data) {
        mlist = data;
    }
    @Override
    public int getItemCount() {
        return mlist.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jokes, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_foot, parent, false);
            FootViewHolder holder = new FootViewHolder(view);
            return holder;
        }
        return null;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            JokesBean jokesBean = mlist.get(position);
            ((ViewHolder)holder).content.setText(jokesBean.getJokesContent());
            ((ViewHolder)holder).ptime.setText(jokesBean.getJokesPtime());
        }
        if (holder instanceof FootViewHolder) {
            ((FootViewHolder)holder).foot_view_item_tv.setText("正在加载");
        }
    }
    public void addMoreJokes(List<JokesBean> data) {
        mlist.addAll(data);
        notifyDataSetChanged();
    }
}