package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.TextNoteDao
import cn.androidpi.data.repository.TextNoteRepo
import cn.androidpi.note.entity.TextNote
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

    override fun addTextNote(text: String): Completable {
        return Completable.fromAction {
            val textNote = TextNote()
            textNote.text = text
            textNoteDao.insert(textNote)
        }
    }

    override fun updateTextNote(textNote: TextNote): Completable {
        return Completable.fromAction {
            textNoteDao.insert(textNote)
        }
    }

    override fun getAllTextNotes(): Single<List<TextNote>> {
        return Single.fromCallable {
            textNoteDao.findAll()
        }
    }
}