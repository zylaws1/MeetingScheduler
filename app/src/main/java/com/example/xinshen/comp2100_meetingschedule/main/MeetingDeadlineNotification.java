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
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.xinshen.comp2100_meetingschedule.R;

// Set deadline reminding notification for meeting
public class MeetingDeadlineNotification extends Service {
    static Timer timer = null;

    // clean all notification before if necessary
    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // add a new notication
    public static void addNotification(int delayTime, String tickerText, String contentTitle, String contentText) {
        Intent intent = new Intent(MainActivity.getContext(), MeetingDeadlineNotification.class);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("contentText", contentText);
        MainActivity.getContext().startService(intent);
    }


    public void onCreate() {
        Log.e("shenxin", "on addNotification create");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    // command call back to add notification
//    public int onStartCommand(final Intent intent, int flags, int startId) {
//        long period = 24 * 60 * 60 * 1000; // 24 hours for a period
//        int delay = intent.getIntExtra("delayTime", 0);
//        if (null == timer) {
//            timer = new Timer();
//        }
//        timer.schedule(new TimerTask() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                // check if the sdk version fit and able to do channel notification
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                    Log.e("shenxin", "Build.VERSION.SDK: " + Build.VERSION.SDK_INT);
//                    return;
//                }
//                String id = "channel_meetScheduler";    //channel id
//                String description = "Meeting Scheduler Notification channel"; //channel description
//                int importance = NotificationManager.IMPORTANCE_LOW;    // channel importance
//                NotificationChannel channel = new NotificationChannel(id, description, importance); // create channel
//                channel.enableLights(true);
//                channel.enableVibration(true);
//
//                NotificationManager notificationManager = (NotificationManager) MainActivity.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.createNotificationChannel(channel); // bond channel to notificationManager
//
//                Notification.Builder builder = new Notification.Builder(MainActivity.getContext());
//                Intent notificationIntent = new Intent(MainActivity.getContext(), MainActivity.class); // jump position
//
//                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.getContext(), 0, notificationIntent, 0);
//                builder.setContentIntent(contentIntent);
//                builder.setSmallIcon(R.drawable.icon);
//                builder.setTicker(intent.getStringExtra("tickerText")); // title
//                builder.setContentText(intent.getStringExtra("contentText")); // drop content
//                builder.setContentTitle(intent.getStringExtra("contentTitle"));// drop title
//                builder.setAutoCancel(true);
//                builder.setDefaults(Notification.DEFAULT_ALL);
//                notificationManager.notify(1, builder.build());
//            }
//
//        }, delay, period);
//        return super.onStartCommand(intent, flags, startId);
//    }


    @Override
    public void onDestroy() {
        Log.e("shenxin", "addNotification destroy");
        super.onDestroy();
    }


    // Register a notification in system actively
    // delay : delay time for the notification in ms.
    // title : notification title.
    // content : notification content.
    public void startNoti(long delay, final String content, final String title) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Init a NotificationManager and clean the content;
                NotificationManager mNotifyMgr = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.cancelAll();
                Notification.Builder mBuilder;
                mBuilder = new Notification.Builder(MainActivity.getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);
                // Check if the sdk version able to do channel notification or use lower sdk method.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // channel way
                    // register and bond notification in channel
                    String id = "channel_meetingscheduler";
                    NotificationChannel channel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH);
                    mNotifyMgr.createNotificationChannel(channel);
                    // set the content for notification by meeting info
                    mBuilder = new Notification.Builder(MainActivity.getContext(), id)
                            .setCategory(Notification.CATEGORY_EVENT)
                            .setLargeIcon(BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.icon))
                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(Icon.createWithResource(MainActivity.getContext(),R.drawable.icon))
                            .setContentTitle(title)
                            .setContentText(content)
                            .setAutoCancel(true);
                }
                mNotifyMgr.notify(1, mBuilder.build());
            }
        }, delay);  //delay time in ms in system to start notification
    }
}