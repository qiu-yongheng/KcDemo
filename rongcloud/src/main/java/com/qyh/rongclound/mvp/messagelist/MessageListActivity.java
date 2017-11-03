package com.qyh.rongclound.mvp.messagelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseActivity;
import com.qyh.rongclound.constant.Constant;

/**
 * @author 邱永恒
 * @time 2017/10/30  10:42
 * @desc ${TODD}
 */
public class MessageListActivity extends BaseActivity {

    public static final String TYPE = "TYPE";
    private int type;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rong_activity_tip);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void processClick(View view) {

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt(TYPE);

        // 根据type请求接口获取数据
        switch (type) {
            case Constant.TYPE_ACTIVITY_TIP:
                setTitle(R.string.rong_title_activity_tip);
                break;
            case Constant.TYPE_ACTIVITY_HANDLE:
                setTitle(R.string.rong_title_activity_handle);
                break;
            case Constant.TYPE_STORY:
                setTitle(R.string.rong_title_story);
                break;
            case Constant.TYPE_FRIEND:
                setTitle(R.string.rong_title_friend);
                break;
            case Constant.TYPE_SOS:
                setTitle(R.string.rong_title_sos);
                break;
            case Constant.TYPE_SYS:
                setTitle(R.string.rong_title_sys);
                break;
            case Constant.TYPE_AWARD:
                setTitle(R.string.rong_title_award);
                break;
        }
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, MessageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
