package com.example.kaixin.elive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaixin.elive.R;

/**
 * Created by kaixin on 2017/5/11.
 */

public class NewsSubFragment extends Fragment {

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
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub  
        View view = inflater.inflate(R.layout.fragment_subnews, null);
        return view;
    }
}

