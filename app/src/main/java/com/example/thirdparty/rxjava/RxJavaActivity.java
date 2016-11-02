package com.example.thirdparty.rxjava;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thirdparty.MainActivity;
import com.example.thirdparty.R;

import java.util.ArrayList;
import java.util.List;

public class RxJavaActivity extends AppCompatActivity implements RxJavaContacts.IView {

    private static final String TAG = "zhuangsj";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private RxJavaContacts.IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_rx_java_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_rx_java_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mPresenter = new DataPresenter(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.getData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateList(List<Model> models) {
        if (models != null) {
            mRecyclerAdapter.clearItem();
            mRecyclerAdapter.addItem(models);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<VH> {

        private List<Model> list = new ArrayList<>();

        public void addItem(List<Model> models) {
            if (models != null) {
                for (Model model : models) {
                    list.add(model);
                }
                notifyDataSetChanged();
            } else
                clearItem();
        }

        public void addItem(Model... models) {
            if (models != null) {
                for (Model model : models) {
                    list.add(model);
                }
                notifyDataSetChanged();
            } else
                clearItem();
        }

        public void clearItem() {
            if (list != null)
                list.clear();
            notifyDataSetChanged();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_rx_java_item, null, false);
            return new VH(content);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.title.setText(list.get(position).name);
            holder.description.setText(list.get(position).description);
            Glide.with(holder.icon.getContext())
                    .load(list.get(position).url_icon)
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.icon);

        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView icon;

        public VH(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_titel);
            description = (TextView) itemView.findViewById(R.id.item_description);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
        }
    }
}
