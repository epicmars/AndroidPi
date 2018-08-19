package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.databinding.FragmentLitefreshSamplesBinding;
import com.androidpi.app.items.LiteRefreshSamples;
import com.androidpi.app.viewholder.LiteRefreshSampleViewHolder;

/**
 * Created by jastrelax on 2018/8/19.
 */
@BindLayout(R.layout.fragment_litefresh_samples)
public class LiteRefreshSamplesFragment extends BaseFragment<FragmentLitefreshSamplesBinding> {

    private RecyclerAdapter adapter;

    public static LiteRefreshSamplesFragment newInstance() {
        Bundle args = new Bundle();
        LiteRefreshSamplesFragment fragment = new LiteRefreshSamplesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(LiteRefreshSampleViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        adapter.setPayloads(LiteRefreshSamples.INSTANCE.getSamples());
    }
}
