package com.androidpi.app.viewholder;

import android.support.v4.app.Fragment;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderLiteRefreshSampleBinding;
import com.androidpi.app.fragment.FragmentFactory;
import com.androidpi.app.items.LiteRefreshSample;

/**
 * Created by jastrelax on 2018/8/19.
 */
@BindLayout(value = R.layout.view_holder_lite_refresh_sample, dataTypes = LiteRefreshSample.class)
public class LiteRefreshSampleViewHolder extends BaseViewHolder<ViewHolderLiteRefreshSampleBinding> {

    public LiteRefreshSampleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        if (data instanceof LiteRefreshSample) {
            LiteRefreshSample liteRefreshSample = (LiteRefreshSample) data;
            binding.tvTitle.setText(liteRefreshSample.getTitle());
            binding.tvDescription.setText(liteRefreshSample.getDescription());

            itemView.setOnClickListener(v -> {
                TemplateActivity.Companion.startWith(itemView.getContext(), 0, liteRefreshSample.getFragmentClass().getName(),
                        new FragmentFactory<Fragment>() {
                            @Override
                            public Fragment create() {
                                return Fragment.instantiate(itemView.getContext(), liteRefreshSample.getFragmentClass().getName());
                            }
                        });
            });
        }
    }
}
