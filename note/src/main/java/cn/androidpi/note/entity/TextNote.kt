package cn.androidpi.note.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by jastrelax on 2017/12/7.
 */
@Entity(tableName = "text_notes")
class TextNote : Note() {

    /**
     * 文本内容
     */
    var text: String? = null

    
}