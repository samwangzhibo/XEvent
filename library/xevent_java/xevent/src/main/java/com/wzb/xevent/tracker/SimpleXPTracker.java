package com.wzb.xevent.tracker;


import android.text.TextUtils;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.xevent.stream.IStreamLogCallback;

import java.util.ArrayList;
import java.util.Arrays;

import static com.wzb.xevent.constant.XPConstant.DEFAULT_RESEND_XPEVENT_ID;

/**
 * 简单版本的Tracker 支持传入xpDescriptions、打点名等基础参数
 * Created by samwangzhibo on 2018/7/19.
 */
public class SimpleXPTracker extends XPEventTracker implements Cloneable {
    /**
     * 外部传入到XEventSDK的打点回调
     */
    private IStreamLogCallback iStreamLogCallback;
    /**
     * Tracker所在分组
     */
    private String group;

    public SimpleXPTracker() {
        this(true, null, "", DEFAULT_RESEND_XPEVENT_ID, null);
    }

    public SimpleXPTracker(XPDescription[] xpDescriptions) {
        this(true, xpDescriptions, "", DEFAULT_RESEND_XPEVENT_ID, null);
    }

    public SimpleXPTracker(XPDescription[] xpDescriptions, String eventName) {
        this(true, xpDescriptions, eventName, DEFAULT_RESEND_XPEVENT_ID, null);
    }

    public SimpleXPTracker(XPDescription[] xpDescriptions, String eventName, String reSendXPEventId) {
        this(true, xpDescriptions, eventName, reSendXPEventId, null);
    }

    public SimpleXPTracker(XPDescription[] xpDescriptions, String eventName, String reSendXPEventId, ILogCallback[] iLogCallbacks) {
        this(true, xpDescriptions, eventName, reSendXPEventId, iLogCallbacks);
    }

    public SimpleXPTracker(XPDescription[] xpDescriptions, String eventName, ILogCallback[] iLogCallbacks) {
        this(true, xpDescriptions, eventName, DEFAULT_RESEND_XPEVENT_ID, iLogCallbacks);
    }

    public SimpleXPTracker(boolean isLoop, XPDescription[] xpDescriptions, String eventName, String reSendXPEventId, ILogCallback[] iLogCallbacks) {
        super(isLoop);
        this.eventName = eventName;
        this.xpDescriptions = xpDescriptions;
        this.reSendXPEventId = reSendXPEventId;
        if (iLogCallbacks != null) {
            this.iLogCallbacks = Arrays.asList(iLogCallbacks);
        }
    }

    public SimpleXPTracker setGroup(String group){
        this.group = group;
        return this;
    }

    public SimpleXPTracker setEventName(String eventName) {
        if (!TextUtils.isEmpty(eventName)) {
            this.eventName = eventName;
        }
        return this;
    }

    public SimpleXPTracker setDescriptions(XPDescription[] descriptions) {
        if (this.xpDescriptions == null)
            this.xpDescriptions = descriptions;
        return this;
    }

    public SimpleXPTracker setDescriptions(XPDescription descriptions) {
        if (this.xpDescriptions == null){
            this.xpDescriptions = new XPDescription[]{
                    descriptions
            };

        }
        return this;
    }

    public SimpleXPTracker setDescriptions(ArrayList<XPDescription> descriptions) {
        if (this.xpDescriptions == null)
            this.xpDescriptions = descriptions.toArray(new XPDescription[]{});
        return this;
    }

    public void setiStreamLogCallback(IStreamLogCallback iStreamLogCallback) {
        if (iStreamLogCallback == null || this.iStreamLogCallback != null) return;
        this.iStreamLogCallback = iStreamLogCallback;
    }

    @Override
    public XPDescription[] getDescription() {
        return xpDescriptions;
    }

    @Override
    public void onEventLog(XPEvent xpEvent) {
        if (iStreamLogCallback != null) {
            iStreamLogCallback.onEventLog(eventName, getAttrs(xpEvent.attrsMap));
        }
    }

    @Override
    public Object getGroup() {
        return !TextUtils.isEmpty(group) ? group : super.getGroup();
    }

}
