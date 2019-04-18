package com.wzb.xevent.tracker;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.wzb.xevent.BindXPEventId;
import com.wzb.xevent.XPConstant;

import com.wzb.xevent.XPEvent;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.logcallback.NullLogCallback;
import com.wzb.xevent.logcallback.SimpleLogCallback;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.util.LogcatUtil;
import com.wzb.xevent.util.MapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wzb.xevent.XPConstant.DEFAULT_RESEND_XPEVENT_ID;

/**
 * Tracker 满足在连续一段时间中多个时刻描述{@link XPDescription}的条件，然后触发打点和回调
 * 包含多个description和一个break功能的Tracker
 * Created by samwangzhibo on 2018/7/19.
 */
public abstract class XPEventTracker implements ITracker, ITrackerCallback {
    private static final String TAG = "XPEventTracker";
    /**
     * 如果这个Tracker有注解，解析注解，并设置{@link #isInitAnnotation}为true
     */
    private boolean isInitAnnotation = false;
    /**
     * 触发所有描述完成后，抛掷新的事件
     */
    protected String reSendXPEventId = DEFAULT_RESEND_XPEVENT_ID;
    /**
     * 打点完成之后的回调
     */
    protected List<ILogCallback> iLogCallbacks = new ArrayList<>();
    /**
     * 是否是循环任务，if true {@link #onEnd()}之后不会解绑 else 解绑
     */
    public boolean isLoop = true;
    /**
     * 状态值 表示是否一个周期结束 {@link #onEnd()}里面设置
     */
    public boolean isEnd = false;
    /**
     * 满足条件的索引，全部满足则可以触发打点
     */
    private int meetConditionIndex = 0;
    /**
     * 打点名
     */
    public String eventName;
    /**
     * 满足所有条件后，打点完成，最后需要重置到的条件索引
     */
    private int eventEndIndex = 0;
    /**
     * tracker保存下来的参数 最后上报的时候需要取出来使用
     */
    private HashMap<String, Object> attrs = new HashMap<>();
    /**
     * description数组，满足所有description则调用{@link #onEventLog(XPEvent)}打点事件
     */
    public XPDescription[] xpDescriptions;
    /**
     * 注销自己的时候 也注销自己的breakTracker
     */
    private XPEventTracker breakEventTracker;
    /**
     * description是否已经初始化 初始化包括把初始化别名映射文件，传递tracker的功能给description使用
     */
    private boolean isDescriptionInit = false;
    /**
     * 描述的别名映射文件
     */
    private HashMap<String, XPDescription> descriptionAliasMap = new HashMap<>();
    /**
     * tracker的组别 可以根据组别一次处理一类tracker
     * 比如 每张卡片有效曝光时有一个tracker，但是我回收时，我是希望一次性回收所有的卡片的tracker
     */
    private Object group; //tracker的分组
    private boolean debugSwitch;
//    public String type; //card subcard 用于在代码里面插桩

    protected XPEventTracker() {
        this(true);
    }

