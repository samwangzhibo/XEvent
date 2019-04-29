XEvent
---
XEvent 是一个基于事件流的跨平台 配置化打点框架。
XEvent is a cross-platform, structured management framework based on event flow.

中文文档[点这](./README.md)!

![image-20190430010746467](https://ws3.sinaimg.cn/large/006tNc79ly1g2k1sbw06ij31ff0u0wji.jpg)



![image-20190430010651968](https://ws4.sinaimg.cn/large/006tNc79ly1g2k1saxcatj31o20kstdk.jpg)

XEvent...

1. **节约人力**，只需要写一套DSL(js、xml)配置，搞定多平台打点，可读性强
2. **数据统一**，多平台使用一套DSL配置，规则相同，打点上报逻辑相同
3. 数据**可靠性**强，业务逻辑和打点逻辑解耦，修改业务不会影响打点
4. **流畅度**好，打点逻辑放在子线程消息队列，不影响主线程渲染流程
5. **开发速度**快，快速配置，快速发布，1天开发量减少到5分钟(举例)
6. **好调试，** 自动化测试，记录用户事件，回放事件，检验打点
7. 规则**动态下发**，不依赖发版