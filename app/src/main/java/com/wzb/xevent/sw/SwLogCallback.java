package com.wzb.xevent.sw;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.util.LogcatUtil;

/**
 * Created by samwangzhibo on 2018/8/10.
 */

public class SwLogCallback implements ILogCallback {
    private static final String TAG = "XPanelSwLogCallback";
    @Override
    public void onLog(XPEvent xpEvent) {
        LogcatUtil.e(TAG, "查看时长的回调");
    }
}