package com.qyh.rongclound.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qyh.rongclound.R;
import com.qyh.rongclound.ui.adapter.TabAdapter;


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

        initFragments(savedInstanceState);

        // 设置adapter
        viewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager(), activityMsgFragment, otherMsgFragment));
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

}
