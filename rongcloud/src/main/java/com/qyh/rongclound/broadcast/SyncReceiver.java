package com.qyh.rongclound.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author 邱永恒
 * @time 2017/10/31  15:20
 * @desc ${TODD}
 */

public class SyncReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SyncReceiver", intent.getAction());
        switch (intent.getAction()) {
            case ReceiverAction.UPDATE_STORY:
                /** 动态提示 */
                break;
            case ReceiverAction.UPDATE_FRIEND:
                /** 新好友 */
                break;
            case ReceiverAction.UPDATE_SOS:
                /** 求救 */
//                NotifyManager.getInstance().sendNotify(1, R.mipmap.dynamic, R.mipmap.dynamic, "sos求救", "您收到一条sos消息, 请点击查看", MessageListActivity.class);
                break;
            case ReceiverAction.UPDATE_CHAT:
                /** 私聊 */
                break;
            case ReceiverAction.UPDATE_SYS:
                /** 系统 */
                break;
            case ReceiverAction.UPDATE_AWARD:
                /** 奖励 */
                break;
            case ReceiverAction.ACTIVITY_HANDLE:
                /** 加活动 */
                break;
            case ReceiverAction.PUSH_SINGUP:
                /** 报名 */
                break;
            case ReceiverAction.PUSH_CHECKIN:
                /** 签到 */
                break;
            case ReceiverAction.PUSH_START:
                /** 开始 */
                break;
            case ReceiverAction.PUSH_END:
                /** 结束 */
                break;
        }
    }
}
