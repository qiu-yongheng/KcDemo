package com.qyh.rongclound.udp;

import android.text.TextUtils;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author 邱永恒
 * @time 2017/10/31  9:02
 * @desc 发送UDP线程
 */

public class UdpSendThread extends Thread{
    private DatagramSocket socket;
    private byte[] bytes;

    public UdpSendThread (DatagramSocket socket, String sendData) {
        if (socket == null) {
            throw new RuntimeException("DatagramSocket is not null");
        }
        if (TextUtils.isEmpty(sendData)) {
            throw new RuntimeException("send data is not null");
        }
        this.socket = socket;
        this.bytes = sendData.getBytes();
    }

    public UdpSendThread (DatagramSocket socket, byte[] bytes) {
        if (socket == null) {
            throw new RuntimeException("DatagramSocket is not null");
        }
        if (bytes == null || bytes.length == 0) {
            throw new RuntimeException("send data is not null");
        }
        this.socket = socket;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        super.run();
        try {
            Log.d("UdpSendThread", "isInterrupted: " + isInterrupted());
            // IP地址
            InetAddress address = InetAddress.getByName("192.168.254.252");
            // 端口
            int port = 9506;
            // 创建数据包
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);

            // 发送数据包
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
