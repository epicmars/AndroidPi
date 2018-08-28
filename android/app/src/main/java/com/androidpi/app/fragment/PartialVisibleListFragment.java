package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.databinding.FragmentPartialVisibleListBinding;
import com.androidpi.app.items.HeaderUnsplashPhoto;
import com.androidpi.app.viewholder.UnsplashPhotoHeaderViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoListViewHolder;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.Collection;
import java.util.List;

/**
 * Created by jastrelax on 2018/8/28.
 */
@BindLayout(R.layout.fragment_partial_visible_list)
public class PartialVisibleListFragment extends BaseFragment<FragmentPartialVisibleListBinding> {
    private UnsplashViewModel unsplashViewModel;
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
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoHeaderViewHolder.class);
        adapter.register(UnsplashPhotoListViewHolder.class);
        adapter.addSinglePayload(new HeaderUnsplashPhoto(null));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResUnsplashPhoto>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResUnsplashPhoto>> listResource) {
                if (listResource == null)
                    return;
                adapter.addPayloads(listResource.data);
            }
        });
    }

    public void setPayloads(Collection<?> payloads) {
        adapter.setPayloads(payloads);
    }

    public void addPayloads(Collection<?> payloads) {
        adapter.addPayloads(payloads);
    }
}
