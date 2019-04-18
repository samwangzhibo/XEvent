package com.wzb.xevent.tracker;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.description.XPDescription;

import java.util.Map;

/**
 * Tracker 满足多个时刻的条件描述并最终触发打点
 * Created by samwangzhibo on 2018/7/19.
 */

public interface ITracker {
    /**
     * 获取描述的方法
     * @return
     */
    XPDescription[] getDescription();

    /**
     * 接受事件的主入口
     * @param xpEvent 事件
     * @return
     */
    XPEvent receiveEvent(XPEvent xpEvent);

    /**
     * 生命周期(触发打点)
     * @param xpEvent 事件
     */
    void onEventLog(XPEvent xpEvent);

    /**
     * 生命周期(触发打点后，抛掷新事件)
     * @param xpEvent 抛掷新事件∂
     * @return
     */
    XPEvent onSendEvent(XPEvent xpEvent);

    /**
     * 生命周期(结束，如果是loop类型的话，会重置)
     */
    void onEnd();

    /**
     * 生命周期(break条件重置)
     */
    void onReset();

    /**
     * tracker的组别 可以根据组别一次处理一类tracker
     * 比如 每张卡片有效曝光时有一个tracker，但是我回收时，我是希望一次性回收所有的卡片的tracker
     *
     * @return
     */
    Object getGroup();

    /**
     * 是否是群组类型
     * @return
     */
    boolean isGroupType();

    /**
     * 将最后一个事件的数据和之前tracker里面的数据做合并
     *
     * @param cardMap 最后一个事件携带的数据
     * @return 合并后的数据
     */
    Map<String, Object> getAttrs(Map<String, Object> cardMap);
}
