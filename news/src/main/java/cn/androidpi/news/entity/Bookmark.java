package cn.androidpi.news.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/5.
 */
@Entity(tableName = "bookmark", indices = {@Index(value = {"url"}, unique = true)})
public class Bookmark implements Parcelable {

    @PrimaryKey
    private Long id;

    /**
     * Created time.
     */
    private Date timestamp = new Date();

    /**
     * Page url.
     */
    private String url;

    /**
     * Original html.
     */
    private String html;

    /**
     * Parsed readable html.
     */
    @ColumnInfo(name = "article_html")
    private String articleHtml;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getArticleHtml() {
        return articleHtml;
    }

    public void setArticleHtml(String articleHtml) {
        this.articleHtml = articleHtml;
    }


    public Bookmark() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
        dest.writeString(this.url);
        dest.writeString(this.html);
        dest.writeString(this.articleHtml);
        dest.writeString(this.title);
    }

    protected Bookmark(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
        this.url = in.readString();
        this.html = in.readString();
        this.articleHtml = in.readString();
        this.title = in.readString();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel source) {
            return new Bookmark(source);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };
}
