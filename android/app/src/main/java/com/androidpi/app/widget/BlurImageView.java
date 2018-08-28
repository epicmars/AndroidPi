package com.androidpi.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class BlurImageView extends AppCompatImageView implements ViewTreeObserver.OnPreDrawListener{

    BlurFactor blurFactor;
    BitmapDrawable blurred;

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    public boolean onPreDraw() {
        if (blurFactor == null) {
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            blurFactor = new BlurFactor();
            blurFactor.width = bitmap.getWidth();
            blurFactor.height = bitmap.getHeight();
            blurFactor.radius = 25;
            blurFactor.sampling = 4;
            blurred = new BitmapDrawable(getContext().getResources(), Blur.of(getContext(), bitmap, blurFactor));
            setImageDrawable(blurred);
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
    }
}
