package com.qyh.rongclound.ui.main;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qyh.rongclound.R;
import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.broadcast.ReceiverAction;
import com.qyh.rongclound.constant.Constant;
import com.qyh.rongclound.udp.UdpService;
import com.qyh.rongclound.ui.adapter.TabAdapter;
import com.qyh.rongclound.util.SpCache;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


/**
 * @author 邱永恒
 * @time 2017/10/27  16:03
 * @desc ${TODD}
 */

public class RongCloudFragment extends Fragment {
    private final String TAG = "RongCloudFragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OtherMsgFragment otherMsgFragment;
    private ActivityMsgFragment activityMsgFragment;
    private TabAdapter tabAdapter;
    private Badge otherBadge;
    private Badge activityBadge;
    private SpCache spCache;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext().getApplicationContext();
        Log.d(TAG, "onViewCreated");
        initView(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    private void initView(View view, Bundle savedInstanceState) {
        spCache = new SpCache(getContext());
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        // 创建fragment
        initFragments(savedInstanceState);
        // 初始化TabLayout
        initTabLayout();
        // 设置小红点
        setRedPoint();
        addUnReadMessageCountChangedObserver();
        receiverBroad();
    }

    /**
     * 当接收到广播, 更新tablayout小红点
     */
    private void receiverBroad() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(ReceiverAction.ACTIVITY_MESSAGE);
        actions.add(ReceiverAction.ACTIVITY_CANCEL);
        actions.add(ReceiverAction.OTHER_MESSAGE);
        actions.add(ReceiverAction.OTHER_CANCEL);
        BroadcastManager.getInstance().addAction(actions, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ReceiverAction.ACTIVITY_MESSAGE:
                        spCache.put(ReceiverAction.ACTIVITY_CANCEL, true);
                        /** 活动消息 */
                        if (activityBadge != null) {
                            activityBadge.setBadgeText("");
                        }
                        break;
                    case ReceiverAction.ACTIVITY_CANCEL:
                        /** 活动消息取消小红点 */
                        int handle = spCache.getInt(ReceiverAction.ACTIVITY_HANDLE, 0);
                        int tip = spCache.getInt(ReceiverAction.ACTIVITY_TIP, 0);

                        int all = handle + tip;
                        spCache.put(ReceiverAction.ACTIVITY_CANCEL, all != 0);
                        if (activityBadge != null) {
                            if (all == 0) {
                                activityBadge.hide(false);
                            } else {
                                activityBadge.setBadgeText("");
                            }
                        }
                        break;

                    case ReceiverAction.OTHER_MESSAGE:
                        spCache.put(ReceiverAction.OTHER_CANCEL, true);
                        /** 其他消息 */
                        if (otherBadge != null) {
                            otherBadge.setBadgeText("");
                        }
                        break;
                    case ReceiverAction.OTHER_CANCEL:
                        /** 其他消息取消小红点 */
                        int story = spCache.getInt(ReceiverAction.UPDATE_STORY, 0);
                        int friend = spCache.getInt(ReceiverAction.UPDATE_FRIEND, 0);
                        int sos = spCache.getInt(ReceiverAction.UPDATE_SOS, 0);
                        int chat = spCache.getInt(ReceiverAction.UPDATE_CHAT, 0);
                        int sys = spCache.getInt(ReceiverAction.UPDATE_SYS, 0);
                        int award = spCache.getInt(ReceiverAction.UPDATE_AWARD, 0);

                        int total = story + friend + sos + chat + sys + award;
                        spCache.put(ReceiverAction.OTHER_CANCEL, total != 0);
                        if (otherBadge != null) {
                            if (total == 0) {
                                otherBadge.hide(false);
                            } else {
                                otherBadge.setBadgeText("");
                            }
                        }
                        break;
                }
            }
        });

        BroadcastManager.getInstance().addAction(Intent.ACTION_TIME_TICK, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    // 检查Service状态
                    boolean isServiceRunning = false;

                    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    //获取正在运行的服务去比较
                    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                        //Log.i(TAG, "Running: " + service.service.getClassName());
                        if (UdpService.class.getName().equals(service.service.getClassName())) {
                            isServiceRunning = true;
                        }
                    }
                    Log.i(TAG, "isRunning: " + UdpService.class.getName() + " - " + isServiceRunning + "");
                    if (!isServiceRunning) {
                        int uid = spCache.getInt(Constant.UID, 0);
                        if (uid != 0) {
                            Log.i(TAG, "isRunningOK: " + "自动重连UID: " + uid);
                            Intent i = new Intent(context, UdpService.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(UdpService.UID, uid);
                            i.putExtras(bundle);
                            getActivity().getApplicationContext().startService(i);
                        }
                    }
                }
            }
        });
    }

    /**
     * 未读消息监听
     */
    private void addUnReadMessageCountChangedObserver() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
    }

    /**
     * 未读消息监听, 如果没有未读消息, 通知更新tablayout
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            spCache.putInt(ReceiverAction.UPDATE_CHAT, i);
            if (i == 0) {
                BroadcastManager.getInstance().sendBroadcast(ReceiverAction.OTHER_CANCEL);
            }
        }
    };

    /**
     * 初始化小红点
     */
    private void setRedPoint() {
        if (activityBadge == null) {
            activityBadge = new QBadgeView(getContext()).bindTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0)).setGravityOffset(51, 8, true).setBadgeTextSize(8, true);
        }
        boolean activity = spCache.get(ReceiverAction.ACTIVITY_CANCEL, false);
        if (activity) {
            activityBadge.setBadgeText("");
        } else {
            activityBadge.hide(false);
        }


        if (otherBadge == null) {
            otherBadge = new QBadgeView(getContext()).bindTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1)).setGravityOffset(51, 8, true).setBadgeTextSize(8, true);
        }
        boolean other = spCache.get(ReceiverAction.OTHER_CANCEL, false);
        if (other) {
            otherBadge.setBadgeText("");
        } else {
            otherBadge.hide(false);
        }
    }

    private void initTabLayout() {
        tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager(), activityMsgFragment, otherMsgFragment, getContext());
        // 设置adapter
        viewPager.setAdapter(tabAdapter);
        // 绑定viewpage
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // 会话列表
            activityMsgFragment = new ActivityMsgFragment();
            // 其他消息
            otherMsgFragment = new OtherMsgFragment();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
    }

}
