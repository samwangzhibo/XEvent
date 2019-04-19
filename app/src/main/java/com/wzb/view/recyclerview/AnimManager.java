package com.wzb.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * the anim manager control the item animation
 * Created by samwangzhibo on 2018/5/24.
 */

public class AnimManager {
    private static final String TAG = "AnimManager";

    private static AnimManager INSTANCE;

    public static final int ANIM_BOTTOM_TO_TOP = 0;
    public static final int ANIM_TOP_TO_BOTTOM = 1;

    private int mAnimType = ANIM_BOTTOM_TO_TOP; //动画类型
    private float mAnimFactor = 0.2f;   //变化因子
    private float alpha = 0.6f;

    public static AnimManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnimManager();
        }
        return INSTANCE;
    }

    public void setAnim(RecyclerView recyclerView, int position, float leftPercent, float percent, float rightPercent){
        View mLeftView = position - 1 > 0 ? recyclerView.getLayoutManager().findViewByPosition(position - 1) : null;  // 左边页
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 右边页
//        Log.d(TAG, "leftPercent=" + leftPercent + "; percent=" + percent + "; rightPercent=" + rightPercent);
        if (mLeftView != null) {
            mLeftView.setAlpha(getAlpha(alpha + leftPercent * (1 - alpha)));
        }
        if (mCurView != null) {
            mCurView.setAlpha(getAlpha(alpha + percent * (1 - alpha)));
        }
        if (mRightView != null) {
            mRightView.setAlpha(getAlpha(alpha + rightPercent * (1 - alpha)));
        }
    }

    float getAlpha(float alpha){
        return alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}