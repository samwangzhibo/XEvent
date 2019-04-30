package com.wzb.xevent.xevent.stream;


import android.text.TextUtils;

import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.XPConstant;
import com.wzb.xevent.debug.DebugConfig;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.parser.ConfigParser;
import com.wzb.xevent.parser.ICreateParseDescription;
import com.wzb.xevent.tracker.SimpleXPTracker;
import com.wzb.xevent.tracker.XPEventTracker;
import com.wzb.xevent.util.CycleExamine;
import com.wzb.xevent.util.FormatUtil;
import com.wzb.xevent.util.LogcatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 流式打点 事件流
 * Created by samwangzhibo on 2018/7/18.
 */

public class XPEventStream implements com.wzb.xevent.xevent.stream.IStream {
    public static final String TAG = "XPEventStream";
    public static final String TAG_PATCH = "patch";
    /**
     * 维护所有tracker的Map
     */
    private ConcurrentHashMap<Object, CopyOnWriteArrayList<XPEventTracker>> trackers = new ConcurrentHashMap<>();
    /**
     * Tracker触发打点时，会回调接口
     */
    private HashMap<String, List<ILogCallback>> logCallBacks = new HashMap<>();
    /**
     * 暂存Tracker返回的event
     */
    private LinkedList<XPEvent> bufferSendEvent = new LinkedList<>();
    /**
     * traker模板对象，以便于group类型tracker深克隆
     */
    private HashMap<Object, CopyOnWriteArrayList<XPEventTracker>> templateTrakers = new HashMap<>();
    /**
     * 外部传入的 为了给simpleTracker使用
     */
    private com.wzb.xevent.xevent.stream.IStreamLogCallback iStreamLogCallback;
    /**
     * 异常判断器
     */
    private CycleExamine cycleExamine = new CycleExamine();
    /**
     * todo
     * 注册事件的回调
     *
     * @param xpEventId 事件id
     * @param callbacks 事件回调
     */
    public void registerLogCallback(String xpEventId, ILogCallback... callbacks) {
        if (logCallBacks.containsKey(xpEventId)) {
            List<ILogCallback> logCallbacks = logCallBacks.get(xpEventId);
            for (ILogCallback callback : callbacks) {
                //把现在没有的回调加上
                if (!logCallbacks.contains(callback)) {
                    logCallbacks.add(callback);
                }
            }

        } else {
            List<ILogCallback> logCallbacks = new ArrayList<>();
            Collections.addAll(logCallbacks, callbacks);
            logCallBacks.put(xpEventId, logCallbacks);
        }
    }

    /**
     * todo
     * 解绑事件的回调
     *
     * @param xpEventId 事件id
     * @param callbacks 事件回调
     */
    public void unregisterLogCallback(int xpEventId, ILogCallback... callbacks) {
        if (logCallBacks.containsKey(xpEventId)) {
            List<ILogCallback> logCallbacks = logCallBacks.get(xpEventId);
            for (ILogCallback callback : callbacks) {
                //把现在有的回调删掉
                if (logCallbacks.contains(callback)) {
                    logCallbacks.remove(callback);
                }
            }
        }
    }

