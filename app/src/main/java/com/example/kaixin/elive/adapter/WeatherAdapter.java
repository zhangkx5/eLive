package com.example.kaixin.elive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.bean.WeatherBean;

import java.util.ArrayList;

/**
 * Created by baoanj on 2017/5/18.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<WeatherBean> mDatas;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, WeatherBean item);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public WeatherAdapter(Context context, ArrayList<WeatherBean> items) {
        super();
        mInflater = LayoutInflater.from(context);
        mDatas = items;
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycler_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.Date = (TextView)view.findViewById(R.id.Date);
        viewHolder.Weather = (TextView)view.findViewById(R.id.Weather);
        viewHolder.Temperature = (TextView)view.findViewById(R.id.Temperature);
        viewHolder.img = (ImageView) view.findViewById(R.id.weather_img);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.Date.setText(mDatas.get(i).getDate());
        viewHolder.Weather.setText(mDatas.get(i).getWeather());
        viewHolder.Temperature.setText(mDatas.get(i).getTemperature());
        viewHolder.img.setImageResource(mDatas.get(i).getImg());

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, i, mDatas.get(i));
                }
            });
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
        TextView Date;
        TextView Weather;
        TextView Temperature;
        ImageView img;
    }
}
