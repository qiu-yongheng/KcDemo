package com.qyh.rongclound.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qyh.rongclound.R;
import com.qyh.rongclound.ui.adapter.TabAdapter;

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

public class ViewPagerFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OtherMsgFragment otherMsgFragment;
    private ActivityMsgFragment activityMsgFragment;
    private TabAdapter tabAdapter;
    private Badge badge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    private void initView(View view, Bundle savedInstanceState) {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        // 创建fragment
        initFragments(savedInstanceState);
        // 初始化TabLayout
        initTabLayout();
        // 设置小红点
        setRedPoint();
        // 监听
//        addOnTabSelectedListener();

        addUnReadMessageCountChangedObserver();

    }

    /**
     * 未读消息监听
     */
    private void addUnReadMessageCountChangedObserver() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
    }

    /**
     * 未读消息监听
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            if (badge != null) {
                badge.setBadgeNumber(i);
            }
        }
    };

    private void addOnTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                /**在这里记录TabLayout选中后内容更新已读标记**/

                View customView = tab.getCustomView();
                TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.text_color_blue));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.text_color_gray));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRedPoint() {
        if (badge == null) {
            badge = new QBadgeView(getContext()).bindTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1)).setGravityOffset(51, 8, true).setBadgeTextSize(8, true);
        } else {
            badge.hide(false);
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