    protected XPEventTracker(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public void checkDescription() {
        if (isDescriptionInit) {
            return;
        }
        xpDescriptions = getDescription();
        if (xpDescriptions == null) return;
        for (int i = 0; i < xpDescriptions.length; i++) {
            XPDescription description = xpDescriptions[i];
            initDescription(description, i);
        }
        isDescriptionInit = true;
    }

    public XPEventTracker setDefaultIndex(int defaultIndex) {
        this.meetConditionIndex = defaultIndex;
        return this;
    }

    public XPEventTracker setEventEndIndex(int eventEndIndex) {
        this.eventEndIndex = eventEndIndex;
        return this;
    }

    private void initDescription(XPDescription description, int i) {
        description.setITrackerCallback(this);
        description.setIndex(i);
        if (!TextUtils.isEmpty(description.getAlias())){ //如果描述具有别名(需要通信) 把别名存储下来
            descriptionAliasMap.put(description.getAlias(), description);
        }
    }

    public XPEventTracker setReSendXPEventId(String reSendXPEventId) {
        this.reSendXPEventId = reSendXPEventId;
        return this;
    }

    public XPEventTracker setILogCallbacks(List<ILogCallback> iLogCallbacks) {
        this.iLogCallbacks = iLogCallbacks;
        return this;
    }

    public XPEventTracker setILogCallbacks(ILogCallback[] iLogCallbacks){
        if (iLogCallbacks != null) {
            this.iLogCallbacks = Arrays.asList(iLogCallbacks);
        }
        return this;
    }

    public XPEventTracker setILogCallback(ILogCallback iLogCallback) {
        if (iLogCallback == null){
            return this;
        }
        if (iLogCallbacks == null){
            iLogCallbacks = new ArrayList<>();
        }
        iLogCallbacks.clear();
        iLogCallbacks.add(iLogCallback);
        return this;
    }

    /**
     * Tracker处理时间的入口
     *
     * @param xpEvent EventStream分发的事件
     * @return XPConstant.SUCCESS_EVENT(成功触发) null(重置，或者没有触发)
     */
    @Override
    public XPEvent receiveEvent(XPEvent xpEvent) {
        if (Looper.myLooper() == Looper.getMainLooper()){
            LogcatUtil.e(TAG, "主线程直接调用");
            return null;
        }
        if (isEnd){
            //重置状态到运行态
            isEnd = false;
        }
        //检查description 并做初始化
        checkDescription();
        //description校验
        if (xpDescriptions == null || (xpDescriptions.length == 0)) {
            isEnd = true;
            return null;
        }
        //判断是否命中break条件
        if (breakEventTracker != null){
            // TODO: 2018/12/17 del 测试使用
            if (TextUtils.equals(eventName, "xpanel_card_eff_ck") && (TextUtils.equals(xpEvent.xpEventId, "card_half_sw")
                    || TextUtils.equals(xpEvent.xpEventId, "card_ck") || TextUtils.equals(xpEvent.xpEventId, "card_half_move_out"))){
                LogcatUtil.e(TAG, "meet it", false);
            }
            XPEvent breakEvent = breakEventTracker.receiveEvent(xpEvent); //分发给breakTracker
            //break条件成功触发
            if (breakEvent != null && breakEvent == XPConstant.SUCCESS_EVENT){
                onReset();
                return null;
            }
        }
        int descriptionCount = xpDescriptions.length;
        //ArrayIndexOutOfBoundsException 多线程问题
        if (meetConditionIndex < descriptionCount) {
            int descriptionState = xpDescriptions[meetConditionIndex].receiveEvent(xpEvent);
            switch (descriptionState){
                case XPConstant.FLAG_DESCRIPTION_UNMEET_CONDITION:
                case XPConstant.FLAG_DESCRIPTION_SUPER_MEET_CONDITION:
                    return null;
                case XPConstant.FLAG_DESCRIPTION_MEET_CONDITION:
                    break;
                case XPConstant.FLAG_DESCRIPTION_BREAK:
                    //命中break条件 重置
                    onReset();
                    return null;
            }
            meetConditionIndex++; //命中条件，索引加1
        }
        if (meetConditionIndex >= descriptionCount) { //满足所有条件
            //打点
            onEventLog(xpEvent);
            XPEvent resendEvent = onSendEvent(xpEvent);
            //不需要重发事件的话 返回成功事件
            if (resendEvent == null) {
                resendEvent = XPConstant.SUCCESS_EVENT;
            }
            //生命周期 终止
            onEnd();
            return resendEvent;
        }
        return null;
    }

    public XPEventTracker setBreakEventTracker(XPEventTracker breakEventTracker) {
        this.breakEventTracker = breakEventTracker;
        return this;
    }

    public XPEventTracker getBreakEventTracker() {
        return breakEventTracker;
    }

    /**
     * 找子类要Descriptions
     */
    @Override
    public abstract XPDescription[] getDescription();

    /**
     * 满足所有Description触发打点 埋点事件放在这里
     *
     * @param xpEvent 最后一个事件
     */
    @Override
    public abstract void onEventLog(XPEvent xpEvent);

    /**
     * Tracker处理完事件了，看是否需要抛掷出新事件
     *
     * @param xpEvent 最后一个事件
     * @return
     */
    @Override
    public XPEvent onSendEvent(final XPEvent xpEvent) {
        //如果有注解的话 初始化注解
        initAnnotation();
        //执行事件完成回调
        for (final ILogCallback ilogCk : iLogCallbacks){
            if (ilogCk != null) {
                if (ilogCk instanceof SimpleLogCallback) {
                    SimpleLogCallback simpleLogCallback = (SimpleLogCallback) ilogCk;
                    if (simpleLogCallback.isMainThread()){
                        if (simpleLogCallback.isSynchronised()){
                            //主线程同步
                            XEvent.getInstance().postToMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ilogCk.onLog(xpEvent);
                                }
                            });
                        }else {
                            //异步
                            XEvent.getInstance().postToMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ilogCk.onLog(xpEvent);
                                }
                            });
                        }
                    } else {
                        if (simpleLogCallback.isSynchronised()){
                            //子线程同步
                            ilogCk.onLog(xpEvent);
                        }else {
                            //异步
                            XEvent.getInstance().handleRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    ilogCk.onLog(xpEvent);
                                }
                            });
                        }
                    }
                } else {
                    XEvent.getInstance().postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ilogCk.onLog(xpEvent);
                        }
                    });
                }
            }
        }
        //抛掷新事件
        if (!TextUtils.equals(reSendXPEventId, DEFAULT_RESEND_XPEVENT_ID)){
            return new XPEvent(reSendXPEventId, getAttrs(xpEvent.attrsMap)).putEventAttrsMap(xpEvent.eventAttrsMap);
        }
        return null;
    }

    /**
     * 初始化索引 每个Tracker对象只检查一次
     */
    private void initAnnotation() {
        if (!isInitAnnotation && TextUtils.equals(reSendXPEventId, DEFAULT_RESEND_XPEVENT_ID)
                && (iLogCallbacks == null || iLogCallbacks.size() == 0)) { //第一次初始化
            if (iLogCallbacks == null){
                iLogCallbacks = new ArrayList<>();
            }
            isInitAnnotation = true;
            BindXPEventId bindXPEventId;
            try {
                if (!getClass().isAnnotation()){
                    return;
                }
                bindXPEventId = getClass().getAnnotation(BindXPEventId.class);
                if (bindXPEventId == null) return;
                reSendXPEventId = bindXPEventId.xpEventId();
                //取出
                for (Class<? extends ILogCallback> ilogCkClass : bindXPEventId.callbacks()){
                    if (ilogCkClass != NullLogCallback.class){
                        try {
                            iLogCallbacks.add(ilogCkClass.newInstance());
                            LogcatUtil.e(TAG, "添加注解的回调成功");
                        } catch (InstantiationException e) {
                            LogcatUtil.e(TAG, "添加注解来的失败 : " + e.toString());
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            LogcatUtil.e(TAG, "添加注解来的失败 : " + e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Tracker的生命周期 表示此次事件流触发了打点
     */
    @Override
    public void onEnd() {
        isEnd = true;
        //设置完成之后需要重置的索引值
        meetConditionIndex = eventEndIndex;
        attrs.clear();
        //重置break条件
        if (breakEventTracker != null){
            breakEventTracker.onEnd();
        }
    }

    /**
     * Tracker的生命周期 表示此次事件流触发了重置状态
     */
    @Override
    public void onReset() {
        isEnd = false;
        meetConditionIndex = 0;
        attrs.clear();
        //重置break条件
        if (breakEventTracker != null){
            breakEventTracker.onEnd();
        }
    }

    public XPEventTracker setGroup(Object group) {
        this.group = group;
        return this;
    }

    /**
     * tracker的组别 可以根据组别一次处理一类tracker
     * 比如 每张卡片有效曝光时有一个tracker，但是我回收时，我是希望一次性回收所有的卡片的tracker
     *
     * @return
     */
    @Override
    public Object getGroup() {
        if (group != null) return group;
        return this;
    }

    public boolean isGroupType(){
        return getGroup() != this;
    }

    /**
     * 将最后一个事件的数据和之前tracker里面的数据做合并
     *
     * @param cardMap 最后一个事件携带的数据
     * @return 合并后的数据
     */
    @Override
    public Map<String, Object> getAttrs(Map<String, Object> cardMap) {
        if (attrs.size() == 0) return cardMap;
        if (cardMap != null && cardMap.size() != 0) {
            attrs.putAll(cardMap);
        }
        return attrs;
    }

    /**
     * 给Description用的回调，用于把Description需要打点的值传入Tracker
     */
    @Override
    public void putAttr(String key, Object value) {
        if (attrs == null){
            attrs = new HashMap<>();
        }
        attrs.put(key, value);
    }

    @Override
    public void putAttr(HashMap<String, Object> outerAttrs) {
        if (attrs == null){
            attrs = new HashMap<>();
        }
        attrs.putAll(outerAttrs);
    }

    /**
     * 给Description用的回调，用于Description之间的通信
     *
     * @param index 要获取的Description的索引(后期根据别名找Description，再新增方法)
     * @return
     */
    @Override
    public XPEvent getXpEventById(int index) {
        if (xpDescriptions == null || xpDescriptions.length <= index) {
            return null;
        }
        return xpDescriptions[index].xpEvent;
    }

    /**
     * 给Description用的回调，用于Description之间的通信
     *
     * @param alias 要获取的Description的别名
     * @return
     */
    @Override
    public XPEvent getXpEventByAlias(String alias) {
        if (descriptionAliasMap.size() == 0){
            return null;
        }
        XPDescription xpDescription = descriptionAliasMap.get(alias);
        if (xpDescription == null){
            return null;
        }
        return xpDescription.xpEvent;
    }

    @Override
    public Map<String, Object> getAttrsMapByAllAliasDescription(Map<String, Object> eventMap) {
        if (descriptionAliasMap.size() == 0) {
            return eventMap;
        }
        Map<String, Object> allDescription = new HashMap<>();
        for (int i = 0; i <= Math.min(meetConditionIndex - 1, xpDescriptions.length - 1); i++) {
            XPDescription description = xpDescriptions[i];
            //脏数据过滤
            if (description == null || description.xpEvent == null || MapUtil.isEmpty(description.xpEvent.allAttrsMap)) {
                continue;
            }
            //只取有别名的Description的数据
            if (TextUtils.isEmpty(description.getAlias())) {
                continue;
            }
            Map<String, Object> aliasAttrMap = description.xpEvent.allAttrsMap;
            for (String key : aliasAttrMap.keySet()) {
                String newKey = description.getAlias() + "." + key;
                allDescription.put(newKey, aliasAttrMap.get(key));
            }
        }
        allDescription.putAll(eventMap);
        return allDescription;
    }

//    public void setType(String type) {
//        this.type = type;
//    }

    @Override
    public SimpleXPTracker getTracker() {
        if (this instanceof SimpleXPTracker){
            return (SimpleXPTracker) this;
        }
        return null;
    }

    //调试好看
    @Override
    public String toString() {
        return getLogName() + "@" + Integer.toHexString(hashCode());
    }

    public String getLogName() {
        if (!TextUtils.isEmpty(eventName)) {
            return eventName;
        } else {
            return reSendXPEventId;
        }
    }

    /**
     * clone tracker from template
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        SimpleXPTracker o = null;
        try {
            o = (SimpleXPTracker) super.clone();
            XPDescription[] cloneDescriptions = new XPDescription[xpDescriptions.length];
            for (int i = 0; i < xpDescriptions.length; i++){
                cloneDescriptions[i] = xpDescriptions[i].clone();
            }
            o.xpDescriptions = cloneDescriptions;
            if (getBreakEventTracker() != null) {
                XPEventTracker breakTracker = (XPEventTracker) breakEventTracker.clone();
                o.setBreakEventTracker(breakTracker);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public void setDebugSwitch(boolean debugSwitch) {
        this.debugSwitch = debugSwitch;
    }

    public void log(String tag, String msg){
        if (debugSwitch){
            Log.e(tag, msg);
        }
    }
}
