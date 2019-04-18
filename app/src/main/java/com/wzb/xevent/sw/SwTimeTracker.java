package com.wzb.xevent.sw;


import com.wzb.xevent.BindXPEventId;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.EventConstant;
import com.wzb.xevent.description.SimpleXPDescription;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.tracker.XPEventTracker;


/**
 * 框架展示时长
 * Created by samwangzhibo on 2018/7/19.
 */
@BindXPEventId(xpEventId = EventConstant.EVENT_PAGE_SW_TIME, callbacks = SwLogCallback.class)
public class SwTimeTracker extends XPEventTracker {
    public SwTimeTracker() {
        super(true);
    }

    @Override
    public XPDescription[] getDescription() {
        return new XPDescription[]{
                new SimpleXPDescription(EventConstant.EVENT_PAGE_KEEP),
                new SimpleXPDescription(EventConstant.EVENT_PAGE_RELEASE)
        };
    }

    @Override
    public void onEventLog(XPEvent xpEvent) {
        long keepTime = getXpEventById(1).getTime() - getXpEventById(0).getTime();
        putAttr("time", keepTime); //获取停留时长
    }

}
