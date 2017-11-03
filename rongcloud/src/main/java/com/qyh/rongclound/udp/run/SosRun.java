package com.qyh.rongclound.udp.run;

import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;

/**
 * @author 邱永恒
 * @time 2017/11/1  8:47
 * @desc 求救
 */

public class SosRun implements Runnable {
    @Override
    public void run() {
        BroadcastManager.getInstance().sendBroadcast(ReceiverAction.UPDATE_SOS);
    }
}
