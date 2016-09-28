package com.huaihuai.android.rb.util;

import com.orhanobut.logger.Logger;

/**
 * @author wangduo
 * @description: https://github.com/orhanobut/logger
 * @email: cswangduo@163.com
 * @date: 16/8/16
 */
public class LogUtil {

    public static void init(String tag) {
        Logger.init(tag);
    }

    public static void d(String message) {
        Logger.d(message);
    }

    public static void d(String tag, String message) {
        Logger.t(tag).d(message);
    }

    public static void e(String message) {
        Logger.e(message);
    }

    public static void e(String tag, String message) {
        Logger.t(tag).e(message);
    }

    public static void w(String message) {
        Logger.w(message);
    }

    public static void w(String tag, String message) {
        Logger.t(tag).w(message);
    }

    public static void v(String message) {
        Logger.v(message);
    }

    public static void v(String tag, String message) {
        Logger.t(tag).v(message);
    }

    public static void wtf(String message) {
        Logger.wtf(message);
    }

    public static void wtf(String tag, String message) {
        Logger.t(tag).wtf(message);
    }

    public static void json(String message) {
        Logger.json(message);
    }

    public static void json(String tag, String message) {
        Logger.t(tag).json(message);
    }

    public static void xml(String message) {
        Logger.xml(message);
    }

    public static void xml(String tag, String message) {
        Logger.t(tag).xml(message);
    }

}
