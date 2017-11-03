package com.qyh.rongclound.udp.run;

import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;

/**
 * @author 邱永恒
 * @time 2017/11/1  8:47
 * @desc 系统
 */

public class SysRun implements Runnable {
    @Override
    public void run() {
        BroadcastManager.getInstance().sendBroadcast(ReceiverAction.UPDATE_SYS);
    }
}
