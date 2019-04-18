package com.wzb;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.wzb.wrapper.XEventWrapper;
import com.wzb.xevent.CustomXPEventStream;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.EventConstant;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.stream.IStreamLogCallback;
import com.wzb.xevent.util.LogcatUtil;
import com.wzb.xeventjs.XEventJsTool;

import java.util.Map;

/**
 * simple sample
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init XEvent engine
        XEvent.getInstance().init();
        XEvent.getInstance().setDefaultEventStream(new CustomXPEventStream(MainActivity.this));
        XEvent.getInstance().setIStreamLogCallback(new IStreamLogCallback() {
            @Override
            public void onEventLog(String eventName, Map<String, Object> attrs) {
                if (TextUtils.isEmpty(eventName)) return;
                //回调
                LogcatUtil.e(TAG, "log : eventName = " + eventName);
            }
        });
        LogcatUtil.sLogEnable = false;

        XEventJsTool.init(MainActivity.this);
        // 发送初始化事件，告知js解析tracker并生成
        XEventWrapper.sendEvent(new XPEvent(EventConstant.XP_EVENT_XEVENT_FRAMEWORK));


    }

    @Override
    protected void onResume() {
        super.onResume();
        XEventWrapper.sendEvent(new XPEvent(EventConstant.EVENT_ONRESUME));

    }

    @Override
    protected void onPause() {
        super.onPause();
        XEventWrapper.sendEvent(new XPEvent(EventConstant.EVENT_ONPAUSE));
    }
}
