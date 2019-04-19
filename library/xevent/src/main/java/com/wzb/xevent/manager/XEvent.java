package com.wzb.xevent.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.stream.IStreamLogCallback;
import com.wzb.xevent.stream.XPEventStream;
import com.wzb.xevent.tracker.XPEventTracker;

/**
 * 入口类 负责把所有事件分发到指定的{@link com.wzb.xevent.stream.XPEventStream}
 * Created by samwangzhibo on 2018/12/26.
 */

public class XEvent implements IXEvent {
    private static final String TAG = "XEvent";
    public static final int FLAG_SEND_EVENT = 99;
    public static final int FLAG_REGISTE_TRACKER_BY_CONFIG = 100;
    public static final int FLAG_REGISTE_TRACKER = 101;

    public static final int FLAG_UNREGISTER_EVENT = 102;
    public static final int FLAG_UNREGISTER_BY_GROUP_EVENT = 103;

    private HandlerThread thread;
    public static boolean isInit = false;
    public static boolean isMainThread = false;
    /**
     * 线程是否释放
     */
    private boolean isRelease = false;
    private Handler mXEventHandler;
    private Handler mMainHandler;
    private IHandler iHandler;
    XPEventStream defaultEventStream;

    private XEvent() {
        checkInit();
    }

    public void checkInit(){
        if (!isInit){
            init();
        }
        isInit = true;
    }

    public static XEvent getInstance() {
        return Inner.instance;
    }

    public void setIStreamLogCallback(IStreamLogCallback iStreamLogCallback) {
        if (defaultEventStream == null){
            return;
        }
        defaultEventStream.setIStreamLogCallback(iStreamLogCallback);
    }

    public interface IHandler {
        void onHandleMessage(Message msg);
    }

    public void setIHandler(IHandler iHandler) {
        if (this.iHandler == null) {
            this.iHandler = iHandler;
        }
    }

    public void setThreadPriority(int newPriority){
        if (thread == null){
            return;
        }
        thread.setPriority(newPriority);
    }

