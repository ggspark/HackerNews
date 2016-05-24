package com.ggspark.hackernews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ggspark.hackernews.models.ItemResponse;
import com.ggspark.hackernews.models.RealmLong;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    private ProgressDialog loadingDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Long storyId;
    private ItemResponse item;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storyId = getIntent().getLongExtra("id", -1);
        if (storyId == -1) {
            finish();
            return;
        }
        item = Realm.getDefaultInstance().where(ItemResponse.class).equalTo("id", storyId).findFirst();
        setContentView(R.layout.activity_story);
        findViewById(R.id.detail_container).setVisibility(View.GONE);
        loadingDialog = new ProgressDialog(CommentActivity.this);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setMessage("Loading");
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView titleText = (TextView) findViewById(R.id.text);
        titleText.setText(item.getTitle());
        titleText.setTextColor(0xFF000000);
        titleText.setTextSize(18);
        getData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadingDialog.show();
        ArrayList<Long> ids = new ArrayList<>();
        for (RealmLong id : item.getKids()) {
            ids.add(id.getKey());
        }
        final Long[] comments = ids.toArray(new Long[ids.size()]);
        count = 0;
        if (comments != null && comments.length > 0) {
            for (Long id : comments) {
                APIServices.HN_SERVICE.getItem(id).enqueue(new Callback<ItemResponse>() {
                    @Override
                    public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                        Realm.getDefaultInstance().beginTransaction();
                        Realm.getDefaultInstance().copyToRealmOrUpdate(response.body());
                        Realm.getDefaultInstance().commitTransaction();
                        count++;
                        if (count == comments.length) {
                            loadingCompleted(comments);
                        }
                    }

                    @Override
                    public void onFailure(Call<ItemResponse> call, Throwable t) {
                        t.printStackTrace();
                        count++;
                        if (count == comments.length) {
                            loadingCompleted(comments);
                        }
                    }
                });
            }
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            loadingDialog.dismiss();
        }
    }

    private void loadingCompleted(Long[] comments) {
        mAdapter = new RecyclerViewAdapter(comments, CommentActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        loadingDialog.dismiss();
    }
}
