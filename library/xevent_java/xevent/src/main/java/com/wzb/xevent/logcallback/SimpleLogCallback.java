package com.wzb.xevent.logcallback;


import com.wzb.xevent.XPEvent;

/**
 * 简单版的结束回调
 * Created by samwangzhibo on 2018/12/29.
 */

public class SimpleLogCallback implements ILogCallback {
    private long duration;
    private boolean isSynchronised;
    private boolean isMainThread;

    public SimpleLogCallback(boolean isSynchronised, boolean isMainThread) {
        this.isSynchronised = isSynchronised;
        this.isMainThread = isMainThread;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public long getDuration() {
        return duration;
    }

    public boolean isSynchronised() {
        return isSynchronised;
    }

    public boolean isMainThread() {
        return isMainThread;
    }

    @Override
    public void onLog(XPEvent xpEvent) {

    }
}
