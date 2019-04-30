package com.wzb.xevent.xeventjs;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.test.util.LogcatUtil;

/**
 * Js 工具类
 * Created by samwangzhibo on 2019/1/30.
 */

public class JsTool extends Object implements IJsBridgeImpl{
    private static final String TAG = "JsTool";
    private Gson gson;
    private Context mContext;
    public JsTool(Gson gson, Context mContext) {
        this.gson = gson;
        this.mContext = mContext;
    }

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void triggerReport(String eventName, String jsonData) {
//        LogcatUtil.e(TAG, jsonData + "");
        XPEvent xpEvent = new Gson().fromJson(jsonData, XPEvent.class);
//        XPanelOmegaUtils.trackEvent(eventName, xpEvent.attrsMap, true, true);
        LogcatUtil.e(TAG, "Event=" + eventName + "         ," + (xpEvent.attrsMap == null ? "" : xpEvent.attrsMap.toString()));
    }

    @JavascriptInterface
    @Override
    public void toast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
