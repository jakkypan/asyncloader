/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.pan.lean.photowall;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by panhongchao on 17/3/19.
 */
public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mLogMessageTv;

    MyAsync myAsync;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mLogMessageTv = (TextView) findViewById(R.id.log_message_tv);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsync.cancel(true);
            }
        });

        myAsync = new MyAsync();
        myAsync.execute();
    }

    public class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "====doInBackground: " + Thread.currentThread().getName());
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "====onCancelled: " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLogMessageTv.setText("123");
            Log.i(TAG, "====onPostExecute: " + Thread.currentThread().getName());
        }
    }
}
