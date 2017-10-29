package com.qyh.rongclound.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyh.rongclound.ui.ActivityMsgFragment;
import com.qyh.rongclound.ui.OtherMsgFragment;

/**
 * @author 邱永恒
 * @time 2017/10/27  16:16
 * @desc viewPager adapter
 */

public class TabAdapter extends FragmentPagerAdapter{
    private final OtherMsgFragment otherMsgFragment;
    private final ActivityMsgFragment activityMsgFragment;
    private String[] titles = new String[] {"活动消息", "其他消息"};

    public TabAdapter(FragmentManager fm, ActivityMsgFragment activityMsgFragment, OtherMsgFragment otherMsgFragment) {
        super(fm);
        this.activityMsgFragment = activityMsgFragment;
        this.otherMsgFragment = otherMsgFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return activityMsgFragment;
        }
        return otherMsgFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
