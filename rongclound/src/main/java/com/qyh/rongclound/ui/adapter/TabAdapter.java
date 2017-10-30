package com.qyh.rongclound.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qyh.rongclound.R;
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
    private final Context context;
    private String[] titles = new String[] {"活动消息", "其他消息"};

    public TabAdapter(FragmentManager fm, ActivityMsgFragment activityMsgFragment, OtherMsgFragment otherMsgFragment, Context context) {
        super(fm);
        this.activityMsgFragment = activityMsgFragment;
        this.otherMsgFragment = otherMsgFragment;
        this.context = context;
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

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(titles[position]);
        return tabView;
    }
}
