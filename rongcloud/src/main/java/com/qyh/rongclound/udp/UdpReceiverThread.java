package com.qyh.rongclound.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;
import com.qyh.rongclound.udp.run.ActivityHandleRun;
import com.qyh.rongclound.udp.run.ActivityTipRun;
import com.qyh.rongclound.udp.run.AwardRun;
import com.qyh.rongclound.udp.run.ChatRun;
import com.qyh.rongclound.udp.run.CheckinRun;
import com.qyh.rongclound.udp.run.EndRun;
import com.qyh.rongclound.udp.run.FriendRun;
import com.qyh.rongclound.udp.run.SingupRun;
import com.qyh.rongclound.udp.run.SosRun;
import com.qyh.rongclound.udp.run.StartRun;
import com.qyh.rongclound.udp.run.StoryRun;
import com.qyh.rongclound.udp.run.SysRun;
import com.qyh.rongclound.util.SpCache;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @author 邱永恒
 * @time 2017/10/31  9:13
 * @desc UDP接收线程
 */

public class UdpReceiverThread extends Thread {
    private final DatagramSocket socket;
    private final Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SpCache spCache;
    private long delayMillis = 1000;
    private Runnable activityApplyRunnable = new ActivityHandleRun();
    private Runnable activityTipRunnable = new ActivityTipRun();
    private Runnable awardRunnable = new AwardRun();
    private Runnable chatRunnable = new ChatRun();
    private Runnable checkInRunnable = new CheckinRun();
    private Runnable endRunnable = new EndRun();
    private Runnable friendRunnable = new FriendRun();
    private Runnable singupRunnable = new SingupRun();
    private Runnable sosRunnable = new SosRun();
    private Runnable startRunnable = new StartRun();
    private Runnable storyRunnable = new StoryRun();
    private Runnable sysRunnable = new SysRun();

    public UdpReceiverThread(Context context, DatagramSocket socket) {
        if (socket == null) {
            throw new RuntimeException("DatagramSocket is not null");
        }
        this.socket = socket;
        this.context = context;
        spCache = new SpCache(context);
    }

    @Override
    public void run() {
        super.run();
        // 创建一个数据包, 接受数据
        DatagramPacket packet = new DatagramPacket(new byte[20], 20);
        while (!isInterrupted()) {
            Log.d("UdpReceiverThread", "isInterrupted: " + isInterrupted());
            try {
                // 接收数据, 并回包
                // 接受数据
                socket.receive(packet);

                // 发送数据包
                socket.send(packet);
                Log.d("UdpReceiverThread", "回包: " + packet.getLength());


                byte[] data = packet.getData();
                Log.e("==", "Thread: " + Thread.currentThread().getId() + "\nbyte[]: " + Arrays.toString(data));

                // TODO: 解析数据
                switch (data[0]) {
                    case 1:
                        // 更新消息中心新消息提醒
                        updateMessage(data[1]);
                        break;
                    case 2:
                        // 活动状态推送通知
                        pushState(data[1]);
                        break;
                }

                // 重置数据包状态, 清空缓存
                packet.setData(new byte[20]);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 活动状态推送通知
     *
     * @param data
     */
    private void pushState(byte data) {
        BroadcastManager.getInstance().sendBroadcast(ReceiverAction.ACTIVITY_MESSAGE);
        switch (data) {
            case 1:
                updateCount(ReceiverAction.PUSH_SINGUP);
                removeHandler(singupRunnable);
                handler.postDelayed(singupRunnable, delayMillis);
                break;
            case 2:
                updateCount(ReceiverAction.PUSH_CHECKIN);
                removeHandler(checkInRunnable);
                handler.postDelayed(checkInRunnable, delayMillis);
                break;
            case 3:
                updateCount(ReceiverAction.PUSH_START);
                removeHandler(startRunnable);
                handler.postDelayed(startRunnable, delayMillis);
                break;
            case 4:
                updateCount(ReceiverAction.PUSH_END);
                removeHandler(endRunnable);
                handler.postDelayed(endRunnable, delayMillis);
                break;
        }
    }

    /**
     * 更新消息中心新消息提醒
     *
     * @param data
     */
    private void updateMessage(byte data) {
        if (data >0 && data <= 6) {
            // 其他消息
            BroadcastManager.getInstance().sendBroadcast(ReceiverAction.OTHER_MESSAGE);
        } else if (data <= 8) {
            // 活动消息
            BroadcastManager.getInstance().sendBroadcast(ReceiverAction.ACTIVITY_MESSAGE);
        }

        switch (data) {
            case 1:
                updateCount(ReceiverAction.UPDATE_STORY);
                removeHandler(storyRunnable);
                handler.postDelayed(storyRunnable, delayMillis);
                break;
            case 2:
                updateCount(ReceiverAction.UPDATE_FRIEND);
                removeHandler(friendRunnable);
                handler.postDelayed(friendRunnable, delayMillis);
                break;
            case 3:
                updateCount(ReceiverAction.UPDATE_SOS);
                removeHandler(sosRunnable);
                handler.postDelayed(sosRunnable, delayMillis);
                break;
            case 4:
                updateCount(ReceiverAction.UPDATE_CHAT);
                removeHandler(chatRunnable);
                handler.postDelayed(chatRunnable, delayMillis);
                break;
            case 5:
                updateCount(ReceiverAction.UPDATE_SYS);
                removeHandler(sysRunnable);
                handler.postDelayed(sysRunnable, delayMillis);
                break;
            case 6:
                updateCount(ReceiverAction.UPDATE_AWARD);
                removeHandler(awardRunnable);
                handler.postDelayed(awardRunnable, delayMillis);
                break;
            case 7:
                updateCount(ReceiverAction.ACTIVITY_HANDLE);
                removeHandler(activityApplyRunnable);
                handler.postDelayed(activityApplyRunnable, delayMillis);
                break;
            case 8:
                updateCount(ReceiverAction.ACTIVITY_TIP);
                removeHandler(activityTipRunnable);
                handler.postDelayed(activityTipRunnable, delayMillis);
                break;
        }
    }

    /**
     * 移除定时任务
     * @param runnable
     */
    private void removeHandler(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    /**
     * 更新小红点计数
     * 暂时不计数
     * @param key
     */
    private void updateCount(String key) {
        synchronized (UdpReceiverThread.class) {
//            int count = spCache.getInt(key, 0);
//            count++;
            spCache.putInt(key, 1);
        }
    }

}
