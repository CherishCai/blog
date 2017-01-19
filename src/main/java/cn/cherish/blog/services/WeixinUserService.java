/**
 * caihongwen.cn Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package cn.cherish.blog.services;

import cn.cherish.blog.entity.WeixinUser;
import cn.cherish.blog.repository.WeixinUserDao;
import cn.cherish.blog.utils.MessageUtil;
import cn.cherish.blog.utils.WeixinMsgUtil;
import cn.cherish.blog.weixin4j.UserInfo;
import cn.cherish.blog.weixin4j.WeixinUtil;
import cn.cherish.blog.weixinjs.WeixinJs;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WeixinUserService {

    @Autowired
    private WeixinUserDao weixinUserDao;

    @Transactional
    public void insert(WeixinUser weixinUser) {
        weixinUserDao.save(weixinUser);
    }

    public WeixinUser findByOpenid(String openid) {
        return openid == null ? null : weixinUserDao.findByOpenid(openid);
    }

    @Transactional
    public String autoResponse(HttpServletRequest request) throws IOException, DocumentException {

        Map<String, String> msgMap = MessageUtil.xmlToMap(request);
        String fromUserName = msgMap.get("FromUserName");//来自发送方帐号（一个OpenID）
        String toUserName = msgMap.get("ToUserName");//发给这边的公众号
        String msgType = msgMap.get("MsgType");
        String content = msgMap.get("Content").trim();

        if ("chat".equalsIgnoreCase(content.trim())) {
            //设置openid
            request.getServletContext().setAttribute(fromUserName,"1");
            //数据库保存想和我聊天的人
            WeixinUser weixinUser = weixinUserDao.findByOpenid(fromUserName);
            if (weixinUser == null || weixinUser.getId() == null) {
                UserInfo userInfo = WeixinUtil.getUserInfo(fromUserName, WeixinJs.getAccess_token(request.getServletContext()));
                weixinUser = new WeixinUser();
                weixinUser.setOpenid(fromUserName);
                if (userInfo != null) {
                    weixinUser.setCity(userInfo.getCity());
                    weixinUser.setHeadimgurl(userInfo.getHeadimgurl());
                    weixinUser.setNickname(userInfo.getNickname());
                    weixinUser.setSex(userInfo.getSex());
                    weixinUser.setSubscribetime(new Date());
                }
                System.out.println("openid:" + fromUserName + "执行保存weixinUser到数据库");
                weixinUserDao.save(weixinUser);
            }
            return MessageUtil.initText(toUserName, fromUserName, "进入聊天模式");
        }else if ("close".equalsIgnoreCase(content.trim())){
            request.getServletContext().removeAttribute(fromUserName);
            return MessageUtil.initText(toUserName, fromUserName, "好的，退出聊天模式");
        }

        //图灵机器人聊天喔
        if (request.getServletContext().getAttribute(fromUserName) != null) {
            return WeixinMsgUtil.askTuLing(toUserName, fromUserName, content);
        }

        //非图灵的普通微信消息
        String message = null;
        if(MessageUtil.MESSAGE_TEXT.equals(msgType)){//文字信息
			/* 核心是解剖对方发过来的文字信息   作出相应的回答  */

            if("1".equals(content) || "天气查询".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.WEATHER);
            }else if(content.startsWith("天气")){
                String city = content.replaceAll("^天气", "").trim();
                if("".equals(city)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.WEATHER);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.queryWeather(city));
                }
            }else if("2".equals(content)){
                message = MessageUtil.initNewsMessage(toUserName, fromUserName);
            }else if("3".equals(content) || "翻译功能".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.TRANSLATRE);
            }else if(content.startsWith("翻译")){//^翻译 指以"翻译"开头的字符串
                String word = content.replaceAll("^翻译", "").trim();
                if("".equals(word)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.TRANSLATRE);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.baduiTranslate(word));
                }
            }else if("4".equals(content) || "图片".equals(content)){
                message = MessageUtil.initImageMessage(toUserName, fromUserName);
            }else if("5".equals(content) || "音乐".equals(content)){
                message = MessageUtil.initMusicMessage(toUserName, fromUserName);
            }else if("6".equals(content) || "快递查询".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.EXPRESSAGE);
            }else if(content.startsWith("快递")){
                String expNo = content.replaceAll("^快递", "").trim();
                if("".equals(expNo)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.EXPRESSAGE);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.queryShipper(expNo));
                }
            //看我博客的朋友喔，推送链接
            }else if("7".equals(content) || "博客".equals(content) || "Cherish博客".equals(content)){
                message = MessageUtil.initBlogMessage(toUserName, fromUserName);
            //图灵机器人回复
            }else if("9".equals(content) || "智能聊天模式".equals(content) || "聊天".equals(content) || "智能聊天".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.TULING);
            }else if("help".equalsIgnoreCase(content) || "?".equals(content) || "？".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.HELP);
            }else{
                message = MessageUtil.initText(toUserName, fromUserName, "你输入了-->" + content +"\n请输入help查看菜单");
            }

            //事件信息
        }else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
            String eventType = msgMap.get("Event");
            if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){//关注
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.SUBSCRIBE);
            }else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){//点击
                message = MessageUtil.initText(toUserName, fromUserName, "点击了啥？");
            }else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
                String url = msgMap.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, url);
            }else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                String key = msgMap.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, key);
            }

        }else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){//位置
            String label = msgMap.get("Label");
            message = MessageUtil.initText(toUserName, fromUserName, label);

        }else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){//图片
            message = MessageUtil.initText(toUserName, fromUserName, "美图/::B");

        }else if(MessageUtil.MESSAGE_VOICE.equals(msgType) || MessageUtil.MESSAGE_MUSIC.equals(msgType)){
            message = MessageUtil.initText(toUserName, fromUserName, "好声音/::$");

        }else if(MessageUtil.MESSAGE_SHORTVIDEO.equals(msgType) || MessageUtil.MESSAGE_VIDEO.equals(msgType)){
            message = MessageUtil.initText(toUserName, fromUserName, "还我快播/::~");

        }else if(MessageUtil.MESSAGE_LINK.equals(msgType)){//链接
            message = MessageUtil.initText(toUserName, fromUserName, "http://www.caihongwen.cn/");

            //什么奇葩情况？
        }else {
            message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.HELP);
        }

        return message;
    }

}