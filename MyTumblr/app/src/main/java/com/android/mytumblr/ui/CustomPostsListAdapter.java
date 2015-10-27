package com.android.mytumblr.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mytumblr.R;
import com.android.mytumblr.model.AltSizesPhoto;
import com.android.mytumblr.model.Photos;
import com.android.mytumblr.model.Posts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomPostsListAdapter extends BaseAdapter implements Html.ImageGetter {
    private static final String TAG = "CustomPostsListAdapter";
    private static final String TYPE_PHOTO = "photo";
    private static final String TYPE_TEXT = "text";
    private static final String TYPE_QUOTE = "quote";
    private Activity mActivity;
    private List<Posts> mPostsList;
    private LayoutInflater mInflater;
    public ImageLoader mImageLoader;

    public CustomPostsListAdapter(Activity activity, List<Posts> tickets) {
        this.mActivity = activity;
        this.mPostsList = tickets;
        mImageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public void setPostsList(List<Posts> posts) {
        String type = null;
        for (Posts post : posts) {
            type = post.getType();
            if (type.equalsIgnoreCase(TYPE_PHOTO) || type.equalsIgnoreCase(TYPE_TEXT) || type.equalsIgnoreCase(TYPE_QUOTE))
                mPostsList.add(post);
            Log.d(TAG, "size of list = " + mPostsList.size());
        }
    }

    @Override
    public int getCount() {
        return mPostsList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mPostsList.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    LevelListDrawable drawable;

    @Override
    public Drawable getDrawable(String source) {
        drawable = new LevelListDrawable();
        Drawable empty = new BitmapDrawable(mActivity.getResources(), mImageLoader.displayImage(source));
        drawable.addLevel(0, 0, empty);
        drawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        Log.d(TAG, "source->>>>>> " + source);
        return drawable;
    }

    public static class ViewHolder {
        public TextView blog_name;
        public TextView notes_count;
        public TextView plain_text;
        public TextView caption_text;
        public ImageView photo_image;
        public TextView title;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;
        if (mInflater == null) {
            mInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            vi = mInflater.inflate(R.layout.tumblr_item_layout, null);

            holder = new ViewHolder();

            holder.blog_name = (TextView) vi.findViewById(R.id.blog_name);
            holder.notes_count = (TextView) vi.findViewById(R.id.notes_count);
            holder.plain_text = (TextView) vi.findViewById(R.id.plain_text);
            holder.caption_text = (TextView) vi.findViewById(R.id.caption);
            holder.photo_image = (ImageView) vi.findViewById(R.id.photo);
            holder.title = (TextView) vi.findViewById(R.id.title);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        Posts post = mPostsList.get(position);
        String type = post.getType();
        Log.d(TAG, "type = " + type);
        holder.blog_name.setText(post.getBlog_name());
        holder.notes_count.setText(post.getNote_count() + " notes");
        if (type.equalsIgnoreCase(TYPE_PHOTO)) {
            Log.d(TAG, "inside photo tag");
            if (post != null) {
                List<Photos> photo = post.getPhotos();
                if (photo != null && photo.size() > 0) {
                    holder.photo_image.setVisibility(View.VISIBLE);
                    holder.caption_text.setVisibility(View.VISIBLE);
                    holder.plain_text.setVisibility(View.GONE);
                    holder.title.setVisibility(View.GONE);
                    Photos p = photo.get(0);
                    List<AltSizesPhoto> altSize = p.getAltSizes();
                    if (altSize != null && altSize.size() > 0) {
                        String urlStr = altSize.get(0).getUrl();
                        mImageLoader.displayImage(urlStr, holder.photo_image);
                    }
                    holder.caption_text.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.caption_text.setText(Html.fromHtml(post.getCaption(), this, null));
                }
            }
        } else if (type.equalsIgnoreCase(TYPE_TEXT)) {
            Log.d(TAG, "inside text tag");
            holder.plain_text.setVisibility(View.VISIBLE);
            holder.caption_text.setVisibility(View.GONE);
            holder.photo_image.setVisibility(View.GONE);
            if (post.getTitle() != null) {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(Html.fromHtml(post.getTitle()));
            } else {
                holder.title.setVisibility(View.GONE);
            }
            holder.plain_text.setMovementMethod(LinkMovementMethod.getInstance());
            holder.plain_text.setText(Html.fromHtml(post.getBody(), this, null));
        } else if (type.equalsIgnoreCase(TYPE_QUOTE)) {
            Log.d(TAG, "inside quote tag");
            holder.plain_text.setVisibility(View.VISIBLE);
            holder.caption_text.setVisibility(View.GONE);
            holder.photo_image.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
            holder.plain_text.setText(Html.fromHtml(post.getText(), this, null));
        } else {
            Log.d(TAG, "incompatible type " + type);
        }

        return vi;

    }

}
