package personal.yulie.android.yuliegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import personal.yulie.android.yuliegallery.utils.AsyncDrawable;
import personal.yulie.android.yuliegallery.utils.BitmapWorkerTask;
import personal.yulie.android.yuliegallery.utils.FileUtils;
import personal.yulie.android.yuliegallery.utils.MD5;

import static personal.yulie.android.yuliegallery.utils.BitmapWorkerTask.TAG;

/**
 * Created by android on 17-9-7.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ItemHolder> {
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;
    private final Condition mCacheStartedCondition = new ReentrantLock().newCondition();
    private static final long MAX_SIZE = 1024 * 1024 * 256;
    private List<String> datas;
    private Context mContext;
    private Bitmap mPlaceHolderBitmap;

    public GalleryAdapter(Context context) {
        super();
        int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };
        mContext = context.getApplicationContext();
        File folder = FileUtils.getOutputFolder();
        datas = Arrays.asList(folder.list());
        mPlaceHolderBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.place_holder);
        try {
            mDiskLruCache =  DiskLruCache.open(FileUtils.getDiskCacheDir(mContext), 1, 1, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //        private TextView mImageName;
        private ImageView mImageView;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_view, parent, false));
//            mImageName = (TextView) itemView.findViewById(R.id.image_name);
            mImageView = (ImageView) itemView.findViewById(R.id.thumb_image);
        }

        public void bind(String data) {
////            mImageName.setText(data);
//            mImageView.setImageResource(R.drawable.place_holder);
//            Bitmap bitmap = BitmapUtils.decodeFrom(FileUtils.getOutputFolder() + File.separator + data, 160, 160);
//            if (null != bitmap) {
//                mImageView.setImageBitmap(bitmap);
//            }

            loadBitmap(FileUtils.getOutputFolder() + File.separator + data, mImageView);
        }
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(datas.get(position));
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void loadBitmap(String path, ImageView imageView) {
        if (cancelPotentialWork(path, imageView)) {

            Bitmap bitmap = mLruCache.get(MD5.getMD5(path.getBytes()));
            if (null != bitmap) {
                Log.i(TAG, "doInBackground: hit memCache");
                imageView.setImageBitmap(bitmap);
                return;
            }

            BitmapWorkerTask task = new BitmapWorkerTask(imageView, mLruCache, mDiskLruCache);
            AsyncDrawable asyncDrawable = new AsyncDrawable(
                    mContext.getResources(),
                    mPlaceHolderBitmap,
                    task
            );
            imageView.setImageDrawable(asyncDrawable);
            task.execute(path);
        }
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String path = bitmapWorkerTask.getPath();
            if (path == null || path.equals("") || !path.equals(data)) {
                // Cancel previous task
                Log.i("AsyncTask", "cancelPotentialWork: ");
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;


    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}
