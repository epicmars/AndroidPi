package cn.androidpi.data.repository

import cn.androidpi.news.entity.Bookmark
import cn.androidpi.news.model.BookmarkListModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by jastrelax on 2018/1/5.
 */
interface BookmarkRepo {

    /**
     * 保存一条书签。
     */
    fun save(bookmark: Bookmark): Completable

    /**
     * 更新一条书签。
     */
    fun update(bookmark: Bookmark): Completable

    /**
     * 加载第一页书签。
     */
    fun loadFirstPage(pageSize: Int = BookmarkListModel.BOOKMARK_PAGE_SIZE): Single<List<Bookmark>>

    /**
     * 加载下一页书签。
     */
    fun loadNextPage(page: Int, size: Int = BookmarkListModel.BOOKMARK_PAGE_SIZE): Single<List<Bookmark>>
}