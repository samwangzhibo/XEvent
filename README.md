XEvent
---
XEvent 是一个基于事件流的跨平台 配置化打点框架。
XEvent is a cross-platform, structured event report framework based on event flow.

中文文档点 [这](./README-CN.md)!

![image-20190430010746467](https://ws3.sinaimg.cn/large/006tNc79ly1g2k1sbw06ij31ff0u0wji.jpg)



![image-20190430010651968](https://ws4.sinaimg.cn/large/006tNc79ly1g2k1saxcatj31o20kstdk.jpg)

### Advantage

XEvent...

1. **Simplify User Work**，if you want to  multi-platform event report, just need to write a  DSL(Descriptive language) Configuration which maybe js or xml.
2. **Data unification**, multi-platform uses a set of DSL configuration, the rules are the same, and the reporting logic is the same
3.  strong Data **reliability**, business logic and data reporting logic is not associated, modify business will not affect data reporting
4. good **fluency**， data reporting logic is placed in the child thread message queue, does not affect the main thread rendering process
5. **Development speed** fast,  faster configuration, faster publish, which one need 1 day to  develop before and now just need 5 minites (example)
6. **easy debugging**, automated testing, recording user events, replay user events, inspection event report
7. Rules **dynamically publish**, do not rely on the release version publish

### Design document

> Mainly covers 3 aspects:
>
> 1. XEvent Technical selection
>
> 2. Underlying modeling，Event、Tracker、Stream、Description and more Concept
> 3. DSL(language) select

[wiki is here](https://github.com/samwangzhibo/XEvent/wiki/XEvent-Design-Document)

## Who is recommended?

1. The reported data of iOS and the data reported by Android are always  **inconsistent**

   >  XEvent will ensure that the data reporting logic of multiple consumers is the same through configuration rules

2. The data reporting code is complex and involves multiple moments (such as a card with 50%+ area exposure for 1 s),  **coupling data reporting code** in the logic code, and **reported data always fluctuates** when a new version is published

3. The data reporting service is always changeable and needs to be released hot. The traditional publishing solution needs to **follow the version**, and it is fatal for data statistics and business development.

   > XEvent Deliver configuration (js、XML) rules in real time

4. The code for reporting data is sometimes complicated, **affecting the performance of the main thread** (such as calculating the exposure of the card when scroll, needing to listen to each scroll and traversing the calculation), and it is more troublesome to do thread processing in every place.

   > The events Event send are processed in the child thread, and the processed result can be defined whether it is handed to the main thread or processed in the child thread.

5. Maybe you still want some features, such as automated testing, data reporting stability, event playback, user behavior analysis, then let's enjoin it!!!

### Summary

​	XEvent has 2 sets of implementations, corresponding to [xevent](library/xevent), [xeventjs](library/xeventjs) under the project library directory, so 2 access methods are provided below.

1. xevent

   > The data reporting configuration framework based on event flow developed by native Java is characterized by excellent performance. The disadvantage is that there is no iOS implementation.

2. xeventjs

   >  The configuration data reporting framework based on event flow implemented by JavaScript, characterized by cross-platform (h5, Android, iOS can use it),  the disadvantage `JS Bridge` time-consuming

## XEvent(Java)  Usage in 4 steps

1. init engine (sample cpde `app/MyApplication`)

   ```java
   // 1. init engine
   XEvent.getInstance().init();
   
   // 2. set stream to dispatch event
   XEvent.getInstance().setDefaultEventStream(new SimpleEventStream(Utils.getStringFromAsset(EVENT_CONFIG_NAME, this)));
   
   // 3. set your handle callback
   XEvent.getInstance().setIStreamLogCallback(new IStreamLogCallback() {
     @Override
     public void onEventLog(String eventName, Map<String, Object> attrs) {
         // handler you data to report
     }
   });
   ```

2. config data report rule DSL  (sample code  `app/assets/xevent_log_test.xml`)

   >  这段规则是统计 `onRsume状态` 到` onPause状态` 的时间，并上报为keep_time打点

   ```java
   <?xml version="1.0" encoding="utf-8"?>
   <trackers>
       <!--统计页面停留时长-->
       <tracker log_name="keep_time" resend_event="toast">
           <!--进入-->
           <description alias="A" id="onresume"/>
           <!--退出-->
           <description alias="B" id="onpause" put_value="{'toast_str':'页面停留时长， 耗时:' + (event_time - A.event_time)/1000 + '秒'}"/>
       </tracker>
   </trackers>
   ```

3. register rule (sample code  `app/MyApplication`)

   ```java
   //如果是采用第一步的方式  默认已经初始化了  可跳过
   simpleEventStream.registerTrakerByConfig(Utils.getStringFromAsset(EVENT_CONFIG_NAME, this)); 
   ```

4. send event  (sample code  `app/MainActivity`)

   ```java
     @Override
     protected void onResume() {
       super.onResume();
       XEvent.getInstance().sendEvent(new XPEvent(EventConstant.EVENT_ONRESUME)); //onresume
     }
   
     @Override
     protected void onPause() {
       super.onPause();
      XEvent.getInstance().sendEvent(new XPEvent(EventConstant.EVENT_ONPAUSE)); //onpause
     }
   ```



Preview effect

> 可以看到在 onPause时候上报了此次用户停留时长

<img src="./shoot/xevent打点效果.gif" width="50%" height="50%" />



## XEvent(JavaScript) Usage in 2 steps

1. init js engine

   ```java
   XEventJsTool.init(MainActivity.this);
   ```

   `XEventJsTool` 是实现的简单版本Js运行时，它依赖于WebView的JSCore环境

   主要涉及以下操作：

   > 1. JSCore加载运行时js代码文件
   >
   >    ```java
   >    webView.evaluateJavascript(Utils.getStringFromAsset("libxevent.js", mContext), null);
   >    ```
   >
   > 2. js运行时注入Java Bridge对象(用于回调Java方法使用)
   >
   >    ```java
   >    webView.addJavascriptInterface(new JsTool(null, mContext), "xpEventManager");
   >    ```
   >
   > 3. 初始化系统的类型，js代码有部分iOS、Android差异化
   >
   >    ```java
   >    webView.evaluateJavascript("init(0)", null);
   >    ```

2. send framework init event

   ```java
   // 发送初始化事件，告知js解析tracker并生成
   XEventWrapper.sendEvent(new XPEvent(EventConstant.XP_EVENT_XEVENT_FRAMEWORK));
   ```


## Add XEvent to your project
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/samwangzhibo3/xevent/xevent/images/download.svg)](https://bintray.com/samwangzhibo3/xevent/xevent/_latestVersion)

### Via Gradle:
1. config your project build.gradle like XEvent/build.gradle (maybe need)
```gradle 
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/samwangzhibo3/xevent/' }
    }
}
```

2. config your moudle build.gradle like XEvent/app/build.gradle
```gradle
compile 'com.wzb.xevent:xevent:1.0'
```
### Via Maven:
```xml
<dependency>
	<groupId>com.wzb.xevent</groupId>
	<artifactId>xevent</artifactId>
	<version>1.0</version>
	<type>pom</type>
</dependency>
```
Or download  [the latest JAR](https://bintray.com/beta/#/samwangzhibo3/xevent/xevent?tab=overview) from JCenter.


## XEvent can do more？

> XEvent(Java) 只是过渡版本，推荐使用XEvent(js) 版本，因为足够跨端

1. XEvent代码（js）和XEvent配置（js）都支持规则动态下发，不依赖发版

2. 事件流拦截用户事件，用于回放、自动化测试

3. 用户行为模型fit客户端化

   - **why？**

   - - **省钱省机器**，减缓服务端对于用户行为实时建模的算力压力
     - **省时间**，服务器不需要等到晚上跑前一天的全量原始数据fit。

   - **principle**

   - - **DSL and Rule dispatch immediately**

   <img src="https://ws4.sinaimg.cn/large/006tNc79ly1g2k29fii6dj30tg0io3zs.jpg" width="80%" height="80%" />

   









