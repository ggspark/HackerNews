
package com.ggspark.hackernews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemResponse extends RealmObject {

    @SerializedName("by")
    @Expose
    private String by;
    @SerializedName("descendants")
    @Expose
    private Long descendants;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Long id;
    @SerializedName("kids")
    @Expose
    private RealmList<RealmLong> kids = new RealmList<RealmLong>();
    @SerializedName("score")
    @Expose
    private Long score;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("parent")
    @Expose
    private Long parent;
    @SerializedName("text")
    @Expose
    private String text;

    /**
     * @return The by
     */
    public String getBy() {
        return by;
    }

    /**
     * @param by The by
     */
    public void setBy(String by) {
        this.by = by;
    }

    /**
     * @return The descendants
     */
    public Long getDescendants() {
        return descendants;
    }

    /**
     * @param descendants The descendants
     */
    public void setDescendants(Long descendants) {
        this.descendants = descendants;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public RealmList<RealmLong> getKids() {
        return kids;
    }

    public void setKids(RealmList<RealmLong> kids) {
        this.kids = kids;
    }

    /**
     * @return The score
     */
    public Long getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /**
     * @return The time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The parent
     */
    public Long getParent() {
        return parent;
    }

    /**
     * @param parent The parent
     */
    public void setParent(Long parent) {
        this.parent = parent;
    }

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

}
