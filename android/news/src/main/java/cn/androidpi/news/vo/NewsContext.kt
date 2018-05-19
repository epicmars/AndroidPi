package cn.androidpi.news.vo

import cn.androidpi.common.json.GsonHelper
import com.google.gson.JsonSyntaxException

/**
 * Created by jastrelax on 2017/12/19.
 */
class NewsContext {

    companion object {
        fun toJson(newsContext: NewsContext?): String? {
            return GsonHelper.gson().toJson(newsContext)
        }

        fun fromJson(newsContextString: String?): NewsContext? {
            try {
                return GsonHelper.gson().fromJson(newsContextString, NewsContext::class.java)
            } catch (e: JsonSyntaxException) {
                return null
            }
        }
    }

    var portalContextList: MutableList<NewsPortalContext> = ArrayList()

    fun addPortalContext(portalContext: NewsPortalContext) {
        if (!portalContextList.contains(portalContext))
            portalContextList.add(portalContext)
    }

    fun getPortalContext(portal: String?): NewsPortalContext? {
        if (portalContextList.isEmpty())
            return null
        for (pc in portalContextList) {
            if (pc.portal == portal)
                return pc
        }
        return null
    }

    fun toJson(): String? {
        return toJson(this)
    }
}

class NewsPortalContext {

    constructor(portal: String?, page: Int) {
        this.portal = portal
        this.page = page
    }

    var portal: String? = null
    var page: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsPortalContext

        if (portal != other.portal) return false
        if (page != other.page) return false

        return true
    }

    override fun hashCode(): Int {
        var result = portal?.hashCode() ?: 0
        result = 31 * result + (page)
        return result
    }


}