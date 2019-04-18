package com.wzb.xevent.stream;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.tracker.XPEventTracker;

/**
 * Stream的接口类
 * Created by samwangzhibo on 2018/8/14.
 */

public interface IStream {
    /**
     * 注册tracker
     *
     * @param xpEventTracker
     */
    void register(XPEventTracker xpEventTracker);

    /**
     * 解绑tracker
     * @param xpEventTracker
     */
    void unRegister(XPEventTracker xpEventTracker);

    /**
     * 根据组别解绑tradker
     * @param group
     */
    void unRegisterByGroup(Object group);

    /**
     * 发送事件给所有tracker
     * @param xpEvent
     */
    void sendEvent(XPEvent xpEvent);

    /**
     * 设置外部最终要调用的打点回调
     * @param iStreamLogCallback
     */
    void setIStreamLogCallback(IStreamLogCallback iStreamLogCallback);

    /**
     * 接收到事件后，通过模板动态创建Tracker
     * @param xpEvent 接收到的事件
     * @return
     */
    boolean createTracker(XPEvent xpEvent);
}
