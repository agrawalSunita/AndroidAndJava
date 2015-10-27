package com.android.mytumblr.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunita on 8/23/15.
 */
public class ImageLoader {
    private ExecutorService mExecutorService;
    private Handler mHandler = new Handler();
    private static final String TAG = "ImageLoader";
    private FileCache mFileCache;
    private MemoryCache mMemoryCache = new MemoryCache();

    public ImageLoader(Context context) {
        mFileCache = new FileCache(context);
        mExecutorService = Executors.newFixedThreadPool(5);
    }

    /**
     * This version downloads any image
     *
     * @param url
     */
    public Bitmap displayImage(String url) {
        Log.d(TAG, "inside displayImage");
        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            // if image is stored in MemoryCache Map then return the same
            return bitmap;
        } else {
            //queue Photo to download from url
            queuePhoto(url);
        }
        return null;
    }

    public void displayImage(String url, ImageView imageView) {
        Log.d(TAG, "inside displayImage");
        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            // if image is stored in MemoryCache Map then
            // Show image in listview row
            imageView.setImageBitmap(bitmap);
        } else {
            //queue Photo to download from url
            queuePhoto(url, imageView);
            //Before downloading image show default image
            imageView.setImageResource(android.R.drawable.gallery_thumb);
        }
    }

    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        // Store image and url in PhotoToLoad object
        Log.d(TAG, "inside queuePhoto");
        PhotoToLoad p = new PhotoToLoad(url, imageView);

        // pass PhotoToLoad object to PhotosLoader runnable class
        // and submit PhotosLoader runnable to executers to run runnable
        // Submits a PhotosLoader runnable task for execution
        mExecutorService.submit(new PhotosLoader(p));
    }

    private void queuePhoto(String url) {
        Log.d(TAG, "inside queuePhoto");
        mExecutorService.submit(new InlinePhotosLoader(url));
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                  // download image from web url
                Bitmap bmp = getBitmap(photoToLoad.url);

                // set image data in Memory Cache
                mMemoryCache.put(photoToLoad.url, bmp);

                // Get bitmap to display
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
                // The runnable will be run on the thread to which this mHandler is attached.
                // BitmapDisplayer run method will call
                mHandler.post(bd);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    class InlinePhotosLoader implements Runnable {
        String mUrl;

        InlinePhotosLoader(String url) {
            this.mUrl = url;
        }

        @Override
        public void run() {
            try {
                // download image from web url
                Bitmap bmp = getBitmap(mUrl);
                // set image data in Memory Cache
                mMemoryCache.put(mUrl, bmp);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }


    private Bitmap getBitmap(String url) {
        Log.d(TAG, "inside getBitmap");
        File f = mFileCache.getFile(url);

        //from SD cache
        //CHECK : if trying to decode file which not exist in cache return null
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        // Download image file from web
        Log.d(TAG, "inside getBitmap before try");
        try {

            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();

            // Constructs a new FileOutputStream that writes to file
            // if file not exist then it will create file
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;

        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                mMemoryCache.clear();
            return null;
        }
    }

    //Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {

        Log.d(TAG, "inside decodeFile");
        try {
            //Decode image size
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            Log.d(TAG, "inside run of BitmapDisplayer");
            // Show bitmap on UI
            if (bitmap != null) {
                Log.d(TAG, "inside run of BitmapDisplayer bitmap!=null");
                photoToLoad.imageView.setImageBitmap(bitmap);
            } else {
                Log.d(TAG, "inside run of BitmapDisplayer bitmap==null");
                photoToLoad.imageView.setImageResource(android.R.drawable.gallery_thumb);
            }

        }
    }
}
