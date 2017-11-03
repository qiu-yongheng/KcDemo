package com.qyh.rongclound.notifycation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

/**
 * @author 邱永恒
 * @time 2017/10/31  15:52
 * @desc ${TODD}
 */

public class NotifyManager {

    private static NotifyManager notificationManager;
    private final Context context;
    private final NotificationManager mNotificationManager;
    private final NotificationCompat.Builder builder;

    public NotifyManager(Context context) {
        this.context = context.getApplicationContext();
        //获取NotificationManager实例
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建 Notification.Builder 对象
        builder = new NotificationCompat.Builder(context);
    }

    public static void init(Context context) {
        if (notificationManager == null) {
            synchronized (NotifyManager.class) {
                if (notificationManager == null) {
                    notificationManager = new NotifyManager(context);
                }
            }
        }
    }

    public static NotifyManager getInstance() {
        return notificationManager;
    }

    /**
     * 发送notify
     *
     * @param notifyId
     * @param largeIconId
     * @param smallIconId
     * @param title
     * @param content
     * @param c
     * @param <T>
     */
    public <T> void sendNotify(int notifyId, int largeIconId, int smallIconId, String title, String content, Class<T> c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIconId))
                .setSmallIcon(smallIconId)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                // 显示带有默认铃声、震动、呼吸灯效果的通知
                .setDefaults(Notification.DEFAULT_ALL);
        mNotificationManager.notify(notifyId, builder.build());
    }
}
