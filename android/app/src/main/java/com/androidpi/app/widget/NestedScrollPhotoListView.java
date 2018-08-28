package com.androidpi.app.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class NestedScrollPhotoListView extends NestedScrollView {

    public NestedScrollPhotoListView(Context context) {
        this(context, null);
    }

    public NestedScrollPhotoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollPhotoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_nested_scroll_photo_list, this);
        setPhoto(R.id.item1, R.mipmap.photo1);
        setPhoto(R.id.item2, R.mipmap.photo2);
        setPhoto(R.id.item3, R.mipmap.photo3);
    }

    private void setPhoto(int viewId, int drawableId) {
        View item = findViewById(viewId);
        ImageView photo = item.findViewById(R.id.iv_image);
        photo.setImageResource(drawableId);
    }
}
