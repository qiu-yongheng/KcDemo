package com.qyh.litemvp.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/14.
 */

public class UdpClient extends Thread {

    private DatagramSocket datagramSocket;
    private byte[] bytes;

    public UdpClient(DatagramSocket datagramSocket, byte[] bytes) {
        this.datagramSocket = datagramSocket;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        super.run();
        try {
            InetAddress address = InetAddress.getByName("112.74.135.168");
            //发送数据报
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 9505);
            datagramSocket.send(packet);
            Log.d("TEST-UDP", Arrays.toString(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
