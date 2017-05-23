package cn.gtgs.base.playpro.utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    public static String NumFormat(String num) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(num)) {
            return "";
        }
        String s = null;
        try {
            float f = Float.parseFloat(num);
            boolean m = false;
            if (f >= 10000) {
                f = f / 10000.0f;
                m = true;
            }
            s = f + "";
            // s = decimalFormat.format(f);
            if (s.contains(".")) {
                String ss;
                if (s.length() > s.indexOf(".") + 2) {
                    ss = s.substring(0, s.indexOf(".") + 3);
                    if (ss.endsWith("00")) {
                        ss = s.substring(0, s.indexOf("."));
                    } else if (ss.endsWith("0")) {
                        ss = s.substring(0, s.indexOf(".") + 2);
                    }
                } else if (s.length() > s.indexOf(".") + 1) {
                    ss = s.substring(0, s.indexOf(".") + 2);
                    if (ss.endsWith("0")) {
                        ss = s.substring(0, s.indexOf("."));
                    }
                } else {
                    ss = s.substring(0, s.indexOf("."));
                }
                s = ss;
            }

            if (m) {
                s = s + "万";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return s;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
