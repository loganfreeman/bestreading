package education.loganfreeman.com.bestreading.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by scheng on 4/1/17.
 */

public class StringUtil {

    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static String safeString(String s) {
        if (s == null) return "";
        return s;
    }

    public static String join(String[] parts, String s) {
        StringBuffer sb = new StringBuffer();
        for(String part : parts) {
            sb.append(part).append(s);
        }
        sb.setLength(sb.length() - 1);
        return  sb.toString();
    }

    public static String replace(String url, String path) {
        String[] parts = url.split("/");
        parts[parts.length-1] = path;
        return join(parts, "/");
    }

    public static String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }
}
