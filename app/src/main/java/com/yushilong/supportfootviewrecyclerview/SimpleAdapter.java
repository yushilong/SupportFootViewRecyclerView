package com.yushilong.supportfootviewrecyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yushilong on 2016/1/12.
 */
public class SimpleAdapter extends BaseAdapter {

    public SimpleAdapter(Activity activity, ArrayList models) {
        super(activity, models);
    }

    class ViewHoler extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHoler(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateGenericViewHoler(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    protected void onBindGenericViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHoler viewHoler = (ViewHoler) holder;
        String model = (String) getModels().get(position);
        viewHoler.title.setText(model);
    }
}
