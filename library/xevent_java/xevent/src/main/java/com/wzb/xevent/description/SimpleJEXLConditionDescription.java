package com.wzb.xevent.description;

import android.text.TextUtils;

import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.XPConstant;
import com.wzb.xevent.parser.JexlExpressionParser;
import com.wzb.xevent.util.FormatUtil;
import com.wzb.xevent.util.LogcatUtil;

import org.apache.commons.jexl3.JexlExpression;

import java.util.HashMap;

/**
 * 简单的支持短语的条件description 解析xml的{@link com.wzb.xevent.parser.ConfigParser#ATTR_CONDITION}，
 * {@link com.wzb.xevent.parser.ConfigParser#ATTR_BREAK_CONDITION,
 * {@link com.wzb.xevent.parser.ConfigParser#ATTR_PUT_VALUE}}后生成的descripiton
 * 能够自动判断短语的条件 并且放置缓存参数到Tracker
 * 支持跨description取值
 * <p>
 *     不传condition 退化为simpleTimeDescription
 * </p>
 * Created by samwangzhibo on 2018/8/15.
 */

public class SimpleJEXLConditionDescription extends SimpleTimeDescription implements Cloneable{
    private static final String TAG = "SimpleJEXLConditionDesc";
    /**
     * 满足的条件表达式
     */
    protected JexlExpression expression;
    /**
     * <K, V> V 需要缓存的参数的表达式
     */
    private HashMap<String, JexlExpression> tempAttrsExpression = new HashMap<>();
    //终止条件
    protected JexlExpression breakExpression;

    private boolean getExpressionResult(JexlExpression expression, XPEvent xpEvent) {
        boolean expressionResult = false;
        try {
            Object result = JexlExpressionParser.getInstance().
                    getResult(expression, getAttrsMapByAllAliasDescription(xpEvent.allAttrsMap));
            if (result != null && result instanceof Boolean) {
                expressionResult = (boolean) result;
            }
        } catch (Exception e) {
            log(TAG, "JEXL表达式解析失败");
//            e.printStackTrace();
        }
        return expressionResult;
    }

    /**
     * 满足条件之后，把参数存储到Tracker，以便Tracker上报
     * @param xpEvent
     */
    public void putTempAttrs2Tracker(XPEvent xpEvent) {
        if (tempAttrsExpression.size() != 0) {
            HashMap<String, Object> attrs = new HashMap<>();
            for (String key : tempAttrsExpression.keySet()) {
                Object value = JexlExpressionParser.getInstance().getResult(tempAttrsExpression.get(key),
                        getAttrsMapByAllAliasDescription(xpEvent.allAttrsMap));
                log(TAG, "put key=" + key + ", value=" + FormatUtil.getLogStr(value));
                if (value != null) {
                    attrs.put(key, value);
                }
            }
            putAttr(attrs); //把暂存的数据放置到Tracker
        }
    }

    /**
     * 设置参数表达式
     * @param tempAttrsExpression <K, V的表达式>
     */
    public void setTempAttrsExpression(HashMap<String, JexlExpression> tempAttrsExpression) {
        this.tempAttrsExpression = tempAttrsExpression;
    }

    public SimpleJEXLConditionDescription setExpression(JexlExpression expression) {
        this.expression = expression;
        return this;
    }

    public void setBreakExpression(JexlExpression breakExpression) {
        this.breakExpression = breakExpression;
    }

    @Override
    protected int getStatus(XPEvent xpEvent) {
        int status = super.getStatus(xpEvent);
        switch (status) {
            case XPConstant.FLAG_DESCRIPTION_BREAK:
                //父description已经break了
                return XPConstant.FLAG_DESCRIPTION_BREAK;
            case XPConstant.FLAG_DESCRIPTION_MEET_CONDITION:
                if (expression != null) {
                    boolean expressionResult = getExpressionResult(expression, xpEvent);
                    if (expressionResult) {
                        //命中条件
                        putTempAttrs2Tracker(xpEvent);
                        log(TAG, (getTracker() != null ? getTracker().eventName : "") + "   JEXL description meet,  "
                                + (TextUtils.isEmpty(xPEventId) ? "" :  "id = " + xPEventId + ",  ") + (expression != null ? expression.getSourceText() : "")
                                + (xpEvent != null ? ("  , " + xpEvent.toString()) : ""));
                        return XPConstant.FLAG_DESCRIPTION_MEET_CONDITION;
                    } else { //没命中条件 看下是否命中终止条件
                        if (TextUtils.equals(xPEventId, "subcard_move") || TextUtils.equals(xPEventId, "subcard_move_out")){
                            log(TAG, (getTracker() != null ? getTracker().eventName : "") + "   JEXL description unmeet,  "
                                    + (TextUtils.isEmpty(xPEventId) ? "" :  "id = " + xPEventId + ",  ") + (expression != null ? expression.getSourceText() : "")
                                    + (xpEvent != null ? ("  , " + xpEvent.toString()) : ""));
                        }
                        status = getBreakStatus(XPConstant.FLAG_DESCRIPTION_UNMEET_CONDITION, xpEvent);
                    }
                } else {
                    //没有条件，退化父description
                    LogcatUtil.e("test2", "JEXL description meet,   "
                            + (TextUtils.isEmpty(xPEventId) ? "" :  "id = " + xPEventId  + ",  ") + (expression != null ? expression.getSourceText() : ""));
                    putTempAttrs2Tracker(xpEvent);
                    return XPConstant.FLAG_DESCRIPTION_MEET_CONDITION;
                }
                break;
            case XPConstant.FLAG_DESCRIPTION_SUPER_MEET_CONDITION:
                //父description不满足 看看满足自己的break条件吗
                status = getBreakStatus(status, xpEvent);
                break;
        }
        return status;
    }

    public int getBreakStatus(int status, XPEvent xpEvent) {
        if (breakExpression != null) {
            //判断break条件是否满足
            if (getExpressionResult(breakExpression, xpEvent)) {
                LogcatUtil.e("test2", "description break : " + xpEvent.xpEventId + ", " + breakExpression.getSourceText());
                status = XPConstant.FLAG_DESCRIPTION_BREAK;
            }
        }
        return status;
    }
}
