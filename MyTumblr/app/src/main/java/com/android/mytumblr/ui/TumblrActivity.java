package com.android.mytumblr.ui;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mytumblr.R;
import com.android.mytumblr.api.TumblrApi;
import com.android.mytumblr.model.Posts;
import com.android.mytumblr.model.ServerResult;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by Sunita on 8/22/2015.
 */

public class TumblrActivity extends ListActivity {
    private static final String BASE_API = "http://api.tumblr.com/v2";
    private static final String TAG = "TumblrActivity";
    private CustomPostsListAdapter mAdapter;
    private List<Posts> mPostsList = new ArrayList<Posts>();
    private TextView mNoPostsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tumblr_activity_layout);
        mNoPostsView = (TextView) findViewById(R.id.list_text);
        mAdapter = new CustomPostsListAdapter(this, mPostsList);
        setListAdapter(mAdapter);
        new PostTask().execute();
    }


    private class PostTask extends AsyncTask<Void, Void, List<Posts>> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            mNoPostsView.setVisibility(View.VISIBLE);
            mNoPostsView.setText(R.string.loading);

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_API)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(TAG, msg);
                        }
                    }).build();
        }

        @Override
        protected List<Posts> doInBackground(Void... params) {
            TumblrApi methods = restAdapter.create(TumblrApi.class);
            ServerResult posts = null;
            try {
                posts = methods.getPosts();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (posts == null)? null : posts.getResposne().getPosts();
        }

        @Override
        protected void onPostExecute(List<Posts> posts) {

            mPostsList = posts;
            if(posts == null) {
                mNoPostsView.setVisibility(View.VISIBLE);
                mNoPostsView.setText(R.string.no_internet_connection);
            } else if (mPostsList.size() < 1) {
                mNoPostsView.setVisibility(View.VISIBLE);
                mNoPostsView.setText(R.string.no_posts_to_show);
            } else {
                mNoPostsView.setVisibility(View.GONE);
                mAdapter.setPostsList(mPostsList);
                setListAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
