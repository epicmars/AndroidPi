package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentLiteRefreshPagerBinding;
import com.androidpi.app.items.LiteRefreshSamples;

/**
 * Created by jastrelax on 2018/8/22.
 */
@BindLayout(R.layout.fragment_lite_refresh_pager)
public class LiteRefreshPagerFragment extends BaseFragment<FragmentLiteRefreshPagerBinding> {

    public static LiteRefreshPagerFragment newInstance() {

        Bundle args = new Bundle();

        LiteRefreshPagerFragment fragment = new LiteRefreshPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPager.setAdapter(new LiteRefreshSamplePagerAdapter(getChildFragmentManager()));
    }

    private class LiteRefreshSamplePagerAdapter extends FragmentPagerAdapter {

        public LiteRefreshSamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(getContext(), LiteRefreshSamples.INSTANCE.getSamples().get(position).getFragmentClass().getName());
        }

        @Override
        public int getCount() {
            return LiteRefreshSamples.INSTANCE.getSamples().size();
        }
    }
}
