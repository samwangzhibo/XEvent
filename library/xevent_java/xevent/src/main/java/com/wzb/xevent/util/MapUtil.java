package com.wzb.xevent.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samwangzhibo on 2018/8/9.
 */

public class MapUtil {
    Map<String, Object> cardMap;
    public MapUtil(){

    }

    public static Map<String, Object> putAttr(Map<String, Object> cardMap, String key, Object value) {
        if (cardMap == null){
            cardMap = new HashMap<>();
        }
        cardMap.put(key, value);
        return cardMap;
    }

    public static Map<String, Object> putAttrs(String key, Object value) {
        return putAttr(null, key, value);
    }

    public static Map<String, Object> getNewMap(Map<String, Object> outerMap) {
        Map<String, Object>  cardMap = new HashMap<>();
        if (outerMap == null || outerMap.size() == 0){
            return cardMap;
        }
        cardMap.putAll(outerMap);
        return cardMap;
    }


    public MapUtil putAttr(String key, Object value) {
        if (cardMap == null){
            cardMap = new HashMap<>();
        }
        cardMap.put(key, value);
        return this;
    }

    public MapUtil putAttr(Map<String, Object> attrMap) {
        if (cardMap == null){
            cardMap = new HashMap<>();
        }
        cardMap.putAll(attrMap);
        return this;
    }

    public Map<String, Object> getMap() {
        return cardMap;
    }

    public static boolean isEmpty(Map map){
        return map == null || map.size() == 0;
    }
}
