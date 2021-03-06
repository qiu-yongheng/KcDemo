package com.qyh.rongclound.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.vise.log.ViseLog;
import com.vise.utils.cipher.BASE64;
import com.vise.utils.convert.ByteUtil;
import com.vise.utils.convert.HexUtil;

/**
 * @author 邱永恒
 * @time 2017/10/23  16:19
 * @desc SharedPreferences存储，支持对象加密存储
 */

public class SpCache {
    private SharedPreferences sp;

    public SpCache(Context context) {
        // 创建对象时, 设置缓存地址
        this(context, "sp_cache");
    }

    public SpCache(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSp() {
        return sp;
    }

    /**
     * 保存数据到SP
     *
     * 可以保存任何数据,
     * @param key key
     * @param ser 保存数据
     */
    public void put(String key, Object ser) {
        try {
            ViseLog.i(key + " put: " + ser);
            if (ser == null) {
                sp.edit().remove(key).apply();
            } else {
                byte[] bytes = ByteUtil.objectToByte(ser);
                bytes = BASE64.encode(bytes);
                put(key, HexUtil.encodeHexStr(bytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public Object get(String key) {
        try {
            String hex = get(key, null);
            if (hex == null) {
                return null;
            }
            byte[] bytes = HexUtil.decodeHex(hex.toCharArray());
            bytes = BASE64.decode(bytes);
            Object obj = ByteUtil.byteToObject(bytes);
            ViseLog.i(key + " get: " + obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否包含key对应的数据
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * 移除缓存
     * @param key
     */
    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

    /**
     * 清空缓存
     */
    public void clear() {
        sp.edit().clear().apply();
    }

    /**
     * 保存String
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (value == null) {
            sp.edit().remove(key).apply();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    /**
     * 保存Boolean
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 保存float
     * @param key
     * @param value
     */
    public void put(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 保存long
     * @param key
     * @param value
     */
    public void put(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 保存int
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 获取String
     * @param key
     * @param defValue
     * @return
     */
    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 获取 Boolean
     * @param key
     * @param defValue
     * @return
     */
    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取float
     * @param key
     * @param defValue
     * @return
     */
    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取int
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    /**
     * 获取long
     * @param key
     * @param defValue
     * @return
     */
    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }
}
