package com.qyh.rongclound.broadcast;

/**
 * @author 邱永恒
 * @time 2017/10/31  11:51
 * @desc 广播action管理
 */

public class ReceiverAction {
    // 活动消息
    public static final String ACTIVITY_MESSAGE = "ACTIVITY_MESSAGE";
    // 活动消息取消小红点
    public static final String ACTIVITY_CANCEL = "ACTIVITY_CANCEL";
    // 其他消息
    public static final String OTHER_MESSAGE = "OTHER_MESSAGE";
    // 其他消息取消小红点
    public static final String OTHER_CANCEL = "OTHER_CANCEL";


    // 动态提示
    public static final String UPDATE_STORY = "UPDATE_STORY";
    // 新好友
    public static final String UPDATE_FRIEND = "UPDATE_FRIEND";
    // 求救
    public static final String UPDATE_SOS = "UPDATE_SOS";
    // 私聊
    public static final String UPDATE_CHAT = "UPDATE_CHAT";
    // 系统
    public static final String UPDATE_SYS = "UPDATE_SYS";
    // 奖励
    public static final String UPDATE_AWARD = "UPDATE_AWARD";
    // 加活动(待处理-可写，队长处理队员申请,队员处理队长邀请)
    public static final String ACTIVITY_HANDLE = "ACTIVITY_HANDLE";
    // 活动其他消息（只读）
    public static final String ACTIVITY_TIP = "ACTIVITY_TIP";


    // 报名
    public static final String PUSH_SINGUP = "PUSH_SINGUP";
    // 签到
    public static final String PUSH_CHECKIN = "PUSH_CHECKIN";
    // 开始
    public static final String PUSH_START = "PUSH_START";
    // 结束
    public static final String PUSH_END = "PUSH_END";
}
