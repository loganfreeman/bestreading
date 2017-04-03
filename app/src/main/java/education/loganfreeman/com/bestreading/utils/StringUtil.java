package education.loganfreeman.com.bestreading.utils;

/**
 * Created by scheng on 4/1/17.
 */

public class StringUtil {

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
}
