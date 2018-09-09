package com.androidpi.app.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.viewholder.SampleUnsplashPhotoViewHolder;
import com.androidpi.app.viewholder.items.SampleUnsplashPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class SampleContentView extends FrameLayout {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private static final List<SampleUnsplashPhoto> photos = new ArrayList<SampleUnsplashPhoto>(){
        {
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
        }
    };

    public SampleContentView(Context context) {
        this(context, null);
    }

    public SampleContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_content, this);
        recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.register(SampleUnsplashPhotoViewHolder.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (recyclerAdapter.getPayloads().isEmpty()) {
            recyclerAdapter.setPayloads(photos);
        }
    }
}
