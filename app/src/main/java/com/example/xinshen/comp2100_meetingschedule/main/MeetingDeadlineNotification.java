package com.example.xinshen.comp2100_meetingschedule.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.xinshen.comp2100_meetingschedule.R;

public class MeetingDeadlineNotification extends Service {
    static Timer timer = null;

    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //添加通知
    public static void addNotification(int delayTime, String tickerText, String contentTitle, String contentText) {
        Intent intent = new Intent(MainActivity.getContext(), MeetingDeadlineNotification.class);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("contentText", contentText);
        MainActivity.getContext().startService(intent);
    }


    public void onCreate() {
        Log.e("shenxin", "========addNotification create=====");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        long period = 24 * 60 * 60 * 1000; //24小时一个周期
        int delay = intent.getIntExtra("delayTime", 0);
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i("shenxin", "Build.VERSION.SDK: " + Build.VERSION.SDK_INT);
                }
                String id = "channel_meetScheduler";//channel的id
                String description = "Meeting Scheduler Notification channel";//channel的描述信息
                int importance = NotificationManager.IMPORTANCE_LOW;//channel的重要性
                NotificationChannel channel = new NotificationChannel(id, description, importance);//生成channel
                channel.enableLights(true);
                channel.enableVibration(true);

                NotificationManager notificationManager = (NotificationManager) MainActivity.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(MainActivity.getContext());
                Intent notificationIntent = new Intent(MainActivity.getContext(), MainActivity.class);//点击跳转位置

                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.getContext(), 0, notificationIntent, 0);
                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.drawable.icon);
                builder.setTicker(intent.getStringExtra("tickerText")); //测试通知栏标题
                builder.setContentText(intent.getStringExtra("contentText")); //下拉通知啦内容
                builder.setContentTitle(intent.getStringExtra("contentTitle"));//下拉通知栏标题
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_ALL);
                notificationManager.notify(1, builder.build());
            }

        }, delay, period);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.e("shenxin", "addNotification destroy");
        super.onDestroy();
    }

    public void startNoti(long delay, final String content, final String title) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationManager mNotifyMgr = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.cancelAll();
                Notification.Builder mBuilder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String id = "channel_1";
                    NotificationChannel channel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH);
                    mNotifyMgr.createNotificationChannel(channel);
                    mBuilder = new Notification.Builder(MainActivity.getContext(), id)
                            .setCategory(Notification.CATEGORY_EVENT)
                            .setLargeIcon(BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.icon))
                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(Icon.createWithResource(MainActivity.getContext(),R.drawable.icon))
                            .setContentTitle(title)
                            .setContentText(content)
                            .setAutoCancel(true);
                } else {
                    mBuilder = new Notification.Builder(MainActivity.getContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(content);
                }
                mNotifyMgr.notify(1, mBuilder.build());
            }
        }, delay);
    }
}