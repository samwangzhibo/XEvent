package com.wzb.xevent;

import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.logcallback.NullLogCallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wzb.xevent.XPConstant.DEFAULT_RESEND_XPEVENT_ID;

/**
 * 用于描述{@link com.wzb.xevent.tracker.XPEventTracker}
 * Created by samwangzhibo on 2018/8/10.
 */
//@Retention注解指定注解可以保留多久
@Retention(RetentionPolicy.RUNTIME)
//@Target注解指定注解能修饰的目标
@Target(ElementType.TYPE)
public @interface BindXPEventId {
    /**
     * Tracker处理完了，如果{@link #xpEventId})有值就直接发出{@link #xpEventId})事件
     */
    String xpEventId() default DEFAULT_RESEND_XPEVENT_ID;

    /**
     *  Tracker触发打点时，如果{@link #callbacks})有值就执行回调
     * @return
     */
    Class<? extends ILogCallback>[] callbacks() default NullLogCallback.class;
}
