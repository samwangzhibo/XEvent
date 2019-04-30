package com.wzb.xevent.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * 工具类
 * Created by samwangzhibo.
 */

public class Util {

    public static String getStringFromAsset(String assetName, Context context){
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(assetName);
            int i = -1;
            byte[] b = new byte[1024];
            while ((i = is.read(b)) != -1) {
                sb.append(new String(b, 0, i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int dip2px(Context context, int dp) {
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dp * scale + 0.5f);
    }
}
