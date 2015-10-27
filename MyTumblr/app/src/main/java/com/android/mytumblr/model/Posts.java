package com.android.mytumblr.model;

/**
 * Created by Sunita on 8/22/2015.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Corresponds to the posts
 */
public class Posts {

    @SerializedName("id")
    private long id;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private String date;
    @SerializedName("caption")
    private String caption;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("photos")
    private List<Photos> photos;
    @SerializedName("text")
    private String text;
    @SerializedName("blog_name")
    private String blog_name;
    @SerializedName("note_count")
    private String note_count;


    public String getNote_count() {
        return note_count;
    }

    public void setNote_count(String note_count) {
        this.note_count = note_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getBlog_name() {
        return blog_name;
    }

    public void setBlog_name(String blog_name) {
        this.blog_name = blog_name;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }
}
