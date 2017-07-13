package com.droidcba.kedditbysteps.commons

import android.os.Parcel
import android.os.Parcelable
import com.droidcba.kedditbysteps.commons.adapter.AdapterConstants
import com.droidcba.kedditbysteps.commons.adapter.ViewType


data class GankNews(
        val error: String,
        val results: List<GankNewsItem>) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GankNews> = object : Parcelable.Creator<GankNews> {
            override fun createFromParcel(source: Parcel): GankNews = GankNews(source)
            override fun newArray(size: Int): Array<GankNews?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.createTypedArrayList(GankNewsItem.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(error)
        dest?.writeTypedList(results)
    }
}

data class GankNewsItem(
        val who: String,
        val desc: String,
        val createdAt: String,
        val publishedAt: String,
        val images: List<String>?,
        val url: String?
) : ViewType, Parcelable {

    override fun getViewType() = AdapterConstants.NEWS

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GankNewsItem> = object : Parcelable.Creator<GankNewsItem> {
            override fun createFromParcel(source: Parcel): GankNewsItem = GankNewsItem(source)
            override fun newArray(size: Int): Array<GankNewsItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(),
            ArrayList<String>().apply{source.readStringList(this)}, source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(who)
        dest?.writeString(desc)
        dest?.writeString(createdAt)
        dest?.writeString(publishedAt)
        dest?.writeStringList(images)
        dest?.writeString(url)
    }
}