    /**
     * 初始化 HandlerThread 和 Handler
     */
    @Override
    public void init() {
        thread = new HandlerThread(TAG + "_" + System.currentTimeMillis());
        thread.start();
        isRelease = false;
        mXEventHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (iHandler != null){
                    iHandler.onHandleMessage(msg);
                }
                if (msg.obj != null && msg.obj instanceof HandlerEntry) {
                    HandlerEntry handlerEntry = ((HandlerEntry) msg.obj);
                    switch (msg.what) {
                        case FLAG_SEND_EVENT:
                            handlerEntry.sendEvent();
                            break;
                        case FLAG_REGISTE_TRACKER_BY_CONFIG:
                            handlerEntry.registerTrakerByConfig();
                            break;
                        case FLAG_REGISTE_TRACKER:
                            handlerEntry.registerTraker();
                            break;
                        case FLAG_UNREGISTER_EVENT:
                            handlerEntry.unRegister();
                            break;
                        case FLAG_UNREGISTER_BY_GROUP_EVENT:
                            handlerEntry.unRegisterByGroup();
                            break;
                    }
                }
            }
        };
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void release() {
        if (thread != null) {
            thread.quit();
        }
        isRelease = true;
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
    }

    public void setDefaultEventStream(XPEventStream defaultEventStream) {
        this.defaultEventStream = defaultEventStream;
    }

    public void sendEvent(XPEvent xEvent) {
        ensureDefaultEventStream();
        sendEvent(defaultEventStream, xEvent);
    }

    private void ensureDefaultEventStream(){
        if (defaultEventStream == null){
            defaultEventStream = new XPEventStream();
        }
    }

    public void sendEvent(XPEventStream xpEventStream, XPEvent xEvent) {
        if (xpEventStream == null || xEvent == null) {
            return;
        }
        if (isMainThread) {
            xpEventStream.sendEvent(xEvent);
        } else if (!isRelease){
            mXEventHandler.sendMessage(Message.obtain(mXEventHandler, FLAG_SEND_EVENT, new HandlerEntry(xpEventStream, xEvent)));
        }
    }

    public void sendEventDelay(XPEvent xEvent, long duration){
        ensureDefaultEventStream();
        sendEventDelay(defaultEventStream, xEvent, duration);
    }

    public void sendEventDelay(final XPEventStream xpEventStream, final XPEvent xEvent, long duration){
        sendMessageDelay(Message.obtain(mXEventHandler, FLAG_SEND_EVENT, new HandlerEntry(xpEventStream, xEvent)), new Runnable() {
            @Override
            public void run() {
                xpEventStream.sendEvent(xEvent);
            }
        }, duration);
    }

    public void registerTrakerByConfig(XPEventStream xpEventStream, String config) {
        if (xpEventStream == null || TextUtils.isEmpty(config)) {
            return;
        }
        if (isMainThread) {
            xpEventStream.registerTrakerByConfig(config);
        } else if (!isRelease){
            mXEventHandler.sendMessage(Message.obtain(mXEventHandler, FLAG_REGISTE_TRACKER_BY_CONFIG, new HandlerEntry(xpEventStream, config)));
        }
    }

    public void registerTraker(XPEventStream xpEventStream, XPEventTracker xpEventTracker) {
        registerTraker(xpEventStream, xpEventTracker, 0);
    }

    public void unRegisterByGroup(final XPEventStream xpEventStream, final Object alias) {
        if (xpEventStream == null || alias == null) {
            return;
        }
        sendMessageDelay(Message.obtain(mXEventHandler, FLAG_UNREGISTER_BY_GROUP_EVENT, new HandlerEntry(xpEventStream).setAlias(alias)), new Runnable() {
            @Override
            public void run() {
                xpEventStream.unRegisterByGroup(alias);
            }
        }, 0);
    }

    public void unRegister(final XPEventStream xpEventStream, final XPEventTracker xpEventTracker) {
        if (xpEventStream == null || xpEventTracker == null) {
            return;
        }
        sendMessageDelay(Message.obtain(mXEventHandler, FLAG_UNREGISTER_EVENT, new HandlerEntry(xpEventStream).setTracker(xpEventTracker)), new Runnable() {
            @Override
            public void run() {
                xpEventStream.unRegister(xpEventTracker);
            }
        }, 0);
    }

    private void sendMessageDelay(Message message, Runnable runnable, long duration){
        if (isMainThread) {
            if (duration > 0) {
                postToMainThread(runnable, duration);
            }else {
                runnable.run();
            }
        } else if ((!isRelease)){
            if (duration > 0) {
                mXEventHandler.sendMessageDelayed(message, duration);
            } else {
                mXEventHandler.sendMessage(message);
            }
        }
    }

    public void registerTraker(final XPEventStream xpEventStream, final XPEventTracker xpEventTracker, long duration) {
        if (xpEventStream == null || xpEventTracker == null) {
            return;
        }
        if (isMainThread) {
            if (duration > 0) {
                postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        xpEventStream.register(xpEventTracker);
                    }
                }, duration);
            }else {
                xpEventStream.register(xpEventTracker);
            }
        } else {
            if (duration > 0) {
                mXEventHandler.sendMessageDelayed(Message.obtain(mXEventHandler, FLAG_REGISTE_TRACKER, new HandlerEntry(xpEventStream, xpEventTracker)), duration);
            } else {
                mXEventHandler.sendMessage(Message.obtain(mXEventHandler, FLAG_REGISTE_TRACKER, new HandlerEntry(xpEventStream, xpEventTracker)));
            }
        }
    }

    static class Inner {
        static XEvent instance = new XEvent();

        private Inner() {

        }
    }

    public static class HandlerEntry {
        public XPEventStream xpEventStream;
        public XPEvent xEvent;
        public String config;
        public Object alias;
        public XPEventTracker tracker;

        public HandlerEntry(XPEventStream xpEventStream) {
            this.xpEventStream = xpEventStream;
        }

        public XPEventTracker xpEventTracker;

        public HandlerEntry(XPEventStream xpEventStream, XPEvent xEvent) {
            this.xpEventStream = xpEventStream;
            this.xEvent = xEvent;
        }

        public HandlerEntry(XPEventStream xpEventStream, String config) {
            this.xpEventStream = xpEventStream;
            this.config = config;
        }

        public HandlerEntry(XPEventStream xpEventStream, XPEventTracker xpEventTracker) {
            this.xpEventStream = xpEventStream;
            this.xpEventTracker = xpEventTracker;
        }

        public void sendEvent() {
            if (xpEventStream != null) {
                xpEventStream.sendEvent(xEvent);
            }
        }

        public void registerTrakerByConfig() {
            if (xpEventStream != null) {
                xpEventStream.registerTrakerByConfig(config);
            }
        }

        public void registerTraker() {
            if (xpEventStream != null) {
                xpEventStream.register(xpEventTracker);
            }
        }

        public void unRegister(){
            if (xpEventStream != null) {
                xpEventStream.unRegister(xpEventTracker);
            }
        }

        public void unRegisterByGroup(){
            if (xpEventStream != null) {
                xpEventStream.unRegisterByGroup(alias);
            }
        }

        public HandlerEntry setAlias(Object alias) {
            this.alias = alias;
            return this;
        }

        public HandlerEntry setTracker(XPEventTracker tracker) {
            this.tracker = tracker;
            return this;
        }

    }

    public void postToMainThread(Runnable runnable) {
        postToMainThread(runnable, 0);
    }

    public void postToMainThread(Runnable runnable, long duration) {
        if (mMainHandler != null) {
            if (duration > 0){
                mMainHandler.postDelayed(runnable, duration);
            }else {
                mMainHandler.post(runnable);
            }
        }
    }

    /**
     * 处理runnnable到{@link #thread}
     * @param runnable
     */
    public void handleRunnable(Runnable runnable){
        if (isRelease){
            return;
        }
        mXEventHandler.post(runnable);
    }

    public void handleRunnableDelay(Runnable runnable, long duration){
        if (isRelease){
            return;
        }
        mXEventHandler.postDelayed(runnable, duration);
    }
}
