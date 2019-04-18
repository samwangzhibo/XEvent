package com.wzb.xevent.parser;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.Map;

/**
 * 使用Java Expression Language的解析器
 * Created by samwangzhibo on 2018/8/31.
 */

public class JexlExpressionParser {
    JexlEngine jexl = new JexlBuilder().create();

    public static JexlExpressionParser getInstance() {
        return InnerClass.instance;
    }

    /**
     * parse condition string to expression object
     * @param condition condition string
     * @return expression object
     */
    public JexlExpression parseExpression(String condition) {
        // Create an expression
        //java.lang.StringIndexOutOfBoundsException: length=4096; regionStart=-1; regionLength=58
        //at java.lang.StringFactory.newStringFromChars(StringFactory.java:265)
        JexlExpression jexlExpression;
        try {
            jexlExpression = jexl.createExpression(condition);
        }catch (Exception e){
            jexlExpression = null;
        }
        return jexlExpression;
    }

    public Object getResult(String condition){
        return getResult(parseExpression(condition), null);
    }

    public Object getResult(String condition, Map<String, Object> attrsMap){
        return getResult(parseExpression(condition), attrsMap);
    }

    /**
     * evaluate the expression, getting the result
     * @param expression expression object
     * @param attrsMap attrs map
     * @return object evaluated from the expression
     */
    public Object getResult(JexlExpression expression, Map<String, Object> attrsMap){
        if (expression == null){
            return null;
        }
        // Create a context and add data
        JexlContext jc = new MapContext();
        if (attrsMap != null && attrsMap.size() != 0) {
            for (String key : attrsMap.keySet()) {
                jc.set(key, attrsMap.get(key));
            }
        }
        Object resultObj = null;
        // Now evaluate the expression, getting the result
        try {
            resultObj = expression.evaluate(jc);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultObj;
    }


    public static class InnerClass {
        private InnerClass() {

        }
        static JexlExpressionParser instance = new JexlExpressionParser();
    }
}
