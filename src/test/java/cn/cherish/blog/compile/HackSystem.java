package cn.cherish.blog.compile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 1:14
 */
public class HackSystem {

    public final static InputStream in = System.in;

    private static ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public final static PrintStream out = new PrintStream(buffer);
    public final static PrintStream err = out;

    public static String getBufferString(){
        return buffer.toString();
    }

    public static void clearBuffer(){
        buffer.reset();
    }

    public static void setSecurityManager(final SecurityManager securityManager) {
        System.setSecurityManager(securityManager);
    }

    public static SecurityManager getSecurityManager() {
        return System.getSecurityManager();
    }

    public static long currentTimeMillis(){
        return System.currentTimeMillis();
    }

}
