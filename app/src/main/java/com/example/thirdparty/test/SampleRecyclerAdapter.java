package com.example.thirdparty.test;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thirdparty.R;

import java.util.ArrayList;

/**
 * Created by zhuangsj on 17-8-12.
 */

public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.ViewHolder> {

    private final ArrayList<String> sampleData = new ArrayList<String>();

    public SampleRecyclerAdapter() {
        for (int i = 0; i < 100; i++) {
            if(i % 3 == 1)
                sampleData.add("item1 = " + i);
            else if(i % 3 == 2)
                sampleData.add("item2 = " + i);
            else
                sampleData.add("item3 = " + i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        android.widget.TextView rowView = new android.widget.TextView(parentViewGroup.getContext());
        rowView.setTextSize(30.0f);
        if(viewType == 0) {
            rowView.setBackgroundColor(Color.RED);
        } else if(viewType == 0) {
            rowView.setBackgroundColor(Color.GREEN);
        } else {
            rowView.setBackgroundColor(Color.YELLOW);
        }
        ViewHolder holder = new ViewHolder (rowView);
        L.d("onCreateViewHolder",viewType + " holder="+holder );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final String rowData = sampleData.get(position);
        viewHolder.textViewSample.setText(rowData);
        viewHolder.itemView.setTag(rowData);
        L.d("onBindViewHolder",position + " holder="+viewHolder);
    }


    @Override
    public int getItemCount() {
        return sampleData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(sampleData.get(position).startsWith("item1"))
            return 0;
        else if(sampleData.get(position).startsWith("item2"))
            return 1;
        else
            return 2;
    }

    public void removeData (int position) {

        sampleData.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int positionToAdd) {

        sampleData.add(positionToAdd, "New element");
        notifyItemInserted(positionToAdd);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final android.widget.TextView textViewSample;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewSample = (android.widget.TextView)itemView;
        }
    }

}