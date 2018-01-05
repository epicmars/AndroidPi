package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.BookmarkDao
import cn.androidpi.data.repository.BookmarkRepo
import cn.androidpi.news.entity.Bookmark
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jastrelax on 2018/1/5.
 */
@Singleton
class BookmarkRepository @Inject constructor() : BookmarkRepo{

    @Inject
    lateinit var mBookmarkDao: BookmarkDao

    override fun save(bookmark: Bookmark): Completable {
        return Completable.fromAction {
            mBookmarkDao.insert(bookmark)
        }
    }

    override fun update(bookmark: Bookmark): Completable {
        return Completable.fromAction {
            mBookmarkDao.update(bookmark)
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