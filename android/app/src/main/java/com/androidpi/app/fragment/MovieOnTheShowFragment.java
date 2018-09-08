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
import com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel;
import com.androidpi.app.databinding.FragmentMovieOnTheShowBinding;
import com.androidpi.app.viewholder.MovieViewHolder;
import com.androidpi.data.remote.dto.ResMoviePage;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(R.layout.fragment_movie_on_the_show)
public class MovieOnTheShowFragment extends BaseFragment<FragmentMovieOnTheShowBinding> {

    TheMovieDbViewModel viewModel;
    RecyclerAdapter adapter;

    public static MovieOnTheShowFragment newInstance() {

        Bundle args = new Bundle();

        MovieOnTheShowFragment fragment = new MovieOnTheShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModelOfActivity(TheMovieDbViewModel.class);
        adapter = new RecyclerAdapter();
        adapter.register(MovieViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        viewModel.getMovieWithinMonthResults().observe(this, new Observer<Resource<ResMoviePage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResMoviePage> resMoviePageResource) {
                if (resMoviePageResource == null)
                    return;
                if (resMoviePageResource.isSuccess()) {
                    adapter.setPayloads(resMoviePageResource.data.getResults());
                }
            }
        });

        if (viewModel.getMovieWithinMonthResults().getValue() == null) {
            viewModel.getMovieWithinMonth();
        }
    }
}
