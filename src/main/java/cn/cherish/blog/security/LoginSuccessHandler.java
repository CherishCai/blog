package cn.cherish.blog.security;

import cn.cherish.blog.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆成功处理逻辑
 * @author Cherish
 * @version 1.0
 * @date 2017/5/12 11:15
 */
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication) throws IOException,ServletException {
        // 获得授权后可得到用户信息
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        // 输出登录提示信息
        log.info("【LoginSuccessHandler】 管理员：{}登录", securityUser.getNickname());
        log.info("【LoginSuccessHandler】 IP :{}", IpUtils.getIpAddress(request));

        request.getSession().setAttribute("nickname", securityUser.getNickname());

        super.onAuthenticationSuccess(request, response, authentication);
    }


}
