package com.androidpi.note.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.androidpi.note.entity.TextNote
import io.reactivex.Single

/**
 * 文本笔记Dao
 * Created by jastrelax on 2017/12/7.
 */
@Dao
interface TextNoteDao {

    /**
     * 插入文本笔记
     */
    @Insert
    fun insert(vararg textNotes: TextNote): Array<Long>

    /**
     * 查找所有文本笔记
     */
    @Query("select * from text_notes;")
    fun findAll(): Single<List<TextNote>>
}