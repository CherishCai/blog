package cn.cherish.blog.security;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * 自定义保存更多的信息
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 11:18
 */
@Data
public class SecurityUser extends org.springframework.security.core.userdetails.User {

    /**
     * 用户id
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickname;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        // TODO 自定义赋值
    }

    public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        // TODO 自定义赋值
    }

    //**************
    // 静态工具方法
    public static SecurityUser ME(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (SecurityUser) authentication.getPrincipal();
    }

    public static String nickname(){
        SecurityUser securityUser = ME();
        if (securityUser == null) {
            return null;
        }
        return securityUser.nickname;
    }

    public static Long id(){
        SecurityUser securityUser = ME();
        if (securityUser == null) {
            return null;
        }
        return securityUser.id;
    }

}
