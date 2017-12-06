package cn.androidpi.app.viewmodel

import cn.androidpi.app.viewmodel.Resource.Status.*

/**
 * a generic class that describes a data with a status
 */
class Resource<T> private constructor(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    fun isError() = status == ERROR

    fun isSuccess() = status == SUCCESS

    fun isLoading() = status == LOADING

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}