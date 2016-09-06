package com.humanheima.android5xdemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    boolean changeZ;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.textClipping1)
    TextView textClipping1;
    @BindView(R.id.textClipping2)
    TextView textClipping2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicctivity);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(BasicActivity.this, UseRecyclerActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BasicActivity.this).toBundle());
            }
        });

        //usePalette();
        testClipping();
    }

    //使用Palette通过加载的图片改变状态栏和ActionBar的色调
    public void usePalette() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.skd);
        //创建Palette对象
        Palette.Builder builder = new Palette.Builder(bitmap);
        Palette palette = builder.generate();
        //充满活力的黑
        Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
        //充满活力的
        Palette.Swatch vibrant = palette.getVibrantSwatch();
        //充满活力的亮
        Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
        //柔和的
        Palette.Swatch muted = palette.getMutedSwatch();
        //柔和的黑
        Palette.Swatch mutedDark = palette.getLightMutedSwatch();
        //柔和的亮
        Palette.Swatch mutedLight = palette.getLightMutedSwatch();
        toolbar.setBackgroundColor(vibrant.getRgb());
        getWindow().setStatusBarColor(vibrant.getRgb());
    }


    @OnClick(R.id.button1)
    public void changeZheight() {
        //改变视图在Z轴上的高度
        if (changeZ) {
            changeZ = false;
            button1.animate().translationZ(0f);
        } else {
            changeZ = true;
            button1.animate().translationZ(100f);
        }
    }

    //测试剪裁功能,没有android:elevation="1dp"是不行的
    public void testClipping() {

        ViewOutlineProvider vp1 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 10);
            }
        };
        textClipping1.setOutlineProvider(vp1);
        ViewOutlineProvider vp2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        };
        textClipping2.setOutlineProvider(vp2);
    }
}
