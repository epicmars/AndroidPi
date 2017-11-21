package cn.androidpi.news.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * 一条新闻。
 * Created by jastrelax on 2017/11/2.
 */

@Entity(tableName = "news",
        indices = arrayOf(Index(value = "news_id", unique = true)))
class News() : Parcelable {
    @PrimaryKey
    var id: Long? = null

    /**
     * 服务端的id
     */
    @ColumnInfo(name = "news_id")
    var newsId: String? = null

    /**
     * 分类
     */
    var category: String? = null

    /**
     * 原始标题
     */
    @ColumnInfo(name = "origin_title")
    var originTitle: String? = null

    /**
     * 发表时间
     */
    @ColumnInfo(name = "publish_time")
    var publishTime: String? = null

    /**
     * 来源名称
     */
    @ColumnInfo(name = "source_name")
    var sourceName: String? = null

    /**
     * 来源链接
     */
    @ColumnInfo(name = "source_url")
    var sourceUrl: String? = null

    /**
     * 标题
     */
    var title: String? = null

    /**
     * 链接
     */
    var url: String? = null

    /**
     * 关键词
     */
    var keywords: Array<String>? = null

    /**
     * 新闻图片链接
     */
    var images: Array<String>? = null

    override fun toString(): String {
        return "News(id=$id, newsId=$newsId, category=$category, originTitle=$originTitle, publishTime=$publishTime, sourceName=$sourceName, sourceUrl=$sourceUrl, title=$title, url=$url, keywords=${Arrays.toString(keywords)}, images=${Arrays.toString(images)})"
    }

    constructor(source: Parcel) : this() {
        id = source.readLong()
        newsId = source.readString()
        category = source.readString()
        originTitle = source.readString()
        publishTime = source.readString()
        sourceName = source.readString()
        sourceUrl = source.readString()
        title = source.readString()
        url = source.readString()
        keywords = source.createStringArray()
        images = source.createStringArray()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeLong(id ?: -1)
        dest.writeString(newsId)
        dest.writeString(category)
        dest.writeString(originTitle)
        dest.writeString(publishTime)
        dest.writeString(sourceName)
        dest.writeString(sourceUrl)
        dest.writeString(title)
        dest.writeString(url)
        dest.writeStringArray(keywords)
        dest.writeStringArray(images)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<News> = object : Parcelable.Creator<News> {
            override fun createFromParcel(source: Parcel): News = News(source)
            override fun newArray(size: Int): Array<News?> = arrayOfNulls(size)
        }
    }
}
