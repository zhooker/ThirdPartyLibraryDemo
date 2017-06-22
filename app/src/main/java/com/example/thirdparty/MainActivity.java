package com.example.thirdparty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mPresenter = new Presenter();
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new HomeAdapter(this, mPresenter, DataManger.getDatas(20)));
    }


    @Override
    protected void onDestroy() {
        mPresenter.finish();
        super.onDestroy();
    }
}
