package com.qyh.kcdemo.app;

import android.app.Application;
import android.content.Context;

import com.qyh.rongclound.broadcast.BroadcastManager;
import com.qyh.rongclound.listener.RongAppContext;
import com.qyh.rongclound.notifycation.NotifyManager;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

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


public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this); // 初始化融云IM

        RongAppContext.init(this); // 初始化全局监听
        BroadcastManager.init(this); // 初始化广播管理类
        NotifyManager.init(this); // 初始化通知管理类

        context = getApplicationContext();

        initLog();
        //        initLiteMvp();
    }


    public static Context getContext() {
        return context;
    }

    private void initLog() {
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    //    private void initLiteMvp() {
    //        ViseHttp.init(this);
    //        ViseHttp.CONFIG()
    //                //配置请求主机地址
    //                .baseUrl("http://api.com/api/")
    //                //配置全局请求头
    //                .globalHeaders(new HashMap<String, String>())
    //                //配置全局请求参数
    //                .globalParams(new HashMap<String, String>())
    //                //配置读取超时时间，单位秒
    //                .readTimeout(30)
    //                //配置写入超时时间，单位秒
    //                .writeTimeout(30)
    //                //配置连接超时时间，单位秒
    //                .connectTimeout(30)
    //                //配置请求失败重试次数
    //                .retryCount(3)
    //                //配置请求失败重试间隔时间，单位毫秒
    //                .retryDelayMillis(1000)
    //                //配置是否使用OkHttp的默认缓存
    //                .setHttpCache(true)
    //                //配置OkHttp缓存路径
    //                .setHttpCacheDirectory(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR))
    //                //配置离线缓存
    //                .cacheOffline(new Cache(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
    //                //配置应用级拦截器
    //                .interceptor(new HttpLogInterceptor().setLevel(HttpLogInterceptor.Level.BODY))
    //                //配置转换工厂
    //                .converterFactory(GsonConverterFactory.create())
    //                //配置适配器工厂
    //                .callAdapterFactory(RxJava2CallAdapterFactory.create());
    //    }
}
