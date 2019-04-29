package com.wzb.xeventjs;

import android.content.Context;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wzb.util.LogcatUtil;
import com.wzb.util.Utils;
import com.wzb.xevent.XPEvent;

import java.lang.reflect.Field;

/**
 * XEvent与js模块实现的交互类
 * Created by samwangzhibo on 2019/2/19.
 */

public class XEventJsTool implements IXEventJsToolImpl{
    private static final String TAG = "XEventJsTool";
    private WebView webView;
    /**
     * 适配器
     */
    private JsXEventAdapter jsXEventAdapter = new JsXEventAdapter();
    private static volatile XEventJsTool instance;

    private XEventJsTool(Context mContext) {
        webView = new WebView(mContext);
        initWebView(mContext);
    }

    public static void init(Context mContext){
        if (instance == null){
            synchronized (XEventJsTool.class){
                if (instance == null){
                    instance = new XEventJsTool(mContext);
                }
            }
        }
    }

    public static XEventJsTool getInstance() {
       return instance;
    }

    private void initWebView(Context mContext) {
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new JsTool(null, mContext), "xpEventManager");//AndroidtoJS类对象映射到js的test对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(Utils.getStringFromAsset("libxevent.js", mContext), null);

//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPTracker.js", mContext), null);
//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPStashTracker.js", mContext), null);
//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPEvent.js", mContext), null);
//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPStream.js", mContext), null);
//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPRegisterTrackers.js", mContext), null);
//        webView.evaluateJavascript(Utils.getStringFromAsset("xevent/XPEventManager.js", mContext), null);

            //初始化osType
            webView.evaluateJavascript("init(0)", null);
        }
    }

    public void sendEvent(final XPEvent xpEvent){
        realSendEvent(xpEvent);
    }

    public void realSendEvent(XPEvent xpEvent) {
        XPEvent jsEvent = jsXEventAdapter.getEvent(xpEvent);
        String eventJsonStr = jsXEventAdapter.getEventStr(jsEvent);

        LogcatUtil.e(TAG, "xpTest.testStr('" + eventJsonStr + "')");

        //大概10-100ms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:sendEvent(" + eventJsonStr + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
    //                Log.e(TAG, "onReceiveValue value= " + value);
    //                Log.e(TAG, "cost time " + (System.currentTimeMillis() - nowTime));
                }
            });
        }
    }

    public void destroy() {
        if (webView != null) {
//            webView.destroy();
//            System.exit(0);
        }
    }

    private void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
//                if (BuildConfig.DEBUG) {
//                    e.printStackTrace();
//                }
            } catch (IllegalAccessException e) {
//                if (BuildConfig.DEBUG) {
//                    e.printStackTrace();
//                }
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
//                if (BuildConfig.DEBUG) {
//                    e.printStackTrace();
//                }
            } catch (ClassNotFoundException e) {
//                if (BuildConfig.DEBUG) {
//                    e.printStackTrace();
//                }
            } catch (IllegalAccessException e) {
//                if (Buildconfig.DEBUG) {
//                    e.printStackTrace();
//                }
            }
        }
    }

}
