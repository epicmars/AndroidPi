package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.databinding.FragmentUnsplashPhotoListBinding;
import com.androidpi.app.viewholder.UnsplashPhotoListViewHolder;

import java.util.Collection;

/**
 * Created by jastrelax on 2018/8/26.
 */
@BindLayout(R.layout.fragment_unsplash_photo_list)
public class UnsplashPhotoListFragment extends BaseFragment<FragmentUnsplashPhotoListBinding> {

    private RecyclerAdapter adapter;

    public static UnsplashPhotoListFragment newInstance() {

        Bundle args = new Bundle();

        UnsplashPhotoListFragment fragment = new UnsplashPhotoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoListViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    public void setPayloads(Collection<?> payloads) {
        adapter.setPayloads(payloads);
    }

    public void addPayloads(Collection<?> payloads) {
        adapter.addPayloads(payloads);
    }
}
