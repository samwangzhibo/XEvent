package com.wzb.xevent.tracker;



import com.wzb.xevent.XPEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracker传递给description的回调 让description能够调用Tracker的方法
 * Created by samwangzhibo on 2018/8/9.
 */

public interface ITrackerCallback {
    /**
     * 根据索引，获取其他Description的event
     *
     * @param index tracker中的索引
     * @return 满足指定索引的description的XPEvent
     */
    XPEvent getXpEventById(int index);

    /**
     * 根据别名，获取其他Description的event
     *
     * @param alias tracker中的描述的别名
     * @return 满足指定索引的description的XPEvent
     */
    XPEvent getXpEventByAlias(String alias);

    /**
     * 给Description用的回调，用于把Description需要打点的值传入Tracker
     */
    void putAttr(String key, Object value);

    /**
     * 给Description用的回调，用于把Description需要打点的值传入Tracker
     * @param attrs description需要存储的值
     */
    void putAttr(HashMap<String, Object> attrs);

    /**
     * 给DSL使用 用于获取所有具有alias的description里面的值
     * @return 返回的map 其中的成员是 <别名.time, 1000>
     */
    Map<String, Object> getAttrsMapByAllAliasDescription(Map<String, Object> eventMap);

    /**
     * 非必要实现
     * 获取tracker
     * @return description的tracker
     */
    SimpleXPTracker getTracker();
}
