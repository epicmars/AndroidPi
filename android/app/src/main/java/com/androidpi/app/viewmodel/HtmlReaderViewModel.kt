package com.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.news.repo.BookmarkRepo
import com.androidpi.news.entity.Bookmark
import com.androidpi.news.model.BookmarkModel
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jastrelax on 2018/1/6.
 */
class HtmlReaderViewModel @Inject constructor() : ViewModel(), BookmarkModel {

    @Inject
    lateinit var mBookmarkRepo: BookmarkRepo

    val mBookmark = MutableLiveData<Bookmark>()

    override fun save() {
        if (mBookmark.value == null)
            return
        mBookmarkRepo.save(mBookmark.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Long> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: Long?) {
                        mBookmark.value?.id = t
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    override fun update() {
        if (mBookmark.value == null)
            return
        mBookmarkRepo.update(mBookmark.value!!)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun getBookmark(url: String?) {
        if (url == null) return
        mBookmarkRepo.findByUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MaybeObserver<Bookmark> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: Bookmark?) {
                        mBookmark.value = t
                    }

                    override fun onError(e: Throwable?) {
                    }

                    override fun onComplete() {
                    }
                })
    }

    fun saveBookmark(url: String?, html: String?, articleHtml: String?) {
        val bookmark = mBookmark.value ?: Bookmark()
        bookmark.url = url ?: bookmark.url
        bookmark.html = html ?: bookmark.html
        bookmark.articleHtml = articleHtml ?: bookmark.articleHtml
        mBookmark.value = bookmark
        if (bookmark.id == null) {
            save()
        }else {
            update()
        }
    }
}