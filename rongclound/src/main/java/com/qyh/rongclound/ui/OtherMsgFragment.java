package com.qyh.rongclound.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseFragment;
import com.qyh.rongclound.listener.OnRecyclerItemClickListener;
import com.qyh.rongclound.mvp.activitytip.ActivityTipPresenter;
import com.qyh.rongclound.ui.adapter.OtherMsgAdapter;

import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * @author 邱永恒
 * @time 2017/10/27  16:22
 * @desc ${TODD}
 */

public class OtherMsgFragment extends BaseFragment<ActivityTipPresenter> {
    private RecyclerView recyclerView;
    private int[] imgs = new int[]{R.mipmap.dynamic, R.mipmap.friend, R.mipmap.sos, R.mipmap.private_chat, R.mipmap.system, R.mipmap.reward};
    private String[] titles = new String[imgs.length];
    private HashMap<Integer, Integer> counts = new HashMap<>();
    private OtherMsgAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_other_msg;
    }

    @Override
    protected void initView(View contentView) {
        titles = getContext().getResources().getStringArray(R.array.other_msg);
        initCount();
        initRecyclerView(contentView);
        addUnReadMessageCountChangedObserver();
    }

    private void initCount() {
        if (counts != null && counts.isEmpty()) {
            counts.put(0, 0);
            counts.put(1, 0);
            counts.put(2, 0);
            counts.put(3, 0);
            counts.put(4, 0);
            counts.put(5, 0);
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

    }

    @Override
    protected void initData() {

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
                    case 3:
                        ConversationListActivity.startActivity(getActivity());
                        break;
                }
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
