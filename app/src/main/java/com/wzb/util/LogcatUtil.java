package com.wzb.util;

import android.util.Log;


public class LogcatUtil {
    public static boolean sLogEnable = true;

    public static void e(String tag, String info) {
        if (sLogEnable) {
            Log.e(tag, info);
        }
    }

    public static void e(String tag, String info, boolean isLog) {
        if (!isLog){
            return;
        }
        if (sLogEnable) {
            Log.e(tag, info);
        }
    }

    public static void w(String tag, String info) {
        if (sLogEnable) {
            Log.w(tag, info);
        }
    }

    public static void i(String tag, String info) {
        if (sLogEnable) {
            Log.i(tag, info);
        }
    }

    public static void d(String tag, String info) {
        if (sLogEnable) {
            Log.d(tag, info);
        }
    }

}
