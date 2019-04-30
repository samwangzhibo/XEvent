package com.wzb.xevent.simple;


import android.text.TextUtils;

import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.AttrsConstant;
import com.wzb.xevent.constant.XPConstant;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.xevent.stream.XPEventStream;
import com.wzb.xevent.tracker.SimpleXPTracker;
import com.wzb.xevent.tracker.XPEventTracker;
import com.wzb.xevent.util.LogcatUtil;

/**
 * 简单版的XEventStream
 * 具有根据事件创建Tracker的功能
 * Created by samwangzhibo on 2018/7/19.
 */

public class SimpleEventStream extends XPEventStream {
  public static final String XPTAG = "GlobalXPEventStream";

  public SimpleEventStream(){

  }
  public SimpleEventStream(String logConfig) {
    if (!TextUtils.isEmpty(logConfig)) {
      registerTrakerByConfig(logConfig);
    }
  }

  @Override
  public boolean createTracker(XPEvent xpEvent) {
    if (TextUtils.equals(xpEvent.xpEventId, XPConstant.XP_EVENT_CREATE_TRACKER)) {
      Object obj = xpEvent.getValue(AttrsConstant.OBJ);
      if (obj == null) {
        LogcatUtil.e(TAG, "tracker create fail, cause obj is null");
        return false;
      }
      String group = xpEvent.getString(XPConstant.XP_GROUP);
      if (TextUtils.isEmpty(group)) {
        LogcatUtil.e(TAG, "tracker create fail, cause group is null");
        return false;
      }
      return createTrackerAndRegister(group, obj);
    }
    return false;
  }

  public boolean createTrackerAndRegister(String group, final Object obj) {
    try {
      registerTrackerByGroupTemplate(group, new XPEventStream.IHandleTracker() {
        @Override
        public void handleTracker(SimpleXPTracker simpleXPTracker) {
          handlerDescription(simpleXPTracker, obj);
        }
      });
      return true;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 给Tracker的description加上对象过滤
   * 递归处理，可能有Break Tracker
   */
  private void handlerDescription(XPEventTracker xpTracker, final Object obj) {
    //处理Tracker
    for (XPDescription xpDescription : xpTracker.getDescription()) {
      if (xpDescription instanceof SimpleCardJEXLConditionDescription) {
        SimpleCardJEXLConditionDescription simpleCardJEXLConditionDescription = (SimpleCardJEXLConditionDescription) xpDescription;
        simpleCardJEXLConditionDescription.setObj(obj);
      }
    }
    XPEventTracker breakTrakcer = xpTracker.getBreakEventTracker();
    if (breakTrakcer != null) {
      handlerDescription(breakTrakcer, obj);
    }
  }
}
