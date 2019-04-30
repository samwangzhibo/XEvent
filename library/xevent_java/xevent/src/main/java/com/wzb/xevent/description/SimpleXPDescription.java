package com.wzb.xevent.description;

import android.text.TextUtils;

import com.wzb.xevent.XPEvent;

import java.util.ArrayList;

/**
 * 简化版description
 * 如果没有传需要校验的事件id 退化为XPDescription
 * Created by samwangzhibo on 2018/7/19.
 */

public class SimpleXPDescription extends XPDescription {
    public String xPEventId;
    private ArrayList<String> eventIds = new ArrayList<>();

    public SimpleXPDescription() {

    }

    public SimpleXPDescription(String xPEventId) {
        eventIds.add(xPEventId);
        this.xPEventId = xPEventId;
    }

    public SimpleXPDescription(ArrayList<String> xPEventIds) {
        eventIds.addAll(xPEventIds);
        if (xPEventIds.size() > 0) {
            xPEventId = xPEventIds.get(0);
        }
    }

    public SimpleXPDescription setXPEventId(String xPEventId) {
        eventIds.add(xPEventId);
        this.xPEventId = xPEventId;
        return this;
    }

    public SimpleXPDescription setXPEventIds(ArrayList<String> xPEventIds) {
        eventIds.addAll(xPEventIds);
        if (xPEventIds.size() > 0) {
            xPEventId = xPEventIds.get(0);
        }
        return this;
    }

    public boolean isMeetCondition(XPEvent xpEvent) { //命中条件
        //如果没有传入id 直接过滤 不做id校验
        if (eventIds == null || eventIds.isEmpty()) {
            return true;
        }
        for (String eventId : eventIds) {
            if (TextUtils.equals(xpEvent.xpEventId, eventId)) {
                return true;
            }
        }
        //事件id是否满足我们指定id
        return false;
    }
}
