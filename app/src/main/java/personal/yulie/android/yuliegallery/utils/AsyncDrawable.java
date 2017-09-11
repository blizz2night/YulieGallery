package personal.yulie.android.yuliegallery.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by android on 17-9-7.
 */

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapWorkerTask> mBitmapWorkerTaskRef;

    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
        super(res,bitmap);
        mBitmapWorkerTaskRef = new WeakReference<>(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return mBitmapWorkerTaskRef.get();
    }
}
