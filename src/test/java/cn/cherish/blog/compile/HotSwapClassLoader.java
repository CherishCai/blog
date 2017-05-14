package cn.cherish.blog.compile;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 1:09
 */
public class HotSwapClassLoader extends ClassLoader {

    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }

}
