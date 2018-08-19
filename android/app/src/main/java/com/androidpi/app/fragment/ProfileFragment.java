package com.androidpi.app.fragment;

import android.os.Bundle;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentProfileBinding;

/**
 * Created by jastrelax on 2018/8/11.
 */
@BindLayout(value = R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment<FragmentProfileBinding>{


    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
