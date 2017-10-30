package com.qyh.rongclound.mvp.activitytip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qyh.litemvp.nucleus.factory.RequiresPresenter;
import com.qyh.rongclound.R;
import com.qyh.rongclound.base.BaseActivity;

/**
 * @author 邱永恒
 * @time 2017/10/30  10:42
 * @desc ${TODD}
 */
@RequiresPresenter(ActivityTipPresenter.class)
public class ActivityTipActivity extends BaseActivity<ActivityTipPresenter> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rong_activity_tip);
    }

    @Override
    protected void initView() {
        setTitle(R.string.rong_title_activity_tip);
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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ActivityTipActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
