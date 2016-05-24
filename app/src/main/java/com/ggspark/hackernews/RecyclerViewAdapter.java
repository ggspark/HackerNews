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
        public TextView text;
        public TextView user;
        public TextView time;
        public TextView comment;

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
        vh.text = (TextView) v.findViewById(R.id.text);
        vh.user = (TextView) v.findViewById(R.id.user);
        vh.time = (TextView) v.findViewById(R.id.time);
        vh.comment = (TextView) v.findViewById(R.id.comment);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ItemResponse item = Realm.getDefaultInstance().where(ItemResponse.class).equalTo("id", mDataset[position]).findFirst();
        if (item != null) {
            if (item.getType().equals("story")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, CommentActivity.class);
                        intent.putExtra("id", mDataset[position]);
                        activity.startActivity(intent);
                    }
                });

                if (item.getTitle() != null) {
                    holder.text.setText(Html.fromHtml(item.getTitle()));
                }
            } else if (item.getType().equals("comment")) {
                if (item.getText() != null) {
                    holder.text.setText(Html.fromHtml(item.getText()));
                }
            }
            if (item.getBy() != null) {
                holder.user.setText(item.getBy());
            }
            if (item.getTime() != null) {
//                Date date = new Date(item.getTime() * 1000L);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                holder.time.setText(Utility.getTimeAgo(item.getTime()));
            }
            if (item.getKids() != null) {
                holder.comment.setText(String.valueOf(item.getKids().size()));
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
