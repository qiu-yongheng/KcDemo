package com.qyh.litemvp.udp;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.kcit.sports.KCSportsAppManager;
import com.kcit.sports.KCSportsApplication;
import com.kcit.sports.receiver.ConnectivityReceiver;
import com.kcit.sports.util.Constants;
import com.kcit.sports.yuntongxun.storage.IMessageSqlManager;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;

public class UdpService extends Service {

    /* 使用时需要保证接收和发送的Socket对象一直 */
    private DatagramSocket udpSocket;

    private UdpBroadcastReceiver udpReceiver;

    public UdpService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        udpReceiver = new UdpBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.kcit.sport.sendMessage");
        registerReceiver(udpReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            udpSocket = new DatagramSocket(9505);

            new UdpReceiver(udpSocket, handler).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setAlarm() {
        try {
            SystemClock.elapsedRealtime();
            long timeMillis = System.currentTimeMillis() + 1000 * 60;
            Intent intent = new Intent("com.kcit.sport.sendMessage");
            intent.putExtra("udpMessage", Integer.valueOf(KCSportsApplication.currentUserInfo.getUserid()));
            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), Constants.REQUEST_ALARM, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeMillis, 1000, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UdpBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "com.kcit.sport.sendMessage":
                    byte[] bytes;
                    int message = intent.getIntExtra("udpMessage", 0);
                    bytes = new byte[5];
                    byte[] uid = intToByte4(message);
                    System.arraycopy(uid, 0, bytes, 1, 4);
                    new UdpClient(udpSocket, bytes).start();
                    setAlarm();
                    break;
            }
        }
    }

    public byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        targets[2] = (byte) (i >> 16 & 0xFF);
        targets[3] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg1) {
            super.handleMessage(msg1);
            switch (msg1.what) {
                case Constants.REPONSE_OK:
                    String message = (String) msg1.obj;
                    String[] info = message.split("~");
                    long time;
                    String txtTime;
                    if (info.length == 8) {
                        try {
                            txtTime = info[7];
                            if (txtTime.contains("\n")) {
                                txtTime = txtTime.replace("\n", "");
                            }
                            time = Long.valueOf(txtTime) * 1000;
                        } catch (Exception ex) {
                            time = System.currentTimeMillis();
                            ex.printStackTrace();
                        }
                    } else {
                        time = System.currentTimeMillis();
                    }
                    if (!IMessageSqlManager.checkMessage(message)) {
                        Intent intent = new Intent("com.hcit.sport.systeminforms");
                        intent.putExtra("xitong", message);
                        sendBroadcast(intent);
                        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
                        msg.setSessionId(Constants.SYSTEM_SESSIONID);
                        msg.setFrom(Constants.SYSTEM_SESSIONID);
                        msg.setBody(new ECTextMessageBody(message));
                        msg.setDirection(ECMessage.Direction.RECEIVE);
                        msg.setMsgTime(time);
                        msg.setNickName("系统通知");
                        msg.setUserData("");
                        msg.setTo("");
                        msg.setMsgId(time + "");
                        msg.setMsgStatus(ECMessage.MessageStatus.SUCCESS);
                        IMessageSqlManager.insertIMessage(msg, msg.getDirection().ordinal());
                    }
                    break;
            }
        }
    };
}
