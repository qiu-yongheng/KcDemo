package com.qyh.rongclound.udp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.qyh.rongclound.udp.UdpService.UID;

/**
 * @author 邱永恒
 * @time 2017/10/31  10:33
 * @desc ${TODD}
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "启动定时器广播");
        int uid = intent.getExtras().getInt(UID);
        // 每次接收到广播, 启动service
        Intent i = new Intent(context, UdpService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(UID, uid);
        i.putExtras(bundle);
        context.startService(i);
    }
}
