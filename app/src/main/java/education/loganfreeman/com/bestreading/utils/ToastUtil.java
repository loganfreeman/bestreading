package education.loganfreeman.com.bestreading.utils;

import android.widget.Toast;

import education.loganfreeman.com.bestreading.BaseApplication;

/**
 * Created by shanhong on 3/30/17.
 */

public class ToastUtil {

    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
