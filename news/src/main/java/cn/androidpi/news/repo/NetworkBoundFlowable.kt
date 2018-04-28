package cn.androidpi.news.repo

import io.reactivex.Flowable
import io.reactivex.functions.Function
import timber.log.Timber

/**
 * Created by jastrelax on 2017/12/8.
 */
abstract class NetworkBoundFlowable<Result> {

    var result: Flowable<Result>

    constructor() {
        result = loadFromDb().flatMap(object : Function<Result, Flowable<Result>> {
            override fun apply(t: Result): Flowable<Result> {
                if (shouldFetch(t)) {
                    return fetchFromNetwork(t)
                } else {
                    return updatedDbResult(t)
                }
            }
        })
    }

    fun fetchFromNetwork(dbResult: Result): Flowable<Result> {
        return createCall()
                .doOnError {
                    onFetchFailed()
                }
                .onErrorResumeNext {
                    t: Throwable -> updatedDbResult(dbResult)
                }
                .flatMap(object : Function<Result, Flowable<Result>> {
                    override fun apply(t: Result): Flowable<Result> {
                        try {
                            saveCallResult(t)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                        return loadFromDb().flatMap {
                            updatedDbResult(it)
                        }
                    }
                })
    }

    abstract fun loadFromDb(): Flowable<Result>

    abstract fun shouldFetch(dbResult: Result): Boolean

    abstract fun createCall(): Flowable<Result>

    abstract fun saveCallResult(result: Result)

    abstract fun updatedDbResult(dbResult: Result): Flowable<Result>

    fun onFetchFailed() {}

    fun getResultAsFlowable(): Flowable<Result> {
        return result
    }

}