    /**
     * todo
     * 根据事件执行回调
     */
    private void invokeLogCallbacks(final XPEvent xpEvent) {
        if (xpEvent == null) return;
        String xpEventId = xpEvent.xpEventId;
        if (logCallBacks.containsKey(xpEventId)) {
            List<ILogCallback> logCallbacks = logCallBacks.get(xpEventId);
            for (final ILogCallback callback : logCallbacks) {
                XEvent.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLog(xpEvent);
                    }
                });
            }
        }
    }

    /**
     * 注册Tracker
     *
     * @param xpEventTracker
     */
    @Override
    public void register(XPEventTracker xpEventTracker) {
        Object group = xpEventTracker.getGroup();
        if (group == null) return;
        putTracker2Map(trackers, group, xpEventTracker);
    }

    private void putTracker2Map(Map<Object, CopyOnWriteArrayList<XPEventTracker>> trackers, Object alias, XPEventTracker xpEventTracker) {
        if (!trackers.containsKey(alias)) {
            CopyOnWriteArrayList<XPEventTracker> trackerList = new CopyOnWriteArrayList<>();
            trackerList.add(xpEventTracker);
            trackers.put(alias, trackerList);
        } else {
            List<XPEventTracker> trackerList = trackers.get(alias);
            trackerList.add(xpEventTracker);
        }
    }

    /**
     * register tracker by config
     *
     * @param config
     */
    public void registerTrakerByConfig(String config) {
        if (!DebugConfig.isDebug) {
            try {
                doRegisterTrakerByConfig(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            doRegisterTrakerByConfig(config);
        }
    }

    private void doRegisterTrakerByConfig(String config) {
        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setICreateParseDescription(getICreateParseDescription());
        List<SimpleXPTracker> trackers = configParser.parseTrakersByConfig(config);
        if (trackers == null || trackers.size() == 0){
            return;
        }
        for (XPEventTracker tracker : trackers) {
            String group = FormatUtil.getString(tracker.getGroup());
            //如果是群组类型，放入模板池
            if (!TextUtils.isEmpty(group)) {
                putTracker2Map(templateTrakers, group, tracker);
            } else {
                register(tracker);
            }
        }
    }

    protected ICreateParseDescription getICreateParseDescription() {
        return null;
    }

    /**
     * 动态注册patch中的Tracker
     * @param config patch的配置
     */
    public void registerTrakerByPatch(String config) {
        unRegisterByGroup(TAG_PATCH);
        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setICreateParseDescription(getICreateParseDescription());
        for (XPEventTracker tracker : configParser.parseTrakersByConfig(config)) {
            tracker.setGroup(TAG_PATCH);
            register(tracker);
        }
    }

    /**
     * 根据别名来解绑Tracker 比如一次性解绑所有卡片Tracker
     *
     * @param alias
     */
    @Override
    public void unRegisterByGroup(Object alias) { //tracker和event解绑
        if (trackers.containsKey(alias)) {
            trackers.remove(alias);
        }
    }

    /**
     * 解绑Tracker
     *
     * @param xpEventTracker
     */
    @Override
    public void unRegister(XPEventTracker xpEventTracker) {
        //群组类型
        if (xpEventTracker.isGroupType()) {
            if (trackers.containsKey(xpEventTracker.getGroup())) {
                trackers.get(xpEventTracker.getGroup()).remove(xpEventTracker);
            }
        }
        if (trackers.containsKey(xpEventTracker)) {
            trackers.remove(xpEventTracker);
        }
    }

    /**
     * 业务层设置打点方法
     *
     * @param iStreamLogCallback 业务层传递的打点方法
     */
    @Override
    public void setIStreamLogCallback(com.wzb.xevent.xevent.stream.IStreamLogCallback iStreamLogCallback) {
        this.iStreamLogCallback = iStreamLogCallback;
    }


    /**
     * 流式打点主入口 放置一个事件到流里面
     *
     * @param xpEvent 事件
     */
    @Override
    public void sendEvent(XPEvent xpEvent) {
        //如果该事件id注册了回调，触发回调
        invokeLogCallbacks(xpEvent);
        if (createTracker(xpEvent)) {
            return;
        }
        Iterator<Map.Entry<Object, CopyOnWriteArrayList<XPEventTracker>>> it = trackers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, CopyOnWriteArrayList<XPEventTracker>> entry = it.next();
            CopyOnWriteArrayList<XPEventTracker> trackerList = entry.getValue();
            //ConcurrentModificationException
            for (XPEventTracker tracker : trackerList) {
                if (tracker instanceof SimpleXPTracker) {
                    ((SimpleXPTracker) tracker).setiStreamLogCallback(iStreamLogCallback);
                }
                // TODO: 2018/12/17 del 测试使用
//                if (TextUtils.equals(tracker.eventName, "xpanel_card_eff_ck")){
//                    LogcatUtil.e(TAG, "meet it", false);
//                }
                if (!tracker.isLoop && tracker.isEnd) {
                    unRegister(tracker);
                } else {
                    XPEvent resultEvent = tracker.receiveEvent(xpEvent);
                    if (resultEvent != null && !TextUtils.equals(resultEvent.xpEventId, XPConstant.SUCCESS_XPEVENT_ID)) {
                        bufferSendEvent.offer(resultEvent);
//                        LogcatUtil.e(TAG, "bufferSendEvent add : " + resultEvent);
                    }
                }
            }
        }
        cycleExamine.enterCycle();
        //处理完所有Tracker之后，发送缓存的event
        XPEvent bufferEvent;
        try {
            /**
             * fix java.util.NoSuchElementException
             at java.util.LinkedList.removeFirstImpl(LinkedList.java:689)
             */
            while (!bufferSendEvent.isEmpty() && bufferSendEvent.peek() != null &&
                    (bufferEvent = bufferSendEvent.poll()) != null) {
                LogcatUtil.e(TAG, "sendEvent bufferEvent : " + bufferEvent);
                sendEvent(bufferEvent);
            }
        }catch (Exception e){
            LogcatUtil.e(TAG, "bufferSendEvent.poll() exception : " + e.getMessage());
        }
        cycleExamine.exitCycle();
    }

    public boolean createTracker(XPEvent xpEvent) {
        return false;
    }

    /**
     * 对于全局使用一个打点的应用，使用该方法
     */
    public static XPEventStream getInstance() {
        return XPEventStreamInner.instance;
    }

    /**
     * 通过模板池创建Tracker
     *
     * @param group          组
     * @param iHandleTracker 把Tracker抛到外面给外层使用
     * @throws CloneNotSupportedException
     */
    public void registerTrackerByGroupTemplate(String group, IHandleTracker iHandleTracker) throws CloneNotSupportedException {
        if (templateTrakers.get(group) == null) {
            return;
        }
        for (XPEventTracker templateTraker : templateTrakers.get(group)) {
            if (templateTraker instanceof SimpleXPTracker) {
                SimpleXPTracker tracker = (SimpleXPTracker) templateTraker.clone();
                if (tracker != null) {
                    if (iHandleTracker != null) {
                        iHandleTracker.handleTracker(tracker);
                    }
                    //抛到队列后面，待会执行
//                    XEvent.getInstance().registerTraker(this, tracker);
                    //同步执行
                    register(tracker);
                }
            }
        }
    }

    public interface IHandleTracker {
        void handleTracker(SimpleXPTracker simpleXPTracker);
    }

    public static class XPEventStreamInner {
        private XPEventStreamInner() {

        }

        static XPEventStream instance = new XPEventStream();
    }
}
