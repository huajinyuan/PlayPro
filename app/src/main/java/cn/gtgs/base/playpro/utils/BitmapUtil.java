package cn.gtgs.base.playpro.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片工具类
 */
public class BitmapUtil {
    private static int THUMBNAIL_SIZE = 140;
    private static int NOMARL_SIZE = 800;


    public static boolean saveBitmap(Bitmap bitmap, File file) {
        return saveBitmap(bitmap, file, 50);
    }

    public static boolean saveBitmap(Bitmap bitmap, File file, int quality) {
        try {
            bitmap.compress(CompressFormat.JPEG, quality, new FileOutputStream(file));
            bitmap.recycle();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            System.gc();
        }
    }

    /**
     * 获取文件的路径
     *
     * @param
     * @return
     */
    private static String getFilePath(Context context, Uri uri) {

        String filePath = null;

        if ("content".equalsIgnoreCase(uri.getScheme())) {

            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{Images.Media.DATA}, null, null, null);

            if (null == cursor) {
                return null;
            }

            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor
                            .getColumnIndex(Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }

        // 从文件中选择
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }

        return filePath;
    }


    public static Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 200) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 200.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
        return bm;
    }

}
