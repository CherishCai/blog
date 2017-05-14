package cn.cherish.blog.compile;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 1:29
 */
public class CompileTest {

    public static void main(String[] args) {
        byte[] bytes = new byte[1024];

        String result = JavaClassExecuter.execute(bytes);
        System.out.println("result = " + result);
    }

}
