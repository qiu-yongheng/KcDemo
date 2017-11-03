package com.qyh.rongclound.ui.conversation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseActivity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author 邱永恒
 * @version $Rev$
 * @time 2017/10/28  13:37
 * @desc ${TODD}
 * @updateAutor $Author$
 * @updateDate $Date$
 * @updatedes ${TODO}
 */


public class ConversationActivity extends BaseActivity {
    public static final String TAG = "ConversationActivity";
    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;
    private String mTargetId;
    private Conversation.ConversationType mConversationType;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    break;
                case SET_VOICE_TYPING_TITLE:
                    break;
                case SET_TARGET_ID_TITLE:
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
    }

    @Override
    protected void initView() {
        mTargetId = getIntent().getData().getQueryParameter("targetId");
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        String title = getIntent().getData().getQueryParameter("title");
        if (TextUtils.isEmpty(title)) {
            setTitle(mTargetId);
        }
        setTitle(title);

        if (mConversationType == Conversation.ConversationType.GROUP) {
            Log.d(TAG, "group");
        } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
            Log.d(TAG, "private");
        } else if (mConversationType == Conversation.ConversationType.DISCUSSION) {
            Log.d(TAG, "discussion");
        }
    }

    @Override
    protected void bindEvent() {
        // 监听输入状态
        setTypingStatusListener();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processClick(View view) {

    }

    private void setTypingStatusListener() {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });
    }

}
