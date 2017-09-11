package personal.yulie.android.yuliegallery.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by android on 17-9-7.
 */

public class FileUtils {
    public static File getDCIMFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }


    public static File getOutputFolder(File dir, String name) {
        File folder = new File(dir, name);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static File getOutputFolder() {
        return FileUtils.getOutputFolder(FileUtils.getDCIMFolder(), "Camera");
    }

    public static File getDiskCacheDir(Context context) {
        return context.getExternalCacheDir();
    }


}
