package com.qyh.rongclound.listener;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;


/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class RongAppContext {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;


    private final static String TAG = "RongAppContext";
    public static final String UPDATE_FRIEND = "update_friend";
    public static final String UPDATE_RED_DOT = "update_red_dot";
    public static final String UPDATE_GROUP_NAME = "update_group_name";
    public static final String UPDATE_GROUP_MEMBER = "update_group_member";
    public static final String GROUP_DISMISS = "group_dismiss";

    private Context mContext;

    private static RongAppContext mRongCloudInstance;

    private static ArrayList<Activity> mActivities;

    public RongAppContext(Context mContext) {
        this.mContext = mContext;
        initListener();
        mActivities = new ArrayList<>();
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {
            synchronized (RongAppContext.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongAppContext(context);
                }
            }
        }
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongAppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * init 后就能设置的监听
     */
    private void initListener() {
        setConversationBehaviorListener(); // 会话界面操作监听
        setConversationListBehaviorListener(); // 会话列表操作监听

        setConnectionStatusListener(); // 监听登录状态

        setUserInfoProvider(); // 设置用户信息内容提供者
        setGroupMemberProvider();// 设置音频视频群组信息内容提供者
        setLocationProvider();//设置地理位置提供者,不用位置的同学可以注掉此行代码

        setReadReceiptConversationType(); // 消息阅读回执

        setSendMessageListener(); // 发送消息监听
        setOnReceiveMessageListener(); // 接收消息监听

        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
        setGroupMembersProvider(); // 群组、讨论组 @ 功能
    }

    /**
     * 设置音频视频群组信息内容提供者
     */
    private void setGroupMemberProvider() {
        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
            @Override
            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {
                Log.d(TAG, groupId + "音视频获取群组成员信息列表");
                //TODO: 返回群组成员userId的集合
                return null;
            }
        });
    }

    /**
     * 群组、讨论组 @ 功能
     */
    private void setGroupMembersProvider() {
        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback iGroupMemberCallback) {
                Log.d(TAG, groupId + "获取群组成员信息列表");
                //获取群组成员信息列表
                //                iGroupMemberCallback.onGetGroupMembersResult(list); // 调用 callback 的 onGetGroupMembersResult 回传群组信息
            }
        });
    }

    /**
     * 发送消息监听
     */
    private void setSendMessageListener() {
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            /**
             * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
             *
             * @param message 发送的消息实例。
             * @return 处理后的消息实例。
             */
            @Override
            public Message onSend(Message message) {
                MessageContent messageContent = message.getContent();

                if (messageContent instanceof TextMessage) {//文本消息
                    TextMessage textMessage = (TextMessage) messageContent;
                    Log.d(TAG, "onSend-TextMessage:" + textMessage.getContent());
                } else if (messageContent instanceof ImageMessage) {//图片消息
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    Log.d(TAG, "onSend-ImageMessage:" + imageMessage.getRemoteUri());
                } else if (messageContent instanceof VoiceMessage) {//语音消息
                    VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                    Log.d(TAG, "onSend-voiceMessage:" + voiceMessage.getUri().toString());
                } else if (messageContent instanceof RichContentMessage) {//图文消息
                    RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                    Log.d(TAG, "onSend-RichContentMessage:" + richContentMessage.getContent());
                } else {
                    Log.d(TAG, "onSent-其他消息，自己来判断处理");
                }
                return message;
            }

            /**
             * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
             *
             * @param message              消息实例。
             * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
             * @return true 表示走自己的处理方式，false 走融云默认处理方式。
             */
            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                if (message.getSentStatus() == Message.SentStatus.FAILED) {
                    if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                        //不在聊天室
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                        //不在讨论组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                        //不在群组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                        //你在他的黑名单中
                    }
                }

                MessageContent messageContent = message.getContent();

                if (messageContent instanceof TextMessage) {//文本消息
                    TextMessage textMessage = (TextMessage) messageContent;
                    Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
                } else if (messageContent instanceof ImageMessage) {//图片消息
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
                } else if (messageContent instanceof VoiceMessage) {//语音消息
                    VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                    Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
                } else if (messageContent instanceof RichContentMessage) {//图文消息
                    RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                    Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
                } else {
                    Log.d(TAG, "onSent-其他消息，自己来判断处理");
                }
                return false;
            }
        });
    }

    private void setOnReceiveMessageListener() {
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            /**
             * 收到消息的处理。
             *
             * @param message 收到的消息实体。
             * @param left    剩余未拉取消息数目。
             * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
             */
            @Override
            public boolean onReceived(Message message, int left) {
                Log.d(TAG, "接收到消息: " + message.getContent().toString());
                return false;
            }
        });
    }

    /**
     * 设置地理位置提供者,不用位置的同学可以注掉此行代码
     */
    private void setLocationProvider() {
        RongIM.setLocationProvider(new RongIM.LocationProvider() {
            @Override
            public void onStartLocation(Context context, LocationCallback locationCallback) {

            }
        });
    }

    /**
     * 设置用户信息内容提供者
     */
    private void setUserInfoProvider() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                Log.d(TAG, "userId is : " + s);
                // TODO: 根据userId, 从本地或网络获取用户信息返回给融云
                if (s.equals("qiuyongheng")) {
                    return new UserInfo("qiuyongheng", "邱永恒", Uri.parse("https://avatars3.githubusercontent.com/u/248093?s=460&v=4"));
                }
                return new UserInfo("gaga", "嘎嘎", Uri.parse("https://avatars2.githubusercontent.com/u/8560582?s=460&v=4"));
            }
        }, true);
    }

    /**
     * 监听登录状态
     */
    private void setConnectionStatusListener() {
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {

            }
        });
    }

    /**
     * 会话列表操作监听
     */
    private void setConversationListBehaviorListener() {
        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            /**
             * 当点击会话头像后执行。
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param s         被点击的用户id。
             * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
                return false;
            }

            /**
             * 当长按会话头像后执行。
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param s         被点击的用户id。
             * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
                return false;
            }

            /**
             * 长按会话列表中的 item 时执行。
             *
             * @param context        上下文。
             * @param view           触发点击的 View。
             * @param uiConversation 长按时的会话条目。
             * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }

            /**
             * 点击会话列表中的 item 时执行。
             *
             * @param context        上下文。
             * @param view           触发点击的 View。
             * @param uiConversation 会话条目。
             * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }
        });
    }

    /**
     * 设置会话界面操作的监听器: 会话界面中点击用户头像、长按用户头像、点击消息、长按消息的操作都在此处理
     */
    private void setConversationBehaviorListener() {
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            /**
             * 当点击用户头像后执行。
             *
             * @param context           上下文。
             * @param conversationType  会话类型。
             * @param userInfo          被点击的用户的信息。
             * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            /**
             * 当长按用户头像后执行。
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param userInfo         被点击的用户的信息。
             * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            /**
             * 当点击消息时执行。
             *
             * @param context 上下文。
             * @param view    触发点击的 View。
             * @param message 被点击的消息的实体信息。
             * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
             */
            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            /**
             * 当长按消息时执行。
             *
             * @param context 上下文。
             * @param view    触发点击的 View。
             * @param message 被长按的消息的实体信息。
             * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }

            /**
             * 当点击链接消息时执行。
             *
             * @param context 上下文。
             * @param link    被点击的链接。
             * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
             */
            @Override
            public boolean onMessageLinkClick(Context context, String link) {
                return false;
            }
        });
    }

    /**
     * 消息阅读回执
     */
    private void setReadReceiptConversationType() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    /**
     * 连接融云回调
     *
     * @return
     */
    public RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.d(TAG, "ConnectCallback connect onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.d(TAG, "ConnectCallback connect onSuccess");
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(final RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "ConnectCallback connect onError-ErrorCode=" + errorCode);
            }
        };
        return connectCallback;
    }

    /**
     * 正在输入的状态提示
     *
     * @return
     */
    public RongIMClient.TypingStatusListener getTypingStatusListener() {
        RongIMClient.TypingStatusListener typingStatusListener = new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType conversationType, String s, Collection<TypingStatus> collection) {

            }
        };
        return typingStatusListener;
    }
}
