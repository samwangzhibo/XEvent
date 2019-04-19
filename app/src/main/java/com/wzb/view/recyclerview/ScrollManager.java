package com.wzb.view.recyclerview;

/**
 * Created by samwangzhibo on 2018/5/24.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wzb.R;
import com.wzb.util.LogcatUtil;
import com.wzb.util.Utils;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.stream.XPEventStream;

import java.util.ArrayList;


public class ScrollManager {
    private static final String TAG = "ScrollManager";

    private XPanelHorizontalRecyclerView mGalleryRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private int mMinShowOmegaY;

    private int mPosition = 0;
    private int width;
    private int pageX;
    ArrayList<SubCardData> childs;

    // 使偏移量为左边距 + 左边Item的可视部分宽度
    private int mConsumeX = 0;
    // 滑动方向
    private Context context;
    private XPEventStream xpEventStream;

    public ScrollManager(XPanelHorizontalRecyclerView mGalleryRecyclerView, Context context, LinearLayoutManager linearLayoutManager) {
        this.mGalleryRecyclerView = mGalleryRecyclerView;
        this.context = context;
        this.linearLayoutManager = linearLayoutManager;
        childs = mGalleryRecyclerView.myRecylerAdapter.childs;
        mMinShowOmegaY = Utils.dip2px(context, 30);
    }

    /**
     * 监听RecyclerView的滑动
     */
    public void initScrollListener() {
        GalleryScrollerListener mScrollerListener = new GalleryScrollerListener();
        mGalleryRecyclerView.addOnScrollListener(mScrollerListener);
    }

    public void setXpEventStream(XPEventStream xpEventStream) {
        this.xpEventStream = xpEventStream;
    }

    class GalleryScrollerListener extends RecyclerView.OnScrollListener {
        int moveLength = 0; //移动总长度
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            if (mHelper != null && mHelper.mCardData != null) {
//                sendEvent(mHelper.mCardData.getXpEvent(XPEventConstant.XP_EVENT_PANEL_HORIZONTAL_CARD_SCROLL_STATUS_CHANGE).putEventAttrs(XPAttrsConstant.XP_HORIZONTAL_CARD_SCROLL_STATUS, newState));
//            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            moveLength += dx;
//            if (mHelper != null &&  mHelper.mCardData != null) {
//                 sendEvent(mHelper.mCardData.getXpEvent(XPEventConstant.XP_EVENT_PANEL_HORIZONTAL_MOVE)
//                        .putEventAttrs(XPAttrsConstant.XP_HORIZONTAL_CARD_SCROLL_DX, dx));
//            }
            checkScrollX();
        }
    }

    private void sendEvent(XPEvent xpEvent) {
        XEvent.getInstance().sendEvent(xpEventStream, xpEvent);
    }


    public void checkScrollX() {
//        int first = linearLayoutManager.findFirstVisibleItemPosition();
//        int end = linearLayoutManager.findLastVisibleItemPosition();
        try {
            int currentParentWidth = mGalleryRecyclerView.getMeasuredWidth();
            boolean getDindex = false;
            int dIndex;
            int lastVisibleIndex = 0;
            for (int i = 0; i < childs.size(); i++) {

//                SubCardData item = childs.get(i);
//                if (i >= first && i <= end) {
//                    View view = linearLayoutManager.getChildAt(i - first);
//                    int left = view.getLeft();
//                    int currentY = currentParentWidth - left;
//                    int right = view.getRight();
//                    if (xpEventStream != null && mHelper.mCardData != null){
//                        xpEventStream.sendEvent(new XPEvent(XPEventConstant.XP_EVENT_PANEL_SUB_CARD_MOVE, item.addCommonData(null)).putEventAttrs(XPAttrsConstant.XP_SUBCARD_LEFT_LEFT, left)
//                                .putEventAttrs(XPAttrsConstant.XP_SUBCARD_LEFT_RIGHT, currentY).putEventAttrs(XPAttrsConstant.XP_SUBCARD_RIGHT_LEFT, right).putEventAttrs(XPAttrsConstant.XP_CARD_MIN_SW_HEIGHT, mMinShowOmegaY)
//                                .putEventAttrs(XPAttrsConstant.XP_OBJ, item).putEventAttrs(XPAttrsConstant.XP_HASHCODE, item.hashCode()));
//                    }
//                    //显示距离需要超出屏幕右边N
//                    if (right >= mMinShowOmegaY || currentY > mMinShowOmegaY) {
//                        item.moveIn();
//                    } else {
//                        item.moveOut();
//                    }
//                } else {
//                    item.moveOut();
//                }

                View view = linearLayoutManager.getChildAt(i);
                if (view != null){
                    int index = (int) view.getTag(R.id.subcard_index);
                    if (!getDindex) {
                        dIndex = index - i; //位移了几个
                        if (dIndex != 0) { //把前面移动的卡片移出
                            getDindex = true;
                            for (int j = 0; j < dIndex; j++) {
                                SubCardData subCardData = childs.get(j);
                                subcardMoveOut(subCardData);
                                LogcatUtil.e(TAG, "moveOut position : " + subCardData.getPosition());
                            }
                        }
                    }
                    SubCardData subCardData = childs.get(index);
                    int leftLeft = view.getLeft();
                    int subcardWidth = view.getMeasuredWidth();
                    int leftRight = currentParentWidth - leftLeft;
                    int rightLeft = view.getRight();
//                    if (subCardData != null){
//                        XPanelEventStream.sendEvent(xpEventStream, new XPEvent(XPEventConstant.XP_EVENT_PANEL_SUB_CARD_MOVE, subCardData.addCommonData(null)).putEventAttrs(XPAttrsConstant.XP_SUBCARD_LEFT_LEFT, leftLeft)
//                                .putEventAttrs(XPAttrsConstant.XP_SUBCARD_LEFT_RIGHT, leftRight).putEventAttrs(XPAttrsConstant.XP_SUBCARD_RIGHT_LEFT, rightLeft).putEventAttrs(XPAttrsConstant.XP_CARD_MIN_SW_HEIGHT, mMinShowOmegaY)
//                                .putEventAttrs(XPAttrsConstant.XP_OBJ, subCardData).putEventAttrs(XPAttrsConstant.XP_CARD_WIDTH, subcardWidth).putEventAttrs(XPAttrsConstant.XP_HASHCODE, subCardData.hashCode())
//                                .putEventAttrs(XPAttrsConstant.ATTR_EVENT_LINK, extensionHelper != null ? extensionHelper.eventLink : "")
//                        );
//                        LogcatUtil.e(TAG, "sendEvent cardmove left_right : " + leftRight +", right_left : " + rightLeft + ", min_height : " + mMinShowOmegaY + ", index : " + index + ", subcardWidth : " + subcardWidth);
//                    }
                    //显示距离需要超出屏幕右边N
                    if (rightLeft >= mMinShowOmegaY || leftRight > mMinShowOmegaY) {
                        subCardData.moveIn();
                    } else {
                        subCardData.moveOut();
                    }

                    if (rightLeft >= 0.6 * subcardWidth && leftRight >= 0.6 * subcardWidth) {
                        subCardData.sixtyMoveIn();
                    } else {
                        subCardData.sixtyMoveOut();
                    }
                    lastVisibleIndex = index;
                } else {
                    if (i <= lastVisibleIndex){ //已经处理过的卡片 不做moveout操作
                        continue;
                    }
                    SubCardData subCardData = childs.get(i);
//                    subCardData.moveOut();
                    subcardMoveOut(subCardData);
                    LogcatUtil.e(TAG, "moveOut position : " + subCardData.getPosition());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subcardMoveOut(SubCardData subCardData) {
        subCardData.moveOut();
        subCardData.sixtyMoveOut();
    }

    public void pauseStatus() {
        if (childs == null) return;
        for (int i = 0; i < childs.size(); i++) {
            SubCardData item = childs.get(i);
//            item.moveOut();
            subcardMoveOut(item);
//            sendEvent(xpEventStream, new XPEvent(XPEventConstant.XP_EVENT_PANEL_SUB_CARD_MOVE_OUT, item.addCommonData(null))
//                    .putEventAttrs(XPAttrsConstant.XP_OBJ, item).putEventAttrs(XPAttrsConstant.XP_HASHCODE, item.hashCode()));
        }
    }

    /**
     * 水平滑动
     *
     * @param recyclerView
     * @param dx
     */
    private void onHoritiontalScroll(final RecyclerView recyclerView, final int dx) {
//        Log.d(TAG, "mConsumeX=" + mConsumeX + "; dx=" + dx);
        mConsumeX += dx;
//        Log.e(TAG, "scroll status : " + recyclerView.getScrollState());

        if (width == 0) {
            width = recyclerView.getMeasuredWidth();
        }
        int screenWidth = ((ViewGroup) recyclerView.getParent()).getMeasuredWidth();
//        Log.e(TAG, "width=" + width + ",screenWidth=" + screenWidth);
        // 让RecyclerView测绘完成后再调用，避免GalleryAdapterHelper.mItemWidth的值拿不到
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (pageX == 0 && recyclerView.getLayoutManager().getChildCount() > 0) {
                    pageX = recyclerView.getLayoutManager().getChildAt(0).getMeasuredWidth();
                }
                if (pageX == 0){
                    return;
                }
                // 获取当前的位置
                int position = getPosition(mConsumeX, pageX);

                float leftPercent = (float) (position * pageX - mConsumeX) / (float) pageX;
                leftPercent = leftPercent < 0f ? 0 : leftPercent;
                float currentPercent = (float) ((position + 1) * pageX - mConsumeX) / (float) pageX;
                currentPercent = currentPercent > 1.0f ? 1.0f : currentPercent;
                float rightPercent = (float) (width + mConsumeX - (position + 1) * pageX) / (float) pageX;
                // 设置动画变化
                AnimManager.getInstance().setAlpha(1);
                AnimManager.getInstance().setAnim(recyclerView, position, leftPercent, currentPercent, rightPercent);
            }
        });

    }


    /**
     * 获取位置
     *
     * @param mConsumeX 实际消耗距离
     * @param pageX     理论消耗距离
     * @return
     */
    private int getPosition(int mConsumeX, int pageX) {
        return (mConsumeX + width) / pageX - 1;
    }

    public int getPosition() {
        return mPosition;
    }
}