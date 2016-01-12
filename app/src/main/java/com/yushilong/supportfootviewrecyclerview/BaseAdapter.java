package com.yushilong.supportfootviewrecyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yushilong on 2015/5/14.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter {
    private ArrayList models;
    public Activity activity;
    private View anchor;
    private RecyclerView recyclerView;
    public static final int TYPE_FOOT = Integer.MIN_VALUE;
    public static final int TYPE_GENERIC = Integer.MIN_VALUE + 1;
    private Object footObject;

    public BaseAdapter(Activity activity, ArrayList models) {
        this.activity = activity;
        this.models = models;
    }

    public BaseAdapter(Activity activity, ArrayList models, final RecyclerView recyclerView, final View anchor) {
        this.models = models;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.anchor = anchor;
        if (anchor != null)
            anchor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                        anchor.setVisibility(View.GONE);
                    }
                }
            });
    }

    public BaseAdapter() {
    }

    class FootViewHoler extends RecyclerView.ViewHolder {
        public FootViewHoler(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOT)
            return new FootViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.googleprogressbar, parent, false));
        else return onCreateGenericViewHoler(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateGenericViewHoler(ViewGroup parent, int viewType);

    protected abstract void onBindGenericViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == models.size() - 1 && models.contains(footObject))
            return TYPE_FOOT;
        else return TYPE_GENERIC;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (anchor != null) {
            if (position > getShowAnchorCount() - 1)
                anchor.setVisibility(View.VISIBLE);
            else
                anchor.setVisibility(View.GONE);
        }
        if (!(holder instanceof FootViewHoler)) {
            onBindGenericViewHolder(holder, position);
        }
    }

    public int getShowAnchorCount() {
        return 10;//default is 10
    }

    public ArrayList getModels() {
        return models;
    }

    public void appendOne(Object object, boolean loadMoreEnable, boolean isRefresh) {
        if (isRefresh)
            reset();
        setLoadMoreEnable(loadMoreEnable);
        if (loadMoreEnable) {
            notifyItemInserted(this.models.size() - 1);
            this.models.add(this.models.size() - 1, object);
        } else {
            notifyItemInserted(this.models.size());
            this.models.add(object);
        }
    }

    public void appendList(ArrayList list, boolean loadMoreEnable, boolean isRefresh) {
        if (isRefresh)
            reset();
        setLoadMoreEnable(loadMoreEnable);
        if (loadMoreEnable) {
            notifyItemRangeInserted(this.models.size() - 1, list.size());
            this.models.addAll(this.models.size() - 1, list);
        } else {
            notifyItemRangeInserted(this.models.size(), list.size());
            this.models.addAll(list);
        }
    }

    public void remove(int position) {
        notifyItemRemoved(position);
        models.remove(position);
    }

    private void setLoadMoreEnable(boolean loadMoreEnable) {
        if (loadMoreEnable) {
            if (footObject == null) {
                footObject = new Object();
                notifyItemInserted(models.size() == 0 ? 0 : models.size() - 1);
                this.models.add(footObject);
            }
        } else {
            if (models.contains(footObject)) {
                notifyItemRemoved(models.size() - 1);
                models.remove(footObject);
            }
        }
    }

    public void reset() {
        models.clear();
        footObject = null;
        notifyDataSetChanged();
    }
}
