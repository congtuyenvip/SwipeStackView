package com.ctvsoft.view.swipelistview;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Nguyen Cong Tuyen on 5/6/2016.
 */
public class SwipeDetector implements View.OnTouchListener {
    private static final int SWIPE_ACTION_BASE = 0;
    public static final int SWIPE_LEFT = SWIPE_ACTION_BASE + 1;
    public static final int SWIPE_RIGHT = SWIPE_ACTION_BASE + 2;
    public static final int SWIPE_UP = SWIPE_ACTION_BASE + 3;
    public static final int SWIPE_DOWN = SWIPE_ACTION_BASE + 4;
    public static final int DURATION = 200;

    private View mView;
    private float mLastX;
    private float mLastY;
    private float mViewXPos;
    private float mViewYPos;
    private boolean mIsGotViewPos = false;
    private boolean mIsTouching = false;
    private OnSwipeActionListener mOnSwipeActionListener;

    public SwipeDetector(View pView) {
        mView = pView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mIsGotViewPos) {
            mViewXPos = mView.getX();
            mViewYPos = mView.getY();
            mIsGotViewPos = true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouching = true;
                mLastX = event.getX();
                mLastY = event.getY();
                mView.setX(mViewXPos);
                mView.setY(mViewYPos);
                mView.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                mIsTouching = true;
                mView.setX(mView.getX() + event.getX() - mLastX);
                mView.setY(mView.getY() + event.getY() - mLastY);
                mView.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                if (mView.getX() - mViewXPos > mView.getWidth() / 4) {
                    startRightDismissAnimation();
                }
                else if (mViewXPos - mView.getX() > mView.getWidth() / 4) {
                    startLeftDismissAnimation();
                }
                else if (mView.getY() - mViewYPos > mView.getHeight() / 4) {
                    startDownDismissAnimation();
                }
                else if (mViewYPos - mView.getY() > mView.getHeight() / 4) {
                    startUpDismissAnimation();
                }
                else {
                    mView.animate().setDuration(DURATION).setInterpolator(new OvershootInterpolator(1.5f)).x(mViewXPos).y(mViewYPos).start();
                }
                mView.getParent().requestDisallowInterceptTouchEvent(true);
                mIsTouching = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsTouching = false;
                break;
        }
        return true;
    }

    private void startDownDismissAnimation() {
        mView.animate().setDuration(DURATION).alpha(0).setInterpolator(new OvershootInterpolator(1.5f)).x(mViewXPos).y(((View) mView.getParent()).getBottom()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnSwipeActionListener != null) {
                    mOnSwipeActionListener.onSwipeFinish(SWIPE_DOWN);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void startUpDismissAnimation() {
        mView.animate().setDuration(DURATION).alpha(0).setInterpolator(new OvershootInterpolator(1.5f)).x(mViewXPos).y(((View) mView.getParent()).getTop()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnSwipeActionListener != null) {
                    mOnSwipeActionListener.onSwipeFinish(SWIPE_UP);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void startLeftDismissAnimation() {
        mView.animate().setDuration(DURATION).alpha(0).setInterpolator(new OvershootInterpolator(1.5f)).x(((View) mView.getParent()).getLeft()).y(mViewYPos).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnSwipeActionListener != null) {
                    mOnSwipeActionListener.onSwipeFinish(SWIPE_LEFT);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void startRightDismissAnimation() {
        mView.animate().setDuration(DURATION).alpha(0).setInterpolator(new OvershootInterpolator(1.5f)).x(((View) mView.getParent()).getRight()).y(mViewYPos).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnSwipeActionListener != null) {
                    mOnSwipeActionListener.onSwipeFinish(SWIPE_RIGHT);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    public boolean isTouching() {
        return mIsTouching;
    }

    public void setOnSwipeActionListener(OnSwipeActionListener pOnSwipeActionListener) {
        mOnSwipeActionListener = pOnSwipeActionListener;
    }
}