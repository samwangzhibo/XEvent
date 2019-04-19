package com.wzb.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wzb.R;
import com.wzb.xevent.XPConstant;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.AttrsConstant;
import com.wzb.xevent.constant.EventConstant;
import com.wzb.xevent.constant.GroupConstant;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * Created by samwangzhibo on 2018/5/24.
 */

class MyRecylerAdapter extends RecyclerView.Adapter<MyRecylerAdapter.MyHolder> {
    private String TAG = "XpanelHorizontalRecyclerView";
    private Context context;
    private StartSnapHelper snapHelper;
    private XPanelHorizontalRecyclerView recyclerView;
    private ArrayList<View> childViews = new ArrayList<>();
    public ArrayList<SubCardData> childs = new ArrayList<>();
    private int pageX;

    public MyRecylerAdapter(Context context, StartSnapHelper snapHelper, XPanelHorizontalRecyclerView recyclerView) {
        this.context = context;
        this.snapHelper = snapHelper;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(childViews.get(viewType), snapHelper, recyclerView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return childViews.size();
    }

    public void addChildView(View childView) {
        childViews.add(childView);
        childView.setTag(R.id.subcard_index, childViews.size() - 1);
        SubCardData subCardData = new SubCardData("", childViews.size() - 1, (String) childView.getTag(R.id.cardId));

        childs.add(subCardData);
        if (recyclerView != null){
//            LogcatUtil.e(DomesticXPEventStream.TAG, "发送subcard通知事件，移除上次的子卡片tracker subcardId=" + subCardData.subCardId);
            recyclerView.sendEvent(new XPEvent(EventConstant.EVENT_CREATE_SUBCARD)
                    .putEventAttrs(XPConstant.XP_GROUP, GroupConstant.GROUP_SUB_CARD).putEventAttrs(AttrsConstant.OBJ, subCardData));

//            LogcatUtil.e(DomesticXPEventStream.TAG, "发送创建子卡片tracker对象的事件 subcardId=" + subCardData.subCardId);
            //创建绑定子卡片对象的子卡片tracker
            recyclerView.sendEventDelay(new XPEvent(EventConstant.EVENT_CREATE_TRACKER)
                    .putEventAttrs(XPConstant.XP_GROUP, GroupConstant.GROUP_SUB_CARD).putEventAttrs(AttrsConstant.OBJ, subCardData), 30);
        }
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private int position = 1;

        public MyHolder(final View itemView, final StartSnapHelper snapHelper, final XPanelHorizontalRecyclerView recyclerView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.e("StartSnapHelper", "item onClick");
                    String url = "";
                    try {
                        url = (String) v.getTag();
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                    int recyclerViewWidth = recyclerView.getMeasuredWidth();
                    int left = itemView.getLeft();
                    int right = itemView.getRight();
                    if (left < recyclerViewWidth && right > recyclerViewWidth) {
                        if (pageX == 0 && recyclerView.getLayoutManager().getChildCount() > 0)
                            pageX = recyclerView.getLayoutManager().getChildAt(0).getMeasuredWidth();
                        recyclerView.smoothScrollBy(pageX, 0);
//                        snapHelper.onFling(4000, 0);
                    } else if (left < 0 && right < recyclerViewWidth && right > 0) {
                        recyclerView.smoothScrollBy(left - recyclerView.decorationPadding, 0);
//                        snapHelper.onFling(-4000, 0);
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        if (itemView.getTag(R.id.cardId) != null) {
                            String cardId = (String) itemView.getTag(R.id.cardId);
                            map.put(AttrsConstant.SUB_CARD_ID, cardId);
//                                Log.e(TAG, "subcard_id : " + cardId);
                        }
                        map.put(AttrsConstant.SUBCARD_POSITION, position - 1);
                        map.put("scroolCard_position", position + "");
                    }
                }
            });
        }

        public void bind(int position) {
            this.position = position + 1; //这里的position从1开始计数
        }
    }
}