package com.humanheima.android5xdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 展示波纹动画效果和StateListAnimator的使用
 */
public class RippleActivity extends AppCompatActivity {

    @BindView(R.id.imgOval)
    ImageView imgOval;
    @BindView(R.id.imgRect)
    ImageView imgRect;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.btnStateListAnimator)
    Button btnStateListAnimator;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
        ButterKnife.bind(this);
    }

    public static Animator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius) {
        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        return animator;
    }

    @OnClick({R.id.imgOval, R.id.imgRect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgOval:
                Animator animator = ViewAnimationUtils.createCircularReveal(imgOval, imgOval.getWidth() / 2, imgOval.getWidth() / 2, imgOval.getWidth(), 0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(2000);
                animator.start();

                break;
            case R.id.imgRect:
                Animator animatorRect = ViewAnimationUtils.createCircularReveal(imgRect, 0, 0, 0, (float) Math.hypot(imgRect.getWidth(), imgRect.getHeight()));
                animatorRect.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorRect.setDuration(2000);
                animatorRect.start();
                break;
        }
    }

    @OnClick(R.id.btnStateListAnimator)
    public void addStateListAnimator() {
        // 也可以在xml中设置 android:stateListAnimator="@animator/statelistanimator"
        StateListAnimator animator = AnimatorInflater.loadStateListAnimator(this, R.animator.statelistanimator);
        btnStateListAnimator.setStateListAnimator(animator);


    }
}
