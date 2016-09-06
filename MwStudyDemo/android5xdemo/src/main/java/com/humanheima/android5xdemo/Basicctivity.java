package com.humanheima.android5xdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Basicctivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicctivity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        usePalette();
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
}
