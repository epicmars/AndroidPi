package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.databinding.FragmentUnsplashPhotoGridBinding;
import com.androidpi.app.viewholder.ErrorViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoGridViewHolder;
import com.androidpi.app.viewholder.items.ErrorItem;

import java.util.Collection;

/**
 * Created by jastrelax on 2018/8/31.
 */
@BindLayout(R.layout.fragment_unsplash_photo_grid)
public class UnsplashPhotoGridFragment extends BaseFragment<FragmentUnsplashPhotoGridBinding> {

    RecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoGridViewHolder.class, ErrorViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    public void setPayloads(Collection<?> payloads) {
        adapter.setPayloads(payloads);
    }

    public void addPayloads(Collection<?> payloads) {
        adapter.addPayloads(payloads);
    }

    public void refreshError(Throwable throwable) {
        adapter.setPayloads(new ErrorItem(throwable.getMessage()));
    }
}
