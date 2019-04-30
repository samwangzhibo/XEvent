package com.wzb.xevent.stream;

import java.util.Map;

/**
 * 给业务层的打点回调
 * Created by samwangzhibo on 2018/8/12.
 */

public interface IStreamLogCallback {
    /**
     * 把打点名和参数交给业务层打点
     */
    void onEventLog(String eventName, Map<String, Object> attrs);
}
