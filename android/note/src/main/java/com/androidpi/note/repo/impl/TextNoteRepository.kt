package com.androidpi.note.repo.impl

import com.androidpi.note.entity.TextNote
import com.androidpi.note.local.dao.TextNoteDao
import com.androidpi.note.repo.TextNoteRepo
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/12/7.
 */
@Singleton
class TextNoteRepository @Inject constructor() : TextNoteRepo {

    @Inject
    lateinit var textNoteDao: TextNoteDao

    override fun addTextNote(textNote: TextNote): Completable {
        return Completable.fromAction {
            textNoteDao.insert(textNote)
        }
    }

    override fun updateTextNote(textNote: TextNote): Completable {
        return Completable.fromAction {
            textNoteDao.insert(textNote)
        }
    }

    override fun getAllTextNotes(): Single<List<TextNote>> {
        return textNoteDao.findAll()
    }
}