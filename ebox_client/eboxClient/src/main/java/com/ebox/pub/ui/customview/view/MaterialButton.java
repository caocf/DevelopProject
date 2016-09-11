package com.ebox.pub.ui.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.ebox.R;


public class MaterialButton extends Button implements MaterialBackgroundDetector.Callback {
    private MaterialBackgroundDetector mDetector;

    public MaterialButton(Context context) {
        super(context);
        init(null, 0);
    }

    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaterialButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MaterialTextView, defStyle, 0);
        int color = a.getColor(R.styleable.MaterialTextView_shadowColor, MaterialBackgroundDetector.DEFAULT_COLOR);
        a.recycle();
        mDetector = new MaterialBackgroundDetector(getContext(), this, this, color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDetector.onSizeChanged(w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mDetector.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superResult = super.onTouchEvent(event);
        return mDetector.onTouchEvent(event, superResult);
    }

    public void cancelAnimator() {
        mDetector.cancelAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        Drawable[] drawables = getCompoundDrawables();
//        if (drawables != null)
//        {
//            Drawable drawableLeft = drawables[0];
//            if (drawableLeft != null) {
//                float textWidth = getPaint().measureText(getText().toString());
//                int drawablePadding = getCompoundDrawablePadding();
//                int drawableWidth = drawableLeft.getIntrinsicWidth();
//                float bodyWidth = textWidth + drawableWidth + drawablePadding;
//                canvas.translate((getWidth() - bodyWidth) / 2, 0);
//            }
//        }

        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
    }

    /**
     * When the view performClick, we should ensure the background animation appears.
     * So we will handle the action in mDetector;
     *
     * @return
     */
    @Override
    public boolean performClick() {
        return mDetector.handlePerformClick();
    }

    /**
     * When the view performClick, we should ensure the background animation appears.
     * So we will handle the action in mDetector;
     *
     * @return
     */
    @Override
    public boolean performLongClick() {
        return mDetector.handlePerformLongClick();
    }


    @Override
    public void performClickAfterAnimation() {
        super.performClick();
    }

    @Override
    public void performLongClickAfterAnimation() {
        super.performLongClick();
    }
}
