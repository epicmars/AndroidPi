package cn.androidpi.note.entity

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by jastrelax on 2017/12/7.
 */
@Entity(tableName = "text_notes")
class TextNote() : Note(), Parcelable {
    /**
     * 文本内容
     */
    var text: String? = null

    constructor(source: Parcel) : this() {
        id = source.readLong()
        createdTime = Date(source.readLong())
        updateTime = Date(source.readLong())
        tags = source.createStringArray()
        category = source.readString()
        text = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeLong(id ?: -1)
        dest.writeLong(createdTime?.time ?: 0L)
        dest.writeLong(updateTime?.time ?: 0L)
        dest.writeStringArray(tags)
        dest.writeString(category)
        dest.writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TextNote> = object : Parcelable.Creator<TextNote> {
            override fun createFromParcel(source: Parcel): TextNote = TextNote(source)
            override fun newArray(size: Int): Array<TextNote?> = arrayOfNulls(size)
        }
    }
}