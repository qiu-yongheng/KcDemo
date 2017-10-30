package com.qyh.rongclound.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseActivity;
import com.qyh.rongclound.mvp.activitytip.ActivityTipPresenter;

import io.rong.imlib.model.Conversation;

/**
 * @author 邱永恒
 * @time 2017/10/30  11:51
 * @desc ${TODD}
 */

public class ConversationListActivity extends BaseActivity<ActivityTipPresenter>{

    private ConversationListFragmentEx conversationListFragmentEx;
    private Conversation.ConversationType[] mConversationsTypes;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
    }

    @Override
    protected void initView() {
        setTitle(R.string.private_chat);

        Fragment conversationList = initConversationList();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, conversationList)
                .commit();
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

    private Fragment initConversationList() {
        if (conversationListFragmentEx == null) {
            ConversationListFragmentEx listFragmentEx = new ConversationListFragmentEx();

            uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE};
            listFragmentEx.setUri(uri);
            conversationListFragmentEx = listFragmentEx;
            return conversationListFragmentEx;
        } else {
            return conversationListFragmentEx;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ConversationListActivity.class);
        context.startActivity(intent);
    }
}
