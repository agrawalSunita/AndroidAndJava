package com.android.mytumblr.api;

import com.android.mytumblr.model.Posts;
import com.android.mytumblr.model.Response;
import com.android.mytumblr.model.ServerResult;

import java.util.List;

import retrofit.http.GET;

/**
 * Tumblr APIs
 */
public interface TumblrApi {

    @GET("/blog/peacecorps.tumblr.com/posts")
    ServerResult getPosts();
}
