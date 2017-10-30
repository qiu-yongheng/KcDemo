package com.qyh.litemvp.udp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kcit.sports.KCSportsAppManager;
import com.kcit.sports.KCSportsApplication;
import com.kcit.sports.util.Constants;
import com.kcit.sports.yuntongxun.storage.AbstractSQLManager;
import com.kcit.sports.yuntongxun.storage.IMessageSqlManager;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/14.
 */

public class UdpReceiver extends Thread {

    private DatagramSocket datagramSocket;
    private Handler mHandler;

    public UdpReceiver(DatagramSocket datagramSocket, Handler mHandler) {
        this.datagramSocket = datagramSocket;
        this.mHandler = mHandler;
    }

    public void activityNotify(byte[] bytes) {
        Intent intent = null;
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);

        switch (bytes[5]) {
            case 2://敲定名单
                msg.setUserData("");
                intent = new Intent("com.hcit.sport.updateActivity");
                break;
            case 4://签到
                msg.setUserData("");
                intent = new Intent("com.hcit.sport.updateActivity");
                break;
            case 5://活动开始
                byte[] b = Arrays.copyOfRange(bytes, 1, 5);
                intent = new Intent("com.kaichuang.sport.startsport");
                intent.putExtra("activityId", "" + byte4ToInt(b, 0));
                break;
        }
        KCSportsAppManager.getContext().sendBroadcast(intent);
    }

    public void sosNotify(String message) {
        Message msg = mHandler.obtainMessage(Constants.REPONSE_OK);
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

    @Override
    public void run() {
        super.run();
        try {
            DatagramPacket inputPacket = new DatagramPacket(new byte[512], 512);
            while (true) {
                //接收数据报
                datagramSocket.receive(inputPacket);
                byte[] bytes = inputPacket.getData();
                switch (bytes[0]) {
                    case 1:
                        sosNotify(new String(inputPacket.getData(), 1, inputPacket.getLength() - 1));
                        break;
                    case 2:
                        activityNotify(bytes);
                        break;
                }

                inputPacket.setData(new byte[512]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TEST-UDP", "error:" + e.getMessage());
        }
    }

    public static int byte4ToInt(byte[] b, int offset) {
        int sum = 0;
        int end = offset + 4;
        for (int i = offset; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= i * 8;
            sum += n;
        }
        return sum;
    }
}