package cn.gtgs.base.playpro.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast工具
 */

public class ToastUtil {
    /**
     * 显示Toast
     * */
    public static void showToast(final String toast, final Context context)
    {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }
}
