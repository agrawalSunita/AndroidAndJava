package com.android.mytumblr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sunita on 8/22/2015.
 */
public class Response {
    @SerializedName("posts")
    private List<Posts> posts;

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }
}
