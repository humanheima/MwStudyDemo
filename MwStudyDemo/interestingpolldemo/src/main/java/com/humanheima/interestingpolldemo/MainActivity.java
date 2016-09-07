package com.humanheima.interestingpolldemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    List<String> titleList;
    List<Integer> imgIdList;
    List<Fragment> fragmentList;
    final static int size = 4;
    int DELAY = 2400;
    int currPos;//当前的fragment
    boolean isAuto = true;
    boolean isFromUser = false;
    ViewPagerAdapter adapter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.imgPoint1)
    ImageView imgPoint1;
    @BindView(R.id.imgPoint2)
    ImageView imgPoint2;
    @BindView(R.id.imgPoint3)
    ImageView imgPoint3;
    @BindView(R.id.imgPoint4)
    ImageView imgPoint4;
    private List<ImageView> imageViewList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (isAuto) {
                    currPos = (currPos + 1) % size;
                    viewPager.setCurrentItem(currPos, true);
                    handler.sendEmptyMessageDelayed(1, DELAY);
                } else {
                    handler.sendEmptyMessageDelayed(1, DELAY);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置全屏*/
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        titleList = new ArrayList<>();
        imgIdList = new ArrayList<>();
        imageViewList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add("英");
        titleList.add("吹");
        titleList.add("思");
        titleList.add("婷");

        imgIdList.add(R.drawable.ying);
        imgIdList.add(R.drawable.chui);
        imgIdList.add(R.drawable.si);
        imgIdList.add(R.drawable.ting);

        imageViewList.add(imgPoint1);
        imageViewList.add(imgPoint2);
        imageViewList.add(imgPoint3);
        imageViewList.add(imgPoint4);
        for (int i = 0; i < size; i++) {
            ImageFragment fragment = ImageFragment.newInstance(titleList.get(i), imgIdList.get(i));
            fragmentList.add(fragment);
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(size);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("case", "onPageSelected");
                currPos = position;
                for (int i = 0; i < size; i++) {
                    if (i == position) {
                        imageViewList.get(i).setBackgroundResource(R.drawable.dot);
                    } else {
                        imageViewList.get(i).setBackgroundResource(R.drawable.dot_gray);

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        Log.e("case", "case 0 SCROLL_STATE_IDLE");
                        isAuto = true;
                        if (isFromUser) {
                            isFromUser = false;
                            handler.sendEmptyMessageDelayed(1, DELAY);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        Log.e("case", "CASE 1 SCROLL_STATE_DRAGGING");
                        isAuto = false;
                        handler.removeMessages(1);
                        isFromUser = true;
                        break;
                    default:
                        break;
                }
            }
        });

        imageViewList.get(0).setBackgroundResource(R.drawable.dot);
        //执行定时任务
         handler.sendEmptyMessageDelayed(1, DELAY);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler=null;
    }
}
