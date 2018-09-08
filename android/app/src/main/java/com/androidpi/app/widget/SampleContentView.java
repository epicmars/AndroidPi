package com.androidpi.app.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class SampleContentView extends NestedScrollView {

    public SampleContentView(Context context) {
        this(context, null);
    }

    public SampleContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_content, this);
        setPhoto(R.id.item1, R.mipmap.photo1, "Photo by Jason Ortego on Unsplash");
        setPhoto(R.id.item2, R.mipmap.photo2, "Photo by Tom Pavlakos on Unsplash");
    }

    private void setPhoto(int viewId, int drawableId, String author) {
        View item = findViewById(viewId);
        ImageView photo = item.findViewById(R.id.iv_photo);
        TextView tvAuthor = item.findViewById(R.id.iv_author);
        photo.setImageResource(drawableId);
        tvAuthor.setText(author);
    }
}
