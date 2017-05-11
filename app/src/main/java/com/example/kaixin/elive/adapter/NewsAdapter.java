package com.example.kaixin.elive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.ImageTask;
import com.example.kaixin.elive.bean.NewsBean;

import java.util.List;

/**
 * Created by kaixin on 2017/5/11.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;

    private List<NewsBean> mlist;
    private LayoutInflater minflater;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView iconimage;
        TextView content;

        public ViewHolder(View view) {
            super(view);
            iconimage = (ImageView)view.findViewById(R.id.newsImg);
            title = (TextView)view.findViewById(R.id.newsTitle);
            content = (TextView)view.findViewById(R.id.newsDetails);
        }
    }
    public NewsAdapter(Context context, List<NewsBean> data) {
        mlist = data;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final NewsBean newsBean = mlist.get(position);
            ((ViewHolder)holder).title.setText(newsBean.getNewsTitle());
            ((ViewHolder)holder).content.setText(newsBean.getNewsDigest());
            ((ViewHolder)holder).iconimage.setTag(newsBean.getTopImg());
            ((ViewHolder)holder).iconimage.setImageResource(R.mipmap.iv_funpic);
            new ImageTask(((ViewHolder)holder).iconimage, newsBean.getTopImg())
                    .execute(newsBean.getTopImg());

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(holder.itemView, position, newsBean);
                    }
                });
            }
            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemLongClickListener.onItemLongClick(holder.itemView, position, newsBean);
                        return true;
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position, NewsBean newsBean);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, NewsBean newsBean);
    }
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
