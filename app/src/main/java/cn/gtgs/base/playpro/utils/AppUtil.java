package cn.gtgs.base.playpro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by gtgs on 2016/10/6.
 */

public class AppUtil {
    /**
     * 关闭键盘
     *
     * @param activity Activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() != null)
            ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * MD5加密
     *
     * @param plainText 需要加密的字符串
     * @return 加密后字符串
     */
    public static String md5(String plainText) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toLowerCase();// 32位的加密（转成小写）

            buf.toString().substring(8, 24);// 16位的加密

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param path    文件路劲
     */
    public static void installAPK(Context context, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

//    /**
//     * 直接拨号，需要增加CALL_PHONE权限
//     *
//     * @param context 上下文
//     * @param phone   手机号码
//     */
//    public static void actionCall(Context context, String phone) {
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//        intent.setAction(Intent.ACTION_CALL);// 直接拨号
//        context.startActivity(intent);
//    }

//    /**
//     * 跳到拨号盘-拨打电话
//     *
//     * @param context 上下文
//     * @param phone   手机号码
//     */
//    public static void actionDial(Context context, String phone) {
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//        intent.setAction(Intent.ACTION_DIAL);// 拨号盘
//        context.startActivity(intent);
//    }
    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return mPackageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return 1;
    }
    public static int getDJ(int gold) {
        return (int) (Math.sqrt(gold / 100 + 4) - 2);
    }
}
