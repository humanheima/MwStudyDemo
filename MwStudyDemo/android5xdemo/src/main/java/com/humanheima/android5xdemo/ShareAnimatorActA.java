package com.humanheima.android5xdemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAnimatorActA extends AppCompatActivity {

    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.btnExplode)
    Button btnExplode;
    @BindView(R.id.btnSlide)
    Button btnSlide;
    @BindView(R.id.btnFade)
    Button btnFade;
    @BindView(R.id.fabButton)
    Button fabButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_animator_act);
        ButterKnife.bind(this);
    }

    /**
     * 设置不同的动画效果,要去设置ShareAnimatorActB的theme里面的属性
     * <item name="android:windowContentTransitions">true</item>
     * 并在ShareAnimatorActB的onCreate（）方法中改变进入或者退出Activity的动画效果 getWindow().setEnterTransition(new Fade());
     * getWindow().setExitTransition(new Slide());
     */

    public void explode(View view) {
        intent = new Intent(this, ShareAnimatorActB.class);
        intent.putExtra("flag", 0);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    } //设置不同的动画效果

    public void slide(View view) {
        intent = new Intent(this, ShareAnimatorActB.class);
        intent.putExtra("flag", 1);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    } //设置不同的动画效果

    public void fade(View view) {
        intent = new Intent(this, ShareAnimatorActB.class);
        intent.putExtra("flag", 2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    } //设置不同的动画效果

    public void share(View view) {
        View fab = findViewById(R.id.fabButton);
        intent = new Intent(this, ShareAnimatorActB.class);
        intent.putExtra("flag", 3);
        //创建单个共享元素,第二个参数代表共享元素,第三个参数是transitionName，两个Activity中共享view的
        // transitionName要一样
        // startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, view, "share").toBundle());
        //创建多个共享元素
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(view, "share"), Pair.create(fab, "btnfab")).toBundle());
    }

    public void changeViewSize(View view) {
        TransitionManager.beginDelayedTransition(llRoot);
        setViewWidth(btnExplode, 500);
        setViewWidth(btnFade, 500);
        setViewWidth(btnSlide, 500);
    }

    private void setViewWidth(View view, int x) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = x;
        view.setLayoutParams(params);
    }
}
