package com.wzb.xevent.test.recyclerview;

import android.text.TextUtils;


import com.wzb.xevent.test.constant.AttrsConstant;
import com.wzb.xevent.test.util.XPanelOmegaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * the data struct for subcard
 * Created by samwangzhibo on 2018/6/1.
 */

public class SubCardData {
    public final String TAG = "SubCardData";
    private int position;
    public String subCardId;
    public String cardId;
    private long time = -1;
    private boolean mMoveIn;
    private boolean mSixtyMoveIn;

    public SubCardData(String cardId, int position, String subCardId) {
        this.cardId = cardId;
        this.position = position;
        this.subCardId = subCardId;
    }

    @Deprecated
    public final boolean moveIn() {
        if (TextUtils.isEmpty(subCardId)) return false;
        if (!mMoveIn) {
            time = System.currentTimeMillis();
            mMoveIn = true;
            Map map = new HashMap();
            addCommonData(map);
            XPanelOmegaUtils.trackEvent(XPanelOmegaUtils.X_PANEL_SUBCARD_SW, map);
            return true;
        }
        return false;
    }

    @Deprecated
    public final boolean sixtyMoveIn() {
        if (TextUtils.isEmpty(subCardId)) return false;
        if (!mSixtyMoveIn) {
            time = System.currentTimeMillis();
            mSixtyMoveIn = true;
            Map map = new HashMap();
            addCommonData(map);
            return true;
        }
        return false;
    }

    public int getPosition() {
        return position;
    }

    @Deprecated
    public final boolean moveOut() {
        if (TextUtils.isEmpty(subCardId)) return false;
        if (mMoveIn) {
            mMoveIn = false;
            Map map = new HashMap();
            addCommonData(map);
            map.put("time", System.currentTimeMillis() - time);
            XPanelOmegaUtils.trackEvent(XPanelOmegaUtils.X_PANEL_SUBCARD_SW_TIME, map);
            return true;
        }
        return false;
    }

    public final boolean sixtyMoveOut() {
        if (TextUtils.isEmpty(subCardId)) return false;
        if (mSixtyMoveIn) {
            mSixtyMoveIn = false;
            Map map = new HashMap();
            addCommonData(map);
            return true;
        }
        return false;
    }

    public Map addCommonData(Map map) {
        if (map == null){
            map = new HashMap();
        }
//        map.put("scroolCard_position", position + 1);
        map.put(AttrsConstant.SUB_CARD_ID, subCardId);
        map.put(AttrsConstant.POSITION, position);
        map.put(AttrsConstant.SUBCARD_POSITION, position);
        map.put(AttrsConstant.CARD_ID, cardId);
        return map;
    }
}
