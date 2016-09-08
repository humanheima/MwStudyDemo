package com.example.loadmoredemo;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.loadmoredemo.adapter.MyAdapter;
import com.example.loadmoredemo.listener.OnItemClickListener;
import com.example.loadmoredemo.listener.OnLoadMoreListener;
import com.example.loadmoredemo.listener.OnScrollListener;
import com.example.loadmoredemo.widget.DragRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout.LayoutParams layoutParams;
    RelativeLayout.LayoutParams innerLayoutParams;
    MyAdapter adapter;
    List<String> dataList = new ArrayList<>();
    int page = 1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rlRemove)
    DragRelativeLayout rlMove;//可移动布局
    @BindView(R.id.imgView)
    ImageView imageView;
    @BindView(R.id.myPtrFrameLayout)
    PtrClassicFrameLayout myPtrFrameLayout;
    @BindView(R.id.rlParent)
    RelativeLayout rlParent;//根布局
    private int offsetY;//在竖直方向上滚动的距离
    private int selectedPosition = -1;//用来标志RecyclerView的那一项被选中，
    private int marginTop;//距屏幕顶部的距离
    private boolean isBig = true;
    private int screenHeight;
    private int screenWidth;

    private int normalHeight;//正常高度
    private int normalWidth;//正常宽度
    private int smallHeight;//小高度
    private int smallWidth;//小宽度
    private int smallMarginTop;//缩小情况下距顶部的高度
    private int normalMarginTop;//正常情况下rlMove到达屏幕底部的时候距离屏幕顶部的高度
    private double ratio;//宽比高的比例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            //检查是否可以刷新
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return offsetY == 0 && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            //下拉刷新的时候会调用这个方法，每次下拉刷新都要把page重置为1,嗨哟奥吧rlMove布局恢复到初始化状态
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                resetRlMove();
                page = 1;

                if (adapter != null) {
                    adapter.setLoadAll(false);
                }
                getData();
            }
        });
        /**
         * 延迟500毫秒后ptrFrameLayout自动刷新会调用checkCanDoRefresh(PtrFrameLayout frame, View content, View header)
         * 检查是否可以刷新，如果可以，就调用onRefreshBegin(PtrFrameLayout frame)
         */
        myPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                myPtrFrameLayout.autoRefresh();
            }
        }, 500);

        //初始化高度
        rlParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (screenHeight == 0) {
                    screenHeight = rlParent.getHeight();
                    screenWidth = rlParent.getWidth();
                    normalWidth = screenWidth;
                    rlMove.setStartX(normalWidth / 4);
                    normalHeight = ScreenUtil.dp2px(200);
                    ratio = (double) normalWidth / normalHeight;
                    Log.e("ratio", "" + ratio);
                    smallWidth = screenWidth / 2;
                    smallHeight = normalHeight / 2;
                    smallMarginTop = screenHeight - smallHeight;
                    normalMarginTop = screenHeight - normalHeight;
                    Log.e("screenHeight", "screenHeight=" + screenHeight + ",screenWidth=" + screenWidth + ",normalWidth=" + normalWidth + ",normalHeight=" + normalHeight +
                            ",smallWidth=" + smallWidth + ",smallHeight=" + smallHeight + ",smallMarginTop=" + smallMarginTop);
                }
            }
        });
        layoutParams = (RelativeLayout.LayoutParams) rlMove.getLayoutParams();
        innerLayoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
    }

    /**
     * 把可移动布局恢复到原始状态
     */
    private void resetRlMove() {
        if (selectedPosition != -1) {
            selectedPosition = -1;
            marginTop = 0;
            isBig = true;
        }
        if (rlMove.getVisibility() == View.VISIBLE) {
            layoutParams.setMargins(0, marginTop, 0, 0);
            layoutParams.height = normalHeight;
            layoutParams.width = normalWidth;
            rlMove.setLayoutParams(layoutParams);

            innerLayoutParams.setMargins(0, 0, 0, 0);
            innerLayoutParams.height = normalHeight;
            innerLayoutParams.width = normalWidth;
            imageView.setLayoutParams(innerLayoutParams);

            rlMove.setVisibility(View.GONE);
        }

    }

    /**
     * 给recyclerView设置适配器
     */
    public void setAdapter() {
        Log.e("tag", "size" + dataList.size());
        if (adapter == null) {
            adapter = new MyAdapter(recyclerView, dataList, new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //每次上拉加载更多之前要把page++
                    page++;
                    //每次上拉加载更多之前设置setLoadAll(false)
                    if (adapter != null) {
                        adapter.setLoadAll(false);
                    }
                    getData();
                }
            });
            adapter.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScroll(int dx, int dy) {
                    offsetY += dy;
                    Log.e("onScroll", "marginTop" + marginTop);//向上滑动的时候 offsetY值变大，向下滑动的时候offsetY值变小
                    if (selectedPosition != -1) {
                        marginTop = selectedPosition * adapter.getItemHeight() - offsetY;
                        changeLocation();//要求改变位置
                    }

                }

                @Override
                public void onScrollStateChanged(int newState) {

                }


            });
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                    if (selectedPosition == position) {
                        resetRlMove();
                    } else {
                        rlMove.setCanDrag(false);
                        selectedPosition = position;
                        marginTop = selectedPosition * adapter.getItemHeight() - offsetY;
                        Log.e("onItemClick", "marginTop =" + marginTop);
                        showRlRemove();
                    }


                }
            });
            recyclerView.setAdapter(adapter);
        }
        //更新适配器
        adapter.reset();
        //如果正在下拉刷新的话，结束下拉刷新
        if (myPtrFrameLayout.isRefreshing()) {
            myPtrFrameLayout.refreshComplete();
        }
    }

    /**
     * 改变rlMove的位置
     */
    private void changeLocation() {
        Log.e("changeLocation", "margintop=" + marginTop);

        if (marginTop <= -adapter.getItemHeight()) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            //Log.e("changeLocation", "第1种情况");
            startAnimation();
        } else if (marginTop > -adapter.getItemHeight() && marginTop < smallMarginTop) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);

            if (marginTop > screenHeight - normalHeight) {
                // 不应该是动画，应该是动态改变宽和高以及距顶部的距离
                Log.e("changeLocation", "第1种情况");
                isBig = true;
                layoutParams.height = screenHeight - marginTop;
                layoutParams.width = (int) ((screenHeight - marginTop) * ratio * 1.5F);
                layoutParams.topMargin = marginTop;
                rlMove.setLayoutParams(layoutParams);

                innerLayoutParams.setMargins(0, 0, 0, 0);
                innerLayoutParams.height = screenHeight - marginTop;
                innerLayoutParams.width = (int) ((screenHeight - marginTop) * ratio);
                imageView.setLayoutParams(innerLayoutParams);

            } else {
                isBig = true;
                Log.e("changeLocation", "第2种情况");
                layoutParams.setMargins(0, marginTop, 0, 0);
                layoutParams.height = normalHeight;
                layoutParams.width = normalWidth;
                rlMove.setLayoutParams(layoutParams);
                innerLayoutParams.setMargins(0, 0, 0, 0);
                innerLayoutParams.height = normalHeight;
                innerLayoutParams.width = normalWidth;
                imageView.setLayoutParams(innerLayoutParams);

            }
            rlMove.setCanDrag(false);
        } else {
            isBig = false;
            rlMove.setCanDrag(true);
            //第三种情况结束后要进行判断一下,是不是已经缩小到最小状态了

            final int currentWidth = layoutParams.width;
            final int currentHeight = layoutParams.height;
            final int currentMargin = layoutParams.topMargin;
            //剩余变化的量
            final int changeWidth = (int) (currentWidth - normalWidth * 0.75F);//剩余变化的宽度
            final int changeHeight = currentHeight - smallHeight;//剩余变化的高度
            final int changeMargin = smallMarginTop - currentMargin;//剩余变化的marginTop
            if (changeWidth > 0 || changeHeight > 0 || changeMargin > 0) {
                //rlMove内部view的宽高和margin
                final int currentInnerWidth = innerLayoutParams.width;
                final int currentInnerHeight = innerLayoutParams.height;
                final int currentInnerLeftMargin = innerLayoutParams.leftMargin;
                //内部剩余变化的量
                final int changeInnerWidth = currentInnerWidth - smallWidth;//剩余变化的宽度
                final int changeInnerHeight = currentInnerHeight - smallHeight;//剩余变化的高度
                Log.e("changeLocation", "第3种情况，currentWidth" + currentWidth + ",currentHeight=" + currentHeight + ",currentMargin" + currentMargin + ",innerWidth=" + currentInnerWidth
                        + ",innerHeight=" + currentInnerHeight + ",innerLeftMargin" + currentInnerLeftMargin);
                //为了解决底部缝隙问题，必须动态调整layoutParams是否靠底部
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                //动画的时间
                final float RATIO = 1.0f * (currentWidth / (normalWidth * 0.75F));
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = (float) animation.getAnimatedValue();
                        layoutParams.width = (int) (currentWidth - changeWidth * fraction);//最终宽度为4/3屏幕宽度
                        layoutParams.height = (int) (currentHeight - changeHeight * fraction);
                        layoutParams.topMargin = (int) (currentMargin + (changeMargin * fraction));
                        rlMove.setLayoutParams(layoutParams);
                        innerLayoutParams.width = (int) (currentInnerWidth - changeInnerWidth * fraction);
                        innerLayoutParams.height = (int) (currentInnerHeight - changeInnerHeight * fraction);
                        imageView.setLayoutParams(innerLayoutParams);
                       /* if (fraction >= 1) {
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                        }*/
                    }
                });
                if (RATIO > 0) {
                    valueAnimator.setDuration((long) (500 * RATIO));
                    valueAnimator.start();
                }

            }
        }

    }

    /**
     * 视频移到右下角
     */
    private void startAnimation() {

        if (isBig) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    layoutParams.width = (int) (normalWidth - smallWidth / 2 * fraction);//最终宽度为4/3屏幕宽度
                    layoutParams.height = (int) (normalHeight - smallHeight * fraction);
                    //layoutParams.leftMargin = (int) (smallWidth * fraction);
                    layoutParams.topMargin = (int) ((smallMarginTop) * fraction);
                    rlMove.setLayoutParams(layoutParams);

                    innerLayoutParams.width = (int) (normalWidth - smallWidth / 2 * fraction);
                    innerLayoutParams.height = (int) (normalHeight - smallHeight * fraction);
                    innerLayoutParams.leftMargin = (int) (smallWidth / 2 * fraction);
                    imageView.setLayoutParams(innerLayoutParams);
                }
            });
            isBig = false;
            rlMove.setCanDrag(true);
            valueAnimator.setDuration(800);
            valueAnimator.start();

        }

    }

    private void showRlRemove() {
        layoutParams.setMargins(0, marginTop, 0, 0);
        layoutParams.height = normalHeight;
        layoutParams.width = normalWidth;
        rlMove.setLayoutParams(layoutParams);
        innerLayoutParams.setMargins(0, 0, 0, 0);
        innerLayoutParams.height = normalHeight;
        innerLayoutParams.width = normalWidth;
        imageView.setLayoutParams(innerLayoutParams);

        rlMove.setVisibility(View.VISIBLE);
    }

    public void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page == 1) {
                    dataList.clear();
                    for (int i = 0; i < 25; i++) {
                        dataList.add("string" + i);
                    }
                    setAdapter();
                } else {
                    if (page > 3) {
                        if (adapter != null) {
                            adapter.setLoadAll(true);
                        }
                    } else {
                        for (int i = 22; i < 33; i++) {
                            dataList.add("string" + i);
                        }
                        setAdapter();
                    }
                }
            }
        }, 2000);


    }

    @OnClick(R.id.imgView)
    public void startAct() {
        if (!isBig && rlMove.getWidth() <= normalWidth * 0.75 + 10) {
            startActivity(new Intent(MainActivity.this, ViewDragHelperActivity.class));
        } else {
            resetRlMove();
        }

    }


}
