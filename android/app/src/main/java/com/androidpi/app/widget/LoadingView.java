package com.androidpi.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/8/17.
 */
public class LoadingView extends View{

    private final float GOLDEN_RATIO = 0.618f;

    private Paint paint;
    private int centerX;
    private int centerY;
    private float gapLength;
    private float lineLength;
    private int current;
    private float strokeWidth;
    private ValueAnimator valueAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        strokeWidth = dp2px(2f);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.text_gray));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2 - strokeWidth;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        gapLength = radius * GOLDEN_RATIO;
        lineLength = radius - gapLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 12; i++) {
            paint.setAlpha(31 + (i + current) % 12 * 224 / 12);
            canvas.drawLine(centerX, centerY + gapLength, centerX, centerY + gapLength + lineLength, paint);
            canvas.rotate(-30, centerX, centerY);
        }
        postInvalidateDelayed(200);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 11);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(1000L);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    current = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        } else if (valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        valueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
