package cn.androidpi.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by jastrelax on 2017/11/2.
 */
@Entity
class User {

    @PrimaryKey
    var id: Long? = null

    var name: String? = null
}