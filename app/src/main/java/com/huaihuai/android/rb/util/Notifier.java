package com.huaihuai.android.rb.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.huaihuai.android.rb.app.MyApplication;
import com.huaihuai.android.rb.R;
import com.huaihuai.android.rb.view.MainActivity;

/**
 * Notification
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Notifier {

    private static Notifier instance = null;

    private NotificationManager notificationManager;

    private static Object INSTANCE_LOCK = new Object();

    public static final int TYPE_WECHAT_SERVICE_RUNNING = 1;

    private static final String TAG = "Notifier";

    Intent mLauncherIntent = null;
    Notification notification = null;

    int count = 0;

    public static Notifier getInstance() {
        if (instance == null)
            synchronized (INSTANCE_LOCK) {
                if (instance == null) {
                    instance = new Notifier();
                }
            }
        return instance;
    }

    private Notifier() {
        this.notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 清除所有通知
     */
    public void cleanAll() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public void cancelByType(int type) {
        if (notificationManager != null) {
            notificationManager.cancel(type);
        }
    }

    public static void main(String[] args) {
        String s = "坏坏: [微信红包]恭喜发财，大吉大利！ ";
        String str = "[微信红包]恭喜发财，大吉大利！ ";
        System.out.print(s.contains(str));
    }

    /**
     */
    public void notify(String title, String message, String tickerText, int type, boolean canClear) {
        try {
            Context context = MyApplication.getInstance();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Intent intent = new Intent();
            PendingIntent contentIntent = null;
            switch (type) {
                case TYPE_WECHAT_SERVICE_RUNNING:
                    intent.setClass(context, MainActivity.class);
                    contentIntent = PendingIntent.getActivity(context, TYPE_WECHAT_SERVICE_RUNNING, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    break;
            }

            builder.setSmallIcon(R.drawable.ic_launcher)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setTicker(tickerText).setWhen(System.currentTimeMillis())
                    .setContentTitle(title).setContentText(message);
            if (Build.VERSION.SDK_INT >= 16) {// Android 4.1之后才有
                builder.setPriority(Notification.PRIORITY_MAX);
            }

            if (contentIntent != null) {
                Notification nt = builder.setContentIntent(contentIntent).build();

                if (canClear)
                    nt.flags |= Notification.FLAG_AUTO_CANCEL;
                else
                    nt.flags |= Notification.FLAG_NO_CLEAR;

                notificationManager.notify(type, nt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
