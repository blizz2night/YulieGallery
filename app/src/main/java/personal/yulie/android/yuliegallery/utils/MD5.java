package personal.yulie.android.yuliegallery.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by android on 17-9-8.
 */

public class MD5 {
    public static String getMD5(byte[] data){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = md.digest(data);
        return byteToHexString(bytes);
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (1 == hex.length()) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
