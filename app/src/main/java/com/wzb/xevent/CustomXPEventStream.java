package com.wzb.xevent;

import android.content.Context;
import android.widget.Toast;

import com.wzb.xevent.constant.AttrsConstant;
import com.wzb.xevent.constant.EventConstant;
import com.wzb.xevent.description.SimpleJEXLConditionDescription;
import com.wzb.xevent.description.SimpleXPDescription;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.logcallback.ILogCallback;
import com.wzb.xevent.parser.ICreateParseDescription;
import com.wzb.xevent.sw.SwTimeTracker;

import com.wzb.util.Utils;
import com.wzb.xevent.simple.SimpleCardJEXLConditionDescription;
import com.wzb.xevent.simple.SimpleEventStream;
import com.wzb.xevent.tracker.SimpleXPTracker;

/**
 * 国内版事件流 用于分发事件
 * Created by samwangzhibo on 2018/7/19.
 */

public class CustomXPEventStream extends SimpleEventStream {
    public static final String XPTAG = "DomesticXPEventStream";
    public static final String EVENT_CONFIG_NAME = "xevent_log_test.xml";
    public static boolean useConfig = true;

    public CustomXPEventStream(final Context mContext) {
        if (useConfig) {
            /**
             * 第一种方式 通过字符串初始化Tracker
             */
            registerTrakerByConfig(Utils.getStringFromAsset(EVENT_CONFIG_NAME, mContext));
        } else {
            /**
             *  第二种方式 通过代码动态配置
             */
            //xpanel蒙层持续时间
            register(new SwTimeTracker());
        }

        /**
         *  注册toast事件观察者
         */
        register(new SimpleXPTracker(new XPDescription[]{
                new SimpleXPDescription(EventConstant.EVENT_TOAST)
        }).setILogCallback(new ILogCallback() {
            @Override
            public void onLog(XPEvent xpEvent) {
                Toast.makeText(mContext, xpEvent.getString(AttrsConstant.TOAST_STR), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected ICreateParseDescription getICreateParseDescription() {
        return new ICreateParseDescription() {
            @Override
            public SimpleJEXLConditionDescription createJEXLConditionDescription() {
                return new SimpleCardJEXLConditionDescription();
            }
        };
    }

}
