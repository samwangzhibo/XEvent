package com.wzb.xeventjs;

/**
 * js层回调java层的代码
 * Created by samwangzhibo on 2019/2/19.
 */

public interface IJsBridgeImpl {
    void triggerReport(String eventName, String jsonData);
}
