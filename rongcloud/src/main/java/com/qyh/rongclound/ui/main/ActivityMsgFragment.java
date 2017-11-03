package com.qyh.rongclound.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qyh.rongclound.R;
import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;
import com.qyh.rongclound.constant.Constant;
import com.qyh.rongclound.mvp.messagelist.MessageListActivity;
import com.qyh.rongclound.notifycation.NotifyManager;
import com.qyh.rongclound.ui.widget.DragPointView;
import com.qyh.rongclound.util.SpCache;

import java.util.ArrayList;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * @author 邱永恒
 * @time 2017/10/27  16:56
 * @desc ${TODD}
 */

public class ActivityMsgFragment extends Fragment implements View.OnClickListener {

    private ConversationListFragment conversationListFragmentEx;
    private Uri uri;
    private Conversation.ConversationType[] mConversationsTypes;
    private RelativeLayout rlActivityTip;
    private DragPointView rongNum;
    private SpCache spCache;
    private RelativeLayout rlActivityHandle;
    private DragPointView rongNumHandle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_msg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        spCache = new SpCache(getContext());
        rlActivityTip = (RelativeLayout) view.findViewById(R.id.rl_activity_tip);
        rlActivityHandle = (RelativeLayout) view.findViewById(R.id.rl_activity_handle);
        rongNum = (DragPointView) view.findViewById(R.id.rong_num);
        rongNumHandle = (DragPointView) view.findViewById(R.id.rong_num_handle);
        rongNum.setText("");
        rongNumHandle.setText("");
        rlActivityTip.setOnClickListener(this);
        rlActivityHandle.setOnClickListener(this);
        initRedPoint(view);
        Fragment conversationList = initConversationList();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, conversationList)
                .commit();
    }

    /**
     * 初始化小红点
     * 1. 查看缓存是否显示红点
     * 2. 只要接收到推送, 就显示红点
     * @param view
     */
    private void initRedPoint(View view) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(ReceiverAction.ACTIVITY_HANDLE);
        actions.add(ReceiverAction.ACTIVITY_TIP);
        BroadcastManager.getInstance().addAction(actions, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ReceiverAction.ACTIVITY_HANDLE:
                        /** 待处理 */
                        showNotify(ReceiverAction.ACTIVITY_HANDLE, Constant.TYPE_ACTIVITY_HANDLE, R.mipmap.dynamic, R.mipmap.dynamic, "待处理", "有新的待处理事件, 点击查看");
                        rongNumHandle.setVisibility(View.VISIBLE);
                        break;
                    case ReceiverAction.ACTIVITY_TIP:
                        /** 活动提示 */
                        showNotify(ReceiverAction.ACTIVITY_TIP, Constant.TYPE_ACTIVITY_TIP, R.mipmap.dynamic, R.mipmap.dynamic, "活动提示", "有新的活动通知, 点击查看");
                        rongNum.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    /**
     * 显示弹窗
     * @param key
     * @param type
     * @param big
     * @param small
     * @param title
     * @param content
     */
    private void showNotify(String key, int type, int big, int small, String title, String content) {
        int count = spCache.getInt(key, 0);
        Bundle bundle = new Bundle();
        bundle.putInt(MessageListActivity.TYPE, type);
        NotifyManager.getInstance().sendNotify(type, big, small, title, content, MessageListActivity.class, bundle);
    }

    /**
     * 初始化会话列表
     * @return
     */
    private Fragment initConversationList() {
        if (conversationListFragmentEx == null) {
            ConversationListFragment listFragmentEx = new ConversationListFragment();

            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
//                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
//                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
//                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")// 讨论组
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };
            listFragmentEx.setUri(uri);
            conversationListFragmentEx = listFragmentEx;
            return conversationListFragmentEx;
        } else {
            return conversationListFragmentEx;
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.rl_activity_tip) {
            spCache.putInt(ReceiverAction.ACTIVITY_TIP, 0);
            rongNum.setVisibility(View.GONE);
            MessageListActivity.startActivity(getActivity(), Constant.TYPE_ACTIVITY_TIP);
        } else if (i == R.id.rl_activity_handle) {
            spCache.putInt(ReceiverAction.ACTIVITY_HANDLE, 0);
            rongNumHandle.setVisibility(View.GONE);
            MessageListActivity.startActivity(getActivity(), Constant.TYPE_ACTIVITY_HANDLE);
        }

        BroadcastManager.getInstance().sendBroadcast(ReceiverAction.ACTIVITY_CANCEL);
    }
}
