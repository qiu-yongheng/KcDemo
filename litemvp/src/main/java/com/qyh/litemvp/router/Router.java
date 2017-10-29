package com.qyh.litemvp.router;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.qyh.litemvp.common.ViseConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activity跳转路由
 */
public class Router {

    private Intent intent;
    private Activity from;
    private Class<?> to;
    private Bundle data;
    private ActivityOptionsCompat options;
    private int requestCode = -1;
    private int enterAnim = ViseConfig.ROUTER_ANIM_ENTER;
    private int exitAnim = ViseConfig.ROUTER_ANIM_EXIT;

    private static RouterCallback callback;

    private Router() {
        intent = new Intent();
    }

    /**
     * 创建对象
     * @param context
     * @return
     */
    public static Router newIntent(Activity context) {
        Router router = new Router();
        router.from = context;
        return router;
    }

    /**
     * 设置跳转目标
     * @param to
     * @return
     */
    public Router to(Class<?> to) {
        this.to = to;
        return this;
    }

    /**
     * 添加flags
     * @param flags
     * @return
     */
    public Router addFlags(int flags) {
        if (intent != null) {
            intent.addFlags(flags);
        }
        return this;
    }

    /**
     * 绑定bundle数据
     * @param data
     * @return
     */
    public Router data(Bundle data) {
        this.data = data;
        return this;
    }

    public Router putBundle(@Nullable String key, Bundle bundle) {
        getBundleData().putBundle(key, bundle);
        return this;
    }

    public Router putByte(@Nullable String key, byte value) {
        getBundleData().putByte(key, value);
        return this;
    }

    public Router putChar(@Nullable String key, char value) {
        getBundleData().putChar(key, value);
        return this;
    }

    public Router putInt(@Nullable String key, int value) {
        getBundleData().putInt(key, value);
        return this;
    }

    public Router putString(@Nullable String key, String value) {
        getBundleData().putString(key, value);
        return this;
    }

    public Router putShort(@Nullable String key, short value) {
        getBundleData().putShort(key, value);
        return this;
    }

    public Router putFloat(@Nullable String key, float value) {
        getBundleData().putFloat(key, value);
        return this;
    }

    public Router putDouble(@Nullable String key, double value) {
        getBundleData().putDouble(key, value);
        return this;
    }

    public Router putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        getBundleData().putCharSequence(key, value);
        return this;
    }

    public Router putParcelable(@Nullable String key, @Nullable Parcelable value) {
        getBundleData().putParcelable(key, value);
        return this;
    }

    public Router putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        getBundleData().putParcelableArray(key, value);
        return this;
    }

    public Router putParcelableArrayList(@Nullable String key,
                                         @Nullable ArrayList<? extends Parcelable> value) {
        getBundleData().putParcelableArrayList(key, value);
        return this;
    }


    public Router putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        getBundleData().putIntegerArrayList(key, value);
        return this;
    }

    public Router putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        getBundleData().putStringArrayList(key, value);
        return this;
    }

    public Router putCharSequenceArrayList(@Nullable String key,
                                           @Nullable ArrayList<CharSequence> value) {
        getBundleData().putCharSequenceArrayList(key, value);
        return this;
    }

    public Router putSerializable(@Nullable String key, @Nullable Serializable value) {
        getBundleData().putSerializable(key, value);
        return this;
    }

    /**
     * 设置跳转动画
     * @param options
     * @return
     */
    public Router options(ActivityOptionsCompat options) {
        this.options = options;
        return this;
    }

    /**
     * 设置请求码
     * @param requestCode
     * @return
     */
    public Router requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * 设置跳转动画
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public Router anim(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * 跳转
     */
    public void launch() {
        try {
            if (intent != null && from != null && to != null) {

                if (callback != null) {
                    callback.onBefore(from, to);
                }

                intent.setClass(from, to);

                intent.putExtras(getBundleData());

                if (options == null) {
                    if (requestCode < 0) {
                        from.startActivity(intent);
                    } else {
                        from.startActivityForResult(intent, requestCode);
                    }

                    if (enterAnim > 0 && exitAnim > 0) {
                        from.overridePendingTransition(enterAnim, exitAnim);
                    }
                } else {
                    if (requestCode < 0) {
                        ActivityCompat.startActivity(from, intent, options.toBundle());
                    } else {
                        ActivityCompat.startActivityForResult(from, intent, requestCode, options.toBundle());
                    }
                }

                if (callback != null) {
                    callback.onNext(from, to);
                }
            }
        } catch (Throwable throwable) {
            if (callback != null) {
                callback.onError(from, to, throwable);
            }
        }
    }

    private Bundle getBundleData() {
        if (data == null) {
            data = new Bundle();
        }
        return data;
    }

    /**
     * 设置跳转回调
     * @param callback
     */
    public static void setCallback(RouterCallback callback) {
        Router.callback = callback;
    }
}
