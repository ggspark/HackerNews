package com.ggspark.hackernews;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 23/May/2016
 */


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // The realm file will be located in Context.getFilesDir() with name "default.realm"
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
