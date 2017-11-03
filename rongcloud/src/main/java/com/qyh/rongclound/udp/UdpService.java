package com.qyh.rongclound.udp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qyh.rongclound.constant.Constant;
import com.qyh.rongclound.util.SpCache;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * @author 邱永恒
 * @time 2017/10/31  8:47
 * @desc ${TODD}
 */

public class UdpService extends Service {
    private final static String TAG = "UdpService";
    private DatagramSocket socket;
    private UdpSendThread udpSendThread;
    private UdpReceiverThread udpReceiverThread;
    public static final String UID = "UID";
    private static final long TIME = 1000 * 50;
    private PowerManager.WakeLock wakeLock;
    private SpCache spCache;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取socket对象
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        spCache = new SpCache(getApplicationContext());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "OnlineService");

        startReceiverTread();
    }

    /**
     * 启动接收线程
     */
    private void startReceiverTread() {
        Log.d(TAG, "startReceiverTread");
        udpReceiverThread = new UdpReceiverThread(getApplicationContext(), socket);
        udpReceiverThread.start();
    }

    /**
     * 每次启动service都会调用
     *
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
    }

    /**
     * 每次启动service都会调用
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int uid;
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            Log.d(TAG, "UdpService 重启: intent = null");
            uid = spCache.getInt(Constant.UID, 0);
        } else {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                Log.d(TAG, "UdpService 重启: bundle = null");
                uid = spCache.getInt(Constant.UID, 0);
            } else {
                uid = bundle.getInt(UID);
            }
        }

        if (uid == 0) {
            throw new RuntimeException("uid is not 0");
        }

        if (udpSendThread != null) {
            udpSendThread.interrupt();
        }

        // 唤醒CPU
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        sendHeart(uid);
        startAlarmTimer(uid);

        // 在服务被系统杀死时会重新被创建，onStartCommand方法会被调用，但是需要注意的是，在调用onStartCommand时传入的intent值可能为空，如果该方法中会用到该参数需要特别注意
        return START_STICKY;
    }

    /**
     * 启动心跳任务
     *
     * @param uid
     */
    private void startAlarmTimer(int uid) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long triggerAtTime = SystemClock.elapsedRealtime() + TIME; // 设置触发时间

        Intent i = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt(UID, uid);
        i.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    /**
     * 发送心跳包
     *
     * @param uid
     */
    private void sendHeart(int uid) {
        // 最好在登录时保存一次就行了
//        spCache.putInt(Constant.UID, uid);
        // 标识类型(1字节) + 业务数据(4字节)
        byte[] heart = new byte[5];
        byte[] bytes = intToByte4(uid);
        System.arraycopy(bytes, 0, heart, 1, 4);
        Log.d(TAG, "send heart: " + Arrays.toString(heart));
        udpSendThread = new UdpSendThread(socket, heart);
        udpSendThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (udpReceiverThread != null) {
            udpReceiverThread.interrupt();
            udpReceiverThread = null;
        }
        if (udpSendThread != null) {
            udpSendThread.interrupt();
            udpSendThread = null;
        }

        cancelAlarm();
        tryReleaseWakeLock();
    }

    /**
     * 关闭定时器
     */
    private void cancelAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 取消时注意UpdateReceiver.class必须与设置时一致,这样才要正确取消
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }

    /**
     * 释放CPU
     */
    private void tryReleaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld() == true) {
            wakeLock.release();
        }
    }

    /**
     * int转字节
     *
     * @param i
     * @return
     */
    public byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        targets[2] = (byte) (i >> 16 & 0xFF);
        targets[3] = (byte) (i >> 24 & 0xFF);
        return targets;
    }
}