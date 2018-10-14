package com.example.htmlviewer.util;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * IStringUtils
 * <p>
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:35 PM
 **/
public class StringUtils {

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是图片url
     * author: Yang Du <youngdu29@gmail.com>
     * created at 26/12/2017 5:01 PM
     */
    public static boolean isImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String urlLowerCase = url.toLowerCase();
        if (urlLowerCase.startsWith("http") &&
                (urlLowerCase.contains(".jpg") || urlLowerCase.contains(".png")
                        ||urlLowerCase.contains(".gif")||urlLowerCase.contains(".webp"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是视频url
     *
     * author: Yang Du <youngdu29@gmail.com>
     * created at 26/12/2017 5:07 PM
     */
    public static boolean isVideoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String urlLowerCase = url.toLowerCase();
        if (urlLowerCase.startsWith("http") &&
                (urlLowerCase.contains(".mp4") || urlLowerCase.contains(".avi")
                        || urlLowerCase.contains(".3gp"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     *  String 转 float
     * author: young du <youngdu29@gmail.com>
     * version: V1.0
     * created at 2018/4/20 18:13
     */
    public static float toFloat(String obj) {
        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }
        return 0F;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     *byte 转 int
     * 
     *author: hezhiWu
     *created at 2017/4/13 17:47
     */
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     *int 转 byte
     * 
     *author: hezhiWu
     *created at 2017/4/13 17:48
     */
    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * 字符串数组转换成字符串
     * @param conjStr 连接字符串
     * author: Yang Du <youngdu29@gmail.com>
     * created at 13/03/2018 3:48 PM
     */
    public static String stringArrayToString(String conjStr, String[] stringArray) {
        StringBuilder stringBuilder = null;
        if (!TextUtils.isEmpty(conjStr) && stringArray != null && stringArray.length > 0) {
            stringBuilder = new StringBuilder();
            for (String str : stringArray) {
                stringBuilder.append(str);
                stringBuilder.append(conjStr);
            }
            if (stringBuilder!=null) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(conjStr));
            }
        }
        return stringBuilder != null ? stringBuilder.toString() : "";

    }
}
