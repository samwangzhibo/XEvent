XEvent
---
XEvent 是一个基于事件流的跨平台 配置化打点框架。
XEvent is a cross-platform, structured management framework based on event flow.

English doc click [here](./README-EN.md)!

![image-20190430010746467](https://ws3.sinaimg.cn/large/006tNc79ly1g2k1sbw06ij31ff0u0wji.jpg)



![image-20190430010651968](https://ws4.sinaimg.cn/large/006tNc79ly1g2k1saxcatj31o20kstdk.jpg)

### 优点

XEvent...

1. **节约人力**，只需要写一套DSL(js、xml)配置，搞定多平台打点，可读性强
2. **数据统一**，多平台使用一套DSL配置，规则相同，打点上报逻辑相同
3. 数据**可靠性**强，业务逻辑和打点逻辑解耦，修改业务不会影响打点
4. **流畅度**好，打点逻辑放在子线程消息队列，不影响主线程渲染流程
5. **开发速度**快，快速配置，快速发布，1天开发量减少到5分钟(举例)
6. **好调试，** 自动化测试，记录用户事件，回放事件，检验打点
7. 规则**动态下发**，不依赖发版

### 设计文档

> 主要涵盖3方面：
>
> 1. XEvent的技术选型
>
> 2. 底层建模，Event、Tracker、Stream、Description等概念
> 3. DSL的选型

[wiki在这](https://github.com/samwangzhibo/XEvent/wiki/XEvent-Design-Document)

## 建议谁使用？

1. iOS的打点和Android打点总是**对不齐**

   >  XEvent 会通过配置化规则保证多个使用端的打点逻辑统一

2. 埋点代码复杂，涉及多个时刻(比如一张卡片50%以上区域曝光1s)， 逻辑代码中**耦合**埋点代码，发新版本的时候**埋点数据总是波动**

3. 打点业务总是多变的，需要热发布，传统的发布方案需要**跟版上线**，一个版本对于埋点统计和业务发展是致命的。

   > XEvent 实时下发配置规则(js、XML)

4. 打点代码有时很复杂，会影响主线程性能(比如滑动的时候去计算卡片的曝光，需要监听每一次滑动并且遍历计算)，每个地方都做线程处理又比较麻烦

   > XEvent 发送的事件都在子线程处理，处理完的结果可以定义是交给主线程还是在子线程处理

5. 可能你还想要一些功能，比如自动化测试埋点稳定性、事件回放、用户行为分析，那赶紧使用吧!!!

### 概要

​	XEvent有2套实现，对应于项目library目录下面的[xevent](library/xevent)、[xeventjs](library/xeventjs), 所以下面提供2种接入方式。

1. xevent

   > 原生Java开发的事件流配置化打点框架，特点是性能优，缺点是没有对应的iOS实现

2. xeventjs

   > JavaScript实现的事件流配置化打点框架，特点是跨端，前端、Android|、iOS均可使用，缺点`JS Bridge` 耗时 

## XEvent(Java) 使用四步走

---

1. 初始化 (样例代码 app/MyApplication)

   ```java
   // 1.初始化引擎
   XEvent.getInstance().init();
   
   // 2.设置事件流去分发事件
   XEvent.getInstance().setDefaultEventStream(new SimpleEventStream(Utils.getStringFromAsset(EVENT_CONFIG_NAME, this)));
   
   // 3.设置处理框架抛回数据的回调
   XEvent.getInstance().setIStreamLogCallback(new IStreamLogCallback() {
     @Override
     public void onEventLog(String eventName, Map<String, Object> attrs) {
         // 处理你的埋点数据上报
     }
   });
   ```

2. 配置打点规则DSL  (样例代码 app/assets/xevent_log_test.xml)

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

3. 注册规则 (样例代码  app/MyApplication)

   ```java
   //如果是采用第一步的方式  默认已经初始化了  可跳过
   simpleEventStream.registerTrakerByConfig(Utils.getStringFromAsset(EVENT_CONFIG_NAME, this)); 
   ```

4. 发送事件  (样例代码 app/MainActivity)

   ```java
     @Override
     protected void onResume() {
       super.onResume();
       XEvent.getInstance().sendEvent(new XPEvent(EventConstant.EVENT_ONRESUME));
     }
   
     @Override
     protected void onPause() {
       super.onPause();
      XEvent.getInstance().sendEvent(new XPEvent(EventConstant.EVENT_ONPAUSE));
     }
   ```



预览

> 可以看到在 onPause时候上报了此次用户停留时长

<img src="./shoot/xevent打点效果.gif" width="50%" height="50%" />



## XEvent(JavaScript) 使用二步走

---

1. 初始化Js引擎

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

2. 发送框架初始化事件

   ```java
   // 发送初始化事件，告知js解析tracker并生成
   XEventWrapper.sendEvent(new XPEvent(EventConstant.XP_EVENT_XEVENT_FRAMEWORK));
   ```



## XEvent 还能干吗？

> XEvent(Java) 只是过渡版本，推荐使用XEvent(js) 版本，因为足够跨端

1. XEvent代码（js）和XEvent配置（js）都支持规则动态下发，不依赖发版

2. 事件流拦截用户事件，用于回放、自动化测试

3. 用户行为模型fit客户端化

   - **为什么？**

   - - **省钱省机器**，减缓服务端对于用户行为实时建模的算力压力
     - **省时间**，服务器不需要等到晚上跑前一天的全量原始数据fit。

   - **原理**

   - - **DSL 与规则实时下发**

   <img src="https://ws4.sinaimg.cn/large/006tNc79ly1g2k29fii6dj30tg0io3zs.jpg" width="80%" height="80%" />

   









