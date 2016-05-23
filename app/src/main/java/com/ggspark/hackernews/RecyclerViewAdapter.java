package com.ggspark.hackernews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ggspark.hackernews.models.ItemResponse;

import io.realm.Realm;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 23/May/2016
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Long[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
        }
    }

    public RecyclerViewAdapter(Long[] ids) {
        mDataset = ids;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTextView = (TextView) v.findViewById(R.id.text);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemResponse item = Realm.getDefaultInstance().where(ItemResponse.class).equalTo("id", mDataset[position]).findFirst();
        if (item != null) {
            holder.mTextView.setText(item.getTitle());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
