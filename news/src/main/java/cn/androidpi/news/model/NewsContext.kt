package cn.androidpi.news.model

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

    var portalContext: MutableList<NewsPortalContext> = ArrayList()

    fun getPortalContext(portal: String?): NewsPortalContext? {
        if (portalContext.isEmpty())
            return null
        for (pc in portalContext) {
            if (pc.portal == portal)
                return pc
        }
        return null
    }

    fun toJson(): String? {
        return Companion.toJson(this)
    }
}

class NewsPortalContext {

    constructor(portal: String?, page: Int?) {
        this.portal = portal
        this.page = page
    }

    var portal: String? = null
    var page: Int? = null
}