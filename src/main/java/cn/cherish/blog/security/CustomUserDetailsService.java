package cn.cherish.blog.security;

import cn.cherish.blog.dal.entity.User;
import cn.cherish.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/4/13 14:10
 */
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        log.info("【loadUserByUsername】 {}", user);

        SecurityUser securityUser = new SecurityUser(
                user.getUsername(), user.getPassword(),
                user.getActive() > 0,
                true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        // TODO 自定义的其它属性
        securityUser.setId(user.getId());
        securityUser.setNickname(user.getNickname());
        return securityUser;
    }

}
