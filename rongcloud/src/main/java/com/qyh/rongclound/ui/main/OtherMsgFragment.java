package com.qyh.rongclound.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseFragment;
import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;
import com.qyh.rongclound.constant.Constant;
import com.qyh.rongclound.listener.OnRecyclerItemClickListener;
import com.qyh.rongclound.mvp.messagelist.MessageListActivity;
import com.qyh.rongclound.notifycation.NotifyManager;
import com.qyh.rongclound.ui.adapter.OtherMsgAdapter;
import com.qyh.rongclound.ui.conversation.ConversationListActivity;
import com.qyh.rongclound.util.SpCache;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * @author 邱永恒
 * @time 2017/10/27  16:22
 * @desc ${TODD}
 */

public class OtherMsgFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private int[] imgs = new int[]{R.mipmap.dynamic, R.mipmap.friend, R.mipmap.sos, R.mipmap.private_chat, R.mipmap.system, R.mipmap.reward};
    private String[] titles = new String[imgs.length];
    private HashMap<Integer, Integer> counts = new HashMap<>();
    private OtherMsgAdapter adapter;
    private SpCache spCache;
    private ArrayList<String> actions = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_other_msg;
    }

    @Override
    protected void initView(View contentView) {
        initCount();
        initRecyclerView(contentView);
    }

    /**
     * 接收广播, 更新小红点
     */
    private void receiverBroad() {
        BroadcastManager.getInstance().addAction(actions, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ReceiverAction.UPDATE_STORY:
                        /** 动态提示 */
                        showNotify(ReceiverAction.UPDATE_STORY, Constant.TYPE_STORY, R.mipmap.dynamic, R.mipmap.dynamic, "动态提示", "动态提示有更新, 点击查看");
                        counts.put(0, 1);
                        break;
                    case ReceiverAction.UPDATE_FRIEND:
                        /** 新好友 */
                        showNotify(ReceiverAction.UPDATE_FRIEND, Constant.TYPE_FRIEND, R.mipmap.dynamic, R.mipmap.dynamic, "新的好友", "有新的好友请求, 点击查看");
                        counts.put(1, 1);
                        break;
                    case ReceiverAction.UPDATE_SOS:
                        /** 求救 */
                        showNotify(ReceiverAction.UPDATE_SOS, Constant.TYPE_SOS, R.mipmap.dynamic, R.mipmap.dynamic, "求救提示", "有新的求救提示, 点击查看");
                        counts.put(2, 1);
                        break;
                    case ReceiverAction.UPDATE_SYS:
                        /** 系统 */
                        showNotify(ReceiverAction.UPDATE_SYS, Constant.TYPE_SYS, R.mipmap.dynamic, R.mipmap.dynamic, "系统消息", "有新的系统消息, 点击查看");
                        counts.put(4, 1);
                        break;
                    case ReceiverAction.UPDATE_AWARD:
                        /** 奖励 */
                        showNotify(ReceiverAction.UPDATE_AWARD, Constant.TYPE_AWARD, R.mipmap.dynamic, R.mipmap.dynamic, "奖励通知", "有新的奖励通知, 点击查看");
                        counts.put(5, 1);
                        break;
                }

                // 更新小红点
                if (adapter != null) {
                    adapter.setCounts(counts);
                    adapter.notifyDataSetChanged();
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

    private void initCount() {
        if (counts != null && counts.isEmpty()) {
            counts.put(0, spCache.getInt(ReceiverAction.UPDATE_STORY, 0));
            counts.put(1, spCache.getInt(ReceiverAction.UPDATE_FRIEND, 0));
            counts.put(2, spCache.getInt(ReceiverAction.UPDATE_SOS, 0));
            counts.put(3, 0);
            counts.put(4, spCache.getInt(ReceiverAction.UPDATE_SYS, 0));
            counts.put(5, spCache.getInt(ReceiverAction.UPDATE_AWARD, 0));
        }
    }

    /**
     * 未读消息监听
     */
    private void addUnReadMessageCountChangedObserver() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
    }

    @Override
    protected void bindEvent() {
        addUnReadMessageCountChangedObserver();
        receiverBroad();
    }

    @Override
    protected void initData() {
        spCache = new SpCache(getContext());
        titles = getContext().getResources().getStringArray(R.array.other_msg);
        actions.add(ReceiverAction.UPDATE_STORY);
        actions.add(ReceiverAction.UPDATE_FRIEND);
        actions.add(ReceiverAction.UPDATE_SOS);
        actions.add(ReceiverAction.UPDATE_SYS);
        actions.add(ReceiverAction.UPDATE_AWARD);
    }

    @Override
    protected void processClick(View view) {

    }

    /**
     * 未读消息监听
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            counts.put(3, i);
            if (adapter != null) {
                adapter.setCounts(counts);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new OtherMsgAdapter(getContext(), titles, imgs, counts);
        adapter.setOnClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        counts.put(0, 0);
                        spCache.putInt(ReceiverAction.UPDATE_STORY, 0);
                        MessageListActivity.startActivity(getActivity(), Constant.TYPE_STORY);
                        break;
                    case 1:
                        counts.put(1, 0);
                        spCache.putInt(ReceiverAction.UPDATE_FRIEND, 0);
                        MessageListActivity.startActivity(getActivity(), Constant.TYPE_FRIEND);
                        break;
                    case 2:
                        counts.put(2, 0);
                        spCache.putInt(ReceiverAction.UPDATE_SOS, 0);
                        MessageListActivity.startActivity(getActivity(), Constant.TYPE_SOS);
                        break;
                    case 3:
                        ConversationListActivity.startActivity(getActivity());
                        break;
                    case 4:
                        counts.put(4, 0);
                        spCache.putInt(ReceiverAction.UPDATE_SYS, 0);
                        MessageListActivity.startActivity(getActivity(), Constant.TYPE_SYS);
                        break;
                    case 5:
                        counts.put(5, 0);
                        spCache.putInt(ReceiverAction.UPDATE_AWARD, 0);
                        MessageListActivity.startActivity(getActivity(), Constant.TYPE_AWARD);
                        break;
                }

                // 更新小红点
                if (adapter != null) {
                    adapter.setCounts(counts);
                    adapter.notifyDataSetChanged();
                }

                BroadcastManager.getInstance().sendBroadcast(ReceiverAction.OTHER_CANCEL);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
    }
}
