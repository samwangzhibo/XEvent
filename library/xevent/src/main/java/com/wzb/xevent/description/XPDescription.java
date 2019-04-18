package com.wzb.xevent.description;

import android.util.Log;

import com.wzb.xevent.XPConstant;

import com.wzb.xevent.XPEvent;
import com.wzb.xevent.debug.IDebug;
import com.wzb.xevent.tracker.ITrackerCallback;
import com.wzb.xevent.tracker.SimpleXPTracker;

import java.util.HashMap;
import java.util.Map;

/**
 * description的抽象类
 * 子类需要复写{@link #isMeetCondition(XPEvent)} 满足条件的描述,
 * 比如
 * <pre>{@code
 *      public boolean isMeetCondition(XPEvent xpEvent) { //命中条件
            //如果没有传入id 直接过滤 不做id校验
            if (TextUtils.isEmpty(xPEventId)) {
            return true;
            }
            //事件id是否满足我们指定id
            return TextUtils.equals(xpEvent.xpEventId, xPEventId);
        }
 * }
 * </pre>
 *
 * 如果需要扩展默认的满足与不满足条件，则需要重写{@link #getStatus(XPEvent)},比如
 *
 * Created by samwangzhibo on 2018/7/19.
 */

public abstract class XPDescription implements IDescription, Cloneable, IDebug {
    /**
     * 满足条件的事件
     * 在满足条件的时候记录下来，用于给其他description获取当前满足事件的状态使用
     */
    public XPEvent xpEvent;
    /**
     * Tracker传递给description的回调 让description能够调用Tracker的方法
     */
    private ITrackerCallback iTrackerCallback;
    /**
     * 记录description在tracker中的索引，以便于通过索引-1获取上一个description的数据
     * {@link com.wzb.xevent.description.SimpleTimeDescription} 中取了上个索引的事件
     */
    protected int index;
    /**
     * 是否是调试模式
     */
    protected boolean isDebug;
    /**
     * {@link com.wzb.xevent.tracker.XPEventTracker} 中通过别名注册描述，并且通过别名取描述满足事件的值
     */
    protected String alias;

    public abstract boolean isMeetCondition(XPEvent xpEvent);

    /**
     * 子类可以重写，如果重写的话，可以扩展满足与不满足2种条件
     * 获取描述的状态，dsl扩展的描述支持{@link com.wzb.xevent.parser.ConfigParser#ATTR_BREAK_TIME}
     * 和{@link com.wzb.xevent.parser.ConfigParser#ATTR_BREAK_CONDITION}
     * @param xpEvent 描述接受的事件
     * @return {@link XPConstant#FLAG_DESCRIPTION_MEET_CONDITION} {@link XPConstant#FLAG_DESCRIPTION_UNMEET_CONDITION} {@link XPConstant#FLAG_DESCRIPTION_BREAK}
     */
    protected int getStatus(XPEvent xpEvent) {
        return isMeetCondition(xpEvent) ? XPConstant.FLAG_DESCRIPTION_MEET_CONDITION : XPConstant.FLAG_DESCRIPTION_UNMEET_CONDITION;
    }

    @Override
    public void setDebugSwitch(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void log(String tag, String msg){
        if (isDebug){
            Log.e(tag, msg);
        }
    }

    @Override
    public void setITrackerCallback(ITrackerCallback iTrackerCallback) {
        this.iTrackerCallback = iTrackerCallback;
    }

    public XPDescription setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public int receiveEvent(XPEvent xpEvent) {
        int status = getStatus(xpEvent);
        if (status == XPConstant.FLAG_DESCRIPTION_MEET_CONDITION) {
            this.xpEvent = xpEvent;
        }
        return status;
    }

    /**
     * 根据索引，获取其他Description的event
     *
     * @param index tracker中的索引
     * @return 满足指定索引的description的XPEvent
     */
    @Override
    public XPEvent getXpEventById(int index) {
        if (iTrackerCallback == null) return null;
        return iTrackerCallback.getXpEventById(index);
    }

    /**
     * 通过别名，获取其他Description的event
     */
    @Override
    public XPEvent getXpEventByAlias(String alias) {
        if (iTrackerCallback == null) return null;
        return iTrackerCallback.getXpEventByAlias(alias);
    }

    /**
     * 放置参数到Traker里面
     */
    @Override
    public void putAttr(String key, Object value) {
        if (iTrackerCallback == null) return;
        iTrackerCallback.putAttr(key, value);
    }

    /**
     * 放置参数到Traker里面
     */
    @Override
    public void putAttr(HashMap<String, Object> attrs) {
        if (iTrackerCallback == null) return;
        iTrackerCallback.putAttr(attrs);
    }

    @Override
    public Map<String, Object> getAttrsMapByAllAliasDescription(Map<String, Object> attrMap) {
        if (iTrackerCallback == null) return null;
        return iTrackerCallback.getAttrsMapByAllAliasDescription(attrMap);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public XPDescription clone() {
        XPDescription cloneObj = null;
        try {
            cloneObj = (XPDescription) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    @Override
    public SimpleXPTracker getTracker(){
        if (iTrackerCallback == null) return null;
        return iTrackerCallback.getTracker();
    }
}
