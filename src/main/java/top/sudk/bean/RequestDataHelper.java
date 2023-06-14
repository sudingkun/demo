package top.sudk.bean;

/**
 * @author we
 */
public class RequestDataHelper {
    /**
     * 请求参数存取
     */
    private static final ThreadLocal<String> REQUEST_DATA = new ThreadLocal<>();


    public static void set(String str) {
        REQUEST_DATA.set(str);
    }


    public static String get() {
        return REQUEST_DATA.get();
    }


    public static void remove() {
        REQUEST_DATA.remove();
    }
}