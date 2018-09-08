package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderLoadingBinding;
import com.androidpi.app.viewholder.items.LoadingItem;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(value = R.layout.view_holder_loading, dataTypes = LoadingItem.class)
public class LoadingViewHolder extends BaseViewHolder<ViewHolderLoadingBinding> {

    public LoadingViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(Object data, int position) {

    }
}
