package com.wzb.xevent.constant;

/**
 * XPEvent事件表
 * Created by samwangzhibo on 2018/7/19.
 */

public class EventConstant {
    /**
     *  初始化容器
     * attrs
     */
    public static final String EVENT_INIT = "init";
    /**
     *  onresume
     * attrs
     */
    public static final String EVENT_ONRESUME = "onresume";
    /**
     *  onresume
     * attrs
     */
    public static final String EVENT_TOAST = "toast";
    /**
     *  onpause
     * attrs
     */
    public static final String EVENT_ONPAUSE = "onpause";
    /**
     * 刷新recyclerView前移除旧卡片
     */
    public static final String EVENT_REFRESH = "refresh";
    /**
     * 展示给用户
     * attrs hasHalfCard
     */
    public static final String EVENT_PAGE_SW = "sw";
    /**
     * xpanel拉起
     */
    public static final String EVENT_PAGE_PULL_UP = "pull_up";
    /**
     * xpanel纵向滑动
     * attrs scroll_distance
     */
    public static final String EVENT_PAGE_SCROLL = "scroll";
    /**
     * 纵向卡片移出 通用
     */
    public static final String EVENT_PAGE_CARD_MOVE_OUT = "card_move_out";
    /**
     * 卡片进入 用于计算卡片曝光
     */
    public static final String EVENT_PAGE_CARD_MOVE_IN = "card_move_in";
    /**
     * 卡片移动 用于计算卡片曝光
     * attrs xp_card_top_top xp_card_top_bottom xp_card_min_sw_height xp_card_height
     */
    public static final String EVENT_PAGE_CARD_MOVE = "card_move";
    /**
     * xpanel卡片曝光
     */
    public static final String EVENT_PAGE_CARD_SW = "card_sw";
    /**
     * xpanel卡片(50%曝光规则)
     */
    public static final String EVENT_PAGE_HALF_CARD_SW = "card_half_sw";
    /**
     * xpanel卡片移出(50%曝光规则)
     */
    public static final String EVENT_PAGE_HALF_CARD_MOVE_OUT= "card_half_move_out";
    /**
     * xpanel卡片曝光时长
     */
    public static final String EVENT_PAGE_CARD_SW_TIME = "card_sw_time";
    /**
     * xpanel 半漏有效曝光
     */
    public static final String EVENT_PAGE_HALF_SW = "half_sw";
    /**
     * XPANEL拉起触发蒙层
     */
    public static final String EVENT_PAGE_KEEP = "keep";
    /**
     * XPANEL释放蒙层
     */
    public static final String EVENT_PAGE_RELEASE = "release";
    /**
     * 进入蒙层时长 进入蒙层状态计时开始，退出蒙层状态计时结束
     */
    public static final String EVENT_PAGE_SW_TIME = "sw_time";
    /**
     * xpanel卡片点击
     */
    public static final String EVENT_PAGE_CARD_CLICK = "card_ck";
    /**
     * xpanel卡片全部曝光
     */
    public static final String EVENT_PAGE_CARD_ALL_SW = "card_all_sw";
    /**
     * xpanel按钮点击
     */
    public static final String EVENT_PAGE_BUTTON_CLICK = "btn_ck";
    /**
     * xpanel子卡片展示
     */
    public static final String EVENT_PAGE_SUBCARD_SW = "subcard_sw";
    /**
     * xpanel子卡片点击
     */
    public static final String EVENT_PAGE_SUBCARD_CLICK = "subcard_ck";
    /**
     * 子卡片移动 用于计算卡片曝光
     * attrs
     */
    public static final String EVENT_PAGE_SUB_CARD_MOVE = "subcard_move";
    /**
     * 子卡片移动出屏幕
     * attrs
     */
    public static final String EVENT_PAGE_SUB_CARD_MOVE_OUT = "subcard_move_out";
    /**
     * xpanel子卡片曝光时长
     */
    public static final String EVENT_PAGE_SUBCARD_SW_TIME = "subcard_sw_time";
    /**
     * 横划卡片滑动
     * attrs dx
     */
    public static final String EVENT_PAGE_HORIZONTAL_MOVE = "horizontal_move";
    /**
     * 横划卡片移动状态变化
     * attrs subcard_scroll_status
     */
    public static final String EVENT_PAGE_HORIZONTAL_CARD_SCROLL_STATUS_CHANGE = "horizontal_card_scroll_status_change";
    /**
     * xpanel卡片100%露出 移出
     */
    public static final String EVENT_PAGE_CARD_ALL_SW_MOVE_OUT = "card_all_sw_move_out";
    /**
     * xpanel卡片100%露出 移入
     */
    public static final String EVENT_PAGE_CARD_ALL_SW_MOVE_IN = "card_all_sw_move_in";
    /**
     * xpanel卡片默认曝光策略 移出
     */
    public static final String EVENT_PAGE_CARD_SW_MOVE_OUT = "card_sw_move_out";
    /**
     * 卡片曝光时间
     * 广告有大于等于50％的像素面积在可视空间
     * 内，且停留大于等于连续1秒
     */
    public static final String EVENT_EFF_SW = "card_eff_sw";
    /**
     * 卡片横划(上报)
     */
    public static final String EVENT_CARD_SCROLL = "card_horizontal_scroll";
    /**
     * 动态创建卡片的Tracker事件
     */
    public static final String EVENT_CREATE_TRACKER = "create_traker";
    /**
     * 网络成功返回
     */
    public static final String EVENT_NETWORK_BACK = "network_back";
    /**
     * 子卡片创建
     */
    public static final String EVENT_CREATE_SUBCARD = "create_subcard";
}
