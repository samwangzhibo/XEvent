package com.wzb.wrapper;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xeventjs.XEventJsTool;

/**
 * XEvent的封装类  屏蔽底层事件处理引擎
 * 1、NA版本的xevent事件框架
 * 2、js版本的xevent事件框架
 *
 * Created by samwangzhibo on 2019/4/17.
 */

public class XEventWrapper {
    public static void sendEvent(final XPEvent xpEvent) {
        // 1.na版本模式
        XEvent.getInstance().sendEvent(xpEvent);

        // 2. js模式
//        XEventJsTool.getInstance().sendEvent(xpEvent);
    }
}
