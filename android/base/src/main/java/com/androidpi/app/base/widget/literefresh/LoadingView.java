package com.androidpi.app.base.widget.literefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.androidpi.app.pi.base.R;


/**
 * Created by jastrelax on 2018/8/17.
 */
public class LoadingView extends View{

    private static final int STATE_IDLE = 0;
    private static final int STATE_ON_PROGRESS = 1;
    private static final int STATE_ON_LOADING = 2;
    private static final int STATE_FINISH = 3;

    private final float GOLDEN_RATIO = 0.618f;

    private Paint paint;
    private int color;
    private int centerX;
    private int centerY;
    private float gapLength;
    private float lineLength;
    private int current;
    private float strokeWidth;
    private ValueAnimator valueAnimator;
    private int progress;
    private int state = STATE_IDLE;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0);
        color = a.getColor(R.styleable.LoadingView_lr_color, context.getResources().getColor(R.color.text_gray));
        a.recycle();

        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        strokeWidth = width / 12;
        float radius = width / 2 - strokeWidth;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        gapLength = radius * GOLDEN_RATIO;
        lineLength = radius - gapLength;

        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == STATE_ON_LOADING || state == STATE_IDLE || state == STATE_FINISH) {
            for (int i = 0; i < 12; i++) {
                paint.setAlpha(31 + (i + current) % 12 * 224 / 12);
                canvas.drawLine(centerX, centerY + gapLength, centerX, centerY + gapLength + lineLength, paint);
                canvas.rotate(-30, centerX, centerY);
            }
        } else if (state == STATE_ON_PROGRESS) {
            for (int i = 11; i >= 0; i--) {
                if (i < 11 - progress) {
                    paint.setAlpha(0);
                } else {
                    paint.setAlpha(31 + 11 * 224 / 12);
                }
                canvas.drawLine(centerX, centerY + gapLength, centerX, centerY + gapLength + lineLength, paint);
                canvas.rotate(30, centerX, centerY);
            }
        }
    }

    public void startProgress() {
        state = STATE_IDLE;
    }

    public void setProgress(float percent) {
        if (state < STATE_ON_LOADING) {
            percent = MathUtils.clamp(percent, 0f, 1f);
            progress = Math.round(percent * 11);
            state = STATE_ON_PROGRESS;
            invalidate();
        }
    }

    public void startLoading() {
        state = STATE_ON_LOADING;
        invalidate();
    }

    public void finishLoading() {
        state = STATE_FINISH;
        invalidate();
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
