package com.yushilong.supportfootviewrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by yushilong on 2015/11/30.
 */
public class MBRecyclerView extends RecyclerView {
    OnBottomListener onBottomListener;

    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        STAGGERED_GRID
    }

    public MBRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MBRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MBRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        addOnScrollListener(new OnRcvScrollListener());
    }

    public class OnRcvScrollListener extends RecyclerView.OnScrollListener {

        private String TAG = getClass().getSimpleName();

        /**
         * layoutManager的类型（枚举）
         */
        protected LAYOUT_MANAGER_TYPE layoutManagerType;

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;
//

        /**
         * 当前滑动的状态
         */
//    private int currentScrollState = 0;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //  int lastVisibleItemPosition = -1;
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findLastCompletelyVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//        currentScrollState = newState;
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1) {//到底最后第1条就开始加载更多数据
                    //Log.d(TAG, "is loading more");
                    if (onBottomListener != null)
                        onBottomListener.onBottom();
                }
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }

    public void setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
    }

    public interface OnBottomListener {
        public void onBottom();
    }
}
