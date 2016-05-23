package com.ggspark.hackernews.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 23/May/2016
 */


public class RealmLong extends RealmObject {
    @PrimaryKey
    private Long key;

    public RealmLong(Long key) {
        this.key = key;
    }

    public RealmLong() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }
}
