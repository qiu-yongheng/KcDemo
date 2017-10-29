package com.qyh.rongclound.ui;

import android.content.Context;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;

/**
 * @author 邱永恒
 * @time 2017/10/27  14:46
 * @desc 自定义的会话列表
 */

public class ConversationListFragmentEx extends ConversationListFragment{
    @Override
    public ConversationListAdapter onResolveAdapter(Context context) {
        return super.onResolveAdapter(context);
    }
}
