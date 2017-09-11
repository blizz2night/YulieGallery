package personal.yulie.android.yuliegallery;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import personal.yulie.android.yuliegallery.utils.FileUtils;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = "InstrumentedTest";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("personal.yulie.android.yuliegallery", appContext.getPackageName());
    }

    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.i(TAG, "test: "+ FileUtils.getDiskCacheDir(appContext));
        Log.i(TAG, "test: "+ appContext.getCacheDir());
    }
}
