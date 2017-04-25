package cn.gtgs.base.playpro.widget.SweetAlert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.pnikosis.materialishprogress.ProgressWheel;

import cn.gtgs.base.playpro.R;


public class ProgressHelper {
    private ProgressWheel mProgressWheel;
    private boolean mToSpin;
    private float mSpinSpeed;
    private int mBarWidth;
    private int mBarColor;
    private int mRimWidth;
    private int mRimColor;
    private boolean mIsInstantProgress;
    private float mProgressVal;
    private int mCircleRadius;

    public ProgressHelper(Context ctx) {
        mToSpin = true;
        mSpinSpeed = 0.75f;
        mBarWidth = ctx.getResources().getDimensionPixelSize(R.dimen.common_circle_width) + 1;
        mBarColor = ctx.getResources().getColor(R.color.success_stroke_color);
        mRimWidth = 0;
        mRimColor = 0x00000000;
        mIsInstantProgress = false;
        mProgressVal = -1;
        mCircleRadius = ctx.getResources().getDimensionPixelOffset(R.dimen.progress_circle_radius);
    }

    public ProgressWheel getProgressWheel () {
        return mProgressWheel;
    }

    public void setProgressWheel (ProgressWheel progressWheel) {
        mProgressWheel = progressWheel;
        updatePropsIfNeed();
    }

    private void updatePropsIfNeed () {
        if (mProgressWheel != null) {
            if (!mToSpin && mProgressWheel.isSpinning()) {
                mProgressWheel.stopSpinning();
            } else if (mToSpin && !mProgressWheel.isSpinning()) {
                mProgressWheel.spin();
            }
            if (mSpinSpeed != mProgressWheel.getSpinSpeed()) {
                mProgressWheel.setSpinSpeed(mSpinSpeed);
            }
            if (mBarWidth != mProgressWheel.getBarWidth()) {
                mProgressWheel.setBarWidth(mBarWidth);
            }
            if (mBarColor != mProgressWheel.getBarColor()) {
                mProgressWheel.setBarColor(mBarColor);
            }
            if (mRimWidth != mProgressWheel.getRimWidth()) {
                mProgressWheel.setRimWidth(mRimWidth);
            }
            if (mRimColor != mProgressWheel.getRimColor()) {
                mProgressWheel.setRimColor(mRimColor);
            }
            if (mProgressVal != mProgressWheel.getProgress()) {
                if (mIsInstantProgress) {
                    mProgressWheel.setInstantProgress(mProgressVal);
                } else {
                    mProgressWheel.setProgress(mProgressVal);
                }
            }
            if (mCircleRadius != mProgressWheel.getCircleRadius()) {
                mProgressWheel.setCircleRadius(mCircleRadius);
            }
        }
    }

    public void resetCount() {
        if (mProgressWheel != null) {
            mProgressWheel.resetCount();
        }
    }

    public boolean isSpinning() {
        return mToSpin;
    }

    public void spin() {
        mToSpin = true;
        updatePropsIfNeed();
    }

    public void stopSpinning() {
        mToSpin = false;
        updatePropsIfNeed();
    }

    public float getProgress() {
        return mProgressVal;
    }

    public void setProgress(float progress) {
        mIsInstantProgress = false;
        mProgressVal = progress;
        updatePropsIfNeed();
    }

    public void setInstantProgress(float progress) {
        mProgressVal = progress;
        mIsInstantProgress = true;
        updatePropsIfNeed();
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * @param circleRadius units using pixel
     * **/
    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
        updatePropsIfNeed();
    }

