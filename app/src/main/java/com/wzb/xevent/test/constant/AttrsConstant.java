package com.wzb.xevent.test.constant;

/**
 * event attr key
 * Event属性表
 * Created by samwangzhibo on 2018/8/10.
 */

public class AttrsConstant {
    /**
     * 事件发送的时间
     */
    public static final String EVENT_TIME = "event_time";
    /**
     * 事件的id
     */
    public static final String EVENT_ID = "event_id";
    /**
     * toast 字段
     */
    public static final String TOAST_STR = "toast_str";
    /**
     * Tracker根据carddata来判断是不是绑定的卡片
     */
    public static final String CARD_DATA = "card_data";
    /**
     * Tracker根据card_hashcode来判断是不是绑定的卡片
     */
    public static final String HASHCODE = "hashcode";
    /**
     * 卡片的高度
     */
    public static final String CARD_Height = "card_height";
    /**
     * 卡片顶部距离屏幕顶部的距离
     */
    public static final String CARD_TOP_TOP = "card_top_top";
    /**
     * 卡片底部距离屏幕顶部的距离
     */
    public static final String CARD_BOTTOM_2_TOP = "card_bottom_top";
    /**
     * 卡片顶部距离屏幕底部的距离
     */
    public static final String CARD_TOP_BOTTOM = "card_top_bottom";
    /**
     * 卡片触发曝光的最小高度
     */
    public static final String CARD_MIN_SW_HEIGHT = "card_min_sw_height";
    /**
     * 子卡片左边到屏幕左边
     */
    public static final String SUBCARD_LEFT_LEFT = "subcard_left_left";
    /**
     * 子卡片左边到屏幕右边
     */
    public static final String SUBCARD_LEFT_RIGHT = "subcard_left_right";
    /**
     * 子卡片右边到屏幕左边
     */
    public static final String SUBCARD_RIGHT_LEFT = "subcard_right_left";
    /**
     * 卡片模板
     */
    public static final String CARD_TEMPLATE = "template";
    /**
     * 上报用的属性
     */
    public static final String TIME = "time";
    /**
     * 卡片id
     */
    public static final String CARD_ID = "card_id";
    /**
     * 子卡片id
     */
    public static final String SUB_CARD_ID = "subcard_id";
    /**
     * 横划卡片滑动状态
     * SCROLL_STATE_IDLE(0)  SCROLL_STATE_DRAGGING(1) SCROLL_STATE_SETTLING(2)
     */
    public static final String HORIZONTAL_CARD_SCROLL_STATUS = "scroll_status";
    /**
     * 横划卡片x位移
     */
    public static final String HORIZONTAL_CARD_SCROLL_DX = "dx";
    /**
     * xpanel滑动距离
     */
    public static final String SCROLL_DISTANCE = "scroll_distance";
    /**
     * 传入Tracker 卡片Tracker通过obj来过滤卡片对象
     */
    public static final String OBJ = "obj";
    /**
     * 动态创建卡片的Tracker事件 分组
     */
    public static final String GROUP = "group";
    /**
     * 位置
     */
    public static final String POSITION = "position";
    /**
     * 子卡片位置
     */
    public static final String SUBCARD_POSITION = "subcard_position";
}
