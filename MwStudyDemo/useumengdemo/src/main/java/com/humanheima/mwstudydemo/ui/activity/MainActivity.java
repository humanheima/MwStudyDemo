package com.humanheima.mwstudydemo.ui.activity;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.humanheima.mwstudydemo.R;
import com.humanheima.mwstudydemo.ui.base.BaseActivity;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.button)
    Button button;
    private PushAgent pushAgent;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        pushAgent = PushAgent.getInstance(this);
        //开启推送并设置注册的回调处理
        pushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        //xiaomi ArV0fajQtzaNoFFgiGbJG17_g_oYyblw7e7AvwocKXzC
                        Log.d("device_token", registrationId);//lenovo s820:AixW486kNa1hY7mey4sN_lAOZIZI69scaOKlEBHy_RHJ

                    }
                });
            }
        });
    }

    @Override
    protected void bindEvent() {
    }


    @OnClick(R.id.button)
    public void onClick() {
        //throw new NullPointerException("nothing is wrong");
    }


}
