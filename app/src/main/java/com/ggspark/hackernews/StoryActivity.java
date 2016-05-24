package com.ggspark.hackernews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ggspark.hackernews.models.ItemResponse;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryActivity extends AppCompatActivity {
    private ProgressDialog loadingDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        findViewById(R.id.card_view).setVisibility(View.GONE);

        loadingDialog = new ProgressDialog(StoryActivity.this);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setMessage("Loading");
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        APIServices.HN_SERVICE.getStories().enqueue(new Callback<Long[]>() {
            @Override
            public void onResponse(Call<Long[]> call, Response<Long[]> response) {
                if (response.body() != null) {
                    final Long[] stories = response.body();
                    //Load all the items in cache
                    count = 0;
                    for (Long id : stories) {
                        APIServices.HN_SERVICE.getItem(id).enqueue(new Callback<ItemResponse>() {
                            @Override
                            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                                Realm.getDefaultInstance().beginTransaction();
                                Realm.getDefaultInstance().copyToRealmOrUpdate(response.body());
                                Realm.getDefaultInstance().commitTransaction();
                                count++;
                                if (count == stories.length) {
                                    loadingCompleted(stories);
                                }
                            }

                            @Override
                            public void onFailure(Call<ItemResponse> call, Throwable t) {
                                t.printStackTrace();
                                count++;
                                if (count == stories.length) {
                                    loadingCompleted(stories);
                                }
                            }
                        });
                    }

                } else {
                    Toast.makeText(StoryActivity.this, "Oops! Unable to get a response.", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    loadingDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Long[]> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(StoryActivity.this, "Oops! Unable to get a response.", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                loadingDialog.dismiss();
            }
        });

    }

    private void loadingCompleted(Long[] stories) {
        mAdapter = new RecyclerViewAdapter(stories, StoryActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        loadingDialog.dismiss();
    }
}
