package com.ebox.ex.adv.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Auto Scroll View Pager
 * <ul>
 * <strong>Basic Setting and Usage</strong>
 * <li>{@link #startAutoScroll()} start auto scroll, or {@link #startAutoScroll(int)} start auto scroll delayed</li>
 * <li>{@link #stopAutoScroll()} stop auto scroll</li>
 * <li>{@link #setInterval(long)} set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}</li>
 * </ul>
 * <ul>
 * <strong>Advanced Settings and Usage</strong>
 * <li>{@link #setDirection(int)} set auto scroll direction</li>
 * <li>{@link #setCycle(boolean)} set whether automatic cycle when auto scroll reaching the last or first item, default
 * is true</li>
 * <li>{@link #setSlideBorderMode(int)} set how to process when sliding at the last or first item</li>
 * <li>{@link #setStopScrollWhenTouch(boolean)} set whether stop auto scroll when touching, default is true</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-30
 */
public class AutoScrollViewPager extends ViewPager {

    public static final int        DEFAULT_INTERVAL            = 1500;

    public static final int        LEFT                        = 0;
    public static final int        RIGHT                       = 1;

    /** do nothing when sliding at the last or first item **/
    public static final int        SLIDE_BORDER_MODE_NONE      = 0;
    /** cycle when sliding at the last or first item **/
    public static final int        SLIDE_BORDER_MODE_CYCLE     = 1;
    /** deliver event to parent when sliding at the last or first item **/
    public static final int        SLIDE_BORDER_MODE_TO_PARENT = 2;

    /** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} **/
    private long                   interval                    = DEFAULT_INTERVAL;
    /** auto scroll direction, default is {@link #RIGHT} **/
    private int                    direction                   = RIGHT;
    /** whether automatic cycle when auto scroll reaching the last or first item, default is true **/
    private boolean                isCycle                     = true;
    /** whether stop auto scroll when touching, default is true **/
    private boolean                stopScrollWhenTouch         = true;
    /** how to process when sliding at the last or first item, default is {@link #SLIDE_BORDER_MODE_NONE} **/
    private int                    slideBorderMode             = SLIDE_BORDER_MODE_NONE;
    /** whether animating when auto scroll at the last or first item **/
    private boolean                isBorderAnimation           = false;
    /** scroll factor for auto scroll animation, default is 1.0 **/
    private double                 autoScrollFactor            = 1.0;
    /** scroll factor for swipe scroll animation, default is 1.0 **/
    private double                 swipeScrollFactor           = 1.0;

    private Handler                handler;
    private boolean                isAutoScroll                = false;
    private boolean                isStopByTouch               = false;
    private float                  touchX                      = 0f, downX = 0f;
    private CustomDurationScroller scroller                    = null;

    public static final int        SCROLL_WHAT                 = 0;

    
    private FlowIndicator mIndicator;
    private HashMap<Integer, Object> mObjs = new LinkedHashMap<Integer, Object>();
    
    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new MyHandler();
        setViewPagerScroller();
    }

    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
    	if (mIndicator!=null) 
    	{
    		 int count = getAdapter().getCount();
    		 if (count==1) {
				mIndicator.setVisibility(View.INVISIBLE);
			}else {
				mIndicator.setVisibility(View.VISIBLE);
			}
    		 mIndicator.setCount(count);
		}
        isAutoScroll = true;
        sendScrollMessage((long) (interval + scroller.getDuration()/ autoScrollFactor * swipeScrollFactor));
    }

    /**
     * start auto scroll
     * 
     * @param delayTimeInMills first scroll delay time
     */
    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    /**
     * set the factor by which the duration of sliding animation will change while swiping
     */
    public void setSwipeScrollDurationFactor(double scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling
     */
    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator)interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
//        if (mIndicator!=null) 
//		{
//			mIndicator.setFocus(currentItem);
//		}
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            return;
        }

        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * <ul>
     * if stopScrollWhenTouch is true
     * <li>if event is down, stop auto scroll.</li>
     * <li>if event is up, start auto scroll again.</li>
     * </ul>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
            touchX = ev.getX();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = touchX;
            }
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null ? 0 : adapter.getCount();
            /**
             * current index is first one and slide to right or current index is last one and slide to left.<br/>
             * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
             * else scroll to last one when current item is first one, scroll to first one when current item is last
             * one.
             */
            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.dispatchTouchEvent(ev);
            }
        }
        getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    scroller.setScrollDurationFactor(autoScrollFactor);
                    scrollOnce();
                    scroller.setScrollDurationFactor(swipeScrollFactor);
                    sendScrollMessage(interval + scroller.getDuration());
                default:
                    break;
            }
        }
    }

    /**
     * get auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     * 
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     * 
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * get auto scroll direction
     * 
     * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    /**
     * set auto scroll direction
     * 
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * whether automatic cycle when auto scroll reaching the last or first item, default is true
     * 
     * @return the isCycle
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * set whether automatic cycle when auto scroll reaching the last or first item, default is true
     * 
     * @param isCycle the isCycle to set
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * whether stop auto scroll when touching, default is true
     * 
     * @return the stopScrollWhenTouch
     */
    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    /**
     * set whether stop auto scroll when touching, default is true
     * 
     * @param stopScrollWhenTouch
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    /**
     * get how to process when sliding at the last or first item
     * 
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
     *         {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return slideBorderMode;
    }

    /**
     * set how to process when sliding at the last or first item
     * 
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
     *        {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }

    /**
     * whether animating when auto scroll at the last or first item, default is true
     * 
     * @return
     */
    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }

    /**
     * set whether animating when auto scroll at the last or first item, default is true
     * 
     * @param isBorderAnimation
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }

	public FlowIndicator getmIndicator() {
		return mIndicator;
	}

	public void setmIndicator(FlowIndicator mIndicator) {
		this.mIndicator = mIndicator;
	}
	
	public void setObjectForPosition(Object obj, int position) {
		mObjs.put(Integer.valueOf(position), obj);
	}
	
	public View findViewFromObject(int position) {
		Object o = mObjs.get(Integer.valueOf(position));
		if (o == null) {
			return null;
		}
		PagerAdapter a = getAdapter();
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (a.isViewFromObject(v, o))
				return v;
		}
		return null;
	}
	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	@Override
	protected void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;
		View left = findViewFromObject(position);
		View right = findViewFromObject(position+1);
		animateTablet(left, right, effectOffset);
	 //	animateStack(left, right, positionOffset, positionOffsetPixels);
	//	animateZoom(left, right, positionOffsetPixels, true);
	//	animateCube(left, right, positionOffsetPixels, true);
		if (mIndicator!=null&&isSmall(positionOffset)) 
		{
			mIndicator.setFocus(position);
		}
	}
	private static final float SCALE_MAX = 0.5f;
	private static final float ZOOM_MAX = 0.5f;
	private static final float ROT_MAX = 15.0f;
	
	private void animateCube(View left, View right, float positionOffset, boolean in) {
			if (left != null) {
				//manageLayer(left, true);
				mRot = (in ? 90.0f : -90.0f) * positionOffset;
				ViewHelper.setPivotX(left, left.getMeasuredWidth());
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(left, mRot);
			}
			if (right != null) {
			//	manageLayer(right, true);
				mRot = -(in ? 90.0f : -90.0f) * (1-positionOffset);
				ViewHelper.setPivotX(right, 0);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(right, mRot);
			}
	}
	
	private void animateZoom(View left, View right, float positionOffset, boolean in) {
			if (left != null) {
			//	manageLayer(left, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*(1-positionOffset) :
					1+ZOOM_MAX - ZOOM_MAX*(1-positionOffset);
				ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(left, mScale);
				ViewHelper.setScaleY(left, mScale);
			}
			if (right != null) {
			//	manageLayer(right, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*positionOffset :
					1+ZOOM_MAX - ZOOM_MAX*positionOffset;
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
			}
	}
	
	protected void animateStack(View left, View right, float positionOffset, int positionOffsetPixels) {		
			if (right != null) {
			//	manageLayer(right, true);
				mScale = (1-SCALE_MAX) * positionOffset + SCALE_MAX;
				mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
				ViewHelper.setTranslationX(right, mTrans);
			}
			if (left != null) {
				left.bringToFront();
			}
	}
	private void manageLayer(View v, boolean enableHardware)
	{
		int layerType = enableHardware ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		if (layerType != v.getLayerType())
			v.setLayerType(layerType, null);
	}
	private float mRot;
	private float mTrans;
	private float mScale;
	protected void animateTablet(View left, View right, float positionOffset) {		
		if (left != null) {
			manageLayer(left, true);
			mRot = 40.0f * positionOffset;
			/*mTrans = getOffsetXForRotation(mRot, left.getMeasuredWidth(),
					left.getMeasuredHeight());*/
			ViewHelper.setPivotX(left, left.getMeasuredWidth()/2);
			ViewHelper.setPivotY(left, left.getMeasuredHeight()/2);
			//ViewHelper.setTranslationX(left, mTrans);
			//ViewHelper.setRotationY(left, mRot);
		}
		if (right != null) {
			manageLayer(right, true);
			mRot = -40.0f * (1-positionOffset);
			/*mTrans = getOffsetXForRotation(mRot, right.getMeasuredWidth(), 
					right.getMeasuredHeight());*/
			ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
			ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
			//ViewHelper.setTranslationX(right, mTrans);
			//ViewHelper.setRotationY(right, mRot);
		}
	}
	private Matrix mMatrix = new Matrix();
	private Camera mCamera = new Camera();
	private float[] mTempFloat2 = new float[2];

	protected float getOffsetXForRotation(float degrees, int width, int height) {
		mMatrix.reset();
		mCamera.save();
		mCamera.rotateY(Math.abs(degrees));
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-width * 0.5f, -height * 0.5f);
		mMatrix.postTranslate(width * 0.5f, height * 0.5f);
		mTempFloat2[0] = width;
		mTempFloat2[1] = height;
		mMatrix.mapPoints(mTempFloat2);
		return (width - mTempFloat2[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
	}
	protected void animateFade(View left, View right, float positionOffset) {
		if (left != null) {
			ViewHelper.setAlpha(left, 1-positionOffset);
		}
		if (right != null) {
			ViewHelper.setAlpha(right, positionOffset);
		}
	}
	
	
}
