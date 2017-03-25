/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.pan.lean.photowall.loaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by panhongchao on 17/3/20.
 */
public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<NetBean> {
    ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader);

        listView = (ListView) findViewById(R.id.my_list);
        progressBar = (ProgressBar) findViewById(R.id.my_progress);

        getSupportLoaderManager().initLoader(100, null, this);
    }

    @Override
    public Loader<NetBean> onCreateLoader(int id, Bundle args) {
        Log.e("111", "onCreateLoader is called");
        return new NetworkLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<NetBean> loader, NetBean data) {
        Log.e("111", "onLoadFinished is called");
        progressBar.setVisibility(View.GONE);
        listView.setAdapter(new MyAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<NetBean> loader) {
        Log.e("111", "onLoaderReset is called");
    }

    @Override
    protected void onStart() {
        Log.e("111", "onStart is called");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e("111", "onStop is called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("111", "onDestroy is called");
        super.onDestroy();
    }

    public class MyAdapter extends BaseAdapter {
        NetBean datas;

        public MyAdapter(NetBean datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.items.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
            TextView txt = (TextView) convertView.findViewById(R.id.txt);
            NetBean.Item item = (NetBean.Item) getItem(position);
            txt.setText(item.img);
            return convertView;
        }
    }
}
