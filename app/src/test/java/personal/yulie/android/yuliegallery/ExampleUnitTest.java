package personal.yulie.android.yuliegallery;

import org.junit.Test;

import personal.yulie.android.yuliegallery.utils.MD5;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        System.out.println(MD5.byteToHexString(new byte[]{0,1, -1, -2, -128, 127}));
    }
}
