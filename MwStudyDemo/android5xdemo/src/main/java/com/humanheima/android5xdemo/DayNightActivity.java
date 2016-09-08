package com.humanheima.android5xdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayNightActivity extends AppCompatActivity {

    @BindView(R.id.btnUseDayTheme)
    Button btnUseDayTheme;
    @BindView(R.id.btnUseNightTheme)
    Button btnUseNightTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_night);
        ButterKnife.bind(this);
    }
    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES); // 选择你需要的
    }

    @OnClick({R.id.btnUseDayTheme, R.id.btnUseNightTheme})
    public void changeTheme(View view) {
        switch (view.getId()) {
            case R.id.btnUseDayTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
            case R.id.btnUseNightTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                break;
        }
    }
}
