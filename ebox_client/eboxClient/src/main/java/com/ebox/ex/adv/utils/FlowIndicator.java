package com.ebox.ex.adv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ebox.R;

@SuppressLint("DrawAllocation")
public class FlowIndicator extends View {
	private int mCount;// Բ��������
	private	int mSpace;// ���
	private	int mRadious;// �뾶
	private	int mNormalColor;//
	private	int mFocusColor;// ������ɫ
	private	int mFocus;// ��������ֵ

	public FlowIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ��ȡ��������
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.Pub_FlowIndicator);
		// ��ȡ����
		mCount = array.getInt(R.styleable.Pub_FlowIndicator_count, 5);
		mRadious = array.getDimensionPixelOffset(
				R.styleable.Pub_FlowIndicator_radius, 8);
		mNormalColor = array.getColor(R.styleable.Pub_FlowIndicator_normal_color,
				0xccffcc);
		mFocusColor = array.getColor(R.styleable.Pub_FlowIndicator_focus_color,
				0xff3366);
		mSpace = array.getDimensionPixelOffset(R.styleable.Pub_FlowIndicator_space,
				10);
		array.recycle(); 
	}

	public void setFocus(int focus) {
		mFocus = focus;
		invalidate();
	}

	public void setCount(int count) {
		this.mCount = count;
		invalidate();
	}

	public int getCount() {
		return mCount;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measuredWidth(widthMeasureSpec),
				measuredHeight(heightMeasureSpec));
	}

	private int measuredHeight(int heightMeasureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = getTopPaddingOffset() + getBottomPaddingOffset() + 2
					* mRadious;
			result = Math.min(result, specSize);
		}

		return result;
	}

	private int measuredWidth(int widthMeasureSpec) {
		int result = 0;
		// ��ȡ�������
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		// ��ȡϵͳ�����Ŀ��
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// ����Ǿ��Ȳ����Ĺ��
			result = specSize;
		} else {
			// �����ģ�������񣬼�warp_content
			result = getLeftPaddingOffset() + getRightPaddingOffset() + 2
					* mCount * mRadious + (mCount - 1) * mSpace;
			result = Math.min(result, specSize);
		}
		return result;
	}

	// ����ͼ��
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// ������߾�
		int leftSpace = (getWidth() - 2 * mRadious * mCount - (mCount - 1)
				* mSpace) / 2;
		// ���λ���������ɫ
		for (int i = 0; i < mCount; i++) {
			int color = i == mFocus ? mFocusColor : mNormalColor;
			paint.setColor(color);
			canvas.drawCircle(getLeftPaddingOffset() + leftSpace + i
					* (2 * mRadious + mSpace), getHeight() / 2, mRadious, paint);
		}

	}
}
