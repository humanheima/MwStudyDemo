package com.humanheima.mwstudydemo;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

/**
 * Created by Administrator on 2016/9/5.
 */
public class App extends Application {
    private PushAgent mPushAgent;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        new AddTagTask("dumingwei,hm").execute();
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Log.e("extra", msg.extra.toString());
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                if (uMessage.builder_id == 1) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("My notification")
                                    .setContentText("Hello World!");
                    Log.d("Notification", "Notification");
                    return mBuilder.build();
                } else {
                    return super.getNotification(context, uMessage);
                }

            }
        };

        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    class AddTagTask extends AsyncTask<Void, Void, String> {

        String tagString;
        String[] tags;

        public AddTagTask(String tag) {
            // TODO Auto-generated constructor stub
            tagString = tag;
            tags = tagString.split(",");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                TagManager.Result result = mPushAgent.getTagManager().add(tags);
                Log.d("addTag", result.toString());
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("addTag", "onPostExecute");
        }
    }

    class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;
        String aliasType;

        public AddAliasTask(String aliasString, String aliasTypeString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
            this.aliasType = aliasTypeString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                return mPushAgent.addAlias(alias, aliasType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                Log.i("addAlias", "alias was set successfully.");
        }

    }

}
