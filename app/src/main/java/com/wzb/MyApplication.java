package com.wzb;

import java.util.Map;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.wzb.util.Utils;
import com.wzb.wrapper.XEventWrapper;
import com.wzb.xevent.CustomXPEventStream;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.AttrsConstant;
import com.wzb.xevent.constant.EventConstant;
import com.wzb.xevent.description.SimpleXPDescription;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.simple.SimpleEventStream;
import com.wzb.xevent.stream.IStreamLogCallback;
import com.wzb.xevent.tracker.SimpleXPTracker;
import com.wzb.xevent.util.LogcatUtil;
import com.wzb.xeventjs.XEventJsTool;

import static com.wzb.xevent.CustomXPEventStream.EVENT_CONFIG_NAME;

/**
 * Created by samwangzhibo on 2019-04-30.
 */
public class MyApplication extends Application {
  private static final String TAG = "MyApplication";

  @Override
  public void onCreate() {
    super.onCreate();
    simpleInit();
//    sampleInit(); // 具有定制化需求
    initJsFramework();
    LogcatUtil.sLogEnable = false;
  }

  private void initJsFramework() {
    // XEvent(JavaScript)
    XEventJsTool.init(this);
    // 发送初始化事件，告知js解析tracker并生成
    XEventWrapper.sendEvent(new XPEvent(EventConstant.XP_EVENT_XEVENT_FRAMEWORK));
  }

  private void sampleInit() {
    // init XEvent engine
    XEvent.getInstance().init();
    // init your Stream to diapatch Event
    XEvent.getInstance().setDefaultEventStream(new CustomXPEventStream(this));
    // set callback to upload log
    XEvent.getInstance().setIStreamLogCallback(new IStreamLogCallback() {
      @Override
      public void onEventLog(String eventName, Map<String, Object> attrs) {
        if (TextUtils.isEmpty(eventName)) return;
        //回调
        LogcatUtil.e(TAG, "log : eventName = " + eventName);
      }
    });
  }

  private void simpleInit() {
    // 1.init XEvent engine
    XEvent.getInstance().init();
    // 2.init your Stream
    SimpleEventStream simpleEventStream = new SimpleEventStream();
      // 2.1 register log xml config
    simpleEventStream.registerTrakerByConfig(Utils.getStringFromAsset(EVENT_CONFIG_NAME, this));
      // 2.2 set log callbak observer
    simpleEventStream.register(new SimpleXPTracker(new XPDescription[]{
        new SimpleXPDescription(EventConstant.EVENT_TOAST)
    }).setILogCallback(new ILogCallback() {
      @Override
      public void onLog(XPEvent xpEvent) {
        Toast.makeText(MyApplication.this, xpEvent.getString(AttrsConstant.TOAST_STR), Toast.LENGTH_SHORT).show();
      }
    }));
    // 3 set stream to diapatch Event
    XEvent.getInstance().setDefaultEventStream(simpleEventStream);
    // set callback to upload log
    XEvent.getInstance().setIStreamLogCallback(new IStreamLogCallback() {
      @Override
      public void onEventLog(String eventName, Map<String, Object> attrs) {
        if (TextUtils.isEmpty(eventName)) return;
        //回调
        LogcatUtil.e(TAG, "log : eventName = " + eventName);
      }
    });
  }


}
