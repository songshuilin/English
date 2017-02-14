package net.naucu.englishxianshi.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class SwitchView extends View implements OnClickListener {

    /**
     * 中间圆球的直径
     */
    private int height;
    /**
     * 开关状态
     */
    private boolean isOn = true;
    private Paint mPaint;
    private RectF mRect;
    // 监听回调
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        // 消除锯齿
        mPaint.setAntiAlias(true);
        setOnClickListener(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSwitch(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(0, 0, getWidth(), getHeight());
    }

    /**
     * 根据不同状态画出开关
     */
    private void drawSwitch(Canvas canvas) {
        int color = mPaint.getColor();
        if (isOn) {
            mPaint.setColor(Color.parseColor("#00a2ed"));
            canvas.drawRoundRect(mRect, height / 2, height / 2, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(mRect.left + height / 2, mRect.top + height / 2, (height / 2 - 2), mPaint);

        } else {
            mPaint.setColor(Color.parseColor("#a5a5a5"));
            canvas.drawRoundRect(mRect, height / 2, height / 2, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(mRect.right - height / 2, mRect.top + height / 2, (height / 2 - 2), mPaint);
        }
        mPaint.setColor(color);
    }

    @Override
    public void onClick(View arg0) {
        if (isOn) {
            isOn = false;
        } else {
            isOn = true;
        }
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, isOn);
        }
        invalidate();
    }

    /**
     * 设置以及获取开关状态
     */
    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
        invalidate();
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, isOn);
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * 设置回调
     */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchView mSwitch, boolean isOn);
    }
}