package com.ctvsoft.view.swipelistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Nguyen Cong Tuyen on 5/6/2016.
 */
public class SwipeStackView extends AdapterView {

    private static int TOP_VIEW_POSITION = 0;
    private static final int MAX_VISIBLE = 5;
    private Adapter mAdapter;
    private boolean mInLayout;
    private View mActiveView;
    private SwipeDetector mSwipeDetector;
    private OnSwipeActionListener mOnSwipeActionListener;
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private DataSetObserver mDataSetObserver;

    public SwipeStackView(Context context) {
        super(context);
    }

    public SwipeStackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeStackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeStackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }

        mAdapter = adapter;

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter == null) {
            return;
        }

        mInLayout = true;
        mAdapter.getCount();

        if (mAdapter.getCount() == 0) {
            removeAllViewsInLayout();
        }
        else {
            // Reset the UI and set top view listener
            removeAllViewsInLayout();
            layoutChildren(0, mAdapter.getCount());
            setTopView();
        }
        mInLayout = false;
    }

    public void setOnSwipeActionListener(OnSwipeActionListener pOnSwipeActionListener) {
        mOnSwipeActionListener = pOnSwipeActionListener;
    }

    private void setTopView() {
        if (getChildCount() > 0) {
            mActiveView = getChildAt(TOP_VIEW_POSITION);
            if (mActiveView != null) {
                mSwipeDetector = new SwipeDetector(mActiveView);
                mSwipeDetector.setOnSwipeActionListener(new OnSwipeActionListener() {
                    @Override
                    public void onSwipeFinish(int pSwipeDirection) {
                        if (mOnSwipeActionListener != null) {
                            mOnSwipeActionListener.onSwipeFinish(pSwipeDirection);
                        }
                    }
                });
                mActiveView.setOnTouchListener(mSwipeDetector);
            }
        }
    }

    private void layoutChildren(int pIndex, int pCount) {
        while (pIndex < Math.min(pCount, MAX_VISIBLE)) {
            View newUnderChild = mAdapter.getView(pIndex, null, this);
            if (newUnderChild.getVisibility() != GONE) {
                makeAndAddView(newUnderChild);
                TOP_VIEW_POSITION = pIndex;
            }
            pIndex++;
        }
    }

    private void makeAndAddView(View pChild) {
        ViewGroup.LayoutParams lp = pChild.getLayoutParams();
        addViewInLayout(pChild, 0, lp, true);
        final boolean needToMeasure = pChild.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(),
                    lp.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(),
                    lp.height);
            pChild.measure(childWidthSpec, childHeightSpec);
        }
        else {
            cleanupLayoutState(pChild);
        }
        int childW = pChild.getMeasuredWidth();
        int childH = pChild.getMeasuredHeight();
        int childLeft = (getWidth() - childW)/2;
        int childTop = (getHeight() - childH)/2;

        pChild.layout(childLeft, childTop, childLeft + lp.width, childTop + lp.height);
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }
}
