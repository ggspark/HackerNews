package com.ggspark.hackernews;

import com.ggspark.hackernews.models.ItemResponse;
import com.ggspark.hackernews.models.RealmLong;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 23/Mar/2016
 */


public class APIServices {

    private static final String HACKER_NEWS_URL = "https://hacker-news.firebaseio.com/v0/";

    private static final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).registerTypeAdapter(new TypeToken<RealmList<RealmLong>>() {}.getType(), new TypeAdapter<RealmList<RealmLong>>() {

                @Override
                public void write(JsonWriter out, RealmList<RealmLong> value) throws IOException {
                    // Ignore
                }

                @Override
                public RealmList<RealmLong> read(JsonReader in) throws IOException {
                    RealmList<RealmLong> list = new RealmList<RealmLong>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(new RealmLong(in.nextLong()));
                    }
                    in.endArray();
                    return list;
                }
            })
            .create();

    private static final Retrofit retrofitService = new Retrofit.Builder()
            .baseUrl(HACKER_NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public static final HNService HN_SERVICE = retrofitService.create(HNService.class);

    //Create a HNService interface to query API
    public interface HNService {
        @GET("topstories.json")
        Call<Long[]> getStories();

        @GET("item/{item_id}.json")
        Call<ItemResponse> getItem(@Path("item_id") Long itemId);
    }

}

