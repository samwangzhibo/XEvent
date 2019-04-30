package com.wzb.xevent.eventsourse;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.xevent.stream.XPEventStream;
import com.wzb.xevent.util.LogcatUtil;

/**
 * Created by samwangzhibo on 2018/7/19.
 */

public class XPEventSourse {
    private static final String TAG = "XPEventSourse";
    public XPEventStream xpEventStream;
    public XPEventSourse(XPEventStream xpEventStream){
        this.xpEventStream = xpEventStream;
    }

    void sendEvent(XPEvent event){
        LogcatUtil.e(TAG, "sendEvent XPEventSourse");
        xpEventStream.sendEvent(event);
    }
}
