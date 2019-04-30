package com.wzb.xevent.xeventjs;


import com.wzb.xevent.XPEvent;

/**
 * js工具类的接口
 * Created by samwangzhibo on 2019/2/19.
 */

public interface IXEventJsToolImpl {
    void sendEvent(XPEvent xpEvent);
    void destroy();
}
