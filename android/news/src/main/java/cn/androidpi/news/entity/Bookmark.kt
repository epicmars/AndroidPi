package cn.androidpi.news.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

import java.util.Date

/**
 * Created by jastrelax on 2018/1/5.
 */
@Entity(tableName = "bookmark", indices = arrayOf(Index(value = *arrayOf("url"), unique = true)))
class Bookmark : Parcelable {

    @PrimaryKey
    var id: Long? = null

    /**
     * Created time.
     */
    var timestamp: Date? = Date()

    /**
     * Page url.
     */
    var url: String? = null

    /**
     * Original html.
     */
    var html: String? = null

    /**
     * Parsed readable html.
     */
    @ColumnInfo(name = "article_html")
    var articleHtml: String? = null

    var title: String? = null


    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(this.id)
        dest.writeLong(if (this.timestamp != null) this.timestamp!!.time else -1)
        dest.writeString(this.url)
        dest.writeString(this.html)
        dest.writeString(this.articleHtml)
        dest.writeString(this.title)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readValue(Long::class.java.classLoader) as Long
        val tmpTimestamp = `in`.readLong()
        this.timestamp = if (tmpTimestamp == -1L) null else Date(tmpTimestamp)
        this.url = `in`.readString()
        this.html = `in`.readString()
        this.articleHtml = `in`.readString()
        this.title = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Bookmark> = object : Parcelable.Creator<Bookmark> {
            override fun createFromParcel(source: Parcel): Bookmark {
                return Bookmark(source)
            }

            override fun newArray(size: Int): Array<Bookmark?> {
                return arrayOfNulls(size)
            }
        }
    }
}
