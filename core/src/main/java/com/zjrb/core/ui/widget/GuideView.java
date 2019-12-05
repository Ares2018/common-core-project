package com.zjrb.core.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zjrb.core.BuildConfig;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.utils.UIUtils;

/**
 * Created by lixinke on 2017/10/24.
 */
public class GuideView extends FrameLayout {
    private static final String TAG = "GuideView";
    private Builder mNext;

    private GuideView(@NonNull Context context, Builder next) {
        super(context);
        mNext = next;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            if (mNext != null) {
                mNext.build();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public interface GravityStrategy {
        void showGuideView(View guideView, View anchorView, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom);
    }

    static class BottomGravity implements GravityStrategy {
        @Override
        public void showGuideView(View guideView, View anchorView, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            if (guideView == null) return;

            Rect rect = getRect(anchorView);
            int bottom=rect.bottom-paddingBottom;
            guideView.layout(0, bottom - guideView.getHeight(), guideView.getWidth(), bottom);
            ((View) guideView.getParent()).invalidate();
        }
    }

    static class TopGravity implements GravityStrategy {
        @Override
        public void showGuideView(View guideView, View anchorView, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            if (guideView == null) return;

            Rect rect = getRect(anchorView);
            int top = rect.top - paddingTop;
            guideView.layout(0, top, guideView.getWidth(), top + guideView.getHeight());
            ((View) guideView.getParent()).invalidate();
        }
    }


    static class RightGravity implements GravityStrategy {
        @Override
        public void showGuideView(View guideView, View anchorView, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            if (guideView == null) return;

            Rect rect = getRect(anchorView);
            guideView.layout(rect.right - guideView.getWidth(), rect.top, rect.right, rect.top + guideView.getHeight());
            ((View) guideView.getParent()).invalidate();
        }
    }

    static class LeftGravity implements GravityStrategy {
        @Override
        public void showGuideView(View guideView, View anchorView, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
            if (guideView == null) return;

            Rect rect = getRect(anchorView);
            guideView.layout(rect.left, rect.top, rect.left + guideView.getWidth(), rect.top + guideView.getHeight());
            ((View) guideView.getParent()).invalidate();
        }
    }

    @NonNull
    private static Rect getRect(View anchorView) {

        if (anchorView == null) {
            return new Rect();
        }

        Rect rect = new Rect();
        anchorView.getGlobalVisibleRect(rect);
        return rect;
    }

    public static class Builder implements ViewTreeObserver.OnGlobalLayoutListener, OnAttachStateChangeListener {
        private Activity mActivity;
        private View mAnchorView;
        private int mResId;
        private String mTag;
        private boolean isHide = false;
        private boolean isDestroy = false;

        private GravityStrategy mGravityStrategy;
        private Builder mNext;
        private ImageView mImageView;
        private GuideView mGuideView;
        private OnGuideListener mOnGuideListener;

        private int mPaddingLeft;
        private int mPaddingTop;
        private int mPaddingRight;
        private int mPaddingBottom;
        private ImageView.ScaleType mScale;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setAnchorView(View view) {
            mAnchorView = view;
            return this;
        }

        public Builder setGuideResource(@DrawableRes int resId) {
            mResId = resId;
            return this;
        }

        public Builder setTag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder setScale(ImageView.ScaleType scale) {
            mScale = scale;
            return this;
        }

        public Builder setGravity(int gravity) {
            mGravityStrategy = new RightGravity();
            switch (gravity) {
                case Gravity.LEFT:
                    mGravityStrategy = new LeftGravity();
                    break;
                case Gravity.BOTTOM:
                    mGravityStrategy = new BottomGravity();
                    break;
                case Gravity.TOP:
                    mGravityStrategy = new TopGravity();
                    break;
            }
            return this;
        }

        public Builder setNext(Builder next) {
            mNext = next;
            return this;
        }

        public Builder setGuidePadding(int left, int top, int right, int bottom) {
            mPaddingLeft = left;
            mPaddingTop = top;
            mPaddingRight = right;
            mPaddingBottom = bottom;
            return this;
        }

        public Builder setOnGuideListener(OnGuideListener onGuideListener) {
            mOnGuideListener = onGuideListener;
            return this;
        }

        public void build() {
            if (isShowGuide(mTag) && !isHide && !isDestroy && mActivity != null && mActivity.getResources() != null && mActivity.getResources().getDisplayMetrics() != null) {
                Drawable bitmap = mActivity.getResources().getDrawable(mResId);
                if (bitmap == null) {
                    if (BuildConfig.DEBUG) {
                        throw new NullPointerException("请调用方法setGuideResource(@DrawableRes int resId)设置引导图片图片");
                    }
                    return;
                }

                if (TextUtils.isEmpty(mTag)) {
                    if (BuildConfig.DEBUG) {
                        throw new NullPointerException("请调用setTag(String tag)设置引导图的唯一标识");
                    }
                    return;
                }

                if (mAnchorView == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "anchor view is null!");
                    }
                    return;
                }

                Rect rect = new Rect();
                mAnchorView.getGlobalVisibleRect(rect);
                if (rect.centerX() <= 0 || rect.centerY() <= 0) {
                    return;
                }

                mGuideView = new GuideView(mActivity, mNext);
                mImageView = new ImageView(mActivity);

                mImageView.setImageDrawable(bitmap);
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImageView.setAdjustViewBounds(true);

                MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mGuideView.addView(mImageView, -1, params);

                mGuideView.setBackgroundColor(Color.parseColor("#b2000000"));
                ((ViewGroup) mActivity.getWindow().getDecorView()).addView(mGuideView);

                mGuideView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                mGuideView.addOnAttachStateChangeListener(this);
                hideGuide(mTag);
            } else if (!isShowGuide(mTag) || isHide) {
                if (mOnGuideListener != null) {
                    mOnGuideListener.onFinish();
                }
                if (mNext != null) {
                    mNext.hide(true);
                    mNext.build();
                }
            }
        }

        @Override
        public void onGlobalLayout() {
            mGravityStrategy.showGuideView(mImageView, mAnchorView, UIUtils.dip2px(mPaddingLeft), UIUtils.dip2px(mPaddingTop), UIUtils.dip2px(mPaddingRight), UIUtils.dip2px(mPaddingBottom));
            mGuideView.bringToFront();
            mGuideView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }

        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            mImageView.setImageBitmap(null);
            mImageView = null;
            mGuideView = null;
            mAnchorView = null;
            mActivity = null;
            mNext = null;
            isDestroy = true;

            if (mOnGuideListener != null) {
                mOnGuideListener.onFinish();
            }
        }

        public void hide(boolean isHide) {
            this.isHide = isHide;
        }
    }


    public interface OnGuideListener {
        void onFinish();
    }

    private static boolean isShowGuide(String tag) {
        return SPHelper.get().get(tag, true);
    }

    private static void hideGuide(String tag) {
        SPHelper.get().put(tag, false).commit();
    }
}
