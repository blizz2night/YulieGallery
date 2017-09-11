package personal.yulie.android.yuliegallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by android on 17-9-7.
 */

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    public static final String TAG = "BitmapWorkerTask";
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;
    private WeakReference<ImageView> mImageViewWeakReference;
    private String mPath;

    public BitmapWorkerTask(ImageView imageView, LruCache<String, Bitmap> lruCache, DiskLruCache diskLruCache) {
        mImageViewWeakReference = new WeakReference<>(imageView);
        mLruCache = lruCache;
        mDiskLruCache = diskLruCache;
    }

    @Override
    protected Bitmap doInBackground(String[] params) {
        mPath = params[0];
        Bitmap bitmap;
        //Log.i("AsyncTask", "doInBackground: ");
        String key = MD5.getMD5(mPath.getBytes());
        if (!isCancelled()) {

            try {
                bitmap = getBitmapFromCache(key);
                if (bitmap != null) {
                    Log.i(TAG, "doInBackground: hit diskCache");
                    mLruCache.put(key, bitmap);
                    return bitmap;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (!isCancelled()) {
            bitmap = BitmapUtils.decodeFrom(mPath, 160, 160);
            if (null != bitmap) {
                mLruCache.put(key, bitmap);
                try {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (null != editor) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                        editor.commit();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return bitmap;
        }
        return null;
    }

    @Nullable
    private Bitmap getBitmapFromCache(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        Bitmap bitmap = null;
        if (null != snapshot) {
            InputStream in = snapshot.getInputStream(0);
            bitmap = BitmapFactory.decodeStream(in);
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        ImageView imageView = mImageViewWeakReference.get();
        if (null != imageView) {

        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (null != mImageViewWeakReference) {
            ImageView imageView = mImageViewWeakReference.get();
            if (null != imageView && null != bitmap) {
                //Log.i(TAG, "setImageBitmap: ");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        if (null != bitmap) {
        }

        if (null != mImageViewWeakReference) {
            mImageViewWeakReference.clear();
            mImageViewWeakReference = null;
        }

        if (null != mLruCache) {
            mLruCache = null;
        }
    }


    public String getPath() {
        return mPath;
    }
}



