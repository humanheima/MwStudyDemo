package com.example.loadmoredemo.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DragRelativeLayout extends RelativeLayout {
    private final ViewDragHelper viewDragHelper;
    private View dragView;
    private boolean canDrag;//是否可以拖动
    private int hideDistance;//拖动超过一定距离后就隐藏
    private int startX;//拖动如果没有隐藏就反弹到原始的位置

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public DragRelativeLayout(Context context) {
        this(context, null);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hideDistance = 14;//距离屏幕x轴的1/3处，向右偏移14px就隐藏
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragView;//得到子view
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (canDrag) {
                    Log.d("DragLayout", "clampViewPositionHorizontal " + left + "," + child.getX());
                    if (child.getX() < hideDistance) {
                        canDrag = false;
                        setVisibility(GONE);
                    }
                    final int leftBound = getPaddingLeft();
                    final int rightBound = getWidth() - dragView.getWidth();
                    final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                    return newLeft;
                }
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (canDrag) {
                    final int topBound = getPaddingTop();
                    final int bottomBound = getHeight() - dragView.getHeight();
                    final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                    return newTop;
                }
                return 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                //当抬起手指的时候如果没有隐藏就回弹到原来的位置
                if (releasedChild == dragView && canDrag) {
                    viewDragHelper.settleCapturedViewAt(startX, 0);
                    invalidate();
                }

            }

            //给内部的view添加了点击事件以后要重写这个方法
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            //给内部的view添加了点击事件以后要重写这个方法
            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canDrag) {
            return viewDragHelper.shouldInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canDrag) {
            viewDragHelper.processTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = getChildAt(0);
    }


}
