package com.qyh.kcdemo.app;

import android.app.Application;

import com.qyh.rongclound.listener.RongAppContext;

import io.rong.imkit.RongIM;

/**
 * @author 邱永恒
 * @version $Rev$
 * @time 2017/10/28  12:09
 * @desc ${TODD}
 * @updateAutor $Author$
 * @updateDate $Date$
 * @updatedes ${TODO}
 */


public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);

        RongAppContext.init(this); // 初始化全局监听
    }
}
