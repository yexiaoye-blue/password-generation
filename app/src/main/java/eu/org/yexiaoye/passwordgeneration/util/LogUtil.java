package eu.org.yexiaoye.passwordgeneration.util;

import android.util.Log;

public class LogUtil {
    public static String sTAG = "LogUtil";
    // 控制是否输出log
    public static boolean sIsRelease = false;

    private LogUtil() {}

    /**
     * 初始话LogUtil可以控制总的是否输出log
     * @param baseTag   base tag
     * @param isRelease 是否输出log
     */
    public static void initLog(String baseTag, boolean isRelease) {
        sTAG = baseTag;
        sIsRelease = isRelease;
    }

    /**
     * verbose ，任何消息都会输出
     * @param TAG   tag
     * @param content   content
     */
    public static void v(String TAG, String content) {
        if (!sIsRelease) {
            Log.v("[" + sTAG + "]" + TAG, content);
        }
    }

    /**
     * debug，除了VERBOSE等级，剩余4个等级的log都会输出
     * @param TAG
     * @param content
     */
    public static void d(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    /**
     * information，一般的提示信息，对应的log等级为INFO
     * @param TAG
     * @param content
     */
    public static void i(String TAG, String content) {
        if (!sIsRelease) {
            Log.i("[" + sTAG + "]" + TAG, content);
        }
    }

    /**
     * warning，警告信息
     * @param TAG
     * @param content
     */
    public static void w(String TAG, String content) {
        if (!sIsRelease) {
            Log.w("[" + sTAG + "]" + TAG, content);
        }
    }

    /**
     * error，一般用于输出异常和报错信息
     * @param TAG
     * @param content
     */
    public static void e(String TAG, String content) {
        if (!sIsRelease) {
            Log.e("[" + sTAG + "]" + TAG, content);
        }
    }
}
