package cn.androidpi.data.repository

import cn.androidpi.note.entity.TextNote
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by jastrelax on 2017/12/7.
 */
interface TextNoteRepo {

    /**
     * 添加一条文本笔记。
     *
     * @param text 添加的文本笔记内容
     */
    fun addTextNote(text: String): Completable

    /**
     * 更新一条文本笔记。
     *
     * @param textNote 更新的笔记
     */
    fun updateTextNote(textNote: TextNote): Completable

    /**
     * 获取所有的文本笔记。
     */
    fun getAllTextNotes(): Single<List<TextNote>>
}