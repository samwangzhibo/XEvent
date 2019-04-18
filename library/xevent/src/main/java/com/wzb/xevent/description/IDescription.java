package com.wzb.xevent.description;


import com.wzb.xevent.XPEvent;
import com.wzb.xevent.tracker.ITrackerCallback;

/**
 *  描述是指在连续多个满足指定条件的时刻组成的流程中的单一时刻
 * Created by samwangzhibo on 2018/8/14.
 */

public interface IDescription extends ITrackerCallback {
    /**
     * description接受Tracker传入的事件的主入口
     * @param xpEvent Tracker传入的事件
     * @return {@link com.wzb.xevent.XPConstant} 满足条件  不满足条件  满足终止条件
     *
     */
    int receiveEvent(XPEvent xpEvent);

    /**
     * 判断事件是否满足描述
     * @param xpEvent 描述接受的事件
     * @return 是否满足描述
     */
    boolean isMeetCondition(XPEvent xpEvent);

    /**
     * Tracker的能力设置给{@link XPDescription}用
     * @param iTrackerCallback
     */
    void setITrackerCallback(ITrackerCallback iTrackerCallback);
}
