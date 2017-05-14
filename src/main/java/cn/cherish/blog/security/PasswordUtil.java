package cn.cherish.blog.security;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * 密码加密工具
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 13:11
 */
public class PasswordUtil {

    private PasswordUtil(){}

    private static final ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();

    public static String sha1(String source){
        return shaPasswordEncoder.encodePassword(source, null);
    }


}
