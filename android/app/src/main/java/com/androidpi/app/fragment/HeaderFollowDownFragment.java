package com.androidpi.app.fragment;

import android.os.Bundle;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentHeaderFollowDownBinding;

/**
 * Created by jastrelax on 2018/8/11.
 */
@BindLayout(value = R.layout.fragment_header_follow_down)
public class HeaderFollowDownFragment extends BaseFragment<FragmentHeaderFollowDownBinding>{

    public static HeaderFollowDownFragment newInstance() {

        Bundle args = new Bundle();

        HeaderFollowDownFragment fragment = new HeaderFollowDownFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
