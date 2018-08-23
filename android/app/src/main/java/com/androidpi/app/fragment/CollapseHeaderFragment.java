package com.androidpi.app.fragment;

import android.os.Bundle;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentCollapseHeaderBinding;

/**
 * Created by jastrelax on 2018/8/23.
 */
@BindLayout(R.layout.fragment_collapse_header)
public class CollapseHeaderFragment extends BaseFragment<FragmentCollapseHeaderBinding> {

    public static CollapseHeaderFragment newInstance() {

        Bundle args = new Bundle();

        CollapseHeaderFragment fragment = new CollapseHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
