/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.pan.lean.photowall.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by panhongchao on 17/3/20.
 */
public class NetworkLoader extends AsyncTaskLoader<NetBean> {
    private NetBean netBean;

    public NetworkLoader(Context context) {
        super(context);
    }

    @Override
    public NetBean loadInBackground() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("111", "loader loadInBackground is called");

        netBean = new NetBean();
        return netBean;
    }

    @Override
    protected void onStartLoading() {
        if (netBean != null) {
            deliverResult(netBean);
        } else {
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(NetBean data) {
        netBean = null;
    }
}
