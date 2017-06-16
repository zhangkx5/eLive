package com.example.kaixin.elive.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.bean.MarkerBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerAdapter extends BaseAdapter {

    private List<MarkerBean> mlist;
    private LayoutInflater minflater;

    public MarkerAdapter(Context context, List<MarkerBean> data) {
        mlist = data;
        minflater = LayoutInflater.from(context);
    }
    @Override
    public MarkerBean getItem(int position) {
        return mlist.get(position);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MarkerAdapter.ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new MarkerAdapter.ViewHolder();
            convertView = minflater.inflate(R.layout.item_marker,null);
            viewHolder.event = (TextView) convertView.findViewById(R.id.mark_event);
            viewHolder.state = (TextView)convertView.findViewById(R.id.mark_state);
            viewHolder.day = (TextView)convertView.findViewById(R.id.mark_day);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MarkerAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.event.setText(mlist.get(position).getEvent());
        long curDate = new Date(System.currentTimeMillis()).getTime();
        long thatDate = 0;
        try {
            thatDate = stringToLong(mlist.get(position).getDate());
        } catch (ParseException e) {
            e.getErrorOffset();
            Log.d("that Date", thatDate+"");
        }
        if (curDate < thatDate) {
            viewHolder.state.setText("还有");
            long day = (thatDate - curDate)/(24*60*60*1000)+1;
            viewHolder.day.setText(day+"天");
        } else if (curDate - thatDate > 1) {
            viewHolder.state.setText("已经");
            long day = (curDate - thatDate)/(24*60*60*1000);
            viewHolder.day.setText(day+"天");
            if (day == 0) {
                viewHolder.state.setText("");
                viewHolder.day.setText("今天");
            }
        } else {
            viewHolder.state.setText(" ");
            viewHolder.day.setText("今天");
        }
        return convertView;
    }
    class  ViewHolder{
        public TextView event;
        public TextView state;
        public TextView day;
    }
    private Date stringToDate(String strTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    private long dateToLong(Date date) {
        return date.getTime();
    }
    private long stringToLong(String strTime) throws ParseException {
        Date date = stringToDate(strTime);
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date);
            return currentTime;
        }
    }
}