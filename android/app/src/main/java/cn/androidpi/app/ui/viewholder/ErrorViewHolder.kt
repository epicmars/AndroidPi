package cn.androidpi.app.ui.viewholder

import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderErrorBinding
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.viewholder.items.ErrorItem

/**
 * Created by jastrelax on 2017/12/7.
 */
@BindLayout(value = R.layout.view_holder_error, dataTypes = arrayOf(ErrorItem::class))
class ErrorViewHolder(itemView: View) : BaseViewHolder<ViewHolderErrorBinding>(itemView) {

    override fun <T : Any?> onBindView(data: T, position: Int) {
        if (data is ErrorItem) {
            mBinding.tvMessage.text = data.message
        }
    }
}