    public int getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(int barWidth) {
        mBarWidth = barWidth;
        updatePropsIfNeed();
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setBarColor(int barColor) {
        mBarColor = barColor;
        updatePropsIfNeed();
    }

    public int getRimWidth() {
        return mRimWidth;
    }

    public void setRimWidth(int rimWidth) {
        mRimWidth = rimWidth;
        updatePropsIfNeed();
    }

    public int getRimColor() {
        return mRimColor;
    }

    public void setRimColor(int rimColor) {
        mRimColor = rimColor;
        updatePropsIfNeed();
    }

    public float getSpinSpeed() {
        return mSpinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        mSpinSpeed = spinSpeed;
        updatePropsIfNeed();
    }

    public static class SuccessTickView extends View {
        private float mDensity = -1;
        private Paint mPaint;
        private final float CONST_RADIUS = dip2px(1.2f);
        private final float CONST_RECT_WEIGHT = dip2px(3);
        private final float CONST_LEFT_RECT_W = dip2px(15);
        private final float CONST_RIGHT_RECT_W = dip2px(25);
        private final float MIN_LEFT_RECT_W = dip2px(3.3f);
        private final float MAX_RIGHT_RECT_W = CONST_RIGHT_RECT_W + dip2px(6.7f);

        private float mMaxLeftRectWidth;
        private float mLeftRectWidth;
        private float mRightRectWidth;
        private boolean mLeftRectGrowMode;

        public SuccessTickView(Context context) {
            super(context);
            init();
        }

        public SuccessTickView(Context context, AttributeSet attrs){
            super(context,attrs);
            init();
        }

        private void init () {
            mPaint = new Paint();
            mPaint.setColor(getResources().getColor(R.color.success_stroke_color));
            mLeftRectWidth = CONST_LEFT_RECT_W;
            mRightRectWidth = CONST_RIGHT_RECT_W;
            mLeftRectGrowMode = false;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            int totalW = getWidth();
            int totalH = getHeight();
            // rotate canvas first
            canvas.rotate(45, totalW / 2, totalH / 2);

            totalW /= 1.2;
            totalH /= 1.4;
            mMaxLeftRectWidth = (totalW + CONST_LEFT_RECT_W) / 2 + CONST_RECT_WEIGHT - 1;

            RectF leftRect = new RectF();
            if (mLeftRectGrowMode) {
                leftRect.left = 0;
                leftRect.right = leftRect.left + mLeftRectWidth;
                leftRect.top = (totalH + CONST_RIGHT_RECT_W) / 2;
                leftRect.bottom = leftRect.top + CONST_RECT_WEIGHT;
            } else {
                leftRect.right = (totalW + CONST_LEFT_RECT_W) / 2 + CONST_RECT_WEIGHT - 1;
                leftRect.left = leftRect.right - mLeftRectWidth;
                leftRect.top = (totalH + CONST_RIGHT_RECT_W) / 2;
                leftRect.bottom = leftRect.top + CONST_RECT_WEIGHT;
            }

            canvas.drawRoundRect(leftRect, CONST_RADIUS, CONST_RADIUS, mPaint);

            RectF rightRect = new RectF();
            rightRect.bottom = (totalH + CONST_RIGHT_RECT_W) / 2 + CONST_RECT_WEIGHT - 1;
            rightRect.left = (totalW + CONST_LEFT_RECT_W) / 2;
            rightRect.right = rightRect.left + CONST_RECT_WEIGHT;
            rightRect.top = rightRect.bottom - mRightRectWidth;
            canvas.drawRoundRect(rightRect, CONST_RADIUS, CONST_RADIUS, mPaint);
        }

        public float dip2px(float dpValue) {
            if(mDensity == -1) {
                mDensity = getResources().getDisplayMetrics().density;
            }
            return dpValue * mDensity + 0.5f;
        }

        public void startTickAnim () {
            // hide tick
            mLeftRectWidth = 0;
            mRightRectWidth = 0;
            invalidate();
            Animation tickAnim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    if (0.54 < interpolatedTime && 0.7 >= interpolatedTime) {  // grow left and right rect to right
                        mLeftRectGrowMode = true;
                        mLeftRectWidth = mMaxLeftRectWidth * ((interpolatedTime - 0.54f) / 0.16f);
                        if (0.65 < interpolatedTime) {
                            mRightRectWidth = MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65f) / 0.19f);
                        }
                        invalidate();
                    } else if (0.7 < interpolatedTime && 0.84 >= interpolatedTime) { // shorten left rect from right, still grow right rect
                        mLeftRectGrowMode = false;
                        mLeftRectWidth = mMaxLeftRectWidth * (1 - ((interpolatedTime - 0.7f) / 0.14f));
                        mLeftRectWidth = mLeftRectWidth < MIN_LEFT_RECT_W ? MIN_LEFT_RECT_W : mLeftRectWidth;
                        mRightRectWidth = MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65f) / 0.19f);
                        invalidate();
                    } else if (0.84 < interpolatedTime && 1 >= interpolatedTime) { // restore left rect width, shorten right rect to const
                        mLeftRectGrowMode = false;
                        mLeftRectWidth = MIN_LEFT_RECT_W + (CONST_LEFT_RECT_W - MIN_LEFT_RECT_W) * ((interpolatedTime - 0.84f) / 0.16f);
                        mRightRectWidth = CONST_RIGHT_RECT_W + (MAX_RIGHT_RECT_W - CONST_RIGHT_RECT_W) * (1 - ((interpolatedTime - 0.84f) / 0.16f));
                        invalidate();
                    }
                }
            };
            tickAnim.setDuration(750);
            tickAnim.setStartOffset(100);
            startAnimation(tickAnim);
        }
    }
}
