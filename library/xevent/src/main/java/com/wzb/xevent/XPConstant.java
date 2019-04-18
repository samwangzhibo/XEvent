package com.wzb.xevent;

/**
 * 常量表
 * Created by samwangzhibo on 2018/8/10.
 */

public class XPConstant {
    public static final String DEFAULT_RESEND_XPEVENT_ID = "-1899";
    public static final String SUCCESS_XPEVENT_ID = "-1898";
    public static XPEvent SUCCESS_EVENT = new XPEvent(XPConstant.SUCCESS_XPEVENT_ID);

    public static final int FLAG_DESCRIPTION_MEET_CONDITION = -1900;
    public static final int FLAG_DESCRIPTION_UNMEET_CONDITION = -1901;
    public static final int FLAG_DESCRIPTION_BREAK = -1902;
    /**
     * 父类的条件满足 自己的条件不满足
     */
    public static final int FLAG_DESCRIPTION_SUPER_MEET_CONDITION = -1903;

    public static final int DEFAULT_XPEVENT_ID = -1899;

    /**
     * 动态创建卡片的Tracker事件 分组
     */
    public static final String XP_GROUP = "group";
    /**
     * 动态创建卡片的Tracker事件
     */
    public static final String XP_EVENT_CREATE_TRACKER = "create_traker";
}
