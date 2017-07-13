package com.droidcba.kedditbysteps.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.droidcba.kedditbysteps.commons.GankNewsItem
import com.droidcba.kedditbysteps.commons.adapter.ViewType
import com.droidcba.kedditbysteps.commons.adapter.ViewTypeDelegateAdapter
import com.droidcba.kedditbysteps.commons.extensions.inflate
import com.droidcba.kedditbysteps.commons.extensions.loadImg
import com.example.thirdparty.R

class NewsDelegateAdapter(val viewActions: onViewSelectedListener) : ViewTypeDelegateAdapter {

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as NewsViewHolder
        holder.bind(item as GankNewsItem)
    }

    inner class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.news_item)) {

        fun bind(item: GankNewsItem) = with(itemView) {
            (findViewById(R.id.img_thumbnail) as ImageView).loadImg(item.images?.let { if(it.size >0) it.get(0) else null })
            (findViewById(R.id.description) as TextView).text = item.desc
            (findViewById(R.id.author) as TextView).text = item.who
            (findViewById(R.id.comments) as TextView).text = item.createdAt
            (findViewById(R.id.time) as TextView).text = item.publishedAt
            super.itemView.setOnClickListener { viewActions.onItemSelected(item.url)}
        }
    }
}