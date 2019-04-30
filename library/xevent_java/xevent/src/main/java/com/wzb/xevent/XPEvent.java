package com.wzb.xevent;

import android.text.TextUtils;

import com.wzb.xevent.util.FormatUtil;
import com.wzb.xevent.util.MapUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * the event which contains {@link #allAttrsMap}({@link #attrsMap}, {@link #eventAttrsMap})
 * and {@link #xpEventId} and {@link #time}
 * Created by samwangzhibo on 2018/7/18.
 */

public class XPEvent {
    private static final String TAG = "XPEvent";
    /**
     * the key of map which means the time that event send
     */
    public static final String TIME = "event_time";
    /**
     * the key of map which means event id
     */
    public static final String XP_EVENT_ID = "event_id";
    /**
     * id of the event
     */
    public String xpEventId;
    /**
     * time the event send
     */
    public long time;
    /**
     * 上报需要的参数
     */
    public Map<String, Object> attrsMap;
    /**
     * 事件判断需要的参数 不需要上报
     */
    public Map<String, Object> eventAttrsMap = new HashMap<>();
    /**
     * 全部参数 包含上报参数和事件参数
     */
    public Map<String, Object> allAttrsMap = new HashMap<>();

    /**
     * new instance by event id
     *
     * @param xpEventId event id
     */
    public XPEvent(String xpEventId) {
        this(xpEventId, null);
    }

    /**
     * new instance by the id of event and the attr map
     *
     * @param xpEventId the id of event
     * @param attrsMap  the attr map which contains data
     */
    public XPEvent(String xpEventId, Map<String, Object> attrsMap) {
        this.xpEventId = xpEventId;
        //传的是引用
        this.attrsMap = MapUtil.getNewMap(attrsMap);
        time = System.currentTimeMillis();
        MapUtil.putAttr(this.eventAttrsMap, TIME, getTime());
        MapUtil.putAttr(this.eventAttrsMap, XP_EVENT_ID, xpEventId);
        allAttrsMap.putAll(this.attrsMap);
        allAttrsMap.putAll(eventAttrsMap);
    }

    /**
     * put the event attr which is not needed upload
     *
     * @param key   key of data
     * @param value key of value
     * @return the event instance
     */
    public XPEvent putEventAttrs(String key, Object value) {
        eventAttrsMap.put(key, value);
        allAttrsMap.put(key, value);
        return this;
    }

    /**
     * put the event attr which is needed upload
     *
     * @param key   key of data
     * @param value key of value
     * @return the event instance
     */
    public XPEvent putAttrs(String key, Object value) {
        attrsMap.put(key, value);
        allAttrsMap.put(key, value);
        return this;
    }

    /**
     * remove the attr from the event
     * @param key
     */
    public void removeAttrs(String key){
        attrsMap.remove(key);
        allAttrsMap.remove(key);
    }

    /**
     * put the event attr which is needed upload
     *
     * @param attrsMap the map contains event attr
     * @return the event instance
     */
    public XPEvent putAttrsMap(Map<String, Object> attrsMap) {
        if (MapUtil.isEmpty(attrsMap)) {
            return this;
        }
        attrsMap.remove(TIME);
        attrsMap.remove(XP_EVENT_ID);
        this.attrsMap.putAll(attrsMap);
        allAttrsMap.putAll(attrsMap);
        return this;
    }

    /**
     * put the event attr which is not needed upload
     *
     * @param eventAttrsMap the map contains event attr
     * @return the event instance
     */
    public XPEvent putEventAttrsMap(Map<String, Object> eventAttrsMap) {
        if (MapUtil.isEmpty(eventAttrsMap)) {
            return this;
        }
        eventAttrsMap.remove(TIME);
        eventAttrsMap.remove(XP_EVENT_ID);
        this.eventAttrsMap.putAll(eventAttrsMap);
        allAttrsMap.putAll(eventAttrsMap);
        return this;
    }

    /**
     * get valu of the key from {@link #eventAttrsMap} and {@link #attrsMap}
     *
     * @param key the key of <K,V>
     * @return the value of <K,V>
     */
    public Object getValue(String key) {
        if (TextUtils.equals(key, TIME)) return time;
        if (TextUtils.equals(key, XP_EVENT_ID)) return xpEventId;
        return getValueByName(key);
    }

    /**
     * the simple method to get value of {@link String} type by {@link #getValue(String)}
     *
     * @param key the key of <K,V>
     * @return the value of <K,V>
     */
    public String getString(String key) {
        Object value = getValueByName(key);
        if (value != null && value instanceof String) {
            return (String) value;
        }
        return "";
    }

    /**
     * the simple method to get value of {@link Integer} type by {@link #getValue(String)}
     *
     * @param key the key of <K,V>
     * @return the value of <K,V>
     */
    public int getInt(String key) {
        Object value = getValue(key);
        if (value == null) {
            return -1;
        }
        return FormatUtil.getInt(value);
    }

    /**
     * get {@link #TIME}
     *
     * @return {@link #TIME}
     */
    public long getTime() {
        return time;
    }

    /**
     * get value from map which contains attr
     *
     * @param key the key of <K,V>
     * @return the value of <K,V>
     */
    private Object getValueByName(String key) {
        if (MapUtil.isEmpty(attrsMap) && MapUtil.isEmpty(eventAttrsMap)) return null;
        Object value = attrsMap.get(key);
        if (value == null) {
            value = eventAttrsMap.get(key);
        }
        return value;
    }

    @Override
    public String toString() {
        return "xpEventId : " + xpEventId + ", eventAttrsMap : " + eventAttrsMap;
    }
}
