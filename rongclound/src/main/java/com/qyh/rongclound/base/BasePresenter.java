package com.qyh.rongclound.base;

import android.os.Bundle;

import com.qyh.litemvp.nucleus.presenter.RxPresenter;

public class BasePresenter<ViewType> extends RxPresenter<ViewType> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
    }
}
