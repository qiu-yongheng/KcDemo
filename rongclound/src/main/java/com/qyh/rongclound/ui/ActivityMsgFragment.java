package com.qyh.rongclound.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qyh.rongclound.R;
import com.qyh.rongclound.mvp.activitytip.ActivityTipActivity;

import io.rong.imlib.model.Conversation;


/**
 * @author 邱永恒
 * @time 2017/10/27  16:56
 * @desc ${TODD}
 */

public class ActivityMsgFragment extends Fragment implements View.OnClickListener {

    private ConversationListFragmentEx conversationListFragmentEx;
    private Uri uri;
    private Conversation.ConversationType[] mConversationsTypes;
    private RelativeLayout rlActivityTip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_msg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view, savedInstanceState);
    }

    private void initView(View view, Bundle savedInstanceState) {
        rlActivityTip = (RelativeLayout) view.findViewById(R.id.rl_activity_tip);
        rlActivityTip.setOnClickListener(this);
        Fragment conversationList = initConversationList();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, conversationList)
                .commit();
    }

    private Fragment initConversationList() {
        if (conversationListFragmentEx == null) {
            ConversationListFragmentEx listFragmentEx = new ConversationListFragmentEx();

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
            ActivityTipActivity.startActivity(getActivity());
        }
    }
}
