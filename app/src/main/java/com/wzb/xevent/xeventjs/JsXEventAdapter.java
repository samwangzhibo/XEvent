package com.wzb.xevent.xeventjs;

import com.google.gson.Gson;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.test.constant.AttrsConstant;

import java.util.Map;

/**
 * 适配器 把Java版本的 {@link XPEvent} 适配成js版本需要的
 * 主要工作是 去掉里面的obj的对象，不然gson要崩
 * Created by samwangzhibo on 2019/3/5.
 */

class JsXEventAdapter {
    private static final String TAG = "JsXEventAdapter";
    private Gson gson = new Gson();

    XPEvent getEvent(XPEvent xpEvent){
        xpEvent.allAttrsMap = null;
        Map<String, Object> eventAttrsMap = xpEvent.eventAttrsMap;
        if (eventAttrsMap.containsKey(AttrsConstant.CARD_DATA)){
            eventAttrsMap.remove(AttrsConstant.CARD_DATA);
        }
        if (eventAttrsMap.containsKey(AttrsConstant.OBJ)){
            eventAttrsMap.remove(AttrsConstant.OBJ);
        }
        return xpEvent;
    }

    String getEventStr(XPEvent xpEvent){
        final long nowTime = System.currentTimeMillis();
        //gson 解析0-1s
        String eventJsonStr = "";
        try {
            eventJsonStr = gson.toJson(xpEvent);
        }catch (Throwable e){
//            e.printStackTrace();
        }
        //打印XEvent
//        LogcatUtil.e(TAG + "_get_event", String.format("sendEvent(%s)", eventJsonStr));

//        LogcatUtil.e(TAG, "realSendEvent : " + xpEvent.xpEventId + ", cost time : " + (System.currentTimeMillis() - nowTime));
        return eventJsonStr;
    }
}
