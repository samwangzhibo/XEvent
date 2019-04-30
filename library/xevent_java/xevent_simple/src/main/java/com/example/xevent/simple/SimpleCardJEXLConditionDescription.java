package com.example.xevent.simple;

import com.wzb.xevent.XPConstant;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.description.SimpleJEXLConditionDescription;



/**
 * 具有卡片筛选的description
 * Created by samwangzhibo on 2018/9/14.
 */

public class SimpleCardJEXLConditionDescription extends SimpleJEXLConditionDescription {
    private static final String TAG = "SimpleCardJEXLCondition";
    private Object cardData;

    public void setObj(Object cardData) {
        this.cardData = cardData;
    }

    boolean isMeetObj(XPEvent xpEvent) { //命中条件
        if (cardData == null) {
            //没有传cardData 默认不做逻辑处理 降级为SimpleJEXLConditionDescription
            return true;
        }
        int hashcodeObj;

        if ((hashcodeObj = xpEvent.getInt(com.example.xevent.simple.constant.AttrsConstant.HASHCODE)) == -1) {
            return false;
        }
        if (cardData.hashCode() == hashcodeObj) {
            return true;
        }
        return false;
    }

    @Override
    protected int getStatus(XPEvent xpEvent) {
        if (!isMeetObj(xpEvent)) {
            // 不是自己卡片的数据都不处理
            return XPConstant.FLAG_DESCRIPTION_UNMEET_CONDITION;
        }
        //meet break parent_meet
        int superStatus = super.getStatus(xpEvent);
        //透传父description状态
        return superStatus;
    }
}
