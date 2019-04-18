package com.wzb.xevent.logcallback;

import com.wzb.xevent.XPEvent;
import java.io.Serializable;

/**
 * 设置给Tracker完成打点时触发的回调
 * Created by samwangzhibo on 2018/8/10.
 */

public interface ILogCallback extends Serializable {
   void onLog(XPEvent xpEvent);
}
