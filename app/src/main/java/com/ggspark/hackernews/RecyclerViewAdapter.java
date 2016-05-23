package com.ggspark.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mImageView;

        public ViewHolder(View v) {
            super(v);
        }
    }

    public RecyclerViewAdapter(Long[] ids, Activity context) {
        mDataset = ids;
        activity = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTextView = (TextView) v.findViewById(R.id.text);
        vh.mImageView = (TextView) v.findViewById(R.id.comment);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ItemResponse item = Realm.getDefaultInstance().where(ItemResponse.class).equalTo("id", mDataset[position]).findFirst();
        if (item != null) {
            if (item.getType().equals("story")) {
                holder.mTextView.setText(Html.fromHtml(item.getTitle()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, CommentActivity.class);
                        intent.putExtra("id", mDataset[position]);
                        activity.startActivity(intent);
                    }
                });
            } else if (item.getType().equals("comment")) {
                holder.mTextView.setText(Html.fromHtml(item.getText()));
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
