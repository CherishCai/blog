package cn.cherish.blog.web;

import cn.cherish.blog.entity.Contact;
import cn.cherish.blog.entity.WeixinUser;
import cn.cherish.blog.services.ContactService;
import cn.cherish.blog.services.WeixinUserService;
import cn.cherish.blog.utils.SessionUtil;
import cn.cherish.blog.weixin4j.OAuthInfo;
import cn.cherish.blog.weixin4j.UserInfo;
import cn.cherish.blog.weixin4j.WeixinConfig;
import cn.cherish.blog.weixin4j.WeixinUtil;
import cn.cherish.blog.weixinjs.Sign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/api")
public class ApiListController extends ABaseController {

    @Autowired
    private WeixinUserService weixinUserService;

    @Autowired
    private ContactService contactService;

    /**
     * 提起授权
     */
    @GetMapping(value = "/toAuth")
    public void toAuth(HttpServletResponse response, HttpSession session) {
        response.setContentType("text/html;charset=utf-8");
        try {
            if (SessionUtil.getWeixinUser(session) != null) {
                response.sendRedirect(WeixinConfig.getValue("indexURL"));
            } else {
                response.sendRedirect(WeixinUtil.getLoginUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 授权回调接口，获取微信用户信息保存到数据库
     */
    @GetMapping("/authCallback")
    public void authCallback(String code, HttpSession session, HttpServletResponse response) {

        OAuthInfo oAuthInfo = WeixinUtil.getOAuthOpenid(code);
        WeixinUser weixinUser = null;
        try {
            weixinUser = weixinUserService.findByOpenid(oAuthInfo.getOpenid());
        } catch (Exception e1) {
            log.debug("openid:" + oAuthInfo.getOpenid() + "第一次登陆本系统weixinUser为空，数据库没有对应的数据");
            e1.printStackTrace();
        }

        try {
            if (weixinUser == null || weixinUser.getId() == null) {
                UserInfo userInfo = WeixinUtil.getUserInfo(oAuthInfo.getOpenid(), oAuthInfo.getAccessToken());
                weixinUser = new WeixinUser();
                weixinUser.setOpenid(oAuthInfo.getOpenid());
                if (userInfo != null) {
                    weixinUser.setCity(userInfo.getCity());
                    weixinUser.setHeadimgurl(userInfo.getHeadimgurl());
                    weixinUser.setNickname(userInfo.getNickname());
                    weixinUser.setSex(userInfo.getSex().shortValue());
                    weixinUser.setSubscribetime(new Date());
                }
                log.debug("openid:" + oAuthInfo.getOpenid() + "执行保存weixinUser到数据库");
                weixinUserService.insert(weixinUser);
            }
            SessionUtil.addWeixinUser(session, weixinUser);

            response.sendRedirect(WeixinConfig.getValue("indexURL"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("openid:" + oAuthInfo.getOpenid() + ":" + e.getMessage());
        }
    }

    /**
     * 获取JssdkInitData
     */
    @GetMapping(value = "/getJsSdkInitData")
    @ResponseBody
    public Map<String, String> getJssdkInitData(HttpServletRequest request, String url) {

        Map<String, String> data = null;

        if (StringUtils.isNotBlank(url))
            data = Sign.sign(request.getServletContext(), url);
        else
            data = Sign.sign(request.getServletContext(), WeixinConfig.getValue("indexURL"));

        return data;
    }

    @PostMapping("/contactMe")
    public Map<String, Object> contactMe(Contact contact){

        try {
            contactService.save(contact);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return getReturnMap(Boolean.TRUE, "感谢您的支持！", null);
    }



}
