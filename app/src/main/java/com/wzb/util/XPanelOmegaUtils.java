package com.wzb.util;

import java.util.Map;

/**
 * 上报的工具类
 * Created by samwangzhibo on 2019/4/19.
 */

public class XPanelOmegaUtils {
    private static final String TAG = "XPanelOmegaUtils";
    public static final String X_PANEL_SUBCARD_SW = "X_PANEL_SUBCARD_SW";
    public static final String X_PANEL_SUBCARD_SW_TIME = "X_PANEL_SUBCARD_SW_TIME";

    public static void trackEvent(String eventName, Map map) {
        LogcatUtil.e(TAG, "trackEvent eventName = " + eventName + ", map= " + map);
    }
}
