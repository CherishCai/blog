package cn.cherish.blog.compile;

import java.lang.reflect.Method;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 1:22
 */
public class JavaClassExecuter {

    public static String execute(byte[] classByte) {
        HackSystem.clearBuffer();

        HotSwapClassLoader classLoader = new HotSwapClassLoader();
        Class clazz = classLoader.loadByte(classByte);
        try {
            Method method = clazz.getMethod("main", new Class[]{String[].class});
            method.invoke(null, new String[]{null});
        } catch (Throwable e) {
            e.printStackTrace(HackSystem.out);
        }

        return HackSystem.getBufferString();
    }
}
