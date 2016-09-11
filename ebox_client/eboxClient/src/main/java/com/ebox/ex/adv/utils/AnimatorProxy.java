package com.ebox.ex.adv.utils;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public final class AnimatorProxy extends Animation
{
  public static final boolean NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK).intValue() < 11;

  private static final WeakHashMap<View, AnimatorProxy> PROXIES = new WeakHashMap();
  private final WeakReference<View> mView;
  private final Camera mCamera = new Camera();
  private boolean mHasPivot;
  private float mAlpha = 1.0F;
  private float mPivotX;
  private float mPivotY;
  private float mRotationX;
  private float mRotationY;
  private float mRotationZ;
  private float mScaleX = 1.0F;
  private float mScaleY = 1.0F;
  private float mTranslationX;
  private float mTranslationY;
  private final RectF mBefore = new RectF();
  private final RectF mAfter = new RectF();
  private final Matrix mTempMatrix = new Matrix();

  public static AnimatorProxy wrap(View view)
  {
    AnimatorProxy proxy = (AnimatorProxy)PROXIES.get(view);

    if ((proxy == null) || (proxy != view.getAnimation())) {
      proxy = new AnimatorProxy(view);
      PROXIES.put(view, proxy);
    }
    return proxy;
  }

  private AnimatorProxy(View view)
  {
    setDuration(0L);
    setFillAfter(true);
    view.setAnimation(this);
    this.mView = new WeakReference(view);
  }

  public float getAlpha() {
    return this.mAlpha;
  }
  public void setAlpha(float alpha) {
    if (this.mAlpha != alpha) {
      this.mAlpha = alpha;
      View view = (View)this.mView.get();
      if (view != null)
        view.invalidate();
    }
  }

  public float getPivotX() {
    return this.mPivotX;
  }
  public void setPivotX(float pivotX) {
    if ((!this.mHasPivot) || (this.mPivotX != pivotX)) {
      prepareForUpdate();
      this.mHasPivot = true;
      this.mPivotX = pivotX;
      invalidateAfterUpdate();
    }
  }

  public float getPivotY() {
    return this.mPivotY;
  }
  public void setPivotY(float pivotY) {
    if ((!this.mHasPivot) || (this.mPivotY != pivotY)) {
      prepareForUpdate();
      this.mHasPivot = true;
      this.mPivotY = pivotY;
      invalidateAfterUpdate();
    }
  }

  public float getRotation() {
    return this.mRotationZ;
  }
  public void setRotation(float rotation) {
    if (this.mRotationZ != rotation) {
      prepareForUpdate();
      this.mRotationZ = rotation;
      invalidateAfterUpdate();
    }
  }

  public float getRotationX() {
    return this.mRotationX;
  }
  public void setRotationX(float rotationX) {
    if (this.mRotationX != rotationX) {
      prepareForUpdate();
      this.mRotationX = rotationX;
      invalidateAfterUpdate();
    }
  }

  public float getRotationY() {
    return this.mRotationY;
  }

  public void setRotationY(float rotationY) {
    if (this.mRotationY != rotationY) {
      prepareForUpdate();
      this.mRotationY = rotationY;
      invalidateAfterUpdate();
    }
  }

  public float getScaleX() {
    return this.mScaleX;
  }
  public void setScaleX(float scaleX) {
    if (this.mScaleX != scaleX) {
      prepareForUpdate();
      this.mScaleX = scaleX;
      invalidateAfterUpdate();
    }
  }

  public float getScaleY() {
    return this.mScaleY;
  }
  public void setScaleY(float scaleY) {
    if (this.mScaleY != scaleY) {
      prepareForUpdate();
      this.mScaleY = scaleY;
      invalidateAfterUpdate();
    }
  }

  public int getScrollX() {
    View view = (View)this.mView.get();
    if (view == null) {
      return 0;
    }
    return view.getScrollX();
  }
  public void setScrollX(int value) {
    View view = (View)this.mView.get();
    if (view != null)
      view.scrollTo(value, view.getScrollY());
  }

  public int getScrollY() {
    View view = (View)this.mView.get();
    if (view == null) {
      return 0;
    }
    return view.getScrollY();
  }
  public void setScrollY(int value) {
    View view = (View)this.mView.get();
    if (view != null)
      view.scrollTo(view.getScrollX(), value);
  }

  public float getTranslationX()
  {
    return this.mTranslationX;
  }
  public void setTranslationX(float translationX) {
    if (this.mTranslationX != translationX) {
      prepareForUpdate();
      this.mTranslationX = translationX;
      invalidateAfterUpdate();
    }
  }

  public float getTranslationY() {
    return this.mTranslationY;
  }
  public void setTranslationY(float translationY) {
    if (this.mTranslationY != translationY) {
      prepareForUpdate();
      this.mTranslationY = translationY;
      invalidateAfterUpdate();
    }
  }

  public float getX() {
    View view = (View)this.mView.get();
    if (view == null) {
      return 0.0F;
    }
    return view.getLeft() + this.mTranslationX;
  }
  public void setX(float x) {
    View view = (View)this.mView.get();
    if (view != null)
      setTranslationX(x - view.getLeft());
  }

  public float getY() {
    View view = (View)this.mView.get();
    if (view == null) {
      return 0.0F;
    }
    return view.getTop() + this.mTranslationY;
  }
  public void setY(float y) {
    View view = (View)this.mView.get();
    if (view != null)
      setTranslationY(y - view.getTop());
  }

  private void prepareForUpdate()
  {
    View view = (View)this.mView.get();
    if (view != null)
      computeRect(this.mBefore, view);
  }

  private void invalidateAfterUpdate() {
    View view = (View)this.mView.get();
    if ((view == null) || (view.getParent() == null)) {
      return;
    }

    RectF after = this.mAfter;
    computeRect(after, view);
    after.union(this.mBefore);

    ((View)view.getParent()).invalidate((int)Math.floor(after.left), (int)Math.floor(after.top), (int)Math.ceil(after.right), (int)Math.ceil(after.bottom));
  }

  private void computeRect(RectF r, View view)
  {
    float w = view.getWidth();
    float h = view.getHeight();

    r.set(0.0F, 0.0F, w, h);

    Matrix m = this.mTempMatrix;
    m.reset();
    transformMatrix(m, view);
    this.mTempMatrix.mapRect(r);

    r.offset(view.getLeft(), view.getTop());

    if (r.right < r.left) {
      float f = r.right;
      r.right = r.left;
      r.left = f;
    }
    if (r.bottom < r.top) {
      float f = r.top;
      r.top = r.bottom;
      r.bottom = f;
    }
  }

  private void transformMatrix(Matrix m, View view) {
    float w = view.getWidth();
    float h = view.getHeight();
    boolean hasPivot = this.mHasPivot;
    float pX = hasPivot ? this.mPivotX : w / 2.0F;
    float pY = hasPivot ? this.mPivotY : h / 2.0F;

    float rX = this.mRotationX;
    float rY = this.mRotationY;
    float rZ = this.mRotationZ;
    if ((rX != 0.0F) || (rY != 0.0F) || (rZ != 0.0F)) {
      Camera camera = this.mCamera;
      camera.save();
      camera.rotateX(rX);
      camera.rotateY(rY);
      camera.rotateZ(-rZ);
      camera.getMatrix(m);
      camera.restore();
      m.preTranslate(-pX, -pY);
      m.postTranslate(pX, pY);
    }

    float sX = this.mScaleX;
    float sY = this.mScaleY;
    if ((sX != 1.0F) || (sY != 1.0F)) {
      m.postScale(sX, sY);
      float sPX = -(pX / w) * (sX * w - w);
      float sPY = -(pY / h) * (sY * h - h);
      m.postTranslate(sPX, sPY);
    }

    m.postTranslate(this.mTranslationX, this.mTranslationY);
  }

  protected void applyTransformation(float interpolatedTime, Transformation t)
  {
    View view = (View)this.mView.get();
    if (view != null) {
      t.setAlpha(this.mAlpha);
      transformMatrix(t.getMatrix(), view);
    }
  }
}