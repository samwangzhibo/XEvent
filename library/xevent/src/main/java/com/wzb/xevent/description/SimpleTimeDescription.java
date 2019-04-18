package com.wzb.xevent.description;

import android.text.TextUtils;
import com.wzb.xevent.XPConstant;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.parser.JexlExpressionParser;
import com.wzb.xevent.util.LogcatUtil;

/**
 * 具有事件间隔筛选的description
 */
public class SimpleTimeDescription extends SimpleXPDescription {
    private static final String TAG = "SimpleTimeDescription";
    /**
     * 时间筛选表达式
     */
    private String timeExpression;
    /**
     * break_time的表达式
     */
    private String breakTimeExpression;

    public SimpleTimeDescription setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
        return this;
    }

    public void setBreakTimeExpression(String breakTimeExpression) {
        this.breakTimeExpression = breakTimeExpression;
    }

    private boolean isMeetTimeExpression(XPEvent xpEvent, String timeExpression) {
        boolean timeResult = false;
        if (index == 0) {
            timeResult = true;
        } else {
            long duration = xpEvent.getTime() - getXpEventById(index - 1).getTime();
            String fullTimeExpression = duration + timeExpression; //拼接时间参数
//            LogcatUtil.e(TAG, "fullTimeExpression : " + fullTimeExpression + ", " + xpEvent);
//                    XPComparisonExpression expression = ExpressionFactory.createExpression(fullTimeExpression);
//                    expression.setDescriptionAndEvent(this, xpEvent);
            try {
                timeResult = (boolean) JexlExpressionParser.getInstance().getResult(fullTimeExpression);
            } catch (ClassCastException e) {

            }
        }
        return timeResult;
    }

    @Override
    protected int getStatus(XPEvent xpEvent) {
        if (super.isMeetCondition(xpEvent)) {
            if (TextUtils.isEmpty(timeExpression)) { //退化为条件description
                return XPConstant.FLAG_DESCRIPTION_MEET_CONDITION;
            } else {
                boolean timeResult = isMeetTimeExpression(xpEvent, timeExpression);
                if (timeResult){
                    return XPConstant.FLAG_DESCRIPTION_MEET_CONDITION;
                }
            }
            if (!TextUtils.isEmpty(breakTimeExpression)){
                //判断break_time是否满足
                if (isMeetTimeExpression(xpEvent, breakTimeExpression)) {
                    LogcatUtil.e("test2", "description break time: " + xpEvent.xpEventId + ", " + breakTimeExpression);
                    return XPConstant.FLAG_DESCRIPTION_BREAK;
                }
            }
            //自己没命中 但是父description命中了
            return XPConstant.FLAG_DESCRIPTION_SUPER_MEET_CONDITION;
        }
        return XPConstant.FLAG_DESCRIPTION_UNMEET_CONDITION;
    }

}
