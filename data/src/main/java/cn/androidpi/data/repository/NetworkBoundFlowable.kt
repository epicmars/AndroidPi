package cn.androidpi.data.repository

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
                    updateDbResult(t)
                    return Flowable.just(t)
                }
            }
        })
    }

    fun fetchFromNetwork(dbResult: Result): Flowable<Result> {
        return createCall()
                .doOnError {
                    onFetchFailed()
                }
                .flatMap(object : Function<Result, Flowable<Result>> {
                    override fun apply(t: Result): Flowable<Result> {
                        try {
                            saveCallResult(t)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                        return loadFromDb()
                    }
                })
    }

    abstract fun loadFromDb(): Flowable<Result>

    abstract fun shouldFetch(dbResult: Result): Boolean

    abstract fun createCall(): Flowable<Result>

    abstract fun saveCallResult(result: Result): Boolean

    open fun updateDbResult(dbResult: Result) {}

    fun onFetchFailed() {}

    fun getResultAsFlowable(): Flowable<Result> {
        return result
    }

}