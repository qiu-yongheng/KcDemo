package com.qyh.rongclound.udp.run;

import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;

/**
 * @author 邱永恒
 * @time 2017/11/1  8:47
 * @desc 签到
 */

public class CheckinRun implements Runnable {
    @Override
    public void run() {
        BroadcastManager.getInstance().sendBroadcast(ReceiverAction.PUSH_CHECKIN);
    }
}
