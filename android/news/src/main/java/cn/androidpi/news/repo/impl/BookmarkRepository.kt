package cn.androidpi.news.repo.impl

import cn.androidpi.news.local.dao.BookmarkDao
import cn.androidpi.news.repo.BookmarkRepo
import cn.androidpi.news.entity.Bookmark
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jastrelax on 2018/1/5.
 */
@Singleton
class BookmarkRepository @Inject constructor() : BookmarkRepo {

    @Inject
    lateinit var mBookmarkDao: BookmarkDao

    override fun save(bookmark: Bookmark): Single<Long> {
        return Single.fromCallable {
            mBookmarkDao.insertOne(bookmark)
        }
    }

    override fun update(bookmark: Bookmark): Completable {
        return Completable.fromAction {
            mBookmarkDao.update(bookmark)
        }
    }

    override fun findByUrl(url: String): Maybe<Bookmark> {
        return Maybe.fromCallable {
            mBookmarkDao.findByUrl(url)
        }
    }

    override fun loadFirstPage(pageSize: Int): Single<List<Bookmark>> {
        return Single.fromCallable {
            mBookmarkDao.firstPage(pageSize)
        }
    }

    override fun loadNextPage(page: Int, size: Int): Single<List<Bookmark>> {
        return Single.fromCallable {
            mBookmarkDao.nextPage(page, size)
        }
    }
}