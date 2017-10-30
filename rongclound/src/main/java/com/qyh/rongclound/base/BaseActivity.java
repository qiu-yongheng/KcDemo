package com.qyh.rongclound.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.qyh.litemvp.event.BusManager;
import com.qyh.litemvp.nucleus.presenter.Presenter;
import com.qyh.litemvp.nucleus.view.NucleusAppCompatActivity;
import com.qyh.litemvp.ui.ActivityManager;
import com.qyh.rongclound.R;


/**
 * @Description: Activity基类
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-12-19 14:51
 */
public abstract class BaseActivity<P extends Presenter> extends NucleusAppCompatActivity<P> implements View.OnClickListener {
    protected Context mContext;
    private SparseArray<View> mViews;
    private ViewFlipper mContentView;
    private LinearLayout mHeadLayout;
    private TextView mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.base);

        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
        mHeadLayout = (LinearLayout) super.findViewById(R.id.layout_head);
        mTitle = (TextView) super.findViewById(R.id.tv_title);


        mContext = this;
        mViews = new SparseArray<>();
        // 注册事件
        if (isRegisterEvent()) {
            // getBus()获取的 RxBusImpl.java 对象是单例的
            BusManager.getBus().register(this);
        }
        // 添加Activity到栈
        ActivityManager.getInstance().addActivity(this);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mContentView.addView(view, lp);
        initView();
        bindEvent();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销事件
        if (isRegisterEvent()) {
            BusManager.getBus().unregister(this);
        }
        // 移除栈
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        processClick(view);
    }

    /**
     * 初始化控件
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E F(int viewId) {
        E view = (E) mViews.get(viewId);

        if (view == null) {
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    protected <E extends View> void C(E view) {
        view.setOnClickListener(this);
    }

    protected boolean isRegisterEvent() {
        return false;
    }

    /**
     * 设置头部是否可见
     *
     * @param visibility
     */
    public void setHeadVisibility(int visibility) {
        mHeadLayout.setVisibility(visibility);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(int resId) {
        mTitle.setText(getString(resId));
    }

    /**
     * 点击返回键
     * @param view
     */
    public void onBackClick(View view) {
        finish();
    }

    /**
     * 初始化子View
     */
    protected abstract void initView();

    /**
     * 绑定事件
     */
    protected abstract void bindEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 点击事件处理
     * @param view
     */
    protected abstract void processClick(View view);
}
