package com.humanheima.android5xdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.btnShowNotification)
    Button btnShowNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btnShowNotification)
    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        long[]pattern={500,500,500,500};//震动时长，间隔时长，震动时长，间隔时长
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.argb(0,255,255,255),1000,1000)
                //.setVibrate(pattern)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("notification title")
                .setContentText("i am the content")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE);
        // Creates an explicit intent for an Activity in your app
        //Intent resultIntent = new Intent(this, ShareAnimatorActB.class);
        Intent resultIntent = new Intent();
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.setClass(this, ShareAnimatorActA.class);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        mNotificationManager.notify(1, builder.build());
    }
}
