package net.naucu.englishxianshi.widget.view.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import net.naucu.englishxianshi.R;

public class LoadingView extends View {

    //indicators
    public static final int BallSpinFadeLoader = 0;

    @IntDef(flag = true,
            value = {
                    BallSpinFadeLoader
            })
    public @interface Indicator {
    }

    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 45;

    //attrs
    int mIndicatorId;
    int mIndicatorColor;

    Paint mPaint;

    IndicatorController mIndicatorController;

    private boolean mHasAnimation;


    public LoadingView(Context context) {
        super(context);
        init(null, 0);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mIndicatorColor = a.getColor(R.styleable.LoadingView_loan_color, Color.WHITE);
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    private void applyIndicator() {
        switch (mIndicatorId) {
            case BallSpinFadeLoader:
                mIndicatorController = new SpinFadeLoaderIndicator();
                break;
        }
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    void drawIndicator(Canvas canvas) {
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation() {
        mIndicatorController.createAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }


}
