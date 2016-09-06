package com.humanheima.android5xdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.humanheima.android5xdemo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UseRecyclerActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spinner)
    Spinner spinner;
    private List<String> dataList;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String spinnerData[] = {"水平", "竖直", "网格"};
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进入和退出Acitivity的动画效果，不要忘记在Activity的Theme中设置
        // <item name="android:windowContentTransitions">true</item>

        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Slide());
        setContentView(R.layout.activity_use_recycler);
        ButterKnife.bind(this);
        dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("haha" + i);
        }
        adapter = new RecyclerViewAdapter(this, dataList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UseRecyclerActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else if (position == 1) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UseRecyclerActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(UseRecyclerActivity.this, 3);
                    gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(gridLayoutManager);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
