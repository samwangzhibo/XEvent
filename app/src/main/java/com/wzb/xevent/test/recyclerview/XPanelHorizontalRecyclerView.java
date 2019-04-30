package com.wzb.xevent.test.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.wzb.xevent.test.util.Utils;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.manager.XEvent;
import com.wzb.xevent.xevent.stream.XPEventStream;

/**
 * the horizontal RecyclerView
 * Created by samwangzhibo on 2018/5/24.
 */

public class XPanelHorizontalRecyclerView extends RecyclerView {
    private String TAG = "XpanelHorizontalRecyclerView";
    private StartSnapHelper snapHelper;
    public int decorationPadding = 0;
    public MyRecylerAdapter myRecylerAdapter;
    private Context mContext;
    private ScrollManager mScrollManager;
    public XPEventStream xpEventStream;

    public XPanelHorizontalRecyclerView(Context context) {
        this(context, null);
    }

    public XPanelHorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XPanelHorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setDecorationPadding(int decorationPadding) {
        this.decorationPadding = decorationPadding;
        XpanelItemDecoration xpanelItemDecoration = new XpanelItemDecoration(decorationPadding);
        addItemDecoration(xpanelItemDecoration);
    }

    public void setXpEventStream(XPEventStream xpEventStream) {
        this.xpEventStream = xpEventStream;
    }

    public void sendEvent(XPEvent xpEvent){
        XEvent.getInstance().sendEvent(xpEventStream, xpEvent);
    }

    public void sendEventDelay(XPEvent xpEvent, long duration){
        XEvent.getInstance().sendEventDelay(xpEventStream, xpEvent, duration);
    }

    private void init(Context context) {
        mContext = context;
        snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(this);

        myRecylerAdapter = new MyRecylerAdapter(context, snapHelper, this);
        setAdapter(myRecylerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false){
            @Override
            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                mScrollManager.checkScrollX();
            }
        };
        setLayoutManager(linearLayoutManager);

        setDecorationPadding(decorationPadding);

        mScrollManager = new ScrollManager(this, context,linearLayoutManager);
        mScrollManager.initScrollListener();
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void addChildView(View childView) {
        myRecylerAdapter.addChildView(childView);
    }

    //如果父布局是recyclerView采用特殊的加入模式
    @Override
    public void addView(View child) {
        myRecylerAdapter.addChildView(child);
    }


    public class XpanelItemDecoration extends RecyclerView.ItemDecoration {

        private int mPadding;
        private int mDividerWidth;

        public XpanelItemDecoration(int padding) {
            mPadding = padding;
            mDividerWidth = Utils.dip2px(mContext, 6) - 16;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int childPosition = parent.getChildAdapterPosition(view);
            int itemCount = parent.getAdapter().getItemCount();
            if (childPosition == itemCount - 1) {
                outRect.set(0, 0, mPadding, 0);
            } else if (childPosition == 0) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, mDividerWidth, 0);
            }
        }

    }
}
